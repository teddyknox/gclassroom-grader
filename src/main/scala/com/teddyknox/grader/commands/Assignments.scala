package com.teddyknox.grader.commands

import java.nio.file.{Files, Path, Paths}

import com.google.api.services.classroom.Classroom
import com.google.api.services.classroom.model.CourseWork
import com.google.api.services.drive.Drive
import com.teddyknox.grader.models.Workspace

import scala.collection.JavaConverters._
import scala.collection.mutable

object Assignments extends Command {

  def apply(args: Seq[String])(implicit classroom: Classroom, drive: Drive): Unit = {
    args match {
      case "list" :: tail => listCourseWork(parseKeyValueArgs(tail)("course"))
      case "grade" :: tail =>
        val params = parseKeyValueArgs(tail)
        gradeCourseWork(params("course"), params("courseWork"), params("testClass"))
      case "help" :: tail => printHelp()
      case default =>
        println(s"Unrecognized command.\n")
        printHelp()
        System.exit(1)
    }

    def listCourseWork(courseId: String): Unit = {
      val assignments = getAllCourseWorks(courseId)
      println("ID\t\t\tTitle")
      assignments.foreach { a =>
        println(s"${a.getId}\t\t${a.getTitle}")
      }
    }

    def getAllCourseWorks(courseId: String): Seq[CourseWork] = {
      var assignments = mutable.ListBuffer[CourseWork]()
      var nextPageToken: Option[String] = None
      do {
        val result = classroom.courses().courseWork().list(courseId).execute()
        nextPageToken = Option(result.getNextPageToken)
        assignments ++= result.getCourseWork.asScala
      } while (nextPageToken.nonEmpty)
      assignments.toSeq
    }

    def gradeCourseWork(courseId: String, courseWorkId: String, testClassPath: String): Unit = {
      val workspace = new Workspace()
      val userIds = workspace.download(courseId, courseWorkId)
      val target = Paths.get("").toAbsolutePath.resolve(testClassPath)
      userIds.foreach { userId =>
        val link = workspace.homeDir
          .resolve(courseId)
          .resolve(courseWorkId)
          .resolve(userId)
          .resolve(target.getFileName.toString)
        Files.deleteIfExists(link)
        Files.createSymbolicLink(link, target)
      }
    }
  }

  def printHelp(): Unit = println("""
   |USAGE: grader assignments list --course <course id>
   |""".stripMargin)
}

// compile testClass and subject class
// run testClass and subject class, capture output
// assemble output into CSV file
