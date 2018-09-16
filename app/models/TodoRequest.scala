package models

import models.Status.Status

case class CreateTodoRequest(content: String,
                             priority: Int = 3,
                             status: Status = Status.NotStarted)

case class UpdateTodoRequest(content: String, priority: Int, status: Status)
