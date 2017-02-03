package org.tasks.persistence

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import slick.driver.H2Driver.api._
import scala.util.Try

/**
  * Created by jayonhuh on 1/30/17.
  */
object DBConnection {

  val db = Database.forURL("jdbc:h2:mem:test;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
  println("opened connection")

  this.createSchema()

  /** Run takes in a sequence of actions, in the DBIO
    *
    * @param action is a sequence of actions
    * @tparam T The type of parameter DBIO is of.
    * @return A try with the type parameter.
    */
  def run[T](action: DBIO[T]): Try[T] = Try {
    val nayvadiusDeMunWilburn: Future[T] = DBConnection.db.run(action)
    Await.result(nayvadiusDeMunWilburn, Duration.Inf)
  }


  /** Creates schema. Will not throw an exception if the schema already exists. */
  def createSchema(): Unit = {
    val action = (Tables.tasks.schema ++ Tables.dependencies.schema ++ Tables.topics.schema).create
    this.run(action) recover {
      case error: Exception if !error.getMessage.matches("Table .+ already exists[\\S\\s]]") => throw error
    }
  }
}
