package com.teddyknox.grader

import java.io.File

trait Base {
  def checkNonEmpty(remainingArgs: Seq[String]): Unit = {
    if(remainingArgs.isEmpty) {
      printHelp()
      System.exit(1)
    }
  }

  def printHelp(): Unit
}

object Grader extends Base {
  lazy val service = Auth.getClassroomService

  def main(args: Array[String]): Unit = {
//    try {
    run(args.toList)
//    } catch {
//      case e: Exception =>
//        println(s"Error: ${e.getMessage}. Exiting.")
//        System.exit(1)
//    }
  }

  def run(args: Seq[String]): Unit = {
    args.head match {
      case "courses" => Courses(service).run(args.tail)
      case "assignments" => Assignments(service).run(args.tail)
      case "submissions" => Submissions(service).run(args.tail)
      case "help" => printHelp()
      case default =>
        println(s"Unrecognized command.\n")
        printHelp()
        System.exit(1)
    }
  }

  def init(): Unit = {
    val fileTree = new File("./assignments")
    fileTree.mkdir()
  }

  def printHelp(): Unit = println("USAGE: grader (init|courses|assignments|submissions)")
}
