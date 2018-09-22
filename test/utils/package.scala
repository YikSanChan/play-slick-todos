import com.typesafe.config.ConfigFactory
import play.api.Configuration

package object utils {
  import MyPostgresProfile.api._
  val db = Database.forURL(url = "jdbc:postgresql://localhost:5432/todos-test", driver = "org.postgresql.Driver")
  val conf = new Configuration(ConfigFactory.load("application-test.conf"))
}

