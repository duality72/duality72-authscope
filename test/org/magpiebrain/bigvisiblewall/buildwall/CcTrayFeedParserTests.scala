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

      builds must haveSize (1)
      val build = builds.head
      build.name must equalTo("My Super Project")
      build.status must equalTo(PASSED)
      build.urlToBuild must equalTo("http://mysuperbuildmachine")
    }

    "Parse single passing build" in {
      val exampleXml =
        <Projects>
          <Project name="My Super Project" activity="Sleeping"
          lastBuildStatus="Failure" lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33"
          webUrl="http://mysuperbuildmachine" />
        </Projects>

      val builds = new CcTrayFeedParser().parse(exampleXml)

      builds must haveSize (1)
      val build = builds.head
      build.name must equalTo("My Super Project")
      build.status must equalTo(FAILED)
      build.urlToBuild must equalTo("http://mysuperbuildmachine")
    }

    "Parse multiple builds for multiple projects" in {
      val exampleXml =
        <Projects>
          <Project name="Project 1" activity="Sleeping"
            lastBuildStatus="Failure" lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33"
            webUrl="http://mysuperbuildmachine/1" />
          <Project name="Project 2" activity="Sleeping"
            lastBuildStatus="Success" lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33"
            webUrl="http://mysuperbuildmachine/2" />
        </Projects>

      val builds = new CcTrayFeedParser().parse(exampleXml)

      builds must haveSize (2)

      val project1Build = builds(0)
      project1Build.name must equalTo("Project 1")
      project1Build.status must equalTo(FAILED)
      project1Build.urlToBuild must equalTo("http://mysuperbuildmachine/1")

      val project2Build = builds(1)
      project2Build.name must equalTo("Project 2")
      project2Build.status must equalTo(PASSED)
      project2Build.urlToBuild must equalTo("http://mysuperbuildmachine/2")
    }

  }

}