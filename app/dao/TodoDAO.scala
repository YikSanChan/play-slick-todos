package dao

import javax.inject.Inject
import models.Status.Status
import models.{Status, Todo}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class TodoDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  implicit val statusMapper =
    MappedColumnType.base[Status, String](_.toString, Status.withName)

  private val todos = TableQuery[TodosTable]

  private lazy val byId = (id: Long) => todos.filter(_.id === id)

  def index(): Future[Seq[Todo]] = db.run {
    todos.result
  }

  def create(content: String, priority: Int, status: Status): Future[Todo] = {
    val todosReturningRow = todos returning todos.map(_.id) into { (todo, id) =>
      todo.copy(id = id)
    }
    db.run {
      todosReturningRow += Todo(0L, content, priority, status)
    }
  }

  def get(id: Long): Future[Option[Todo]] = db.run {
    byId(id).result.headOption
  }

  def update(id: Long,
             content: String,
             priority: Int,
             status: Status): Future[Option[Todo]] = {
    db.run {
      byId(id)
        .map(todo => (todo.content, todo.priority, todo.status))
        .update((content, priority, status))
    } map {
      case 0 => None
      case _ => Some(Todo(id, content, priority, status))
    }
  }

  def delete(id: Long): Future[Int] = db.run {
    byId(id).delete
  }

  private class TodosTable(tag: Tag) extends Table[Todo](tag, "todo") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def content = column[String]("content")
    def priority = column[Int]("priority")
    def status = column[Status]("status")

    def * = (id, content, priority, status).mapTo[Todo]
  }
}
