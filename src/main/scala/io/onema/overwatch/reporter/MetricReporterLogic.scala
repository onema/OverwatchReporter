/**
  * This file is part of the ONEMA userverless-overwatch Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2018, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <software@onema.io>
  */

package io.onema.overwatch.reporter

import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import com.amazonaws.services.cloudwatch.model.{Dimension, MetricDatum, PutMetricDataRequest}
import com.typesafe.scalalogging.Logger
import io.onema.userverless.model.Metric
import MetricReporterLogic._

import scala.collection.JavaConverters._

class MetricReporterLogic(val client: AmazonCloudWatch) {

  //--- Fields ---
  val log = Logger(classOf[MetricReporterLogic])

  //--- Methods ---
  def report(parsedMetrics: Seq[Metric]): Unit = {
    parsedMetrics.groupBy(_.appName).foreach {case(namespace, metrics) =>
      if(namespace == "APP_NAME_IS_UNDEFINED") {
        log.warn(s"""NAMESPACE "$namespace" IS NOT VALID AND WILL NOT BE REPORTED!""")
        return
      }
      putMetrics(namespace, metrics)
    }
  }

  def putMetrics(namespace: String, metrics: Seq[Metric]): Unit = {
    val ns = toPascalCase(namespace)

    // Create metric datum for each metric we are going to report, and batch it in groups of 20
    val metricDatum: Iterator[Seq[MetricDatum]] = metrics.map(metric => {
      log.debug(s"""About to put metric "$metric"""")
      val dimensions = metric.tagMap.map {
        case (k, v) => new Dimension().withName(k).withValue(v)
      }.toList.asJava
      new MetricDatum()
        .withMetricName(metric.name)
        .withUnit(metric.unit)
        .withValue(metric.value)
        .withDimensions(dimensions)
    }).grouped(20)

    // for each batch create and submit a new request
    metricDatum.foreach(x => {
      val request = new PutMetricDataRequest()
        .withNamespace(s"$ns")
      log.debug(s"BATCHED ${x.length} metrics for the namespace $ns")
      x.foreach(request.withMetricData(_))
      client.putMetricData(request)
    })
  }
}

object MetricReporterLogic {
  val log = Logger(classOf[MetricReporterLogic])

  //--- Methods ---
  def toPascalCase(value: String): String = {
    value.split("-|_|\\.|\\s").map(x => x.head.toUpper + x.tail).mkString("")
  }
}
