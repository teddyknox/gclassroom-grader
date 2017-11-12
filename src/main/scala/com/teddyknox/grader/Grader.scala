package com.teddyknox.grader

import java.io.File

import com.teddyknox.grader.commands.{Assignments, Command, Courses}


object Grader extends Command {
  lazy implicit private val classroom = Auth.getClassroomService
  lazy implicit private val drive = Auth.getDriveService

  def main(args: Array[String]): Unit = {
//    try {
      Grader(args.toList)
//    } catch {
//      case e: Exception =>
//        println(s"Error: ${e.getMessage}. Exiting.")
//        System.exit(1)
//    }
  }

  def apply(args: List[String]): Unit = {
    args.head match {
      case "courses" => Courses(args.tail)
      case "assignments" => Assignments(args.tail)
      case "help" => printHelp()
      case default =>
        println(s"Unrecognized command.\n")
        printHelp()
        System.exit(1)
    }
  }

  def printHelp(): Unit = println("USAGE: grader (init|courses|assignments|submissions)")
}
