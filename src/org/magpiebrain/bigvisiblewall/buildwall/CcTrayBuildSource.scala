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
 * @author Sam Newman (sam.newman@gmail.com)
 */

class CcTrayBuildSource(val url: String, val client: WebClient) extends BuildSource {

  def get : List[Build] = {
    val feedParser = new CcTrayFeedParser()
    return new SimpleBuildFactory().make(feedParser.parse(client.getAsXml(url)))
  }

}