package dao

import models.{Label, Status, Todo}
import org.specs2.specification.BeforeAfterAll
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.PlaySpecification
import utils.MyPostgresProfile.api._

class TodoDAOImplSpec extends PlaySpecification with BeforeAfterAll {

  lazy val todos = TableQuery[TodosTable] // Need to be lazy, otherwise 'before' throws NPE
  val fakeApp = GuiceApplicationBuilder().configure(utils.conf).build()
  val todoDao = fakeApp.injector.instanceOf[TodoDAOImpl]

  var todo1: Todo = _
  var todo2: Todo = _
  var todo3: Todo = _

  def size = await(utils.db.run(todos.size.result))

  def beforeAll = {
    todo1 = Todo(1, "item1", 1, Status.NotStarted, List(Label.Medium))
    todo2 = Todo(2, "item2", 2, Status.NotStarted, List(Label.Easy))
    todo3 = Todo(3, "item3", 4, Status.NotStarted, List(Label.Hard))
    await(utils.db.run(todos forceInsertAll List(todo1, todo2, todo3)))
  }

  def afterAll = {
    await(utils.db.run(todos.delete))
  }

  "TodoDAOImpl#index" should {
    "returns all todo items" in {
      val result = await(todoDao.index())
      result.size should_=== 3
      result must contain(todo1)
      result must contain(todo2)
      result must contain(todo3)
    }
  }

  "TodoDAOImpl#create" should {
    "returns a created todo item and increments items size with 1" in {
      val result = await(todoDao.create("item4", 3, Status.NotStarted, List(Label.Easy, Label.Coding)))
      result.content should_=== "item4"
      result.priority should_=== 3
      result.status should_=== Status.NotStarted
      result.labels should_=== List(Label.Easy, Label.Coding)
      size should_=== 4
    }
  }

  "TodoDAOImpl#get" should {
    "returns the expected todo item for a valid get" in {
      await(todoDao.get(1)) should_=== Some(todo1)
      await(todoDao.get(2)) should_=== Some(todo2)
      await(todoDao.get(3)) should_=== Some(todo3)
      // It assumes there exists only one item with content "item4"
      // Should be replaced by (_.id !== any of [1,2,3])
      val result = await(utils.db.run(todos.filter(_.content === "item4").result)).head
      result.content should_=== "item4"
      result.priority should_=== 3
      result.status should_=== Status.NotStarted
      result.labels should_=== List(Label.Easy, Label.Coding)
    }

    "returns None for an invalid get" in {
      await(todoDao.get(-1)) should_=== None
    }
  }

  "TodoDAOImpl#update" should {
    "returns the updated todo item for a valid update" in {
      val result = await(todoDao.update(2, "item2", 1, Status.InProgress, List(Label.Hard)))
      result should_=== Some(Todo(2, "item2", 1, Status.InProgress, List(Label.Hard)))
    }

    "returns None for an invalid update" in {
      val result = await(todoDao.update(-1, "item2", 1, Status.InProgress, List(Label.Hard)))
      result should_=== None
    }
  }

  "TodoDAOImpl#delete" should {
    "returns # of deleted todo items and decrements items size with 1 for a valid delete" in {
      await(todoDao.delete(1)) should_=== 1
      size should_=== 3
    }

    "returns 0 and keeps items size unchanged for an invalid delete" in {
      await(todoDao.delete(-1)) should_=== 0
      size should_=== 3
    }
  }
}
