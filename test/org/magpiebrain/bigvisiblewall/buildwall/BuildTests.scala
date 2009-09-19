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

class BuildTests extends Specification with JUnit {

  "Build" should {

    "Be considered working if it is performing an activity" in {
      new Build("", UNKNOWN, None, "Building").working must_== false
      
    }

    "Be considered not working if it is not performing an activity" in {
      new Build("", UNKNOWN, None, "Sleeping").working must_== true
    }

  }
}