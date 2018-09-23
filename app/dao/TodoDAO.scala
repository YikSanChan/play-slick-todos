package dao

import com.google.inject.ImplementedBy
import javax.inject.Inject
import models.Label.Label
import models.Status.Status
import models.Todo
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[TodoDAOImpl])
trait TodoDAO {
  def index(): Future[Seq[Todo]]

  def create(content: String,
             priority: Int,
             status: Status,
             labels: List[Label]): Future[Todo]

  def get(id: Long): Future[Option[Todo]]

  def update(id: Long,
             content: String,
             priority: Int,
             status: Status,
             labels: List[Label]): Future[Option[Todo]]

  def delete(id: Long): Future[Int]
}

trait TodoDAOHelper {

  import MyPostgresProfile.api._

  val todos = TableQuery[TodosTable]
  lazy val byId = (id: Long) => todos.filter(_.id === id)
  val todosReturningRow = todos returning todos.map(_.id) into { (todo, id) =>
    todo.copy(id = id)
  }
}

class TodoDAOImpl @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[MyPostgresProfile]
    with TodoDAO
    with TodoDAOHelper {

  import MyPostgresProfile.api._

  def index(): Future[Seq[Todo]] = db.run {
    todos.result
  }

  def create(content: String,
             priority: Int,
             status: Status,
             labels: List[Label]): Future[Todo] = db.run {
    todosReturningRow += Todo(0L, content, priority, status, labels)
  }

  def get(id: Long): Future[Option[Todo]] = db.run {
    byId(id).result.headOption
  }

  def update(id: Long,
             content: String,
             priority: Int,
             status: Status,
             labels: List[Label]): Future[Option[Todo]] = {
    db.run {
      byId(id)
        .map(todo => (todo.content, todo.priority, todo.status, todo.labels))
        .update((content, priority, status, labels))
    } map {
      case 0 => None
      case _ => Some(Todo(id, content, priority, status, labels))
    }
  }

  def delete(id: Long): Future[Int] = db.run {
    byId(id).delete
  }
}
