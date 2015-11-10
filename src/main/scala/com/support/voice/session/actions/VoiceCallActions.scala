package com.support.voice
package session.actions

import com.support.voice.core.util.SupportProductEnums.SayActionVoice
import scala.xml.{Elem, Text}

object VoiceCallActions {

  sealed trait VoiceCallAction {
    def getXml : Elem
  }

  def getResponse(actions: List[VoiceCallAction]) =
    <Response>{actions.map(_.getXml)}</Response>

  def generateRejectResponse =
    getResponse(List(RejectAction()))

  case class SayAction( text: String,
                        voice: SayActionVoice.Value = SayActionVoice.Man,
                        playBeep: Boolean = false) extends VoiceCallAction {
    def getXml =
      <Say voice={voice.toString} playBeep={playBeep.toString}>{text}</Say>
  }

  case class PlayAction(url: String) extends VoiceCallAction {
    def getXml =
        <Play url={url}/>
  }

  case class GetDigitsAction(
                              action: VoiceCallAction,
                              timeout: Int = 30,
                              finishOnKey: Option[String] = None
                              ) extends VoiceCallAction {
    def getXml =
      finishOnKey match {
        case Some(key) =>
          <GetDigits timeout={timeout.toString} finishOnKey={key}>{action.getXml}</GetDigits>
        case None =>
          <GetDigits timeout={timeout.toString}>{action.getXml}</GetDigits>
      }
  }

  case class DialAction(
                         phoneNumbers: List[String],
                         record: Boolean = false,
                         sequential: Boolean = false,
                         ringbackTone: Option[String] = None
                         ) extends VoiceCallAction {
    def getXml = {
        <Dial phoneNumbers={phoneNumbers.mkString(",")} record={record.toString} sequential={sequential.toString} ringbackTone={getXmlText(ringbackTone)}/>
    }
  }

  case class EnqueueAction(
                            holdMusic: Option[String] = None,
                            queueName: Option[String] = None
                            ) extends VoiceCallAction {
    def getXml =
        <Enqueue holdMusic={getXmlText(holdMusic)} name={getXmlText(queueName)}/>
  }

  case class DequeueAction(
                            phoneNumber: String,
                            queueName: Option[String] = None
                            ) extends VoiceCallAction {
    def getXml = {
        <Dequeue phoneNumber={phoneNumber} name={getXmlText(queueName)}/>
    }
  }

  case class RejectAction() extends VoiceCallAction {
    def getXml = <Reject/>
  }

  case class RedirectAction(
                             url: String
                             ) extends VoiceCallAction {
    def getXml = <Redirect>{url}</Redirect>
  }

  private def getXmlText[T](value: Option[T]) : Option[Text] = {
    value match {
      case Some(elt) => Some(Text(elt.toString))
      case None      => None
    }
  }

}
