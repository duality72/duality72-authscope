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


import org.specs.runner.JUnit
import org.specs.Specification

class CcTrayFeedParserTests extends Specification with JUnit {

  "CCTrayFeedParser" should {

    "Parse single passing build" in {
      val exampleXml =
        <Projects>
          <Project name="My Super Project" activity="Sleeping"
          lastBuildStatus="Success" lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33"
          webUrl="http://mysuperbuildmachine" />
        </Projects>

      val builds = new CcTrayFeedParser().parse(exampleXml)
      builds must containInOrder( List(("My Super Project", PASSED, "http://mysuperbuildmachine", "Sleeping")) )
    }

    "Parse single failing build" in {
      val exampleXml =
        <Projects>
          <Project name="My Super Project" activity="Sleeping"
          lastBuildStatus="Failure" lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33"
          webUrl="http://mysuperbuildmachine" />
        </Projects>

      val builds = new CcTrayFeedParser().parse(exampleXml)
      builds must containInOrder( List(("My Super Project", FAILED, "http://mysuperbuildmachine", "Sleeping")) )
    }

    "Parse multiple builds for multiple projects" in {
      val exampleXml =
        <Projects>
          <Project name="Project 1" activity="Sleeping"
            lastBuildStatus="Failure" lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33"
            webUrl="http://mysuperbuildmachine/1" />
          <Project name="Project 2" activity="Building"
            lastBuildStatus="Success" lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33"
            webUrl="http://mysuperbuildmachine/2" />
        </Projects>

      val builds = new CcTrayFeedParser().parse(exampleXml)

      builds must containInOrder(
        List(
          ("Project 1", FAILED, "http://mysuperbuildmachine/1", "Sleeping"),
          ("Project 2", PASSED, "http://mysuperbuildmachine/2", "Building") )
        )
     }
  }

}