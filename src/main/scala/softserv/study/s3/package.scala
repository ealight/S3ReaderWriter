package softserv.study

import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}

package object s3 {
  implicit val system: ActorSystem = ActorSystem("Actor")
  val config: Config = ConfigFactory.load()
}
