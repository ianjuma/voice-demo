package com.support.voice
package session

import com.support.voice.core.util.SupportProductEnums.{CallSessionStatus, CallDirection, CallSessionMode}
import com.support.voice.session.actions.VoiceCallActions.VoiceCallAction


object VoiceSessionServiceMessages {

  case class VoiceSessionServiceRequest(
                                         sessionMode: CallSessionMode.Value,
                                         keyword: Option[String],
                                         isActive: Boolean,
                                         sessionId: String,
                                         direction: CallDirection.Value,
                                         callerNumber: String,
                                         destinationNumber: String,
                                         callStartTime: String,
                                         dtmfDigits: Option[String],
                                         durationInSeconds: Option[Int],
                                         recordingUrl: Option[String],
                                         dialDurationInSeconds: Option[Int],
                                         dialDestinationNumber: Option[String],
                                         queueDurationInSeconds: Option[Int],
                                         dequeuedToPhoneNumber: Option[String],
                                         dequeuedToSessionId: Option[String],
                                         status: Option[CallSessionStatus.Value]
                                         )

  case class VoiceSessionServiceResponse(
                                          text: String
                                          )

  private[session] case class SessionRegistryEntry(
                                                    userId: Int,
                                                    sessionMode: CallSessionMode.Value,
                                                    handlerId: Int,
                                                    callState: String
                                                    )

  private[session] case class DialplanHandlerResponse(
                                                       newState: Option[String] = None,
                                                       actions: List[VoiceCallAction] = List()
                                                       ) {
    if (newState != None) {
      require(!actions.nonEmpty, "Invalid response. Please provide the actions required to transition to the new state")
    }
  }
}




