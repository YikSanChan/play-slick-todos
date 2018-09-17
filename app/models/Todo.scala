package models

import models.Label.Label
import models.Status.Status
import play.api.libs.json._

object Status extends Enumeration {
  type Status = Value
  val NotStarted, InProgress, Finished = Value

  // TODO(#19): implicit val format = Json.formatEnum(this)
  implicit val readsStatus = Reads.enumNameReads(Status)
  implicit val writesStatus = Writes.enumNameWrites
}

object Label extends Enumeration {
  type Label = Value
  val Easy, Medium, Hard, Coding, Design, Research = Value

  // TODO(#19): implicit val format = Json.formatEnum(this)
  implicit val readsLabel = Reads.enumNameReads(Label)
  implicit val writesLabel = Writes.enumNameWrites
}

object TodoFormat {
  implicit val writesTodo = Json.writes[Todo]
}

case class Todo(id: Long,
                content: String,
                priority: Int,
                status: Status,
                labels: List[Label])
