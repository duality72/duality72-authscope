package org.magpiebrain.bigvisiblewall.buildwall


import collection.mutable.ArrayBuffer
import scala.xml.Node

class CcTrayFeedParser {

  def parse(xml: Node) : List[Tuple4[String, BuildStatus, String, String]] = {
    val builds = new ArrayBuffer[Tuple4[String, BuildStatus, String, String]]()

    for (val build <- xml \\ "Project") {
      val name = (build \\ "@name").text
      val status = toBuildStatus((build \\ "@lastBuildStatus").text)
      val buildUrl = (build \\ "@webUrl").text
      val activity = (build \\ "@activity").text
      builds += (name, status, buildUrl, activity)
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