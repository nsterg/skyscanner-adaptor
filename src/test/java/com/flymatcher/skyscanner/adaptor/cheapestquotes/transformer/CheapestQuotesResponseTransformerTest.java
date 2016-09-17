package com.flymatcher.skyscanner.adaptor.cheapestquotes.transformer;

import static com.flymatcher.skyscanner.adaptor.api.builders.LegBuilder.aLeg;
import static com.flymatcher.skyscanner.adaptor.api.builders.SkyscannerCheapestQuotesResponseBuilder.aSkyscannerCheapestQuotesResponse;
import static com.flymatcher.skyscanner.adaptor.api.builders.SkyscannerQuoteBuilder.aSkyscannerQuote;
import static com.flymatcher.skyscanner.cheapestquotes.builders.BrowseQuotesResponseAPIDtoBuilder.aBrowseQuotesResponseAPIDto;
import static com.flymatcher.skyscanner.cheapestquotes.builders.SkyscannerLegBuilder.aSkyscannerLeg;
import static com.flymatcher.skyscanner.cheapestquotes.builders.QuoteDtoBuilder.aQuoteDto;
import static com.flymatcher.skyscanner.cheapestquotes.carrier.builders.CarriersDtoBuilder.aCarriersDto;
import static com.flymatcher.skyscanner.cheapestquotes.currency.builders.CurrencyDtoBuilder.aCurrencyDto;
import static com.flymatcher.skyscanner.cheapestquotes.place.builders.PlaceDtoBuilder.aPlaceDto;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.Test;

import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.adaptor.api.builders.LegBuilder;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

public class CheapestQuotesResponseTransformerTest {

  private static final String OUT_BOUND_DATE = "2016-10-10T00:00:00";
  private static final String IN_BOUND_DATE = "2016-10-20T00:00:00";
  private static final String CARRIER = "easyjet";
  private static final String ORIGIN = "Athens International";
  private static final String DESTINATION1 = "Madrid";
  private static final String DESTINATION2 = "Paris Charles de Gaulle";

  private final CheapestQuotesResponseTransformer transformer =
      new CheapestQuotesResponseTransformerImpl();

  @Test
  public void shouldTransform() throws ParseException {
    final BrowseQuotesResponseAPIDto skyscannerResponse = createBrowseQuotesResponseAPIDto();

    // @formatter:off
    final SkyscannerCheapestQuotesResponse expected = aSkyscannerCheapestQuotesResponse().withQuotes(
                                                            aSkyscannerQuote().withDirect(true).withPrice(62)
                                                              .withInboundLeg(buildInBoundLeg(DESTINATION1))
                                                              .withOutboundLeg(buildOutBoundLeg(DESTINATION1)),
                                                            aSkyscannerQuote().withDirect(true).withPrice(72)
                                                              .withInboundLeg(buildInBoundLeg(DESTINATION2))
                                                              .withOutboundLeg(buildOutBoundLeg(DESTINATION2)))
                                                          .build();
    // @formatter:on
    final SkyscannerCheapestQuotesResponse actual = transformer.transform(skyscannerResponse);
    assertEquals(expected, actual);


  }


  private LegBuilder buildOutBoundLeg(final String destination) throws ParseException {
    return aLeg().withCarrier(CARRIER).withOrigin(ORIGIN).withDestination(destination)
        .withDepartureDate(OUT_BOUND_DATE);
  }

  private LegBuilder buildInBoundLeg(final String destination) throws ParseException {
    return aLeg().withCarrier(CARRIER).withOrigin(destination).withDestination(ORIGIN)
        .withDepartureDate(IN_BOUND_DATE);
  }

  private BrowseQuotesResponseAPIDto createBrowseQuotesResponseAPIDto() {
    // @formatter:off
    return aBrowseQuotesResponseAPIDto()
                    .withQuotes(aQuoteDto()
                                .withDirect(true)
                                .withInboundLeg(aSkyscannerLeg()
                                                .withCarrierIds(asList(1050))
                                                .withDepartureDate("2016-10-20T00:00:00")
                                                .withDestinationId(40920)
                                                .withOriginId(67652))
                                .withOutboundLeg(aSkyscannerLeg()
                                                .withCarrierIds(asList(1050))
                                                .withDepartureDate("2016-10-10T00:00:00")
                                                .withDestinationId(67652)
                                                .withOriginId(40920))                                                                    
                                .withMinPrice(62)
                                .withQuoteDateTime("2016-08-06T22:01:00")
                                .withQuoteId(1), 
                                aQuoteDto()
                                .withDirect(true)
                                .withInboundLeg(aSkyscannerLeg()
                                                .withCarrierIds(asList(1050))
                                                .withDepartureDate("2016-10-20T00:00:00")
                                                .withDestinationId(40920)
                                                .withOriginId(44759))
                                .withOutboundLeg(aSkyscannerLeg()
                                                .withCarrierIds(asList(1050))
                                                .withDepartureDate("2016-10-10T00:00:00")
                                                .withDestinationId(44759)
                                                .withOriginId(40920))                                                                    
                                .withMinPrice(72)
                                .withQuoteDateTime("2016-08-18T11:56:00")
                                .withQuoteId(2))
                    .withPlaces(aPlaceDto()
                                      .withCityId("ATHE")
                                      .withCityName("Athens")
                                      .withCountryName("Greece")
                                      .withIataCode("ATH")
                                      .withName("Athens International")
                                      .withPlaceId(40920)
                                      .withSkyscannerCode("ATH")
                                      .withType("Station"),
                                aPlaceDto()
                                      .withCityId("MADR")
                                      .withCityName("Madrid")
                                      .withCountryName("Spain")
                                      .withIataCode("MAD")
                                      .withName("Madrid")
                                      .withPlaceId(67652)
                                      .withSkyscannerCode("MAD")
                                      .withType("Station"),
                                aPlaceDto()
                                      .withCityId("PARI")
                                      .withCityName("Paris")
                                      .withCountryName("France")
                                      .withIataCode("CDG")
                                      .withName("Paris Charles de Gaulle")
                                      .withPlaceId(44759)
                                      .withSkyscannerCode("CDG")
                                      .withType("Station"),
                                aPlaceDto()
                                      .withCityId("LOND")
                                      .withCityName("London")
                                      .withCountryName("United Kingdom")
                                      .withIataCode("LGW")
                                      .withName("London Gatwick")
                                      .withPlaceId(65655)
                                      .withSkyscannerCode("LGW")
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
                                      .withName("easyjet"))
                  .build();
    // @formatter:on
  }
}
