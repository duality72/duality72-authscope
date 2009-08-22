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

import collection.mutable.HashSet
import common.WebClient
import scala.xml.Node

class BuildWall(val client: WebClient) {

   def render(ccTrayUrl: String, prefixes: Seq[String]) : Node = {
     val feedParser = new CcTrayFeedParser()

     var builds = feedParser.parse(client.getAsXml(ccTrayUrl))

     builds = builds.sort((b1, b2) => (b1.name compareTo b2.name) < 0)

      if (!prefixes.isEmpty) {
       var filteredBuids = HashSet[Build]()
       for (prefix <- prefixes) {
         filteredBuids ++= builds.filter(_.name.startsWith(prefix))
       }
       builds = filteredBuids.toList
     }

     return new BuildList(builds).asHtml
   }
  
}