package com.teddyknox.grader

import scala.collection.JavaConverters._

object Grader {
  lazy val service = Auth.getClassroomService

  def main(args: Array[String]): Unit = {
    checkNonEmpty(args)
    args.head match {
      case "courses" => courses(args.tail)
      case "assignments" => assignments(args.tail)
      case "help" => printHelp()
      case default =>
        println(s"Unrecognized command '${args.head}'. Printing usage...\n")
        printHelp()
        System.exit(1)
    }
  }

  def courses(args: Seq[String]): Unit = {
    checkNonEmpty(args)
    args.head match {
      case "list" => listCourses()
      case "help" => printCoursesHelp()
      case default =>
        println(s"Unrecognized command 'grader courses ${args.head}'. Printing usage...\n")
        printCoursesHelp()
        System.exit(1)
    }
  }

  def assignments(args:Seq[String]): Unit = {
    checkNonEmpty(args)
    println("USAGE: grader assignments (list)")
  }

  def listCourses(): Unit = {
    val courses = service.courses().list()
      .setPageSize(10)
      .execute()
    println("ID\t\t\tName")
    courses
      .getCourses.asScala
      .foreach { c =>
        println(s"${c.getId}\t\t${c.getName}")
      }
  }

  def checkNonEmpty(remainingArgs: Seq[String]): Unit = {
    if(remainingArgs.length == 0) {
      printHelp()
      System.exit(1)
    }
  }

  def printHelp(): Unit = println("""
      |USAGE: grader (courses|assignments)
      |
    """.stripMargin)

  def printCoursesHelp(): Unit = println("""
      |USAGE: grader courses (list)
      |
    """.stripMargin)
}
