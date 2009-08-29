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


import collection.mutable.ArrayBuffer
import common.WebClient
import org.specs.runner.JUnit
import org.specs.Specification
import scala.xml.Node

class BuildWallTests extends Specification with JUnit {

  "Build Wall" should {

    "Sort project names alphabetically" in {
      val webClient = new StubWebClient
      webClient.serve(
        <Projects>
          <Project name="B" activity="Sleeping" lastBuildStatus="Success"
                   lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33" webUrl="http://someLocation" />
          <Project name="A" activity="Sleeping" lastBuildStatus="Success"
                   lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33" webUrl="http://someLocation" />
          <Project name="C" activity="Sleeping" lastBuildStatus="Success"
                   lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33" webUrl="http://someLocation" />
        </Projects>)

      val client = new BuildWall(webClient)
      val buildWallHtml = client.render("SomeUrl", List(), new SimpleBuildFactory())

      val buildNames = buildNamesInElem(buildWallHtml)
      buildNames must containInOrder(List("A", "B", "C"))
    }

    "Filter build results by project name prefix" in {
      val webClient = new StubWebClient
      webClient.serve(
        <Projects>
          <Project name="Project 1 :: Some Stuff" activity="Sleeping" lastBuildStatus="Success"
                   lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33" webUrl="http://someLocation" />
          <Project name="Project 2 :: Some Other Stuff :: defaultJob" activity="Sleeping" lastBuildStatus="Success"
                   lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33" webUrl="http://someLocation" />
        </Projects>)

      val client = new BuildWall(webClient)
      val buildWallHtml = client.render("SomeUrl", List("Project 1"), new SimpleBuildFactory())
      
      val buildNames = buildNamesInElem(buildWallHtml)
      buildNames must haveSize(1)
      buildNames(0) must equalTo("Project 1 :: Some Stuff")
    }

    "Show all builds when no prefix is specified" in {
      val webClient = new StubWebClient
      webClient.serve(
        <Projects>
          <Project name="Project 1 :: Some Stuff" activity="Sleeping" lastBuildStatus="Success" lastBuildLabel="3"
                   lastBuildTime="2009-07-27T15:03:33" webUrl="http://someLocation" />
          <Project name="Project 2 :: Some Other Stuff :: defaultJob" activity="Sleeping" lastBuildStatus="Success"
                   lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33" webUrl="http://someLocation" />
        </Projects>)

      val client = new BuildWall(webClient)
      val buildWallHtml = client.render("SomeUrl", List(), new SimpleBuildFactory())

      val buildNames = buildNamesInElem(buildWallHtml)
      buildNames must haveSize(2)
      buildNames(0) must equalTo("Project 1 :: Some Stuff")
      buildNames(1) must equalTo("Project 2 :: Some Other Stuff :: defaultJob")
    }

    "Can filter results using multiple prefixes" in {
      val webClient = new StubWebClient
      webClient.serve(
        <Projects>
          <Project name="Project 1 :: Some Stuff" activity="Sleeping" lastBuildStatus="Success"
                   lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33" webUrl="http://someLocation" />
          <Project name="Project 2 :: Some Other Stuff :: defaultJob" activity="Sleeping" lastBuildStatus="Success"
                   lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33" webUrl="http://someLocation" />
          <Project name="Project 3 :: Some New Stuff" activity="Sleeping" lastBuildStatus="Success"
                   lastBuildLabel="3" lastBuildTime="2009-07-27T15:03:33" webUrl="http://someLocation" />
        </Projects>)

      val client = new BuildWall(webClient)
      val buildWallHtml = client.render("SomeUrl", List("Project 1", "Project 3"), new SimpleBuildFactory())

      val buildNames = buildNamesInElem(buildWallHtml)
      buildNames must haveSize(2)
      buildNames must haveTheSameElementsAs(List("Project 1 :: Some Stuff", "Project 3 :: Some New Stuff"))
    }
  }

  private def buildNamesInElem(root: Node) : Seq[String] = {
    val buildNames = new ArrayBuffer[String]
    for (val build <- elementsWithClass(root, "build")) {
      buildNames += textOfElementWithClassname(build, "project")
    }
    return buildNames
  }

  private def elementsWithClass(root: Node, className: String) : Seq[Node] = {
    return (root \\ "_").filter(n => hasClassName((n \\ "@class").text, className))
  }

  private def hasClassName(classList: String, className: String) : Boolean = {
    return classList.split(" ").contains(className)
  }

  private def textOfElementWithClassname(root: Node, className: String) : String = {
    val elems = elementsWithClass(root, className)

    if (elems.size != 1) {
      throw new RuntimeException("Only a single element expected")
    }

    return elems(0).text
  }

  class StubWebClient extends WebClient {
    var xmlToServe: Node = <xml />

    def getAsXml(url: String) : Node = xmlToServe

    def serve(xml: Node) = {
      xmlToServe = xml
    }

    def setBasicCredentials(username: String, password: String, host: String) {}

    def setProxyConfig(hostname: String, port: Int) {}
  }

}