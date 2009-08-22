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

/*
 Takes the given HTML fragment & CSS and creates a valid HTML page complete with header, title etc
 TODO: This seems very specific to the BuildWall stuff - should probably rename this to make it more specific
 */
class HtmlPage {

  def apply(cssUrl: String, htmlFragment: Node) : Node = {
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
      <head>
          <link rel="stylesheet" href="/static/common.css" type="text/css" />
          <link rel="stylesheet" href={ cssUrl } type="text/css" />
          <meta http-equiv="refresh" content="30" />
      </head>
      <body>
        { htmlFragment }
      </body>
    </html>
  }

}