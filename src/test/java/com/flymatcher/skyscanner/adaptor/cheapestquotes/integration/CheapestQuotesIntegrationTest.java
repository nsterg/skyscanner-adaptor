package com.flymatcher.skyscanner.adaptor.cheapestquotes.integration;

import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static com.jayway.restassured.RestAssured.given;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.http.HttpStatus.SC_OK;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.flymatcher.skyscanner.adaptor.bootstrap.ServiceRunner;
import com.github.restdriver.clientdriver.ClientDriverRule;
import com.jayway.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ServiceRunner.class})
@TestPropertySource(locations = "classpath:/integration/test-application.properties")
@WebIntegrationTest("server.port:0")
public class CheapestQuotesIntegrationTest {

  @Value("${local.server.port}")
  private int port;

  @Value("${server.contextPath}")
  private String contextPath;

  @Value("${skyscanner.cheapest-quotes-base-url}")
  private String cheapestQuotesBaseUrl;

  @Value("${skyscanner.api-key}")
  private String apiKey;

  @Rule
  public ClientDriverRule driver = new ClientDriverRule(7001);

  @Before
  public void beforeEachTest() {
    RestAssured.port = port;
  }

  @Test
  public void shouldReturnHappyResponse() throws IOException {

    final String skyscannerResponse = readFileToString(new File(
        "src/test/resources/integration/responses/skyscanner-cheapest-quotes-response-200.json"));

    driver.addExpectation(
        onRequestTo("/GR/GBP/en-GB/ATH/anywhere/2016-10-10/2016-10-20").withMethod(GET)
            .withParam("apiKey", apiKey),
        giveResponse(skyscannerResponse, "application/json").withStatus(200));

    // @formatter:off
    given()
        .pathParam("country","GR")
        .pathParam("city","ATH")
        .pathParam("currency","GBP")
        .pathParam("locale","en-GB")
        .pathParam("outboundPartialDate","2016-10-10")
        .pathParam("inboundPartialDate","2016-10-20")
        .when()
           .get(buildRequestUrlStr())
        .then()
           .assertThat()
             .body(sameJSONAs(readFileToString(new File(
               "src/test/resources/integration/responses/adaptor-cheapest-quotes-response-200.json"))))
             .statusCode(SC_OK);
    // @formatter:on
  }

  private String buildRequestUrlStr() {
    return "http://localhost:" + port + contextPath
        + "/v1/cheapest-quotes/{country}/{city}/{currency}/{locale}/{outboundPartialDate}/{inboundPartialDate}";
  }

}

