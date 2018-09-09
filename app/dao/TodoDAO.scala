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

  private lazy val byId = (id: Long) => todos.filter(_.id === id)

  def index(): Future[Seq[Todo]] = db.run {
    todos.result
  }

  def create(content: String): Future[Todo] = {
    val todosReturningRow = todos returning todos.map(_.id) into { (todo, id) =>
      todo.copy(id = id)
    }
    db.run {
      todosReturningRow += Todo(0L, content)
    }
  }

  def get(id: Long): Future[Option[Todo]] = db.run {
    byId(id).result.headOption
  }

  def update(id: Long, content: String): Future[Option[Todo]] = {
    db.run {
      byId(id)
        .map(todo => todo.content)
        .update(content)
    } map {
      case 0 => None
      case _ => Some(Todo(id, content))
    }
  }

  def delete(id: Long): Future[Int] = db.run {
    byId(id).delete
  }

  private class TodosTable(tag: Tag) extends Table[Todo](tag, "TODO") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def content = column[String]("CONTENT")

    def * = (id, content) <> (Todo.tupled, Todo.unapply)
  }
}
