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

import com.amazonaws.services.sns.AmazonSNS
import com.typesafe.scalalogging.Logger
import NotificationReporterFunction.Notification
import io.onema.json.Extensions._
import Model.Email

class NotificationReporterLogic(val snsClient: AmazonSNS, val mailerTopic: String, val sender: String, val recipients: Option[String]) {

  //--- Fields ---
  private val log = Logger("errorReporterLogic")

  //--- Methods ---
  def report(notification: Notification): Unit = {
    val subject = s"${notification.functionName} NOTIFICATION"
    val tempalte: String =
      s"""
         |<div>
         |<link href="https://cdnjs.cloudflare.com/ajax/libs/prism/1.15.0/themes/prism.css" rel="stylesheet" />
         |<h2>${notification.functionName} Notification</h2>
         |<h3>${notification.message}</h3>
         |
         |</div>
         |
      """.stripMargin
    recipients.map(_.split(",")).foreach(to => {
      val email = Email(to, sender, subject, tempalte).asJson
      log.debug(email)
      snsClient.publish(mailerTopic, email)
    })
  }
}
