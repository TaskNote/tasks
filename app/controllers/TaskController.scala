package controllers

import org.tasks.persistence.{DBReader, DBWriter, Task}
import javax.inject._

import org.tasks.Forms
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

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


  def task = Action { implicit request =>
    Forms.taskForm.bindFromRequest.fold(
      formWithErrors => {
        // TODO need more error handling here
        BadRequest(views.html.tasks(List.empty[Task]))
      },
      (task: Task) => {
        DBWriter.putTask(task)
        Redirect(routes.TaskController.get)
      }
    )
  }

  def submitTask() = Action {
    Ok(views.html.taskSubmit(Forms.taskForm))
  }
}
