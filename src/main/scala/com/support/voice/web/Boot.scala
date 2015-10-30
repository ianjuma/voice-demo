package com.support.voice.web

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.io.IO
import com.support.voice.web.services._
import spray.can.Http

object Boot extends App {
  implicit val system = ActorSystem("voice-support-system")

  /* Use Akka to create our Spray Service */
  val service = system.actorOf(Props[WebServiceActor], "voice-support-service")

  /* and bind to Akka's I/O interface */
  IO(Http) ! Http.Bind(
    service,
    system.settings.config.getString("app.interface"),
    system.settings.config.getInt("app.port")
  )

  /* Allow a user to shutdown the service easily */
  println("Hit any key to exit.")
  val result = scala.io.StdIn.readLine()
  system.shutdown()
}

/* Our Server Actor is pretty lightweight; simply mixing in our route trait and logging */
class WebServiceActor extends Actor with VoiceService with ActorLogging {
  def actorRefFactory = context
  def receive = runRoute(supportServiceRoute)
}
