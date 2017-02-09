package controllers

import org.tasks.persistence._
import javax.inject._

import org.tasks.Forms
import play.api._
import play.api.data.Form
import play.api.mvc._
import play.api.i18n.{I18nSupport, MessagesApi}

/**
  * Created by ergo on 2/1/17.
  */
@Singleton
// TODO rename to AppController or App or something that reflects this is global to all tables
class TaskController @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {


  /** @return all tasks in the database */
  def getAll = Action {
    val tasks: List[Task] = DBReader.getAllTasks()
    Ok(views.html.allTasks(tasks))
  }


  /**
    * Post endpoint for writing a [[Task]] directly. Useful for testing.
    *
    * @param title
    * @param note
    * @return
    */
  def put(title: String, note: String) = Action {
    val task: Task = Task(0, title, note)
    DBWriter.putTask(task)
    Ok(views.html.allTasks(DBReader.getAllTasks()))
  }


  /**
    * Get form for adding a task.
    * @return
    */
  def form = Action {
    Ok(views.html.submitTask(Forms.taskForm))
  }


  /**
    * Post endpoint for task creation.
    *
    * Constructs a [[Task]] from form input, writes that task to the database, then redirects to view all tasks.
    *
    * TODO a way to enter multiple tasks at once from the ui
    *
    * @return
    */
  def submit = Action { implicit request =>
    Forms.taskForm.bindFromRequest.fold(
      (formWithErrors: Form[Task]) => {
        Logger.error(s"error in form $formWithErrors")
        formWithErrors.errors foreach { err =>
          Logger.error(err.message)
        }

        // TODO need more error handling here
        BadRequest(views.html.allTasks(List.empty[Task]))
      },
      (task: Task) => {
        Logger.info(s"got task from form: $task")
        DBWriter.putTask(task)
        Redirect(routes.TaskController.getAll)
      }
    )
  }
}
