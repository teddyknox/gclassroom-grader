package com.teddyknox.grader

import java.nio.file.{Files, Paths}

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
        gradeCourseWork(params("course"), params("assignment"), params("testClass"))
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
      assignments
    }

    def gradeCourseWork(courseId: String, courseWorkId: String, testClassPath: String): Unit = {
      val workspace = new Workspace()
      val courseWorkDir = workspace.homeDir
        .resolve(courseId)
        .resolve(courseWorkId)

      // Download student code
      val userIds = workspace.download(courseId, courseWorkId)

      // Build list of student grade dir paths
      val gradeDirs = userIds.map { userId =>
        courseWorkDir.resolve(userId)
      }

      // Symlink the test class into each grade dir path
      val target = Paths.get("").toAbsolutePath.resolve(testClassPath)
      gradeDirs.foreach { gradeDir =>
        val link = gradeDir.resolve(target.getFileName.toString)
        Files.deleteIfExists(link)
        Files.createSymbolicLink(link, target)
      }

      // Compile each grade dir
      val validGradeDirs = gradeDirs.flatMap { gradeDir =>
        val process = new ProcessBuilder("/bin/bash", "-c", "javac *.java")
          .directory(gradeDir.toFile)
          .start()
        if (process.waitFor() == 0) {
          println(s"Compilation succeeded for $gradeDir")
          Some(gradeDir)
        } else {
          println(s"Compilation failed for $gradeDir")
          None
        }
      }

      val validUserIds = validGradeDirs.map(_.getFileName.toString)
      println(validUserIds)

      // Run test suite for each grade dir
      validGradeDirs.foreach { gradeDir =>
        new ProcessBuilder("/bin/bash", "-c", "java -cp \".\" CalculateTest")
          .directory(gradeDir.toFile)
          .inheritIO()
          .start()
      }
    }
  }

  def printHelp(): Unit = println("""
   |USAGE: grader assignments <command>
   |
   |Possible commands:
   |    list --course <courseId>
   |    grade --course <courseId> --assignment <assignmentId> --testClass <test class path>
   |    help
   |
   |""".stripMargin)
}
