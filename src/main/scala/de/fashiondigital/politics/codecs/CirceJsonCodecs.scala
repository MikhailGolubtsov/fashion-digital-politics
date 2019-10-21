package de.fashiondigital.politics.codecs

import de.fashiondigital.politics.domain.model.SpeechStats
import io.circe.{Encoder, Printer}
import io.circe.generic.semiauto.deriveEncoder

object CirceJsonCodecs {

  implicit val jsonEncoder: Encoder[SpeechStats] = deriveEncoder

  // pretty print json output for circe
  // note, that by default None value is serialized as 'null'
  implicit val printer = Printer.spaces2
}
