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

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.sns.{AmazonSNS, AmazonSNSClientBuilder}
import io.onema.json.Extensions._
import io.onema.userverless.configuration.lambda.EnvLambdaConfiguration
import io.onema.userverless.events.Sns.SnsEvent
import io.onema.userverless.exception.MessageDecodingException
import io.onema.userverless.function.SnsHandler
import io.onema.userverless.mailerreporter.ErrorReporterFunction.Email
import io.onema.userverless.model.Log.{LogErrorMessage, Rename}
import org.json4s._
import org.json4s.jackson.JsonMethods._


class ErrorReporterFunction extends SnsHandler[LogErrorMessage] with EnvLambdaConfiguration {

  //--- Fields ---
  val mailerTopic: String = getValue("mailer/topic").getOrElse(throw new RuntimeException("The mailer topic is required"))
  val recipients: Option[String] = getValue("recipients")
  val sender: String = getValue("sender").getOrElse(throw new RuntimeException("The sender env var is required"))
  val client: AmazonSNS = AmazonSNSClientBuilder.defaultClient()

  //--- Methods ---
  override def execute(event: LogErrorMessage, context: Context): Unit = {
    val errorStr = event.asJson(Rename.errorMessage)
    val prettyStr = pretty(render(parse(errorStr)))
    if(event.function == context.getFunctionName){
      return
    }
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
      client.publish(mailerTopic, email)
    })
  }

  override def jsonDecode(json: String): LogErrorMessage = {
    val records = json.jsonDecode[SnsEvent].Records
    records match {
      case record::Nil =>
        record.Sns.Message.map(json => json.jsonDecode[LogErrorMessage](Rename.errorMessage)) match {
          case Some(event: LogErrorMessage) => event
          case None => throw new MessageDecodingException("No message class available for decoding")
        }
      case _ =>
        throw new MessageDecodingException("The SNS Event contains no records, this should never happen!")
    }
  }
}

object ErrorReporterFunction {
  case class Email(to: Seq[String], from: String, subject: String, body: String, replyTo: String = "", raw: Boolean = false)
}
