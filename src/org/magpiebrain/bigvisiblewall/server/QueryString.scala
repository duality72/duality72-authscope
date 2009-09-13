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


import collection.mutable.{Buffer, ArrayBuffer, HashMap}

class QueryString(val underlyingString: String) {

  val map = parse(underlyingString)

  def get[A](key: String, f: String => A) : A = {
    return f(get(key))
  }

  def get(paramName: String) : String = {
    return map(paramName)(0)
  }

  def getIntAsOption(paramName: String): Option[Int] = {
    if (map.isDefinedAt(paramName)) {
      return Some(get(paramName).toInt)
    }
    return None
  }

  def getAll(paramName: String) : List[String] = {
    return List() ++ map(paramName)
  }

  def getAllOrElse(paramName: String, default: List[String]): List[String] = {
    if (map.isDefinedAt(paramName)) {
      return List() ++ map(paramName)
    }
    return default
  }

  def getOrElse(paramName: String, default: String) : String = {
    if (map.isDefinedAt(paramName)) {
      return get(paramName)
    }
    return default
  }

  def parse(queryString: String) : Map[String, Buffer[String]] = {
    if (queryString == null || queryString.isEmpty) {
      return Map()
    }
    
    val values = new HashMap[String, Buffer[String]]
    val pairSplits = queryString.split("&")

    for (pairSplit <- pairSplits) {
      val keyValue = pairSplit.split("=")
      val key = keyValue(0)
      val value = keyValue(1)

      if (values.isDefinedAt(key)) {
        values(key).append(value)
      } else {
        values.put(key, new ArrayBuffer() + value)
      }
    }

    return collection.immutable.Map[String, Buffer[String]]() ++ values
  }
}