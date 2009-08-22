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
package org.magpiebrain.bigvisiblewall.cardwall.web


import cardwall.model.{NOBODY, Card}
import scala.xml.Node

class CardList(val cards: Seq[Card]) {

  def asHtml: Node = {
    <ul>
      { cards map (card => asHtml(card)) }
    </ul>
  }

  private def asHtml(card: Card) : Node = {
    card.owner match {
      case NOBODY => {
        <li class = "card"><span class = "title">{ card.name }</span></li>
      }

      case _ => {
        <li class = "card">
          <span class = "title">{ card.name }</span>
          <span class = "owner">{ card.owner.name }</span>
        </li>
      }
    }

  }

}