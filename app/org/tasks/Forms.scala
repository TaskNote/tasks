package org.tasks

import java.sql.Date

import org.tasks.persistence.Task
import play.api.data.Form
import play.api.data.Forms._

/**
  * Created by ergo on 2/2/17.
  */
// TODO rename, conflicts with play classes
object Forms {

  private[this] def input2Task(inputTitle: String, inputNote: String, inputDueDate: Option[Date]): Task =
    Task.apply(title = inputTitle, note = inputNote)

  private[this] def task2Input(task: Task) = Option((task.title, task.note, task.dueDate ))

  val taskForm: Form[Task] = Form(
    mapping(
      "title" -> text,
      "note" -> text,
      "dueDate" -> optional(sqlDate)
    )(input2Task)(task2Input)
  )
}
