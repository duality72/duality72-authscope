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
class CruiseProjectTests extends Specification with JUnit {

  "Cruise Project" should {

    "Use the status on unknown if there are no child builds" in {
      new CruiseProject("Project").getStatus must beEqualTo(UNKNOWN)
    }

    "Have a status of unknown if any child build is unknown and there are no failures" in {
      val project = new CruiseProject("Project")
      project.addChild(new Build("Child", UNKNOWN, None, ""))
      project.addChild(new Build("Child", PASSED, None, ""))

      project.getStatus must beEqualTo(UNKNOWN)
    }

    "Have a status of passed if all children have passed" in {
      val project = new CruiseProject("Project")
      project.addChild(new Build("Child", PASSED, None, ""))
      project.addChild(new Build("Child", PASSED, None, ""))

      project.getStatus must beEqualTo(PASSED)
    }

    "Have a status of failed if any child is failed" in {
     val project = new CruiseProject("Project")
      project.addChild(new Build("Child", UNKNOWN, None, ""))
      project.addChild(new Build("Child", PASSED, None, ""))
      project.addChild(new Build("Child", FAILED, None, ""))

      project.getStatus must beEqualTo(FAILED)
    }

  }

}