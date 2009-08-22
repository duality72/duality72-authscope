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


import cardwall.model.{User, Card}
import cardwall.repository.ProjectRepository
import collection.mutable.ListBuffer
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.{By}
import org.specs.runner.JUnit
import org.specs.Specification
import scala.collection.jcl.Conversions._

class CardWallTests extends Specification with JUnit {

  "Story Wall Server" should {
    val stubProjectRepo = new StubProjectRepository()
    val server = new WallServer(null, stubProjectRepo, 12345, null)
    val driver = new HtmlUnitDriver

    doFirst {
      server.start
    }

    doBefore {
      stubProjectRepo.clear
    }

    "Display 4 Cards by default" in {
      stubProjectRepo.addCards(
        Card("One"),
        Card("Two"),
        Card("Three"),
        Card("Four"),
        Card("Five")
        )

      driver.navigate.to("http://localhost:12345/")

      val cardElements = convertList(driver.findElements(By.className("card")))
      val cardTitles = cardElements map (elem => elem.findElement(By.className("title")).getText)

      cardTitles must haveSize(4)
      cardTitles must contain("One")
      cardTitles must contain("Two")
      cardTitles must contain("Three")
      cardTitles must contain("Four")
    }

    "Display a restricted number of cards on request" in {
      stubProjectRepo.addCards(
        Card("One"),
        Card("Two")
        )

      driver.navigate.to("http://localhost:12345/?limit=2")

      val cardElements = convertList(driver.findElements(By.className("card")))
      val cardTitles = cardElements map (elem => elem.findElement(By.className("title")).getText)

      cardTitles must haveSize(2)
      cardTitles must contain("One")
      cardTitles must contain("Two")
    }

    "Can handle requesting more cards than there are" in {
      stubProjectRepo.addCards(Card("One"))

      driver.navigate.to("http://localhost:12345/?limit=2")

      val cardElements = convertList(driver.findElements(By.className("card")))
      val cardTitles = cardElements map (elem => elem.findElement(By.className("title")).getText)

      cardTitles must haveSize(1)
      cardTitles must contain("One")
    }

    doLast {
      server.stop
    }

  }

  class StubProjectRepository extends ProjectRepository {
    var cardList = new ListBuffer[Card]
    
    def cards : List[Card] = {
      return cardList.toList
    }

    def users : Map[String, User] = {
      return Map("1" -> new User("1", "Some User"))
    }

    def addCards(newCards: Card*) = {
      cardList.appendAll(newCards)
    }

    def clear = cardList.clear
  }

}