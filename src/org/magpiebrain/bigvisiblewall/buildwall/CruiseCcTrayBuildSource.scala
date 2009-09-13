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


import common.WebClient

/**
 * Cruise takes its structure of Builds, Steps and stages, and enters them as seperate flat
 * entries in the cctray feed, e.g.:
 *
 * <pre>
 * Project :: Stage 1
 * Project :: Stage 1 :: Step 1
 * Project :: Stage 1 :: Step 2
 * </pre>
 *
 * This source handles this to create a hirearchical build which can be drilled into
 *
 * @author Sam Newman (sam.newman@gmail.com)
 */

//TODO I dislike having to pass in the depth param here
class CruiseCcTrayBuildSource(val url: String, val client: WebClient, val depth: Int) extends BuildSource {

  def get : List[Build] = {
    val feedParser = new CcTrayFeedParser()
    return new CruiseBuildFactory(depth).make(feedParser.parse(client.getAsXml(url)))
  }

}