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

import cardwall.model.{NOBODY, UNKNOWN, User, Card}
import org.specs.runner.JUnit
import org.specs.Specification


class CardListTests extends Specification with JUnit {

  "Card List" should {

    "Render single owner-less card" in {
      val expectedHtml =
        <ul>
          <li class="card"><span class="title">This is a card</span></li>
        </ul>

      val card = new Card("This is a card", UNKNOWN, NOBODY)
      val html = new CardList(List(card)).asHtml

      html must equalIgnoreSpace(expectedHtml)
    }

    "Render card with an owner" in {
      val expectedHtml =
        <ul>
          <li class="card">
            <span class="title">This is a card</span>
            <span class="owner">A User</span>
          </li>
        </ul>

      val card = new Card("This is a card", UNKNOWN, new User("1", "A User"))
      val html = new CardList(List(card)).asHtml

      html must equalIgnoreSpace(expectedHtml)
    }

    "Render multiple cards" in {
      val expectedHtml =
        <ul>
          <li class="card"><span class="title">This is a card</span></li>
          <li class="card">
            <span class="title">This is another card</span>
            <span class="owner">A User</span>
          </li>
        </ul>

      val firstCard = new Card("This is another card", UNKNOWN, new User("1", "A User"))
      val secondCard = new Card("This is a card", UNKNOWN, NOBODY)
      val html = new CardList(List(firstCard, secondCard)).asHtml

      html must equalIgnoreSpace(expectedHtml)
    }
  }
}