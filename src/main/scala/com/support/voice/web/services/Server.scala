package com.support.voice.web
package services

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import com.support.voice.web.services.HandleCall.handleCall
import com.support.voice.web.services.ProcessDtmfService.ResponseString
import org.json4s._
import spray.can.Http
import spray.can.server.Stats
import spray.httpx.Json4sSupport
import spray.routing._

import scala.concurrent.duration._

/* Used to mix in Spray's Marshalling Support with json4s */
object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}

case class DtmfCallbackParams(isActive: String, sessionId: String,
                               callerNumber: String, destinationNumber: String, dtmfDigits: Int)

/* Our case class, used for request and responses */
case class VoiceQuery(voice: String)
case class VoiceCallbackParams(isActive: String, sessionId: String,
                       callerNumber: String, destinationNumber: String)

case class VoiceResponse( response: String, errorMessage: Option[String])

/* Our route directives, the heart of the service.
 * Note you can mix-in dependencies should you so chose */
trait VoiceService extends HttpService {

  import Json4sProtocol._

  // These implicit values allow us to use futures in this trait.
  implicit def executionContext = actorRefFactory.dispatcher

  implicit val timeout = Timeout(5 seconds)

  //Our worker Actor handles the work of the request.
  val processCallbackService = actorRefFactory.actorOf(Props[HandleCallActor], "process-callback-service")
  val processDtmfService = actorRefFactory.actorOf(Props[ProcessDtmfActor], "process-dtmf-callback")

  val supportServiceRoute: Route =
    pathSingleSlash {
      get {
        complete(s"Voice Support Product")
      }
    } ~
      path("voice" / "callback") {
        post {
          formFields('isActive.as[String], 'sessionId.as[String], 'callerNumber.as[String],
            'destinationNumber.as[String]).as(VoiceCallbackParams) { request =>
            complete {
              val serviceRequest = VoiceCallbackParams(
                isActive = request.isActive,
                sessionId = request.sessionId,
                callerNumber = request.callerNumber,
                destinationNumber = request.destinationNumber
              )

              (processCallbackService ? serviceRequest).mapTo[VoiceResponse] map { result =>
                result
              }
              /*respondWithStatus(Created) {
                entity(as[VoiceCallbackParams]) { serviceRequest =>
                  processCallback(serviceRequest)
                }
              }*/
            }
          }
        } ~
          path("voice" / Segment) { id =>
            get {
              complete(s"detail ${id}")
            } ~
              delete {
                complete(s"detail ${id}")
              } ~
              post {
                complete(s"update ${id}")
              }
          } ~
          path("stats") {
            complete {
              // This is another way to use the Akka ask pattern with Spray
              actorRefFactory.actorSelection("/user/IO-HTTP/listener-0")
                .ask(Http.GetStats)(1.second)
                .mapTo[Stats]
            }
          }
      }
      path("voice" / "callback" / "processDtmf") {
        post {
          formFields('isActive.as[String], 'sessionId.as[String], 'callerNumber.as[String],
            'destinationNumber.as[String], 'dtmfDigits.as[Int]).as(DtmfCallbackParams) { request =>
            complete {
              val serviceRequest = DtmfCallbackParams(
                isActive = request.isActive,
                sessionId = request.sessionId,
                callerNumber = request.callerNumber,
                destinationNumber = request.destinationNumber,
                dtmfDigits = request.dtmfDigits
              )

              (processDtmfService ? serviceRequest).mapTo[VoiceResponse] map { result =>
                result
              }
            }
          }
        }
      }

    def processCallback[T](query: VoiceCallbackParams) = {
      // We use the Ask pattern to return
      // a future from our worker Actor,
      // which then gets passed to the complete
      // directive to finish the request.
      val response = (processCallbackService ? handleCall(query))
        .mapTo[ResponseString]
        .map(result => result)
        .recover { case _ => "error" }

      complete(response)
    }
}
