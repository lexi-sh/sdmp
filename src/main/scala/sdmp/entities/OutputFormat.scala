package sdmp.entities

sealed trait OutputFormat
case object Csv extends OutputFormat
case object Parquet extends OutputFormat
case object Json extends OutputFormat
