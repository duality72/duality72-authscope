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

import common.CommonsWebClient
import fixture.FakeHttpServer
import org.specs.runner.JUnit
import org.specs.Specification

/**
 * Functional test - brings up a fake mingle...
 *
 * TODO: We should stub out the WebClient used here and make this a smaller scoped test,
 * then create a higher-level test for a single scenario elsewhere
 */
class ProjectRepositoryTests extends Specification with JUnit {

  var fakeServer = FakeHttpServer.usingRandomFreePort

  "Project Repository" should {

    doFirst {
      fakeServer.start
    }

    "Retrieve Users From Mingle" in {
      val userXml = <users type="array">
						  <user>
						    <activated type="boolean">true</activated>
						    <admin type="boolean">false</admin>
						    <email>fred@example.com</email>
						    <user_id type="integer">28</user_id>
						    <login>fred</login>
						    <name>Fred Flintstone</name>
						    <version_control_user_name/>
						  </user>
						</users>

      fakeServer.serveHtmlForUrl("/projects/MyProject/users.xml", userXml)

      val repo = new MingleProjectRepository("http://localhost:" + fakeServer.getPort + "/", "MyProject", new CommonsWebClient)
      repo.users must haveKey("28")
    }

    "Retrieve Cards from Mingle" in {
      val userXml = <users type="array">
                      <user>
                        <activated type="boolean">true</activated>
                        <admin type="boolean">false</admin>
                        <email>fred@example.com</email>
                        <user_id type="integer">24</user_id>
                        <login>fred</login>
                        <name>Fred Flintstone</name>
                        <version_control_user_name/>
                      </user>
                    </users>

      var cardsXml = <cards type="array">
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

      fakeServer.serveHtmlForUrl("/projects/MyProject/users.xml", userXml)
      fakeServer.serveHtmlForUrl("/projects/MyProject/cards.xml", cardsXml)

      val repo = new MingleProjectRepository("http://localhost:" + fakeServer.getPort + "/", "MyProject", new CommonsWebClient)
      repo.cards must haveSize(1)
    }

    doLast {
      fakeServer.stop
    }

  }

}
