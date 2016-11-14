package com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient;

import static com.flymatcher.skyscanner.adaptor.cheapestquotes.dto.builders.CheapestQuotesRequestBuilder.aCheapestQuotesRequest;
import static com.flymatcher.skyscanner.cheapestquotes.builders.BrowseQuotesResponseAPIDtoBuilder.aBrowseQuotesResponseAPIDto;
import static com.flymatcher.skyscanner.cheapestquotes.builders.QuoteDtoBuilder.aQuoteDto;
import static com.flymatcher.skyscanner.cheapestquotes.builders.SkyscannerLegBuilder.aSkyscannerLeg;
import static com.flymatcher.skyscanner.cheapestquotes.carrier.builders.CarriersDtoBuilder.aCarriersDto;
import static com.flymatcher.skyscanner.cheapestquotes.currency.builders.CurrencyDtoBuilder.aCurrencyDto;
import static com.flymatcher.skyscanner.cheapestquotes.place.builders.PlaceDtoBuilder.aPlaceDto;
import static java.nio.charset.Charset.forName;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.dto.CheapestQuotesRequest;
import com.flymatcher.skyscanner.adaptor.exception.SkyscannerBadRequestException;
import com.flymatcher.skyscanner.adaptor.exception.SkyscannerServerException;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

public class CheapestQuotesClientTest {

  private static final String SKYSCANNER_CHEAPEST_QUOTES_BASE_URL = "base-url";
  private static final String CHEAPEST_QUOTES_PATH_URL =
      "/GR/EUR/en-GB/ATH/ESP/2016-10-10/2016-10-20";

  private static final String SKYSCANNER_API_KEY = "api-key";

  private static final String CHEAPEST_QUOTES_URL = SKYSCANNER_CHEAPEST_QUOTES_BASE_URL
      + CHEAPEST_QUOTES_PATH_URL + "?apiKey=" + SKYSCANNER_API_KEY;

  private static final String ERROR_MESSAGE = "Could not get a valid skyscanner quote response.";
  private static final String VALIDATION_MESSAGE =
      "Skyscanner quote response included validation errors.";

  private CheapestQuotesClient client;

  @Mock
  private RestTemplate mockRestTemplate;
  @Mock
  private ResponseEntity<BrowseQuotesResponseAPIDto> mockResponseEntity;


