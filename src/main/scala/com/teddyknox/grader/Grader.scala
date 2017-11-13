package com.teddyknox.grader

import com.google.api.services.classroom.Classroom
import com.google.api.services.drive.Drive


object Grader extends Command {
  lazy implicit private val classroom: Classroom = Auth.getClassroomService
  lazy implicit private val drive: Drive = Auth.getDriveService

  def main(args: Array[String]): Unit = Grader(args.toList)

  def apply(args: Seq[String]): Unit = {
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

  def printHelp(): Unit = println(
    """USAGE: grader <command>
      |
      |Possible commands:
      |    courses
      |    assignments
      |    help
      |
    """.stripMargin)
}
