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
package org.magpiebrain.bigvisiblewall.common


import scala.io.Source
import org.apache.commons.httpclient.auth.AuthScope
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.{MultiThreadedHttpConnectionManager, HttpStatus, UsernamePasswordCredentials, HttpClient}
import scala.xml.{XML, Node}

trait WebClient {

  /**
   * GETs a URL, attempting to transform the response into XML.
   * Any response other than a 200 results in a RuntimeException
   */
  def getAsXml(url: String) : Node

  def setBasicCredentials(username: String, password: String, host: String)

  def setProxyConfig(hostname: String, port: Int)
}


/**
 *  Wraps apache common's HttpClient to provide a simpler API
 */
class CommonsWebClient extends WebClient {
  val httpClient = new HttpClient
  httpClient.setHttpConnectionManager(new MultiThreadedHttpConnectionManager())

  def getAsXml(url: String) : Node = {
    return XML.loadString(get(url))
  }

  def setProxyConfig(hostname: String, port: Int) {
    httpClient.getHostConfiguration.setProxy(hostname, port)
  }

  def setBasicCredentials(username: String, password: String, host: String) {
    val defaultcreds = new UsernamePasswordCredentials(username, password);
    httpClient.getState().setCredentials(new AuthScope(host, 80, AuthScope.ANY_REALM), defaultcreds);
  }

   /*
	 GETs a URL, throwing a RuntimeException if we don't get a 200 response
	  */
  private def get(url: String) : String = {
    val method = new GetMethod(url)

    using(method) {
      val statusCode = httpClient.executeMethod(method)

      if (statusCode != HttpStatus.SC_OK) {
        throw new RuntimeException("Got status code " + statusCode + " when trying to retrieve " + url)
      }

      val stringBuilder = new StringBuilder
      for {
        (line) <- Source.fromInputStream(method.getResponseBodyAsStream)
      } stringBuilder.append(line)

      stringBuilder.toString
    }
  }

  private def using(method: GetMethod)(fn: String) : String = {
    try {
      fn
    } finally {
      method.releaseConnection()
    }
  }
}