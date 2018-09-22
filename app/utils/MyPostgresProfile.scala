package utils

import com.github.tminglei.slickpg._

trait MyPostgresProfile extends ExPostgresProfile with PgEnumSupport {

  override val api = MyAPI

  object MyAPI extends API {
    implicit val statusTypeMapper = createEnumJdbcType("status_enum", models.Status)
    implicit val labelListTypeMapper =
      createEnumListJdbcType("label_enum", models.Label)
  }
}

object MyPostgresProfile extends MyPostgresProfile
