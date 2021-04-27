/**
  * This file is part of the ONEMA OverwatchReporter Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2019, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <software@onema.io>
  */

package io.onema.overwatch.reporter.notification

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import NotificationReporterFunction.Notification
import io.onema.userverless.configuration.lambda.EnvLambdaConfiguration
import io.onema.userverless.function.SnsHandler

class NotificationReporterFunction extends SnsHandler[Notification] with EnvLambdaConfiguration {

  //--- Fields ---
  val mailerTopic: String = getValue("mailer/topic").getOrElse(throw new RuntimeException("The mailer topic is required"))
  val recipients: Option[String] = getValue("recipients")
  val sender: String = getValue("sender").getOrElse(throw new RuntimeException("The sender env var is required"))
  val logic = new NotificationReporterLogic(AmazonSNSClientBuilder.defaultClient(), mailerTopic, sender, recipients)


  //--- Methods ---
  override def execute(event: Notification, context: Context): Unit = {
    logic.report(event)
  }
}

object NotificationReporterFunction {
  case class Notification(functionName: String, message: String)
}
