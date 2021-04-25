/**
  * This file is part of the ONEMA overwatch-reporter Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2019, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <software@onema.io>
  */

package io.onema.overwatch.reporter.notifications

object Model {
  case class Email(to: Seq[String], from: String, subject: String, body: String, raw: Boolean = false, replyTo: Option[String] = None, attachments: Option[Seq[String]] = None)
}
