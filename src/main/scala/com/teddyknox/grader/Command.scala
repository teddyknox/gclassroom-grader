package com.teddyknox.grader

trait Command {
  def parseKeyValueArgs(args: Seq[String]): Map[String, String] = {
    args.grouped(2).collect {
      case Seq(rawKey, value) =>
        val key = if (rawKey.take(2) == "--") rawKey.drop(2) else rawKey
        key -> value
    }.toMap
  }
}
