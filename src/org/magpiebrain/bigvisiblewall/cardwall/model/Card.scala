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
package org.magpiebrain.bigvisiblewall.cardwall.model

class Card(val name: String, val status: DevelopmentStatus, val owner: User) {
    override def toString() : String = {
      return "Story #" + owner + " - " + name + "[" + status + "]" 
    }
}

object Card {

  def apply(name: String, status: String, owner: User) = new Card(name, toDevelopmentStatus(status), owner)

  def apply() : Card = new Card("A Card From Default", UNKNOWN, new User("1", "Default User"))

  def apply(name: String) : Card = new Card(name, UNKNOWN, new User("1", "Default User"))
  
  private def toDevelopmentStatus(status: String) : DevelopmentStatus = {
    status match {
      case "Done" => return DONE
      case "In Development" => return IN_DEVELOPMENT
      case _ => return UNKNOWN
    }
  }
}

