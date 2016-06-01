/*
 * Copyright 2015 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.gate.controllers

import com.netflix.spinnaker.gate.services.BuildService
import com.netflix.spinnaker.gate.services.internal.IgorService
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class AuthControllerSpec extends Specification {

  MockMvc mockMvc

  def server = new MockWebServer()

  final URLENCODED_QUERY = "/auth/redirect?to=http%3A%2F%2Fdeck.spinnaker.io%2F%23%2Finfrastructure"
  final URLDECODED_URL = "http://deck.spinnaker.io/#/infrastructure"

  void cleanup() {
    server.shutdown()
  }

  void setup() {
    server.start()
    AuthController authController = new AuthController()
    authController.deckBaseUrl = new URL("http://deck.spinnaker.io")
    mockMvc = MockMvcBuilders.standaloneSetup(authController).build()
  }

  void 'should parse urlencoded urls'() {
    when:
    MockHttpServletResponse response = mockMvc.perform(get(URLENCODED_QUERY)
      .accept(MediaType.APPLICATION_JSON)).andReturn().response

    then:
    response.getHeader("Location") == URLDECODED_URL
  }


}
