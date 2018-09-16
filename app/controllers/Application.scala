package controllers

import dao.TodoDAO
import javax.inject.Inject
import models.TodoFormat._
import models.TodoRequestFormats._
import models.{CreateTodoRequest, UpdateTodoRequest}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class Application @Inject()(todoDao: TodoDAO, cc: ControllerComponents)(
    implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def index = Action.async {
    todoDao.index().map { todos =>
      Ok(Json.toJson(todos))
    }
  }

  def create = Action.async(parse.json[CreateTodoRequest]) { implicit request =>
    todoDao.create(request.body.content,
                   request.body.priority,
                   request.body.status) map { todo =>
      Ok(Json.toJson(todo))
    }
  }

  def details(id: Long) = Action.async {
    todoDao.get(id) map {
      case Some(todo) => Ok(Json.toJson(todo))
      case None       => NotFound
    }
  }

  def update(id: Long) = Action.async(parse.json[UpdateTodoRequest]) {
    implicit request =>
      todoDao.update(id,
                     request.body.content,
                     request.body.priority,
                     request.body.status) map {
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
