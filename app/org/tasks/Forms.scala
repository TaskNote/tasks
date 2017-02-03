package org.tasks

import org.tasks.persistence.Task
import play.api.data.Form
import play.api.data.Forms._

/**
  * Created by ergo on 2/2/17.
  */
// TODO rename, conflicts with play classes
object Forms {

  def input2Task(title: String, note: String): Task = Task.apply(title = title, note = note)
  def task2Input(task: Task) = Option((task.title, task.note))

  val taskForm: Form[Task] = Form(
    mapping(
      "title" -> text,
      "note" -> text
    )(input2Task)(task2Input)
  )
}
