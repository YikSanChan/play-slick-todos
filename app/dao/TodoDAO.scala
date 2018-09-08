package dao

import javax.inject.Inject
import models.Todo
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class TodoDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val todos = TableQuery[TodosTable]

  def index(): Future[Seq[Todo]] = db.run(todos.result)

  private class TodosTable(tag: Tag) extends Table[Todo](tag, "todo") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def content = column[String]("content")

    def * = (id, content) <> (Todo.tupled, Todo.unapply)
  }
}
