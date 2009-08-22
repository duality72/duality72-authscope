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
package org.magpiebrain.bigvisiblewall.fixture

import collection.mutable.HashMap
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.Request

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.net.ServerSocket
import org.eclipse.jetty.util.log.{StdErrLog, Log}
import scala.xml.{Node, Elem}

class FakeHttpServer(port: Int) {


  //Warning - this installs this logger for ALL jetty instances in process
  Log.setLog(new WarnLogger)

  val handler = new UrlHandler()
  val server = new Server(port)

  def getPort: Int = port

  def start() = {
    server.setHandler(handler)
    server.start()
  }

  def serveHtmlForUrl(url: String, html: Elem) = {
    handler.serveHtmlForUrl(url, html)
  }

  def stop = {
    server.stop()
  }
}

object FakeHttpServer {
  def usingRandomFreePort: FakeHttpServer = {
    val socket = new ServerSocket(0)
    val port = socket.getLocalPort()
    socket.close()
    return new FakeHttpServer(port)
  }
}

protected class UrlHandler extends AbstractHandler {

  val mappings = new HashMap[String, Node]

  def serveHtmlForUrl(url: String, html: Node) = {
    mappings += url -> html
  }

  override def handle(target: String, request: HttpServletRequest, response: HttpServletResponse) = {
	  response.setContentType("text/html");

    if (mappings.contains(target)) {
      response.getWriter().println(mappings(target).toString())
        response.setStatus(HttpServletResponse.SC_OK)
    } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND)
    }

	  (request.asInstanceOf[Request]).setHandled(true);
  }
}

/**
 * Only logs WARN and above to StdErr.
 */
private class WarnLogger extends StdErrLog {

  override def info(msg: String, o1: Object, o2: Object) = {}

}
