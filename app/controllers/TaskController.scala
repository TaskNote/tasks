package controllers

import org.tasks.persistence.{DBReader, DBWriter, Task}
import javax.inject._

import play.api._
import play.api.mvc._

/**
  * Created by ergo on 2/1/17.
  */
@Singleton
class TaskController @Inject() extends Controller {


  /** @return all tasks in the database */
  def get = Action {
    val tasks: List[Task] = DBReader.getAllTasks()
    Ok(views.html.tasks(tasks))
  }


  def put(title: String, note: String) = Action {
    val task: Task = Task(0, title, note)
    DBWriter.putTask(task)
    Ok(views.html.tasks(DBReader.getAllTasks()))
  }
}
