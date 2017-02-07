package org.tasks

import java.sql.Date

import org.tasks.persistence.{Dependency, Task, TaskTopic}
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

  private[this] def input2Topic(inputTopic: String): TaskTopic =
    TaskTopic.apply(description = inputTopic)

  private[this] def topic2Input(topic: TaskTopic): Option[String] = Option(topic.description)




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

  val topicForm: Form[TaskTopic] = Form(
    mapping(
      "topic" -> text
    )(input2Topic)(topic2Input)
  )


}
