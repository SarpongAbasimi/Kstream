package models

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._
import models.Greetings.{GoodAfternoon, GoodEvening, GoodMorning}

sealed abstract class Greetings(
    to: String
) extends Product
    with Serializable {
  def greet: String = this match {
    case GoodMorning(to)   => s"Good Morning $to"
    case GoodAfternoon(to) => s"Good Afternoon $to"
    case GoodEvening(to)   => s"Good Evening $to"
  }

  override def toString: String = s"models.Greetings to $to"
}

object Greetings {
  implicit val config = Configuration.default.withDefaults.withDiscriminator("type")

  final case class GoodMorning(to: String) extends Greetings(to)
  object GoodMorning {
    implicit val codec: Codec.AsObject[GoodMorning] = deriveConfiguredCodec[GoodMorning]
  }

  final case class GoodAfternoon(to: String) extends Greetings(to)
  object GoodAfternoon {
    implicit val codec: Codec.AsObject[GoodAfternoon] = deriveConfiguredCodec[GoodAfternoon]
  }

  final case class GoodEvening(to: String) extends Greetings(to)
  object GoodEvening {
    implicit val codec: Codec.AsObject[GoodEvening] = deriveConfiguredCodec[GoodEvening]
  }
}
