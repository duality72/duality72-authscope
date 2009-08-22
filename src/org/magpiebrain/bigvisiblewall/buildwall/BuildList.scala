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


import scala.xml.Node

/**
 * HTML representation of a list of builds
 */

class BuildList(val builds: List[Build]) {

  def asHtml(): Node = {
    return <ul class="builds">{ builds.map(build => asHtml(build)) }</ul>
  }

  private def asHtml(build: Build) = {
    <li class={ "build " + build.status.name.toLowerCase}>
      <a class="project" href={ build.urlToBuild }>{ build.name }</a>
    </li>
  }

}