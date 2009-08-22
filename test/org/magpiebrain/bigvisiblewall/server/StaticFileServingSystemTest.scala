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


import java.io.{FileWriter, File}
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.specs.runner.JUnit
import org.specs.Specification

class StaticFileServingSystemTest extends Specification with JUnit {

  "Static File Serving" should {

    "Serve a file from the static files directory" in {
      val tempDir = createTempDir("TEST-DIR")
      val fakeCssFile = File.createTempFile("FakeCssFile", null, tempDir)

      val fileWriter = new FileWriter(fakeCssFile)
      try {
        fileWriter.write("Some Awesome CSS")
      } finally {
        fileWriter.close
      }

      val server = new WallServer(null, null, 12345, tempDir.getAbsolutePath)
      try {
        server.start
        val driver = new HtmlUnitDriver
        driver.navigate.to("http://localhost:12345/static/" + fakeCssFile.getName)
        driver.getPageSource.trim must equalTo("Some Awesome CSS")
      } finally {
        server.stop
      }

    }
  }

  private def createTempDir(dirName: String) : File = {
    val tempDir = File.createTempFile("dirName", null)

    if (!tempDir.delete()) {
      throw new RuntimeException("Unable to delete temp dir")
    }

    if (!tempDir.mkdir()) {
      throw new RuntimeException("Unable to create temp dir")
    }

    return tempDir
  }

}