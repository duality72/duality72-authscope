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
package org.magpiebrain.bigvisiblewall.cardwall.repository.mingle

import org.magpiebrain.bigvisiblewall.common.WebClient
import org.magpiebrain.bigvisiblewall.cardwall.model.{Card, User}
import org.magpiebrain.bigvisiblewall.cardwall.repository.ProjectRepository
import scala.xml.XML

class MingleProjectRepository(val mingleUrl : String, val project : String, val client : WebClient) extends ProjectRepository {

  def cards : List[Card] = {
    val cardsXml = client.getAsXml(mingleUrl + "projects/" + project + "/cards.xml")
    return new CardParser(users).parse(cardsXml)
  }
 
  def users : Map[String, User] = {
    val usersXml = client.getAsXml(mingleUrl + "projects/" + project + "/users.xml")
    return new UserParser(new PhotoRepository()).parse(usersXml)
  }

}