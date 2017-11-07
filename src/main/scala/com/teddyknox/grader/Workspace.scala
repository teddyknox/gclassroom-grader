package com.teddyknox.grader

import java.io.File

import com.google.api.services.classroom.Classroom

class Workspace(path: File)(implicit classroom: Classroom) {
  if (!path.isDirectory) {
    throw new IllegalArgumentException("Workspace path should be a directory.")
  }

  if (!path.exists() && !path.mkdir()) {
    throw new IllegalArgumentException("Workspace path could not be created, please check permissions and try again.")
  }

  def listCourses: Seq[Course] = {
    path.listFiles().filter(_.isDirectory).map(f => Course(f.getName.toInt))
  }

  def saveCourse(course: Course): Unit = {
    val courseDir = new File(path.getPath + File.separator + course.id.toString)
    val created = courseDir.mkdir()
    if (!created) {
      throw new IllegalStateException(s"Insufficient permissions for creating course directory ${courseDir.getPath}")
    }
  }
}
