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


import specs.runner.JUnit
import specs.Specification

/**
 * @author Sam Newman (sam.newman@gmail.com)
 */
class SimpleBuildFactoryTests extends Specification with JUnit {

  "Build Factory" should {
    "Create a single build" in {
      val factory = new SimpleBuildFactory(None)
      val data = List(("Project", FAILED, "http://url"))
      factory.make(data).head must beEqualTo(new Build("Project", FAILED, Some("http://url")))
    }

    "Does not fold builds unless specified" in {
      val factory = new SimpleBuildFactory(None)
      val data = List(
       ("Project :: Step 1", FAILED, "http://url/1"),
       ("Project :: Step 2", PASSED, "http://url/2")
      )

      factory.make(data) must containInOrder(
        List(
          new Build("Project :: Step 1", FAILED, Some("http://url/1")),
          new Build("Project :: Step 2", PASSED, Some("http://url/2"))
        )
      )
    }

    "Create a build with one level of substeps" in {
      val factory = new SimpleBuildFactory(Some(1))
      val data = List(
        ("Project :: Stage 1 :: Step 1", FAILED, "http://url/1"),
        ("Project :: Stage 1 :: Step 2", PASSED, "http://url/2")
      )

      val build = factory.make(data).head

      build.name must equalTo("Project :: Stage 1")
      build.urlToBuild must equalTo(None)

      build.steps must containInOrder (List(
        new Build("Step 1", FAILED, Some("http://url/1")),
        new Build("Step 2", PASSED, Some("http://url/2"))
      ))

      build.status must equalTo(FAILED)
    }

    "Create a build with two levels of substeps" in {
      skip("Not implemented yet")
      val factory = new SimpleBuildFactory(Some(2))
      val data = List(
        ("Project :: Stage 1 :: Step 1", FAILED, "http://url/1"),
        ("Project :: Stage 1 :: Step 2", PASSED, "http://url/2")
      )

      val build = factory.make(data).head

      build.name must equalTo("Project")
      build.urlToBuild must equalTo(None)
      build.status must equalTo(FAILED)
      
      val step = build.steps(0)
      step.name must equalTo("Stage 1")
      step.urlToBuild must equalTo(None)
      build.status must equalTo(FAILED)

      step.steps must containInOrder (List(
        new Build("Step 1", FAILED, Some("http://url/1")),
        new Build("Step 2", PASSED, Some("http://url/2"))
      ))

    }

  }
  
}