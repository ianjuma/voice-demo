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

      // Compose the response
      val response = "<?xml version='1.0' encoding='UTF-8'?>;"
      val end_response = "<Say>Hello, Welcome to the support voice Product'</Say>;" + "</Response>;"

      response.concat("<Response>;")
      response.concat(end_response)

      sender ! ResponseString(response)
    }
  }
}
