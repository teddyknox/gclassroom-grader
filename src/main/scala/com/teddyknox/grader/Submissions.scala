package com.teddyknox.grader

import com.google.api.services.classroom.Classroom
import com.google.api.services.classroom.model.{CourseWork, StudentSubmission}

import scala.collection.JavaConverters._
import scala.collection.mutable

case class Submissions(private val service: Classroom) extends Base {

  def run(args: Seq[String]): Unit = {
    args match {
      case "download" :: "--course" :: courseId :: "--assignment" :: assignmentId :: tail =>
        listSubmissions(courseId, assignmentId)
      case "download" :: "--assignment" :: assignmentId :: "--course" :: courseId :: tail =>
        listSubmissions(courseId, assignmentId)
      case "help" :: tail => printHelp()
      case default =>
        println(s"Unrecognized command.\n")
        printHelp()
        System.exit(1)
    }
  }

  def getAllSubmissions(courseId: String, assignmentId: String): Seq[StudentSubmission] = {
    var submissions = mutable.ListBuffer[StudentSubmission]()
    var nextPageToken: Option[String] = None
    do {
      val result = service.courses().courseWork().studentSubmissions().list(courseId, assignmentId).execute()
      nextPageToken = Option(result.getNextPageToken)
      submissions ++= result.getStudentSubmissions.asScala
    } while (nextPageToken.nonEmpty)
    submissions.toSeq
  }

  def listSubmissions(courseId: String, assignmentId: String): Unit = {
    val submissions = getAllSubmissions(courseId, assignmentId)
//    submissions.fo
  }

  def printHelp(): Unit = println("""
   |USAGE: grader submissions download --course <course id> --assignment <assignment id>
   |""".stripMargin)
}

