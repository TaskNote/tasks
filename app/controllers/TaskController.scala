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
  def get = Action {
    val tasks: List[Task] = DBReader.getAllTasks()
    Ok(views.html.tasks(tasks))
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
    Ok(views.html.tasks(DBReader.getAllTasks()))
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
  def createTask = Action { implicit request =>
    Forms.taskForm.bindFromRequest.fold(
      (formWithErrors: Form[Task]) => {
        Logger.error(s"error in form $formWithErrors")
        formWithErrors.errors foreach { err =>
          Logger.error(err.message)
        }

        // TODO need more error handling here
        BadRequest(views.html.tasks(List.empty[Task]))
      },
      (task: Task) => {
        Logger.info(s"got task from form: $task")
        DBWriter.putTask(task)
        Redirect(routes.TaskController.get)
      }
    )
  }


  /**
    * Get form for adding a task.
    * @return
    */
  def addTaskForm() = Action {
    Ok(views.html.taskSubmit(Forms.taskForm))
  }



  def allDependencies = Action {
    Ok(views.html.allDependencies(DBReader.getAllDependencies()))
  }



  def createDependency = Action { implicit request =>
    Forms.dependencyForm.bindFromRequest.fold(
      (formWithErrors: Form[Dependency]) => {
        Logger.error(s"error in form $formWithErrors")
        formWithErrors.errors foreach { err =>
          Logger.error(err.message)
        }

        // TODO need more error handling here
        BadRequest(views.html.tasks(List.empty[Task]))
      },
      (dependency: Dependency) => {
        Logger.info(s"got dependency from form: $dependency")
        // TODO error handling
        val ancestor: Task = DBReader.getTaskById(dependency.taskID).get
        val child: Task = DBReader.getTaskById(dependency.dependencyTaskID).get
        DBWriter.putDependency(ancestor, child)

        Redirect(routes.TaskController.allDependencies)
      }
    )
  }


  def allTopics = Action {
    Ok(views.html.allTopics(DBReader.getAllTaskTopic()))
  }


  def addTopicForm() = Action {
    Ok(views.html.topicsSubmit(Forms.topicForm))
  }

  def createTopic = Action { implicit request =>
    Forms.topicForm.bindFromRequest.fold(
      (formWithErrors: Form[TaskTopic]) => {
        Logger.error(s"error in form $formWithErrors")
        formWithErrors.errors foreach { err =>
          Logger.error(err.message)
        }

        // bad request redirect
        BadRequest(views.html.tasks(List.empty[Task]))
      },
      (topic: TaskTopic) => {
        Logger.info(s"got topic from form: $topic")

        DBWriter.putTaskTopic(topic)

        // TODO: redirect to a different page
        Redirect(routes.TaskController.get)

      }

    )

  }




  // TODO redirect to viewing all dependencies for task
  def addDependencyForm() = Action {
    Ok(views.html.dependencySubmit(Forms.dependencyForm))
  }
}
