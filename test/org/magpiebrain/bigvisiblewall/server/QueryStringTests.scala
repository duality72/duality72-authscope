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


import org.specs.runner.JUnit
import org.specs.Specification

class QueryStringTests extends Specification with JUnit {

  "Query String Parser" should {

    "Extract single name value pair" in {
      val parser = new QueryString("limit=5")
      parser.get("limit") must equalTo("5")
    }

    "Extract multiple name pair values" in {
      val parser = new QueryString("limit=5&offset=10")
      parser.get("limit") must equalTo("5")
      parser.get("offset") must equalTo("10")
    }

    "Return a default value if the underlying value is not specified" in {
      val parser = new QueryString("")
      parser.getOrElse("limit", "5") must equalTo("5")
    }

    "Extract multiple values for the same param if requested" in {
      val parser = new QueryString("prefix=a&prefix=b")
      parser.getAllOrElse("prefix", List()) must haveTheSameElementsAs(List("a", "b"))
    }

    "Provide a default when requesting multiple values for the same param" in {
      val parser = new QueryString("")
      parser.getAllOrElse("prefix", List("Default")) must haveTheSameElementsAs(List("Default"))
    }

    "Apply a function to a value" in {
      val parser = new QueryString("range=5,10") 
      val result = parser.get("range", ((value) => new Tuple2(value.split(",")(0), value.split(",")(1))))
      result._1 must beEqualTo("5")
      result._2 must beEqualTo("10")
    }
    
  }

}