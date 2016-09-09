package com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient;

import static com.flymatcher.skyscanner.adaptor.api.builders.CheapestQuotesRequestBuilder.aCheapestQuotesRequest;
import static com.flymatcher.skyscanner.cheapestquotes.builders.BrowseQuotesResponseAPIDtoBuilder.aBrowseQuotesResponseAPIDto;
import static com.flymatcher.skyscanner.cheapestquotes.builders.OutboundLegBuilder.aOutboundLeg;
import static com.flymatcher.skyscanner.cheapestquotes.builders.QuoteDtoBuilder.aQuoteDto;
import static com.flymatcher.skyscanner.cheapestquotes.carrier.builders.CarriersDtoBuilder.aCarriersDto;
import static com.flymatcher.skyscanner.cheapestquotes.currency.builders.CurrencyDtoBuilder.aCurrencyDto;
import static com.flymatcher.skyscanner.cheapestquotes.place.builders.PlaceDtoBuilder.aPlaceDto;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import com.flymatcher.skyscanner.adaptor.api.CheapestQuotesRequest;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

public class CheapestQuotesClientTest {

  private final String SKYSCANNER_CHEAPEST_QUOTES_BASE_URL = "base-url";
  private static final String CHEAPEST_QUOTES_PATH_URL =
      "/{country}/{currency}/{locale}/{city}/anywhere/{outboundPartialDate}/{inboundPartialDate}";

  private final String SKYSCANNER_API_KEY = "api-key";


  private CheapestQuotesClient client;

  @Mock
  private RestTemplate mockRestTemplate;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    client = new CheapestQuotesClientImpl(mockRestTemplate, SKYSCANNER_CHEAPEST_QUOTES_BASE_URL,
        SKYSCANNER_API_KEY);
  }

  @Test
  public void shouldGetCheapestQuotes() {

    final CheapestQuotesRequest cheapestQuotesRequest =
        aCheapestQuotesRequest().withDefaultValues().build();

    final BrowseQuotesResponseAPIDto expected = createBrowseQuotesResponseAPIDto();

    given(mockRestTemplate.getForObject(
        SKYSCANNER_CHEAPEST_QUOTES_BASE_URL + CHEAPEST_QUOTES_PATH_URL + "?apiKey="
            + SKYSCANNER_API_KEY,
        BrowseQuotesResponseAPIDto.class, buildParametersMap(cheapestQuotesRequest)))
            .willReturn(expected);

    final BrowseQuotesResponseAPIDto actual = client.getCheapestQuotes(cheapestQuotesRequest);
    assertEquals(expected, actual);

  }

  private BrowseQuotesResponseAPIDto createBrowseQuotesResponseAPIDto() {
    // @formatter:off
    return aBrowseQuotesResponseAPIDto()
                    .withQuotes(aQuoteDto()
                                .withDirect(true)
                                .withInboundLeg(aOutboundLeg()
                                                .withCarrierIds(asList(1050))
                                                .withDepartureDate("2016-10-23T00:00:00")
                                                .withDestinationId(65655)
                                                .withOriginId(40074))
                                .withOutboundLeg(aOutboundLeg()
                                                .withCarrierIds(asList(1050))
                                                .withDepartureDate("2016-10-10T00:00:00")
                                                .withDestinationId(40074)
                                                .withOriginId(66270))                                                                    
                                .withMinPrice(62)
                                .withQuoteDateTime("2016-08-06T22:01:00")
                                .withQuoteId(1), 
                                aQuoteDto()
                                .withDirect(false)
                                .withInboundLeg(aOutboundLeg()
                                                .withCarrierIds(asList(1324))
                                                .withDepartureDate("2016-12-30T00:00:00")
                                                .withDestinationId(65698)
                                                .withOriginId(40074))
                                .withOutboundLeg(aOutboundLeg()
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

  private Map<String, String> buildParametersMap(final CheapestQuotesRequest request) {
    final Map<String, String> vars = new HashMap<>();
    vars.put("country", request.getCountry());
    vars.put("currency", request.getCurrency());
    vars.put("city", request.getCity());
    vars.put("locale", request.getLocale());
    vars.put("outboundPartialDate", request.getOutboundPartialDate().toString());
    vars.put("inboundPartialDate", request.getInboundPartialDate().toString());

    return vars;
  }

}
