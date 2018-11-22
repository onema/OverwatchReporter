/**
  * This file is part of the ONEMA uServerlessErrorReporter Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2018, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <software@onema.io>
  */

package io.onema.userverless.mailerreporter

import com.amazonaws.services.sns.AmazonSNS
import io.onema.userverless.model.Log.{LogErrorMessage, Rename}
import io.onema.json.Extensions._
import io.onema.userverless.mailerreporter.ErrorReporterFunction.Email

class ErrorReporterLogic(val snsClient: AmazonSNS, val mailerTopic: String, val sender: String, val recipients: Option[String]) {

  //--- Methods ---
  def report(event: LogErrorMessage, prettyStr: String): Unit = {
    val subject = s"${event.appName}: ${event.function} ERROR"
    val tempalte: String =
    s"""
        |<div>
        |<link href="https://cdnjs.cloudflare.com/ajax/libs/prism/1.15.0/themes/prism.css" rel="stylesheet" />
        |<h2>${event.appName}: ${event.function} ERROR</h2>
        |<h3>${event.message}</h3>
        |
        |<div style="background: #f0f3f3; overflow:auto;width:auto;border:solid gray;border-width:.1em .1em .1em .8em;padding:.2em .6em;">
        |  <table>
        |  <tr>
        |  <td>
        |   <pre class="language-js">
        |   $prettyStr
        |   </pre>
        |  </td>
        |  </tr>
        |  </table>
        |</div>
        |
        |</div>
        |
      """.stripMargin
    recipients.map(_.split(",")).foreach(to => {
      val email = Email(to, sender, subject, tempalte).asJson
      snsClient.publish(mailerTopic, email)
    })
  }
}
