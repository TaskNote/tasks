package org.tasks.persistence

import java.sql.Date

import org.junit.{Before, Test}
import org.scalatest.Matchers
import org.scalatest.junit.JUnitSuite

/**
  * Created by jayonhuh on 1/30/17.
  */
class DBSpec extends JUnitSuite with Matchers {

  @Before
  def createSchema(): Unit = {
    DBConnection.createSchema()
  }

  @Test
  def writeTask(): Unit = {
    // TODO topicId  should be untitled if not given a topic

    // getting current date in a value to use
    val date = new Date(System.currentTimeMillis())

    // Creating two tasks to add to db
    val task: Task = Task(0, title = "to do", note = "some note", dueDate = Option(date))
    val anotherTask: Task = Task(0, title = "to do", note = "some other note")
    val insertedTask: Task = DBWriter.putTask(task).get
    val anotherInsertedTask: Task = DBWriter.putTask(anotherTask).get

    // verify that the IDs of the tasks are not equal.
    insertedTask.id should not be anotherInsertedTask.id

    // creating an identical task to 'task' should fail
    intercept[Exception] { DBWriter.putTask(task).get }
  }

  @Test
  def writeTaskTopic(): Unit = {

    // Create two taskTopics to add to db
    val taskTopic: TaskTopic = TaskTopic(0, description = "test")
    val taskTopic2: TaskTopic = TaskTopic(0, description = "test2")

    // insert both to DB
    val insertedTopic: TaskTopic = DBWriter.putTaskTopic(taskTopic).get
    val insertedTopic2: TaskTopic = DBWriter.putTaskTopic(taskTopic2).get

    // confirm that the ID are different
    insertedTopic.id should not be insertedTopic2.id



  }

  @Test
  def readTask(): Unit = {

    // Try to read all tasks
    val allTasks: List[Task] = DBReader.getAllTasks()

    // verify that the list is not empty
    allTasks.isEmpty should not be true

    // Create a task with a topic ID
    val task: Task = Task(0, title = "to do", topicID = Option(3), note = "some note")
    val insertedTask: Task = DBWriter.putTask(task).get

    // verify that we can return the singular task and that it is the one created above
    val listWithTask: List[Task] = DBReader.getTasksForTopicID(insertedTask.topicID.get)
    listWithTask foreach {task: Task => task.id should be (insertedTask.id) }

  }


}
