/**
  * This file is part of the ONEMA uServerlessReporter Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2019, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <software@onema.io>
  */

package io.onema.overwatch.reporter.metric

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder
import com.amazonaws.services.lambda.runtime.Context
import io.onema.userverless.configuration.lambda.EnvLambdaConfiguration
import io.onema.userverless.function.SnsHandler
import io.onema.userverless.model.Metric

class MetricReporterFunction extends SnsHandler[Seq[Metric]] with EnvLambdaConfiguration {

  //--- Fields ---
  val logic = new MetricReporterLogic(AmazonCloudWatchClientBuilder.defaultClient())

  //--- Methods ---
  override def execute(event: Seq[Metric], context: Context): Unit = {
    logic.report(event)
  }
}
