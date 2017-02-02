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

}


