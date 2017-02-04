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

  private[this] def input2Task(inputTitle: String, inputNote: String, inputIsDone: Boolean, inputDueDate: Date): Task =
    Task.apply(title = inputTitle, note = inputNote, isDone = inputIsDone)
  // todo, dont call .get
  private[this] def task2Input(task: Task) = Option((task.title, task.note, task.isDone, task.dueDate.get ))

  val taskForm: Form[Task] = Form(
    mapping(
      "title" -> text,
      "note" -> text,
      "isDone" -> boolean,
      "dueDate" -> sqlDate
    )(input2Task)(task2Input)
  )
}
