package org.magpiebrain.bigvisiblewall.buildwall


import collection.mutable.ArrayBuffer
import scala.xml.Node

class CcTrayFeedParser {

  def parse(xml: Node) : List[Build] = {
    val builds = new ArrayBuffer[Build]()

    for (val build <- xml \\ "Project") {
      val name = (build \\ "@name").text
      val status = toBuildStatus((build \\ "@lastBuildStatus").text)
      val buildUrl = (build \\ "@webUrl").text
      builds += new Build(name, status, buildUrl)
    }
    builds.toList
  }

  private def toBuildStatus(str: String) : BuildStatus = {
    str match {
      case "Success" => PASSED
      case "Failure" => FAILED
      case _ => UNKNOWN
    }
  }
}