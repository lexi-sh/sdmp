# Spark Data Management Platform

![master](https://github.com/christophertrml/sdmp/actions/workflows/scala.yml/badge.svg?branch=master)

The Spark Data Management Platform (or SDMP for short) is a standardized way to log off what each dataset you use within your Spark data pipelines means, where they are, and watch the status of them. The SDMP UI offers a view into these datasets, as well as an easy way to see a preview of the data and also delete or move any datasets in case you wish to quickly re-run a job.

## Status

[] Scala library

[] Scala Implicit import

[] UI tool


## Usage


```

def diModuleOrMain() {
    // Initiate your logger
    val sdmpLogger = PrintLogger()
    // or...
    val sdmpLogger = S3Logger(amazonS3Client, bucket, key)
    // or...
    val sdmpLogger = FileLogger(basePathString)
}

// Usage
case class MyDataPipeline(spark: SparkSession, sdmpLogger: S3Logger) {
    import sdmpLogger.implicits

    def run() {
        val df = ...
        df.write.sdmpParquet("/my/path", "optional description")
            //  .sdmpCsv
            //  .sdmpJson
    }
}
```

This passes the write commands through to spark, but also logs data regarding this write operation. This (and the associated metadata file) can then be read manually in whatever tooling you use, or used with `sdmp-ui`.

## Metadata

If you wish to read the metadata file directly for your own internal tooling, the specification is as follows:


JSON file:
```
{
    [
        {
            "uri": "s3://parquet-bucket/parquet-key1/key2/key3",
            "format": "csv"|"json"|"parquet",
            "description": "optional description given",
            "status": "empty"|"completed"|"in progress",
            "size": null|123456.5 // megabytes,
            "stacktrace": "...",
            "package": "..."
        }
    ]
}
```

## Development

1. Install localstack
2. aws --endpoint-url=http://localhost:4566 s3 mb s3://sdmp-test-bucket