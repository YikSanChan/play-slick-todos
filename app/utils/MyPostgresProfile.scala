package utils

import com.github.tminglei.slickpg._

trait MyPostgresProfile extends ExPostgresProfile with PgEnumSupport {

  override val api = MyAPI

  object MyAPI extends API {
    implicit val statusTypeMapper = createEnumJdbcType("status", models.Status)
  }
}

object MyPostgresProfile extends MyPostgresProfile
