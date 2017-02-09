package org.tasks

import java.sql.Date

import org.tasks.persistence.{Dependency, Task, Category}
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


  private[this] def input2Dependency(inputFrom: Int, inputTo: Int): Dependency =
    Dependency.apply(taskID = inputFrom, dependencyTaskID = inputTo)

  private[this] def dependency2Input(dependency: Dependency): Option[(Int, Int)] = Option(dependency.taskID, dependency.dependencyTaskID)

  private[this] def input2Category(inputCategory: String): Category =
    Category.apply(description = inputCategory)

  private[this] def category2Input(category: Category): Option[String] = Option(category.description)




  val taskForm: Form[Task] = Form(
    mapping(
      "title" -> text,
      "note" -> text,
      "dueDate" -> optional(sqlDate)
    )(input2Task)(task2Input)
  )

  val dependencyForm: Form[Dependency] = Form(
    mapping(
      "ancestor" -> number,
      "child" -> number
    )(input2Dependency)(dependency2Input)
  )

  val categoryForm: Form[Category] = Form(
    mapping(
      "category" -> text
    )(input2Category)(category2Input)
  )
}
