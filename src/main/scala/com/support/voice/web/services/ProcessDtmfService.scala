package com.support.voice.web
package services

import akka.actor.{Actor, ActorLogging}

object ProcessDtmfService {
  case class ResponseString(xml: String)
  case class getDtmf(query: VoiceCallbackParams)
}

// dtmf callback service
class ProcessDtmfActor extends Actor with ActorLogging {
  import ProcessDtmfService._

  def receive = {
    case getDtmf(query) => {
      log.info(s"Create ${query}")

      // Compose the response
      val response = "<?xml version='1.0' encoding='UTF-8'?>;"
      val end_response = "<Say>We're redirecting you, hold on.'</Say>;" + "</Response>;"

      response.concat("<Response>;")
      response.concat(end_response)

      sender ! ResponseString(response)
    }
  }
}

