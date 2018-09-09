package controllers

import dao.TodoDAO
import javax.inject.Inject
import models.{Todo, TodoRequest}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class Application @Inject()(todoDao: TodoDAO, cc: ControllerComponents)(
    implicit ec: ExecutionContext)
    extends AbstractController(cc) {
  implicit val writesTodo = Json.writes[Todo]
  implicit val readsTodoRequest =
    Json.using[Json.WithDefaultValues].reads[TodoRequest]

  def index = Action.async {
    todoDao.index().map { todos =>
      Ok(Json.toJson(todos))
    }
  }

  def create = Action.async(parse.json[TodoRequest]) { implicit request =>
    todoDao.create(request.body.content, request.body.priority) map { todo =>
      Ok(Json.toJson(todo))
    }
  }

  def details(id: Long) = Action.async {
    todoDao.get(id) map {
      case Some(todo) => Ok(Json.toJson(todo))
      case None       => NotFound
    }
  }

  def update(id: Long) = Action.async(parse.json[TodoRequest]) {
    implicit request =>
      todoDao.update(id, request.body.content, request.body.priority) map {
        case Some(todo) => Ok(Json.toJson(todo))
        case None       => InternalServerError
      }
  }

  def delete(id: Long) = Action.async {
    todoDao.delete(id) map {
      case 0 => BadRequest
      case _ => Ok
    }
  }
}
