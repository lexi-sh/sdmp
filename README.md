# Spark Data Management Platform

The Spark Data Management Platform (or SDMP for short) is a standardized way to log off what each dataset you use within your Spark data pipelines means, where they are, and watch the status of them. The SDMP UI offers a view into these datasets, as well as an easy way to see a preview of the data and also delete or move any datasets in case you wish to quickly re-run a job.

## Status

[] Scala library
[] Scala Implicit import
[] UI tool


## Usage


```

// This could be anywhere in your job, like perhaps in your DI module or in your job's main 
def initModule() {
    SdmpGlobalSession.logUsing(s3client, "my-logging-bucket", "my-logging-key")
}


import sdmp.implicits._

def generateDataFrame(spark: SparkSession) {
    val df = ...
    df.write.sdmpParquet("s3://parquet-bucket/parquet-key1/key2/key3", "optional description of data")
          //.sdmpCsv
          //.sdmpJson
}
```

This passes the write commands through to spark, but also logs the dataframe to the logging location. This (and the associated metadata file) can then be read manually in whatever tooling you use, or used with `sdmp-ui`.

## Metadata 

If you wish to read the metadata file directly for your own internal tooling, the specification is as follows:


JSON file:
{
    [
        {
            "uri": "s3://parquet-bucket/parquet-key1/key2/key3",
            "format": "csv"|"json"|"parquet",
            "description": "optional description given",
            "status": "empty"|"completed"|"in progress",
            "size": null|123456.5 // megabytes
        }
    ]
}