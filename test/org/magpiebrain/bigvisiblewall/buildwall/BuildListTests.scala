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

      val build = new Build("My Super Project", PASSED, Some("http://mybuild"))
      val buildList = new BuildList(List(build))
      buildList.asHtml must equalIgnoreSpace(expectedHtml)
    }

    "Display HTML for a single failing build" in {
      val expectedHtml =
        <ul class="builds">
          <li class="build failed">
            <a class="project" href="http://mybuild">My Super Project</a>
          </li>
        </ul>

      val build = new Build("My Super Project", FAILED, Some("http://mybuild"))
      val buildList = new BuildList(List(build))
      buildList.asHtml must equalIgnoreSpace(expectedHtml)
    }
    
    "Display HTML for a single build with unknown state" in {
      val expectedHtml =
        <ul class="builds">
          <li class="build unknown">
            <a class="project" href="http://mybuild">My Super Project</a>
          </li>
        </ul>

      val build = new Build("My Super Project", UNKNOWN, Some("http://mybuild"))
      val buildList = new BuildList(List(build))
      buildList.asHtml must equalIgnoreSpace(expectedHtml)
    }

//    //TODO This logic should be outside of the HTML rendering - need a model for the builds
//    "Display a build as failed if any builds collapsed inside it have faied" in {
//      val expectedHtml =
//        <ul class="builds">
//          <li class="build failed">
//            <span class="project">My Project</span>
//            <ul class="steps">
//              <li class="build failed"><a class="project" href="http://mybuild/1">Build Step 1</a></li>
//              <li class="build passed"><a class="project" href="http://mybuild/2">Build Step 2</a></li>
//            </ul>
//          </li>
//        </ul>
//
//      val firstStep = new Build("My Project :: Build Step 1", UNKNOWN, "http://mybuild/1")
//      val secondStep = new Build("My Project :: Build Step 2", UNKNOWN, "http://mybuild/2")
//
//      val buildList = new BuildList(List(firstStep, secondStep))
//    }


  }
}