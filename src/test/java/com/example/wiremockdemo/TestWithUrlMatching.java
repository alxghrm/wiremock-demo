package com.example.wiremockdemo;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

@WireMockTest
public class TestWithUrlMatching extends BaseTest {

   @Test
   void testWithUrlMatching() throws IOException {

      stubFor(get(urlPathMatching("/baeldung/.*"))
          .willReturn(aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "application/json")
              .withBody("\"testing-library\": \"Wiremock\"")));

      CloseableHttpClient httpClient = HttpClients.createDefault();
      HttpGet request = new HttpGet("http://localhost:8080/baeldung/wiremock");
      HttpResponse httpResponse = httpClient.execute(request);
      String response = convertResponseToString(httpResponse);

      verify(getRequestedFor(urlEqualTo("/baeldung/wiremock")));
      assertEquals(200, httpResponse.getStatusLine().getStatusCode());
      assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
      assertEquals("\"testing-library\": \"Wiremock\"", response);
   }
}
