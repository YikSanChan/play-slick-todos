package models

import models.Status.Status
import play.api.libs.json.{Format, JsResult, JsValue, Json}

object Status extends Enumeration {
  type Status = Value
  val NotStarted, InProgress, Finished = Value

  implicit val formats: Format[Status] = new Format[Status] {
    override def reads(json: JsValue): JsResult[Status] =
      json.validate[String].map(Status.withName)

    override def writes(o: Status): JsValue = Json.toJson(o.toString)
  }
}

case class Todo(id: Long, content: String, priority: Int, status: Status)
