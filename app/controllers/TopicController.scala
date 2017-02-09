package controllers

import javax.inject.Inject

import org.tasks.Forms
import org.tasks.persistence.{DBReader, DBWriter, Task, TaskTopic}
import play.api.Logger
import play.api._
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

/**
  * Created by ergo on 2/8/17.
  */
class TopicController @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /** @return all topics in the database */
  def getAll = Action {
    Ok(views.html.allTopics(DBReader.getAllTaskTopic()))
  }


  def form = Action {
    Ok(views.html.submitTopic(Forms.topicForm))
  }

  def submit = Action { implicit request =>
    Forms.topicForm.bindFromRequest.fold(
      (formWithErrors: Form[TaskTopic]) => {
        Logger.error(s"error in form $formWithErrors")
        formWithErrors.errors foreach { err =>
          Logger.error(err.message)
        }

        // bad request redirect
        BadRequest(views.html.allTasks(List.empty[Task]))
      },
      (topic: TaskTopic) => {
        Logger.info(s"got topic from form: $topic")

        DBWriter.putTaskTopic(topic)

        // TODO: redirect to a different page
        Redirect(routes.TaskController.getAll)
      }
    )
  }
}
