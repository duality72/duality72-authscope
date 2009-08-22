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

import org.magpiebrain.bigvisiblewall.cardwall.model.User
import model.User
import org.specs._
import org.specs.runner.JUnit

class CardParserTests extends Specification with JUnit {

  "Card Parser" should {
    
    "Parse a single card with owner" in {
      val xml = <cards type="array">
        <card>
          <card_type_name>Story</card_type_name>
          <cp_3_user_id type="integer">24</cp_3_user_id>ser_id>
          <cp_css_status></cp_css_status>
          <cp_date_added type="date">2009-06-16</cp_date_added>
          <cp_iteration>14</cp_iteration>
          <cp_story_status>In QA</cp_story_status>
          <description>Drcse</description>
          <id type="integer">7984</id>
          <name>As a consumer I want to view my saved searches</name>
        </card>
      </cards>

      val owner = new User("24", "A User")
      val cardParser = new CardParser(Map("24" -> owner))

      val cards = cardParser.parse(xml)
      cards.size must be equalTo(1)
      cards(0).owner must be equalTo(owner)
    }

    "Parse a single card without an owner" in {
      val xml = <cards type="array">
        <card>
          <card_type_name>Story</card_type_name>
          <cp_3_user_id type="integer"></cp_3_user_id>ser_id>
          <cp_css_status></cp_css_status>
          <cp_date_added type="date">2009-06-16</cp_date_added>
          <cp_iteration>14</cp_iteration>
          <cp_story_status>In QA</cp_story_status>
          <description>Drcse</description>
          <id type="integer">7984</id>
          <name>As a consumer I want to view my saved searches</name>
        </card>
      </cards>

      val cardParser = new CardParser(Map())

      val cards = cardParser.parse(xml)
      cards.size must be equalTo(1)
    }
  }

}
