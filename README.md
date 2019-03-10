# OverwatchReporter

## Overview
The OverwatchReporter is a [µServerless](https://github.com/onema/uServerless) app that consumes messages generated 
by the [µServerlessOverwatch](https://github.com/onema/uServerlessOverwatch) and reports errors to the 
(LambdaMailer)[https://github.com/onema/LambdaMailer] and metrics to AWS CloudWatch Metrics.

## Dependencies
- [µServerlessOverwatch](https://github.com/onema/uServerlessOverwatch) 
- [LambdaMailer](https://github.com/onema/LambdaMailer)

## Intallation
> **NOTE**:
> 
> This project currently uses the [serverless framework](https://serverless.com) to deploy the app so you must have it installed.

```
sbt assembly
```

And deploy with serverless
```
serverless deploy
```

## Uninstall

```
serverless remove
```
