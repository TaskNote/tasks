package controllers

import org.tasks.persistence.{DBReader, DBWriter, Task}
import javax.inject._

import org.tasks.Forms
import play.api._
import play.api.mvc._
import play.api.i18n.{I18nSupport, MessagesApi}

/**
  * Created by ergo on 2/1/17.
  */
@Singleton
class TaskController @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {


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


  def createTask = Action { implicit request =>
    Forms.taskForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.info(s"error in form $formWithErrors")
        // TODO need more error handling here
        BadRequest(views.html.tasks(List.empty[Task]))
      },
      (task: Task) => {
        Logger.info(s"got task $task from form")
        DBWriter.putTask(task)
        Redirect(routes.TaskController.get)
      }
    )
  }

  def addTaskForm() = Action {
    Ok(views.html.taskSubmit(Forms.taskForm))
  }
}
