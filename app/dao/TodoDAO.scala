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

  def create(content: String, priority: Int): Future[Todo] = {
    val todosReturningRow = todos returning todos.map(_.id) into { (todo, id) =>
      todo.copy(id = id)
    }
    db.run {
      todosReturningRow += Todo(0L, content, priority)
    }
  }

  def get(id: Long): Future[Option[Todo]] = db.run {
    byId(id).result.headOption
  }

  def update(id: Long, content: String, priority: Int): Future[Option[Todo]] = {
    db.run {
      byId(id)
        .map(todo => (todo.content, todo.priority))
        .update((content, priority))
    } map {
      case 0 => None
      case _ => Some(Todo(id, content, priority))
    }
  }

  def delete(id: Long): Future[Int] = db.run {
    byId(id).delete
  }

  private class TodosTable(tag: Tag) extends Table[Todo](tag, "todo") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def content = column[String]("content")
    def priority = column[Int]("priority")

    def * = (id, content, priority) <> (Todo.tupled, Todo.unapply)
  }
}
