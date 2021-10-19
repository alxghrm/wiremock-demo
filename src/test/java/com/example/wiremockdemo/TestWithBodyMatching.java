package com.example.wiremockdemo;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@WireMockTest
public class TestWithBodyMatching extends BaseTest {

  @Test
  void matchRequestBody() throws IOException {

    stubFor(post(urlEqualTo("/baeldung/wiremock"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withRequestBody(containing("\"testing-library\": \"Wiremock\""))
        .withRequestBody(containing("\"website\": \"wiremock.org\""))
        .willReturn(aResponse().withStatus(200))
    );

    InputStream jsonInputStream = this.getClass().getClassLoader()
        .getResourceAsStream("wiremock_intro.json");
    String jsonString = convertInputStreamToString(jsonInputStream);
    StringEntity entity = new StringEntity(jsonString);

    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost request = new HttpPost("http://localhost:8080/baeldung/wiremock");
    request.addHeader("Content-Type", "application/json");
    request.setEntity(entity);
    HttpResponse response = httpClient.execute(request);

    verify(postRequestedFor(urlEqualTo("/baeldung/wiremock"))
        .withHeader("Content-Type", equalTo("application/json")));
    Assertions.assertEquals(200, response.getStatusLine().getStatusCode());
    System.out.println("Response Body ===============================");
    System.out.println(response.getEntity().getContent());
  }

}
