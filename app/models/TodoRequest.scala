package models

import models.Label.Label
import models.Status.Status
import play.api.libs.json.Json

object TodoRequestFormats {
  implicit val readsCreateTodoRequest =
    Json.using[Json.WithDefaultValues].reads[CreateTodoRequest]
  implicit val readsUpdateTodoRequest = Json.reads[UpdateTodoRequest]
}

case class CreateTodoRequest(content: String,
                             priority: Int = 3,
                             status: Status = Status.NotStarted,
                             labels: List[Label] = List.empty)

case class UpdateTodoRequest(content: String,
                             priority: Int,
                             status: Status,
                             labels: List[Label])
