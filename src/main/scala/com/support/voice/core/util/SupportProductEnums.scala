package com.support.voice
package core.util

object SupportProductEnums {
  object CallDirection extends Enumeration {
    val Inbound  = Value(1)
    val Outbound = Value(2)
  }

  object CallSessionStatus extends Enumeration {
    val Queued    = Value(1)
    val Success   = Value(2)
    val Failed    = Value(3)
    val Cancelled = Value(4)
  }

  object CallRecordingStatus extends Enumeration {
    val New       = Value(1)
    val Processed = Value(2)
  }

  object CallSessionMode extends Enumeration {
    val Standard = Value(1)
    val Campaign = Value(2)
  }

  object SayActionVoice extends Enumeration {
    val Man   = Value("Man")
    val Woman = Value("Woman")
  }
}
