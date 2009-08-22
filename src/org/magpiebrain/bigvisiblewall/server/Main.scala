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

import org.magpiebrain.bigvisiblewall.server.CommandLineParsing._
import common.CommonsWebClient
import java.io.File

object Main {

  def main(args: Array[String]) {

    val proxyHost = parseOption("-proxy.host", args)
    val proxyPort = parseOption("-proxy.port", args)
    val username = parseOption("-http.username", args)
    val password = parseOption("-http.password", args)

    val webClient = new CommonsWebClient()

    if (proxyHost.isDefined && proxyPort.isDefined) {
      webClient.setProxyConfig(proxyHost.get, proxyPort.get.toInt)
    } else {
      println("No proxy settings specified. Use -proxy.host and -proxy.port if you need a proxy.")
    }

    if (username.isDefined && password.isDefined) {
      webClient.setBasicCredentials(username.get, password.get, null)
    } else {
      println("No BASIC auth settings specified. Use -http.username and -http.password if you need this.")
    }

    new WallServer(webClient, null, 8080, new File("./web/").getAbsolutePath).start
  }

}