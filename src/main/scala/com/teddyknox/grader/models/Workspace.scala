package com.teddyknox.grader.models

import java.io.{BufferedOutputStream, File, FileOutputStream}
import java.nio.file.{Files, Path, Paths}

import com.google.api.services.classroom.Classroom
import com.google.api.services.drive.Drive

import scala.collection.JavaConverters._

object Workspace {
  private val HOME_PATH = ".google-classroom-grader"
}

class Workspace()(implicit classroom: Classroom, drive: Drive) {
  import Workspace._

  val homeDir = Paths.get(HOME_PATH).toAbsolutePath
  setupDirectory(homeDir)

  /* Returns the list of userIds with a valid coursework submission. */
  def download(courseId: String, courseWorkId: String): Set[String] = {
    val courseDir = homeDir.resolve(courseId)
    setupDirectory(courseDir)
    val courseWorkDir = courseDir.resolve(courseWorkId)
    setupDirectory(courseWorkDir)

    val submissions =
      classroom.courses().courseWork().studentSubmissions()
        .list(courseId, courseWorkId).execute()
        .getStudentSubmissions.asScala
        .filter { submission =>
          submission.getAssignmentSubmission != null && submission.getAssignmentSubmission.getAttachments != null
        }.flatMap { submission =>
          submission.getAssignmentSubmission
            .getAttachments.asScala
            .flatMap { attachment =>
              Option(attachment.getDriveFile)}
            .filter(_.getTitle.toLowerCase.matches(".*\\.java$"))
            .map { attachment => (submission.getUserId, attachment) }
          }

    submissions.foreach { case (userId, dfile) =>
      val userDir = courseWorkDir.resolve(userId)
      setupDirectory(userDir)

      val submissionFile = userDir.resolve(dfile.getTitle)
      if (!Files.exists(submissionFile)) {
        println(s"Downloading $submissionFile...")
        val outputStream = new BufferedOutputStream(Files.newOutputStream(submissionFile))
        try {
          drive.files()
            .get(dfile.getId)
            .setAlt("media")
            .executeMediaAndDownloadTo(outputStream)
        } finally {
          outputStream.close()
        }
      }
    }

    submissions.map {
      case (userId, _) => userId
    }.toSet
  }

  private def setupDirectory(path: Path): Unit = {
    if (Files.exists(path) && !Files.isDirectory(path)) {
      Files.delete(path)
    }

    if (!Files.exists(path)) {
      Files.createDirectory(path)
    }
  }
}
