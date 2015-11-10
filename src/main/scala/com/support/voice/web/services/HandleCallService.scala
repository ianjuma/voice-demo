package com.support.voice.web
package services

import akka.actor.{Actor, ActorLogging}

object HandleCall {
  case class ResponseString(xml: String)
  case class handleCall(query: VoiceCallbackParams)
}

class HandleCallActor extends Actor with ActorLogging {
  import HandleCall._

  def receive = {
    case handleCall(query) => {
      log.info(s"Create ${query}")

      query.isActive match {
        case "1" => {
          // Compose the response
          val response = "<?xml version='1.0' encoding='UTF-8'?>;"
          val end_response = "<Say>Hello, welcome to Africa's Talking, for Informational services dial 1, " +
            "for technical services dial 2'</Say>;"
          val finish = "</Response>;"

          response.concat("<Response>;")
          response.concat(end_response)
          response.concat(finish)

          sender ! ResponseString(response)
        }
        case "0" => {
          // store call session info
          val sessionId = query.sessionId

          log.info(s"$sessionId")
          val response = ""

          sender ! ResponseString(response)
        }

      }
    }
  }
}
