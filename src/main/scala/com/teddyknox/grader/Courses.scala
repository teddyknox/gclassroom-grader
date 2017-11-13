package com.teddyknox.grader

import com.google.api.services.classroom.Classroom
import com.google.api.services.classroom.model.Course

import scala.collection.JavaConverters._
import scala.collection.mutable

object Courses extends Command {

  def apply(args: Seq[String])(implicit service: Classroom): Unit = {
    listCourses()

    def listCourses(): Unit = {
      val courses = getAllCourses
      println("ID\t\t\tName\t\t\tSection")
      courses.foreach { c =>
        println(s"${c.getId}\t\t${c.getName}\t\t${c.getSection}")
      }
    }

    def getAllCourses: Seq[Course] = {
      var courses = mutable.ListBuffer[Course]()
      var nextPageToken: Option[String] = None
      do {
        val result = service.courses().list().execute()
        nextPageToken = Option(result.getNextPageToken)
        courses ++= result.getCourses.asScala
      } while (nextPageToken.nonEmpty)
      courses
    }
  }
}
