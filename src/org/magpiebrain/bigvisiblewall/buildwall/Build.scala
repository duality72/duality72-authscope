/*
 * Copyright 2009 Sam Newman
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package org.magpiebrain.bigvisiblewall.buildwall

class Build(val name: String, val status: BuildStatus, val urlToBuild: Option[String]) {

  def steps: List[Build] = List()

  override def toString(): String = {
    return "Name:" + name + " Status:" + status + " URL:" + urlToBuild
  }

  override def equals(other : Any) : Boolean = other match {
    case that : Build => (
      this.name == that.name &&
      this.status == that.status &&
      this.urlToBuild == that.urlToBuild
    )
    case _ => false
  }

}
