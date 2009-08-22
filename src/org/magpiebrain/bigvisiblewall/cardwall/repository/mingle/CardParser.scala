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
package org.magpiebrain.bigvisiblewall.cardwall.repository.mingle

import model.{NOBODY, Card, User}
import scala.xml.Node
import scala.collection.mutable.ArrayBuffer


class CardParser(val users: Map[String, User]) {

  def parse(xml: Node) : List[Card] = {
    val cards = new ArrayBuffer[Card]()

    for (val card <- xml \\ "card") {
      val name = (card \\ "name").text
      val id = (card \\ "id").text
          
      // These two fields are custom to the R2 project...
      val storyStatus = (card \\ "cp_story_status").text     
      val ownerId = (card \\ "cp_3_user_id").text

      cards += Card(name, storyStatus, users.getOrElse(ownerId, NOBODY))
    }

    return cards.toList
  }

}
