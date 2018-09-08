package controllers

import dao.TodoDAO
import javax.inject.Inject
import models.Todo
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class Application @Inject()(todoDao: TodoDAO, cc: ControllerComponents)(
    implicit ec: ExecutionContext)
    extends AbstractController(cc) {
  implicit val writesTodo = Json.writes[Todo]
  def index = Action.async {
    todoDao.index().map { todos =>
      Ok(Json.toJson(todos))
    }
  }
}
