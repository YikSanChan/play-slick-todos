package models

import models.Status.Status

case class TodoRequest(content: String,
                       priority: Int = 3,
                       status: Status = Status.NotStarted)
