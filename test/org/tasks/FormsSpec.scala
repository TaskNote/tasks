package org.tasks

import org.junit.Test
import org.scalatest.Matchers
import org.scalatest.junit.JUnitSuite
import org.tasks.persistence.Task

/**
  * Created by ergo on 2/2/17.
  */
class FormsSpec extends JUnitSuite with Matchers {


  @Test
  def taskForm(): Unit = {
    val data: Map[String, String] = Map("title" -> "some title", "note" -> "feed the cats", "isDone" -> "false", "dueDate" ->  "2017-07-10")
    val task: Task = Forms.taskForm.bind(data).get
    task.title should be ("some title")
    task.note should be ("feed the cats")
  }
}
