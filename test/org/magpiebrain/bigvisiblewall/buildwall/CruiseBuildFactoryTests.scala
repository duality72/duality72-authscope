/*
 * Copyright ${year} Sam Newman
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
 */package org.magpiebrain.bigvisiblewall.buildwall


import specs.runner.JUnit
import specs.Specification

/**
 * @author Sam Newman (sam.newman@gmail.com)
 */

class CruiseBuildFactoryTests extends Specification with JUnit {

  "Create a build with one level of substeps" in {
      val factory = new CruiseBuildFactory()

      // Cruise sends a record for a build itself - e.g. Project :: Stage - as well as a
      // record for each step inside the stage - Project :: Stage :: Step1
      val data = List(
        ("Project :: Stage 1 :: Step 1", FAILED, "http://url/stage1/step1"),
        ("Project :: Stage 1 :: Step 2", PASSED, "http://url/stage1/step2"),
        ("Project :: Stage 1", UNKNOWN, "http://url/stage1")
      )

      val build = factory.make(data).head
      build.name must equalTo("Project")
      build.urlToBuild must equalTo(None)
      build.status must equalTo(UNKNOWN)

      val stage = build.getChildren.head
      stage.name must equalTo("Project :: Stage 1")
      stage.urlToBuild must equalTo(Some("http://url/stage1"))
      stage.status must equalTo(UNKNOWN)

      stage.children must containAll (List(
        new Build("Project :: Stage 1 :: Step 1", FAILED, Some("http://url/stage1/step1")),
        new Build("Project :: Stage 1 :: Step 2", PASSED, Some("http://url/stage1/step2"))
      ))
    }

}