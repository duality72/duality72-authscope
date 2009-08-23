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
class BuildFactoryTests extends Specification with JUnit {

  "Build Factory" should {
    "Create a single build" in {
      val factory = new BuildFactory()
      val data = List(("Project", FAILED, "http://url"))
      factory.make(data).head must beEqualTo(new Build("Project", FAILED, "http://url"))
    }

//
//    "Create a build with multiple steps" {
//      val factory = new BuildFactory()
//
//    }
//
//    "Create a build with a deep tree of steps" {
//      val factory = new BuildFactory()
//
//    }
  }
  
}