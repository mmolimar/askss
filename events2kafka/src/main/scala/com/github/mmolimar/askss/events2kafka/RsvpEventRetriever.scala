package com.github.mmolimar.askss.events2kafka

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, _}
import akka.stream.scaladsl.{Flow, Framing}
import akka.util.ByteString
import com.github.mmolimar.askss.common.RSVP
import com.github.mmolimar.askss.common.implicits._
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}

object Constants {
  val MEETUP_URL = config.getString("meetup.url")

  val REST_PROXY_URL = config.getString("rest-proxy.url") + "/" +
    config.getString("rest-proxy.topic")

  val CONTENT_TYPE = headers.`Content-Type`(MediaType.customWithFixedCharset("application",
    "vnd.kafka.json.v1+json", HttpCharsets.`UTF-8`)).contentType
}

object RsvpEventRetriever extends App with LazyLogging {

  val httpRequest = HttpRequest(
    method = HttpMethods.GET,
    uri = Constants.MEETUP_URL
  )

  val frame = Flow[ByteString]
    .via(Framing.delimiter(
      delimiter = ByteString("\n"),
      maximumFrameLength = 3000,
      allowTruncation = false))

  Http().singleRequest(httpRequest).map { response =>
    logger.info(s"Got ${response.status} status code from Meetup. Processing records...")
    response.status match {
      case StatusCodes.OK =>
        response.entity.dataBytes
          .via(frame)
          .map(_.utf8String)
          .map(str => {
            println(str)
            str
          })
          .map(_.toEvent)
          .runForeach(produceRecord(_))

      case _ =>
        logger.error("Unexpected error, shutting down")
        system.terminate();
    }
  }

  def produceRecord(rsvp: RSVP): Unit = {
    Http().singleRequest(HttpRequest(
      method = HttpMethods.POST,
      uri = Constants.REST_PROXY_URL,
      entity = HttpEntity(Constants.CONTENT_TYPE,
        rsvp.fromEvent.getBytes())
    )).onComplete {
      case Success(response) => response.status match {
        case StatusCodes.OK => logger.trace(s"Message sent to Kafka: ${rsvp.toString}")
        case err: StatusCode => logger.warn(s"Message could not be sent to Kafka: ${err.value}")
      }
      case Failure(e) => logger.error("Error connecting to Rest-Proxy", e)
    }
  }
}