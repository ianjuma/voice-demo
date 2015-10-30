package com.support.voice.web
package services

import org.scalatest._
import spray.http.{ContentTypes, HttpEntity}
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest

class BasicSampleSpec extends FreeSpec with Matchers with ScalatestRouteTest with VoiceService {
  def actorRefFactory = system

  "The spraysample Route" - {
    "when listing entities" - {
      "returns a JSON list" in {
        // Mix in Json4s, but only for this test
        import Json4sProtocol._

        Get("/voice") ~> supportServiceRoute ~> check {
          assert(contentType.mediaType.isApplication)

          // Check content type
          contentType.toString should include("application/json")
          // Try serializaing as a List of Foo
          val response = responseAs[List[String]]

          response.size should equal(2)
          response(0).bar should equal("foo1")

          // Check http status
          status should equal(OK)
        }
      }
    }
    "when posting an entity" - {
      "gives a JSON response" in {
        // FIXME: Should be able to send entity as JObject
        Post("/voice", HttpEntity(ContentTypes.`application/json`, """{"bar": "woot!"}""")) ~> supportServiceRoute ~> check {
          responseAs[String] should include("\"I got a response: Ok")
          status should equal(Created)
        }
      }
    }
  }
}

