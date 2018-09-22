package dao

import models.Label.Label
import models.Status.Status
import models.Todo
import slick.lifted.Tag
import utils.MyPostgresProfile.api._

class TodosTable(tag: Tag) extends Table[Todo](tag, "todo") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def content = column[String]("content")
  def priority = column[Int]("priority")
  def status = column[Status]("status")
  def labels = column[List[Label]]("labels", O.Default(List.empty))

  def * = (id, content, priority, status, labels).mapTo[Todo]
}
