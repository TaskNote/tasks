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

  // functions used for submitting tasks
  private[this] def input2Task(inputTitle: String, inputNote: String, inputDueDate: Option[Date],
                               inputCategoryID: Option[Int]): Task = {
    Task.apply(title = inputTitle, note = inputNote, dueDate = inputDueDate, categoryID = inputCategoryID)
  }

  private[this] def task2Input(task: Task): Option[(String, String, Option[Date], Option[Int])] = {
    Option((task.title, task.note, task.dueDate, task.categoryID ))
  }


  private[this] def input2EditedTask(inputId: Int,
                                     inputTitle: String,
                                     inputNote: String,
                                     inputCategoryID: Option[Int],
                                     inputDueDate: Option[Date],
                                     inputIsDone: Boolean): Task = {
    Task.apply(id = inputId, title = inputTitle, note = inputNote, categoryID = inputCategoryID, dueDate = inputDueDate, isDone = inputIsDone)
  }


  private[this] def editedTask2Input(task: Task): Option[(Int, String, String, Option[Int], Option[Date], Boolean)] = {
    Option(task.id, task.title, task.note, task.categoryID, task.dueDate, task.isDone)
  }


  private[this] def input2Dependency(inputFrom: Int, inputTo: Int): Dependency =
    Dependency.apply(taskID = inputFrom, dependencyTaskID = inputTo)

  private[this] def dependency2Input(dependency: Dependency): Option[(Int, Int)] = Option(dependency.taskID, dependency.dependencyTaskID)

  private[this] def input2Category(inputCategory: String): Category =
    Category.apply(description = inputCategory)

  private[this] def category2Input(category: Category): Option[String] = Option(category.description)




  // form used for submitting tasks
  val taskForm: Form[Task] = Form(
    mapping(
      "title" -> text,
      "note" -> text,
      "dueDate" -> optional(sqlDate),
      "categoryID" -> optional(number)
    )(input2Task)(task2Input)
  )


  // form used for editing tasks
  val taskEditForm: Form[Task] = Form(
    mapping(
      // TODO we need a way to hide this or otherwise make it uneditable
      "id" -> number,
      "title" -> text,
      "note" -> text,
      "categoryID" -> optional(number),
      "dueDate" -> optional(sqlDate),
      "isDone" -> boolean
    )(input2EditedTask)(editedTask2Input)
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
