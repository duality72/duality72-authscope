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

class BuildList(val builds: List[Build], val displayType: String) {

  def asHtml(): Node = {
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
      <head>
          <link rel="stylesheet" href="/static/common.css" type="text/css" />
          <link rel="stylesheet" href={ cssForDisplayType } type="text/css" />
          <meta http-equiv="refresh" content="30" />
      </head>
      <body>
        { content(builds) }
      </body>
    </html>
  }

  private def content(builds: List[Build]): Elem = {
    displayType match {
      case "single" => <div> { builds.map(build => asTable(build)) } </div>
      case "smart" => {
        if (builds.length == 1) {
          <div> { builds.map(build => asTable(build)) } </div>
        } else {
          <ul class="builds">{ builds.map(build => buildToHtml(build)) }</ul>
        }
      }
      case _ => <ul class="builds">{ builds.map(build => buildToHtml(build)) }</ul>
    }
  }

  private def asTable(build: Build): Elem = {
    <table class={ "build " + build.getStatus.name.toLowerCase }>
              <tr valign="middle" align="center">
                <td>{ linkToBuild(build) }</td>
              </tr>
            </table>
  }

  private def cssForDisplayType: String = {
    displayType match {
      case "list" => "/static/buildwall-list.css"
      case "single" => "/static/buildwall-single.css"
      case "smart" => {
        if (builds.length == 1) {
          "/static/buildwall-single.css"
        } else if (builds.length < 6) {
          "/static/buildwall-list.css"
        } else {
          "/static/buildwall-grid.css"
        }
      }
      case _ => "/static/buildwall-grid.css"
    }
  }

  private def linkToBuild(build: Build): Elem = {
    //TODO: Should deal more elegantly with the optionality with the URL
    val className = "project" + (if(build.working) " working" else "")
    var buildLink = <a class={ className } href={ build.urlToBuild.getOrElse("") }>{ build.name }</a>
    if (!build.urlToBuild.isDefined) {
      buildLink = <span class={ className }>{ build.name }</span>
    }
    return buildLink
  }

  private def buildToHtml(build: Build) = {
    <li class={ "build " + build.getStatus.name.toLowerCase}>
      { linkToBuild(build) }
      { stepsToHtml(build.children) }
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