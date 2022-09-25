package models

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

sealed abstract class Greetings(
    to: String
) extends Product
    with Serializable

object Greetings {
  implicit val config =
    Configuration.default.withDefaults
      .withDiscriminator("type")
  implicit val greetingCodec: Codec.AsObject[
    Greetings
  ] = deriveConfiguredCodec[Greetings]

  final case class GoodMorning(to: String) extends Greetings(to)
  object GoodMorning {
    implicit val codec: Codec.AsObject[
      GoodMorning
    ] = deriveConfiguredCodec[GoodMorning]
  }

  final case class GoodAfternoon(to: String) extends Greetings(to)
  object GoodAfternoon {
    implicit val codec: Codec.AsObject[
      GoodAfternoon
    ] = deriveConfiguredCodec[GoodAfternoon]
  }

  final case class GoodEvening(to: String) extends Greetings(to)
  object GoodEvening {
    implicit val codec: Codec.AsObject[
      GoodEvening
    ] = deriveConfiguredCodec[GoodEvening]
  }
}
