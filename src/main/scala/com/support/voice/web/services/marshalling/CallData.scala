package com.support.voice.web
package services.marshalling

import spray.http.ContentType._
import spray.http.HttpEntity
import spray.http.MediaTypes._
import spray.httpx.marshalling._

case class CallBackRequest( isActive: Int, sessionId: String,
                            direction: String, callNumber: String,
                            destinationNumber: String) {
  require(isActive <= 1, "Call States can Only be 1 or 0")
  def getStatus = isActive
}

case class CallBackResponseItem( response: String, errorMessage: Option[String] )
case class CallBackResponse( response: CallBackResponseItem, errorMessage: Option[String])

object CallBackResponse {
  implicit val HttpResponseMarshaller =
    Marshaller.of[CallBackResponse](`text/plain`) { (value, contentType, ctx) =>
      contentType match {
        case _ =>
          ctx.marshalTo(HttpEntity(contentType, value.toString))
      }
    }

  def fromServiceResponse(serviceResponse: CallBackResponseItem) = {
    serviceResponse.errorMessage match {
      case Some(errorMessage) =>
        CallBackResponse(serviceResponse, Some(errorMessage))
      case None =>
        CallBackResponse(serviceResponse, None)
    }
  }

}