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

import org.specs._
import org.specs.runner.JUnit

class UserParserTests extends Specification with JUnit {
  
  "User Parser Tests" should {

    "parse single users" in {
       val sampleXml = <users type="array">
						  <user>
						    <activated type="boolean">true</activated>
						    <admin type="boolean">false</admin>
						    <email>barney@example.com</email>
						    <user_id type="integer">29</user_id>
						    <login>barney</login>
						    <name>Barnaby Rogers</name>
						    <version_control_user_name/>
						  </user>
						</users>

      val users = new UserParser(new PhotoRepository).parse(sampleXml)
      users must haveKey("29")
    }

    "parse two users" in {
      val sampleXml = <users type="array">
						  <user>
						    <activated type="boolean">true</activated>
						    <admin type="boolean">false</admin>
						    <email>fred@example.com</email>
						    <user_id type="integer">28</user_id>
						    <login>fred</login>
						    <name>Fred Flintstone</name>
						    <version_control_user_name/>
						  </user>
						  <user>
						    <activated type="boolean">true</activated>
						    <admin type="boolean">false</admin>
						    <email>barney@example.com</email>
						    <user_id type="integer">29</user_id>
						    <login>barney</login>
						    <name>Barnaby Rogers</name>
						    <version_control_user_name/>
						  </user>
						</users>
      
      val users = new UserParser(new PhotoRepository).parse(sampleXml)
      users must haveKey("28")
      users must haveKey("29")
    }
  }

}
