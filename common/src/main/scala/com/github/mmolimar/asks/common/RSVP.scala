package com.github.mmolimar.asks.common

case class RSVP(rsvp_id: Long,
                visibility: String,
                response: String,
                guests: Int,
                event: Option[Event],
                venue: Option[Venue],
                member: Option[Member],
                group: Option[Group])

case class Event(event_id: String,
                 event_name: String,
                 event_url: String)

case class Venue(venue_id: Long,
                 venue_name: String,
                 lon: Double,
                 lat: Double)

case class Member(member_id: Int,
                  member_name: String)

case class Group(group_id: Long,
                 group_name: String,
                 group_lat: Double,
                 group_lon: Double,
                 group_city: String,
                 group_country: String,
                 group_state: Option[String],
                 group_urlname: String,
                 group_topics: Option[Array[GroupTopic]])

case class GroupTopic(urlkey: String,
                      topic_name: String)
