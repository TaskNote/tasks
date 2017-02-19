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
    val task: Task = Task(title = "to do", note = "some note", dueDate = Option(date))
    val anotherTask: Task = Task(title = "to do", note = "some other note")
    val insertedTask: Task = DBWriter.putTask(task).get
    val anotherInsertedTask: Task = DBWriter.putTask(anotherTask).get

    // verify that the IDs of the tasks are not equal.
    insertedTask.id should not be anotherInsertedTask.id

    // creating an identical task to 'task' should fail
    intercept[Exception] { DBWriter.putTask(task).get }
  }


  /** Checks that we can update a task in the db. */
  @Test
  def updateTask(): Unit = {
    val task: Task = DBWriter.putTask(Task(title = "to do", note = "some note")).get
    DBWriter.updateTask(task.id, task.copy(title = "updated title"))

    val updatedTask: Task = DBReader.getTaskById(task.id).get
    updatedTask.id should be (task.id)
    updatedTask.title should be ("updated title")
  }


  @Test
  def writeCategory(): Unit = {

    // Create two taskTopics to add to db
    val category: Category = Category(0, description = "test")
    val category2: Category = Category(0, description = "test2")

    // insert both to DB
    val insertedCategory: Category = DBWriter.putCategory(category).get
    val insertedCategory2: Category = DBWriter.putCategory(category2).get

    // confirm that the ID are different
    insertedCategory.id should not be insertedCategory2.id



  }

  @Test
  def readTask(): Unit = {

    // Try to read all tasks
    val allTasks: List[Task] = DBReader.getAllTasks()

    // verify that the list is not empty
    allTasks.isEmpty should not be true

    // Create a task with a topic ID
    val task: Task = Task(0, title = "to do", categoryID = Option(3), note = "some note")
    val insertedTask: Task = DBWriter.putTask(task).get

    // verify that we can return the singular task and that it is the one created above
    val listWithTask: List[Task] = DBReader.getTasksForCategoryID(insertedTask.categoryID.get)
    listWithTask foreach {task: Task => task.id should be (insertedTask.id) }

  }


  @Test
  // TODO more robust tests
  def dependencies(): Unit = {
    //Add two tasks and add a dependency between one another
    val task: Task = Task(title = "to do", categoryID = Option(3), note = "some note")
    val anotherTask: Task = Task(title = "to do 2", categoryID = Option(3), note = "some note again")
    val parent: Task = DBWriter.putTask(task).get
    val child: Task = DBWriter.putTask(anotherTask).get

    val insertedDependency: Dependency = DBWriter.putDependency(parent, child).get

    // Verify that the dependency was created correctly
    insertedDependency.taskID should be (parent.id)
    insertedDependency.dependencyTaskID should be (child.id)

    // Test getting the child and verify it is the correct task
    val directChildren: List[Task] = DBReader.getDirectChildren(parent.id)
    directChildren should have length 1
    val insertedChild: Task = directChildren.head
    insertedChild.id should be (child.id)

    // Test getting the parent and verify it is the correct task
    val directParents: List[Task] = DBReader.getDirectParents(child.id)
    directParents should have length 1
    val insertedParent: Task = directParents.head
    insertedParent.id should be (parent.id )

    // add a new dependency to the child
    val insertedGrandchild: Task = DBWriter.putTask(
      Task(
        title = "another one - dj khaled",
        categoryID = Option(3),
        note = " whatever"
      )
    ).get

    DBWriter.putDependency(child, insertedGrandchild)

    // get a list of tasks by calling the transitive dependency task function
    val descendants: List[Task] = DBReader.getDescendants(insertedParent.id)
    descendants map{ _.id } equals List(child.id, insertedGrandchild.id) should be (true)

    val ancestors: List[Task] = DBReader.getAncestors(insertedGrandchild.id)
    println(ancestors)
    ancestors map { _.id } equals List(child.id, parent.id) should be (true)
  }
}
