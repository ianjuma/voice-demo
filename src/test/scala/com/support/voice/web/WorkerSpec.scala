package com.support.voice.web
package services

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.scalatest.{BeforeAndAfterAll, FreeSpecLike, Matchers}

class WorkerSpec extends TestKit(ActorSystem("WorkerSpec"))
    with ImplicitSender
    with FreeSpecLike
    with Matchers
    with BeforeAndAfterAll {
  import SupportActor._

  val actorRef = TestActorRef[SupportActor]

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Worker" - {
    "creates" - {
      "receive Ok" - {
        val future = actorRef ! Create(new VoiceCallbackParams("bar"))
        expectMsgClass(classOf[Ok])
      }
    }
  }
}
