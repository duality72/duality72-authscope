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

class CompositeBuildSourceTests extends Specification with JUnit {

  "Composite Build Source" should {

    "Get builds from multiple sources" in {
      val build1 = new Build("1", UNKNOWN, None)
      val build2 = new Build("2", UNKNOWN, None)
      val build3 = new Build("3", UNKNOWN, None)

      val source1 = new BuildSource() {
        def get : List[Build] = List(build1)
      }

      val source2 = new BuildSource() {
        def get : List[Build] = List(build2, build3)
      }

      val compositeSource = new CompositeBuildSource(source1, source2)

      compositeSource.get must containAll(List(build1, build2, build3))
    }
    
  }

}