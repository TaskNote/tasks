import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {

    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }

  }

  "HomeController" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Your new application is ready.")
    }

  }

  "CountController" should {

    "return an increasing count" in {
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "0"
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "1"
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "2"
    }

  }


  // TODO not sure how this will interact with other test wrt to the db
  "TaskController" should {
    "write a task to the database" in {
      val tasks = route(app, FakeRequest(POST, "/tasks/internet/eating")).get
      status(tasks) mustBe OK
      contentType(tasks) mustBe Some("text/html")
      contentAsString(tasks) must include regex "Task\\(\\d+,internet,eating,None,"

      val moreTasks = route(app, FakeRequest(POST, "/tasks/internet/eatingAgain")).get
      status(moreTasks) mustBe OK
      contentType(moreTasks) mustBe Some("text/html")
      contentAsString(moreTasks) must (include regex "Task\\(\\d+,internet,eating" and include regex "Task\\(\\d+,internet,eatingAgain")
    }

    "write a task using a form" in {

    }
  }
}
