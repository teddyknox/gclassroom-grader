package com.teddyknox.grader.commands

import scala.collection.immutable.HashMap

trait Command {
  def checkNonEmpty(remainingArgs: Seq[String]): Unit = {
    if(remainingArgs.isEmpty) {
      printHelp()
      System.exit(1)
    }
  }

  def printHelp(): Unit

  def parseKeyValueArgs(args: Seq[String]): Map[String, String] = {
    args.grouped(2).collect {
      case Seq(rawKey, value) =>
        val key = if (rawKey.take(2) == "--") rawKey.drop(2) else rawKey
        key -> value
    }.toMap
  }
}
