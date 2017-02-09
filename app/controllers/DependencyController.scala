package controllers

import javax.inject.Inject

import org.tasks.Forms
import org.tasks.persistence.{DBReader, DBWriter, Dependency, Task}
import play.api._
import play.api.Logger
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

/**
  * Created by ergo on 2/8/17.
  */
class DependencyController @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /** @return all dependencies in the database. */
  def getAll = Action {
    Ok(views.html.allDependencies(DBReader.getAllDependencies()))
  }


  // TODO redirect to viewing all dependencies for task
  def form = Action {
    Ok(views.html.submitDependency(Forms.dependencyForm))
  }


  /**
    * Post endpoint for dependency creation.
    * @return
    */
  def submit = Action { implicit request =>
    Forms.dependencyForm.bindFromRequest.fold(
      (formWithErrors: Form[Dependency]) => {
        Logger.error(s"error in form $formWithErrors")
        formWithErrors.errors foreach { err =>
          Logger.error(err.message)
        }

        // TODO need more error handling here
        BadRequest(views.html.allTasks(List.empty[Task]))
      },
      (dependency: Dependency) => {
        Logger.info(s"got dependency from form: $dependency")
        // TODO error handling
        val ancestor: Task = DBReader.getTaskById(dependency.taskID).get
        val child: Task = DBReader.getTaskById(dependency.dependencyTaskID).get
        DBWriter.putDependency(ancestor, child)

        Redirect(routes.DependencyController.getAll)
      }
    )
  }
}
