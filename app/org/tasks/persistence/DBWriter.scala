package org.tasks.persistence

import play.api.Logger

import scala.util.Try
import slick.driver.H2Driver.api._


/**
  * Created by jayonhuh on 1/30/17.
  */
object DBWriter {


  /**
    * Inserts `task` as a new row in the database
    *
    * @param task [[Task]] to be inserted.
    * @return a copy of `task` with the id allocated by the database.
    */
  def putTask(task: Task): Try[Task] = {

    // creating a DBIO that will get the id from the table for the task being put in the db and copy a
    // new Task instance with the ID to be returned.
    val action: DBIO[Task] = Tables.tasks returning Tables.tasks.map(_.id) into { (task, id) => task.copy(id = id) } += task
    DBConnection.run(action)

  }


  def updateTask(taskId: Int, newTask: Task): Try[Int] = {

    Logger.info(s"updating task with id $taskId")
    val q = for { task <- Tables.tasks if task.id === taskId } yield task
    // TODO not sure if we need this copy here, i think we want to ensure the ids remain the same
    DBConnection.run(q.update(newTask.copy(id = taskId)))
  }

  
  /**
    * Inserts `category` as a new row in the database
    *
    * @param category [[Category]] to be inserted.
    * @return a copy of `category` with the id allocated by the database.
    */
  def putCategory(category: Category): Try[Category] = {

    val action: DBIO[Category] = Tables.categories returning Tables.categories.map(_.id) into { (cat, id) => cat.copy(id = id) } += category
    DBConnection.run(action)

  }

  /**
    *
    * @param task the [[Task]] with a dependency.
    * @param dependency [[Task]] that are the dependencies for the task param above
    * @return a try that contains the [[Dependency]] that was inserted
    */
  def putDependency(task: Task, dependency: Task): Try[Dependency] = this.putDependencies(task, List(dependency)) map { _.head }

  /**
    *
    * @param task the task with the dependencies.
    * @param dependencies a list of tasks that are the dependencies for the task param above
    * @return a try that contains the List of dependencies that was inserted
    */
  def putDependencies(task: Task, dependencies: List[Task]): Try[List[Dependency]] = {
    val deps: List[Dependency] = dependencies map { dependsOn => Dependency(0, task.id, dependsOn.id) }

    val action: DBIO[Seq[Dependency]] = Tables.dependencies returning Tables.dependencies.map(_.id) into {
      (depe, id) => depe.copy(id = id)
    } ++= deps

    DBConnection.run(action) map { _.toList }
  }
}
