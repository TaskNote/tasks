package controllers

import javax.inject.Inject

import org.tasks.Forms
import org.tasks.persistence.{DBReader, DBWriter, Task, Category}
import play.api.Logger
import play.api._
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

/**
  * Created by ergo on 2/8/17.
  */
class CategoryController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /** @return all topics in the database */
  def getAll = Action {
    Ok(views.html.allCategories(DBReader.getAllCategories()))
  }


  def form = Action {
    Ok(views.html.submitCategory(Forms.categoryForm))
  }

  def submit = Action { implicit request =>
    Forms.categoryForm.bindFromRequest.fold(
      (formWithErrors: Form[Category]) => {
        Logger.error(s"error in form $formWithErrors")
        formWithErrors.errors foreach { err =>
          Logger.error(err.message)
        }

        // bad request redirect
        BadRequest(views.html.allTasks(List.empty[Task]))
      },
      (category: Category) => {
        Logger.info(s"got category from form: $category")

        DBWriter.putCategory(category)

        // TODO: redirect to a different page
        Redirect(routes.TaskController.getAll)
      }
    )
  }
}
