package com.github.mmolimar.askss.common

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

package object implicits {

  import com.typesafe.config.{Config, ConfigFactory, ConfigResolveOptions}

  lazy implicit val executionContext = scala.concurrent.ExecutionContext.Implicits.global

  lazy implicit val config: Config = ConfigFactory
    .load(getClass.getClassLoader,
      ConfigResolveOptions.defaults.setAllowUnresolved(true))
    .resolve

  lazy implicit val system = ActorSystem("RsvpActor", config)

  lazy implicit val materializer = ActorMaterializer()

  implicit def pimpRSVP(rsvp: RSVP) = new PimpedRSVP(rsvp)

  implicit def pimpString(json: String) = new PimpedString(json)

}

import spray.json._

protected class PimpedRSVP(rsvp: RSVP) {

  implicit val jsonWriter = RsvpJsonProtocol.kafkaRecordWriter

  def fromEvent: String = {
    rsvp.toJson.compactPrint
  }
}

protected class PimpedString(json: String) {

  implicit val jsonFormat = RsvpJsonProtocol.rsvpFormat

  def toEvent: RSVP = {
    JsonParser(json).convertTo[RSVP]
  }
}

protected object RsvpJsonProtocol extends DefaultJsonProtocol {

  implicit val groupTopicFormat = jsonFormat2(GroupTopic)
  implicit val groupFormat = jsonFormat9(Group)
  implicit val eventFormat = jsonFormat3(Event)
  implicit val venueFormat = jsonFormat4(Venue)
  implicit val memberFormat = jsonFormat2(Member)
  implicit val rsvpFormat = jsonFormat8(RSVP)
  implicit val kafkaRecordWriter = KafkaRecordJsonFormat

  protected object KafkaRecordJsonFormat extends RootJsonWriter[RSVP] {

    def write(rsvp: RSVP) = JsObject(
      "records" -> JsArray(Vector(JsObject(
        "key" -> JsNumber(rsvp.rsvp_id),
        "value" -> rsvp.toJson(rsvpFormat).asJsObject)))
    )
  }

}
