package org.tasks.persistence

import slick.driver.H2Driver.api._

/**
  * Created by jayonhuh on 1/30/17.
  */
object DBReader {


  /**
    * This method will call a query to the DB and return all TaskTopics No Param.
    *
    * @return a list of TaskTopic tables.
    */
  def getAllTaskTopic(): List[TaskTopic] = {

    // creating a query statement, where we are querying for all TaskTopics, which will be returned
    // as a list of TaskTopic, the case class.
    val query: Query[TaskTopics, TaskTopic, Seq] = for {
      taskTopic <- Tables.topics
    } yield taskTopic

    DBConnection.run(query.result).get.toList

  }

  /**
    *
    * This method will return all tasks given a taskTopic ID.
    *
    * @param topicID the integer value that represents the ID of the topic
    *
    * @return a list of topics.
    */

  def getTasksForTopicID(topicID: Int): List[Task] = {

    // Creating a query statement to query for all tasks where the task.topID id is equal to the one provided
    val query: Query[Tasks, Task, Seq] = for {

      // for all tasks if task's topic ID is equal to topic ID in param
      task <- Tables.tasks if task.topicID === topicID

    } yield task

    DBConnection.run(query.result).get.toList

  }

  /**
    * This method will call a query to the DB and return all Tasks. No param.
    *
    * @return a list of Task tables.
    */
  def getAllTasks(): List[Task] = {

    // creating a query statement, where we are querying for all Tasks, which will be returned
    // as a list of Task, the case class.
    val query: Query[Tasks, Task, Seq] = for {
      task <- Tables.tasks
    } yield task

    DBConnection.run(query.result).get.toList

  }


  /**
    * This method will call a query to the DB and return all Dependencies. No Param.
    *
    * @return a list of Dependency tables.
    */
  def getAllDependencies(): List[Dependency] = {

    // creating a query statement, where we are querying for all Dependencies, which will be returned
    // as a list of Dependency, the case class.
    val query: Query[Dependencies, Dependency, Seq] = for {
      dependency <- Tables.dependencies
    } yield dependency

    DBConnection.run(query.result).get.toList

  }

  /**
    * This method will return all tasks that are dependencies for the taskID provided. Note, this does NOT bring
    * back the tree of tasks.
    *
    * @param taskID the task ID of the task the dependencies are for
    *
    * @return the dependency tasks for the taskID given
    *
    */

  //TODO Write tests for this 
  def getDependencyTasksforTaskID(taskID: Int): List[Task] = {

    // Create query
    val query: Query[Tasks, Task, Seq] = for {

      dep <- Tables.dependencies if dep.taskID === taskID
      task <- Tables.tasks if task.id === dep.dependencyTaskID

    } yield task

    DBConnection.run(query.result).get.toList
  }

  /**
    * This method will return all tasks that are dependent on the task, which will be represented by the task ID.
    * This
    */


}


