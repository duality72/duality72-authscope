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

class BuildListTests extends Specification with JUnit {

  "A BuildList" should {

    "Display HTML for a single passing build" in {
      val expectedHtml =
        <ul class="builds">
          <li class="build passed">
            <a class="project" href="http://mybuild">My Super Project</a>
          </li>
        </ul>

      val build = new Build("My Super Project", PASSED, "http://mybuild")
      val buildList = new BuildList(List(build), None)
      buildList.asHtml must equalIgnoreSpace(expectedHtml)
    }

    "Display HTML for a single failing build" in {
      val expectedHtml =
        <ul class="builds">
          <li class="build failed">
            <a class="project" href="http://mybuild">My Super Project</a>
          </li>
        </ul>

      val build = new Build("My Super Project", FAILED, "http://mybuild")
      val buildList = new BuildList(List(build), None)
      buildList.asHtml must equalIgnoreSpace(expectedHtml)
    }
    
    "Display HTML for a single build with unknown state" in {
      val expectedHtml =
        <ul class="builds">
          <li class="build unknown">
            <a class="project" href="http://mybuild">My Super Project</a>
          </li>
        </ul>

      val build = new Build("My Super Project", UNKNOWN, "http://mybuild")
      val buildList = new BuildList(List(build), None)
      buildList.asHtml must equalIgnoreSpace(expectedHtml)
    }

  }
}