  @Rule
  public ExpectedException expectedException = none();

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    client = new CheapestQuotesClientImpl(mockRestTemplate, SKYSCANNER_CHEAPEST_QUOTES_BASE_URL,
        SKYSCANNER_API_KEY, new ObjectMapper());
  }

  @Test
  public void shouldGetCheapestQuotes() {

    final CheapestQuotesRequest cheapestQuotesRequest =
        aCheapestQuotesRequest().withDefaultValues().build();

    final BrowseQuotesResponseAPIDto expected = createBrowseQuotesResponseAPIDto();

    given(
        mockRestTemplate.exchange(CHEAPEST_QUOTES_URL, GET, null, BrowseQuotesResponseAPIDto.class))
            .willReturn(mockResponseEntity);

    given(mockResponseEntity.getBody()).willReturn(expected);

    given(mockResponseEntity.getStatusCode()).willReturn(OK);

    final BrowseQuotesResponseAPIDto actual = client.getCheapestQuotes(cheapestQuotesRequest);

    assertEquals(expected, actual);

  }

  @Test
  public void shouldThrowSkyscannerBadRequestFor400() {

    final CheapestQuotesRequest cheapestQuotesRequest =
        aCheapestQuotesRequest().withDefaultValues().build();

    final String errorJson =
        "{\"ValidationErrors\": [{\"ParameterName\": \"apikey\", \"Message\": \"ApiKey invalid\"}]}";

    given(
        mockRestTemplate.exchange(CHEAPEST_QUOTES_URL, GET, null, BrowseQuotesResponseAPIDto.class))
            .willThrow(buildHttpStatusCodeException(BAD_REQUEST, errorJson));

    expectedException.expect(SkyscannerBadRequestException.class);
    expectedException.expectMessage(VALIDATION_MESSAGE);

    client.getCheapestQuotes(cheapestQuotesRequest);


  }

  @Test
  public void shouldThrowSkyscannerServerErrorForUnexpectedValidationError() {

    final CheapestQuotesRequest cheapestQuotesRequest =
        aCheapestQuotesRequest().withDefaultValues().build();

    final String errorJson = "{\"error\":\"Some unexpected json response\"}";

    given(
        mockRestTemplate.exchange(CHEAPEST_QUOTES_URL, GET, null, BrowseQuotesResponseAPIDto.class))
            .willThrow(buildHttpStatusCodeException(BAD_REQUEST, errorJson));

    expectedException.expect(SkyscannerServerException.class);
    expectedException.expectMessage(
        ERROR_MESSAGE + " Error: Could not unmarshal error response. Response was: " + errorJson);

    client.getCheapestQuotes(cheapestQuotesRequest);


  }

  @Test
  public void shouldThrowSkyscannerServerErrorFor500() {

    final CheapestQuotesRequest cheapestQuotesRequest =
        aCheapestQuotesRequest().withDefaultValues().build();

    final String errorJson = "Something went terribly wrong";

    given(
        mockRestTemplate.exchange(CHEAPEST_QUOTES_URL, GET, null, BrowseQuotesResponseAPIDto.class))
            .willThrow(buildHttpStatusCodeException(INTERNAL_SERVER_ERROR, errorJson));

    expectedException.expect(SkyscannerServerException.class);
    expectedException.expectMessage(ERROR_MESSAGE + " Error: Internal Server Error.");

    client.getCheapestQuotes(cheapestQuotesRequest);


  }

  private HttpClientErrorException buildHttpStatusCodeException(final HttpStatus statusCode,
      final String responseBody) {
    return new HttpClientErrorException(statusCode, statusCode.getReasonPhrase(),
        responseBody.getBytes(), forName("UTF-8"));
  }

  private BrowseQuotesResponseAPIDto createBrowseQuotesResponseAPIDto() {
    // @formatter:off
    return aBrowseQuotesResponseAPIDto()
                    .withQuotes(aQuoteDto()
                                .withDirect(true)
                                .withInboundLeg(aSkyscannerLeg()
                                                .withCarrierIds(asList(1050))
                                                .withDepartureDate("2016-10-23T00:00:00")
                                                .withDestinationId(65655)
                                                .withOriginId(40074))
                                .withOutboundLeg(aSkyscannerLeg()
                                                .withCarrierIds(asList(1050))
                                                .withDepartureDate("2016-10-10T00:00:00")
                                                .withDestinationId(40074)
                                                .withOriginId(66270))                                                                    
                                .withMinPrice(62)
                                .withQuoteDateTime("2016-08-06T22:01:00")
                                .withQuoteId(1), 
                                aQuoteDto()
                                .withDirect(false)
                                .withInboundLeg(aSkyscannerLeg()
                                                .withCarrierIds(asList(1324))
                                                .withDepartureDate("2016-12-30T00:00:00")
                                                .withDestinationId(65698)
                                                .withOriginId(40074))
                                .withOutboundLeg(aSkyscannerLeg()
                                                .withCarrierIds(asList(1324))
                                                .withDepartureDate("2016-12-26T00:00:00")
                                                .withDestinationId(40074)
                                                .withOriginId(65698))                                                                    
                                .withMinPrice(165)
                                .withQuoteDateTime("2016-08-18T11:56:00")
                                .withQuoteId(2))
                    .withPlaces(aPlaceDto()
                                      .withCityId("ABER")
                                      .withCityName("Aberdeen")
                                      .withCountryName("United Kingdom")
                                      .withIataCode("ABZ")
                                      .withName("Aberdeen")
                                      .withPlaceId(40074)
                                      .withSkyscannerCode("ABZ")
                                      .withType("Station"))
                    .withCurrencies(aCurrencyDto()
                                      .withCode("GBP")
                                      .withDecimalDigits(2)
                                      .withDecimalSeparator(".")
                                      .withRoundingCoefficient(0)
                                      .withSpaceBetweenAmountAndSymbol(false)
                                      .withSymbolOnLeft(true)
                                      .withThousandsSeparator(","))
                    .withCarriers(aCarriersDto()
                                      .withCarrierId(1050)
                                      .withName("easyJet"))
                  .build();
    // @formatter:on
  }

}
