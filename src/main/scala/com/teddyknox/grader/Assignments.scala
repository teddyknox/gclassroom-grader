package com.teddyknox.grader

import com.google.api.services.classroom.Classroom
import com.google.api.services.classroom.model.CourseWork

import scala.collection.JavaConverters._
import scala.collection.mutable

case class Assignments(private val service: Classroom) extends Base {

  def run(args: Seq[String]): Unit = {
    args match {
      case "list" :: "--course" :: courseId :: tail => listCourseWork(courseId)
      case "help" :: tail => printHelp()
      case default =>
        println(s"Unrecognized command.\n")
        printHelp()
        System.exit(1)
    }
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
      val result = service.courses().courseWork().list(courseId).execute()
      nextPageToken = Option(result.getNextPageToken)
      assignments ++= result.getCourseWork.asScala
    } while (nextPageToken.nonEmpty)
    assignments.toSeq
  }

  def printHelp(): Unit = println("""
   |USAGE: grader assignments list --course <course id>
   |""".stripMargin)
}
