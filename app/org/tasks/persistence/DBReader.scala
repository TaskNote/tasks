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

  // TODO return an option?
  def getTaskById(id: Int): Option[Task] = {
    val query = Tables.tasks filter { _.id === id }
    DBConnection.run(query.result).get.headOption
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
    * This method will return all tasks that are direct dependencies for the taskID provided. Note, this does NOT bring
    * back the tree of tasks.
    *
    * @param taskID the task ID of the task the dependencies are for
    *
    * @return the dependency tasks for the taskID given
    *
    */

  //TODO Write tests for this
  def getDependencyTasks(taskID: Int): List[Task] = {

    // Create query
    val query: Query[Tasks, Task, Seq] = for {

      dep <- Tables.dependencies if dep.taskID === taskID
      task <- Tables.tasks if task.id === dep.dependencyTaskID

    } yield task

    DBConnection.run(query.result).get.toList
  }

  /**
    * This method will return all distinct transitive dependency tasks for the taskID provided
    * (all child dependencies of the task with the given id.)
    *
    * @param taskID the ID of the [[Task]] to get the transitive dependency tasks for
    *
    * @param transitives helper parameter used solely by the recursion in the function. DO NOT POPULATE.
    *                    TODO we probably want to use a nested helper def instead so we dont pollute the api.
    *
    * @return a list of tasks that represent all of the transitive dependency tasks for the task ID
    *
    *
    * @TODO Write Tests
    */

  def getTransitiveDependencyTasks(taskID: Int, transitives: List[Task] = List.empty): List[Task] = {

    val allTasks = DBReader.getAllTasks()

    println("TASKS HERE RIGHT HERE")
    allTasks map(task => println(task.id))

    val allDependencies = DBReader.getAllDependencies()

    allDependencies map(dep => println(dep.id))


    // Create Query
    val query: Query[Tasks, Task, Seq] = for {

      // querying for all dependencies where dependency's task ID equals task ID
      dep <- Tables.dependencies if dep.taskID === taskID

      // then querying for tasks where the dependencies from aboves' dependencyTaskID is equal to the task's ID
      task <- Tables.tasks if task.id === dep.dependencyTaskID

    } yield task


    // get the query's result
    val dependencyList: List[Task] = DBConnection.run(query.result).get.toList

    dependencyList map(task => println(task.id))

    // if we are at the base case where we cannot traverse the dependencies further, return all distinct transitives
    if (dependencyList.isEmpty) transitives

    // otherwise, call itself for each dependency and add them to the list of transitive tasks
    else {
      val returnList = dependencyList flatMap{ task => this.getTransitiveDependencyTasks(task.id, transitives ++ dependencyList) }
      returnList.distinct
    }


  }

  /**
    * This method will return all distinct transitive dependents of the task for the taskID provided
    * (all ancestral tasks that depend on the [[Task]] with the given id.)
    *
    * @param taskID the ID of the [[Task]] to get the transitive dependents for
    *
    * @param transitives helper parameter used solely by the recursion in the function. DO NOT POPULATE.
    *                    TODO we probably want to use a nested helper def instead so we dont pollute the api.
    *
    * @return a list of transitive dependent tasks
    *
    *
    * @TODO Write tests
    */

  def getTransitiveDependentTasks(taskID: Int, transitives: List[Task] = List.empty): List[Task] = {

    // Create Query
    val query: Query[Tasks, Task, Seq] = for {

      // query for all dependencies where the dependencies dependency task ID is equal to taskID
      dep <- Tables.dependencies if dep.dependencyTaskID === taskID

      // get all tasks where its task ID is equal to the taskID from the dependencies above
      task <- Tables.tasks if dep.taskID === task.id

    } yield task

    // get query result
    val dependentList: List[Task] = DBConnection.run(query.result).get.toList

    // if the list is empty return the distinct tasks in the transitives list
    if (dependentList.isEmpty) transitives.distinct

    // otherwise call itself for each tasks in the dependent list and add its results to the transitive list
    else {
      val children = dependentList flatMap{ task => this.getTransitiveDependentTasks(task.id) }
      transitives ++ children

    }


  }

  /**
    * This method will return all tasks that are direct dependents of the task, which will be represented by the task ID.
    *
    * @param taskID ID of the task we are finding dependents for
    *
    * @return a list of tasks that are the dependents
    *
    */

  def getDependentTasks(taskID: Int): List[Task] = {

    // Create query
    val query: Query[Tasks, Task, Seq] = for {

      dependency <- Tables.dependencies if dependency.dependencyTaskID === taskID
      task <- Tables.tasks if task.id === dependency.taskID


    } yield task

    DBConnection.run(query.result).get.toList


  }

}


