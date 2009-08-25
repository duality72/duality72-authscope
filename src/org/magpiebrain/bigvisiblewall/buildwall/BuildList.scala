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


import scala.xml.{Elem, Node}

/**
 * HTML representation of a list of builds
 */

class BuildList(val builds: List[Build]) {

  def asHtml(): Node = {
    return <ul class="builds">{ builds.map(build => buildToHtml(build)) }</ul>
  }

  private def buildToHtml(build: Build) = {
    //TODO: Should deal more elegantly with the optionality with the URL
    var buildLink = <a class="project" href={ build.urlToBuild.getOrElse("") }>{ build.name }</a>
    if (!build.urlToBuild.isDefined) {
      buildLink = <span class="project">{ build.name }</span>
    }

    <li class={ "build " + build.status.name.toLowerCase}>
      { buildLink }
      { stepsToHtml(build.steps) }
    </li>
  }

  private def stepsToHtml(steps: Seq[Build]) : Elem = {
    if (!steps.isEmpty) {
      return <ul class="steps">
          { steps.map(build => buildToHtml(build)) }
        </ul>
    }
    return null
  }

}