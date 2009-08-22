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

/**
 * This functions from Eric Torreborre's post here: http://etorreborre.blogspot.com/2007/07/scala-to-heaven-first-step.html
 * TODO: Find out the licence for this stuff...
 */
object CommandLineParsing {

  def parseOption(name:String, args:Array[String]): Option[String] = {
    for (i <- List.range(0, args.length - 1))
      if (args(i).equalsIgnoreCase(name) && i < args.length -1 && args(i+1) != null)
        return Some(args(i+1))
    return None
  }

  def getOptionValue(name:String, defaultValue:String, args:Array[String]): String = {
    parseOption(name, args) match {
      case None => defaultValue
      case Some(value) => value
    }
  }
  
}