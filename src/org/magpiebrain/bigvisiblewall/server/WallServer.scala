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
package org.magpiebrain.bigvisiblewall.server


import org.magpiebrain.bigvisiblewall.buildwall._
import org.magpiebrain.bigvisiblewall.cardwall.repository.{ProjectRepository}
import org.magpiebrain.bigvisiblewall.cardwall.web.CardList
import org.magpiebrain.bigvisiblewall.common.WebClient
import org.eclipse.jetty.util.resource.Resource
import java.io.File
import java.net.URLDecoder
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.{Request, Server}
import scala.xml.{Elem, Node}

class WallServer(val webClient: WebClient, val projectRepository: ProjectRepository, val port: Int, val staticFileDir: String) {

  val server = new Server(port)
  server.setHandler(new UrlDispatcher(webClient, projectRepository, staticFileDir))

  def start = {
    server.start  
  }

  def stop = {
    server.stop
  }
}

private class UrlDispatcher(val webClient: WebClient, val projectRepository: ProjectRepository, val staticFileDir: String) extends AbstractHandler {

  override def handle(target: String, request: HttpServletRequest, response: HttpServletResponse) = {
    response.setContentType("text/html");
    handleUrl(target, request.getQueryString, response)
    (request.asInstanceOf[Request]).setHandled(true);
  }

  //TODO pull different URL handlers into seperate classes
  private def handleUrl(target : String, queryStringAsString : String, response : HttpServletResponse) {

    try {
      val queryString = new QueryString(queryStringAsString)

      if (target.equals("/cardWall")) {
        //TODO Pull out Int from query string...
        val limit = queryString.getOrElse("limit", "4")
        ok(response, new CardList(projectRepository.cards.slice(0, limit.toInt)).asHtml)
      } else if (target.startsWith("/static/")) {
        val staticFilename = target.drop(8)
//        okString(response, new StaticFile(staticFileDir, staticFilename).contents)

        val resource = Resource.newResource(staticFileDir + File.separator + staticFilename)
        resource.writeTo(response.getOutputStream, 0, resource.length)
        response.setStatus(HttpServletResponse.SC_OK)
      } else if (target.equals("/")) {
        //TODO pull URL from query string
        val sources = queryString.getAllOrElse("source", List()) map (url => new CcTrayBuildSource(URLDecoder.decode(url), webClient))
        val projectPrefixes = queryString.getAllOrElse("prefix", List())
        val cruiseSources = queryString.getAllOrElse("cruiseSource", List()) map (v => cruiseSource(webClient, v))

        val source = new PrefixFilteringBuildSource(new CompositeBuildSource(List() ++ sources ++ cruiseSources), projectPrefixes) with ByNameSorting
        val displayType = queryString.getOrElse("display", "smart")

        ok(response, new BuildWall().render(source, displayType))
      } else {
        notFound(response, <h1>Not Found</h1>)
      }
    } catch {
      case e => {
        e.printStackTrace
        error(response, <html><head> <meta http-equiv="refresh" content="60"></meta></head><h1>Error</h1><pre>{e}</pre></html>)
      }
    }
  }

  private def cruiseSource(client: WebClient, value: String) : CruiseCcTrayBuildSource = {
    if (!value.contains(",")) {
      return new CruiseCcTrayBuildSource(URLDecoder.decode(value), client, 1);
    }
    val parts = value.split(",")
    return new CruiseCcTrayBuildSource(URLDecoder.decode(parts(0)), client, parts(1).toInt)
  }

  private def ok(response: HttpServletResponse, html: Node) = {
     okString(response, html.toString)
  }

  private def okString(response: HttpServletResponse, value: String) = {
     response.setStatus(HttpServletResponse.SC_OK)
     response.getWriter().println(value)
  }

  private def notFound(response: HttpServletResponse, html: Elem) = {
     response.setStatus(HttpServletResponse.SC_NOT_FOUND)
     response.getWriter().println(html.toString())
  }

  private def error(response: HttpServletResponse, html: Elem) = {
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    response.getWriter().println(html.toString())
  }
}