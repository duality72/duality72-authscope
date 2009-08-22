package org.magpiebrain.bigvisiblewall.buildwall



sealed abstract class BuildStatus(val name: String) {
  override def toString : String = name
}

case object PASSED extends BuildStatus("Passed")
case object FAILED extends BuildStatus("Failed")
case object UNKNOWN extends BuildStatus("Unknown")
