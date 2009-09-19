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


import collection.mutable.{ArrayBuffer, HashMap}

/**
 * @author Sam Newman (sam.newman@gmail.com)
 */
class CruiseBuildFactory(val depth: Int) extends BuildFactory {

  def make(data: List[Tuple4[String, BuildStatus, String, String]]): List[Build] = {
    //Sort the list so we find the root nodes first
    val sortedEntries = data.sort((d1, d2) => (d1._1.split(" :: ").length < d2._1.split(" :: ").length))
    val builds = new HashMap[String, Build]

    for (entry <- sortedEntries) {
      val buildName = entry._1
      if (isStage(buildName)) {
        builds.put(buildName, new Build(buildName, entry._2, Some(entry._3)))
      } else {
        builds(stageName(buildName)).addChild(new Build(buildName, entry._2, Some(entry._3)))
      }
    }

    // Now we have a list of stages that need to be nested under a 'root' Builds
    // which represents each project
    val stages = builds.values.toList
    val projects = new HashMap[String, Build]

    for (stage <- stages) {
      val projectName = projectNameFromStageName(stage.name)
      if (!projects.isDefinedAt(projectName)) {
        projects.put(projectName, new CruiseProject(projectName))
      }
      projects(projectName).addChild(stage)
    }

    return filterByDepth(depth, projects.values.toList)
  }

  private def filterByDepth(depth: Int, projects: List[Build]) : List[Build] = {
    //TODO should just rewrite this as a recursive fn with depth control...
    if (depth == 2) {
      val buf = new ArrayBuffer[Build]()
      projects.foreach ( (project) => buf ++= project.getChildren)
      return buf.toList
    } else if (depth == 3) {
    val buf = new ArrayBuffer[Build]()
      projects.foreach ( (project) => project.getChildren.foreach( (child) => buf ++= child.getChildren))
      return buf.toList
    }

    return projects
  }

  /*
   * A 'stage' in cruise, is Project :: Quick Build - something at a level deeper
   * is considered a step inside the stage
   */
  private def isStage(name: String) : Boolean = {
    return name.split(" :: ").length == 2
  }

  private def stageName(name: String) : String = {
    return name.reverse.split(" :: ", 2)(1).reverse
  }

  private def projectNameFromStageName(name: String) : String = {
    return name.split(" :: ")(0)
  }

}