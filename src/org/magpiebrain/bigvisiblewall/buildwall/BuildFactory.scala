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


import collection.mutable.HashMap

/**
 * @author Sam Newman (sam.newman@gmail.com)
 */

class BuildFactory(val collapseLevel: Option[Int]) {

  def make(data: List[Tuple3[String, BuildStatus, String]]): List[Build] = {
    val builds = new HashMap[String, Build]()

    if (collapseLevel.isDefined) {
      for (entry <- data) {
        val buildName = entry._1
        val stepStatus = entry._2

        //We collapse from the bottom up - so A :: B :: C becomes A :: B, with a child of C with a one-level collapse
        val partsOfName = buildName.reverse.split(" :: ", 2)
        val parentName = partsOfName(1).reverse
        val stepName = partsOfName(0).reverse
        val step = new Build(stepName, stepStatus, Some(entry._3))

        if (!builds.isDefinedAt(parentName)) {
          val build = new Build(parentName, PASSED, None)
          build.addStep(step)
          builds.put(parentName, build)
        } else {
          builds(parentName).addStep(step)
        }

        if (step.status == FAILED) {
          builds(parentName).status = FAILED
        } else if (step.status == UNKNOWN && builds(parentName).status != FAILED) {
          builds(parentName).status = UNKNOWN
        } else if (builds(parentName).status != FAILED && builds(parentName).status != UNKNOWN) {
          builds(parentName).status = PASSED
        }
        
      }
      return builds.values.toList
    } else {
      return data map toBuild
    }
  }

  private def toBuild(data: Tuple3[String, BuildStatus, String]) = new Build(data._1, data._2, Some(data._3))

}