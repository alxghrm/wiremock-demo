package com.example.wiremockdemo;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.client.RestTemplate;

public class BaseTest {

  RestTemplate restTemplate;
  WireMockServer wireMockServer;

  @BeforeEach
  void configureSystemUnderTest() {
    this.wireMockServer = new WireMockServer();
    this.wireMockServer.start();
    configureFor("localhost", 8080);
  }

  @AfterEach
  void stopWireMockServer() {
    this.wireMockServer.stop();
  }

  String convertResponseToString(HttpResponse httpResponse) throws IOException {

    InputStream responseStream = httpResponse.getEntity().getContent();

    return convertInputStreamToString(responseStream);
  }

  String convertInputStreamToString(InputStream inputStream) {

    Scanner scanner = new Scanner(inputStream, "UTF-8");
    String string = scanner.useDelimiter("\\Z").next();
    scanner.close();
    return string;
  }
}
