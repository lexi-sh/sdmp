package sdmp.entities

sealed trait OutputStatus
case object Empty extends OutputStatus
case object Completed extends OutputStatus
case object InProgress extends OutputStatus
