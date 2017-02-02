package org.tasks.persistence

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

  
  /**
    * Inserts `taskTopic` as a new row in the database
    *
    * @param taskTopic [[TaskTopic]] to be inserted.
    * @return a copy of `taskTopic` with the id allocated by the database.
    */
  def putTaskTopic(taskTopic: TaskTopic): Try[TaskTopic] = {

    val action: DBIO[TaskTopic] = Tables.topics returning Tables.topics.map(_.id) into { (taskTopic, id) => taskTopic.copy(id = id) } += taskTopic
    DBConnection.run(action)

  }

  /**
    *
    * @param task the task with the dependencies.
    * @param dependencies a list of tasks that are the dependencies for the task param above
    * @return a try that contains the List of dependencies that was inserted
    */
  def putDependency(task: Task, dependencies: List[Task]): Try[List[Dependency]] = {
    
    val deps: List[Dependency] = dependencies map { dependsOn => Dependency(0, task.id, dependsOn.id) }
    

    // TODO use batch insert
    val actions = deps map { dep =>
      Tables.dependencies returning Tables.dependencies.map(_.id) into { (depe, id) => depe.copy(id = id) } += dep
    }
    Try(actions map { DBConnection.run(_).get })
  }
}
