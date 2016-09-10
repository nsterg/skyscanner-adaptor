package com.flymatcher.skyscanner.adaptor.cheapestquotes.service;

import static com.flymatcher.skyscanner.adaptor.api.builders.InOutBoundLegBuilder.aInOutBoundLeg;
import static com.flymatcher.skyscanner.adaptor.api.builders.SkyscannerCheapestQuotesResponseBuilder.aSkyscannerCheapestQuotesResponse;
import static com.flymatcher.skyscanner.adaptor.api.builders.SkyscannerQuoteBuilder.aSkyscannerQuote;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.Test;

import com.flymatcher.skyscanner.adaptor.api.CheapestQuotesRequest;
import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.adaptor.api.builders.CheapestQuotesRequestBuilder;
import com.flymatcher.skyscanner.adaptor.api.builders.InOutBoundLegBuilder;

public class QuoteServiceTest {

  private static final String OUT_BOUND_DATE = "2016-10-10T00:00:00";
  private static final String IN_BOUND_DATE = "2016-10-20T00:00:00";
  private static final String CARRIER = "easyjet";
  private static final String ORIGIN = "ATH";
  private static final String DESTINATION1 = "LND";
  private static final String DESTINATION2 = "MAD";

  private final QuoteService service = new QuoteServiceImpl();

  @Test
  public void shouldGetCheapestQuotesResponse() throws ParseException {

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
    final CheapestQuotesRequest request =
        CheapestQuotesRequestBuilder.aCheapestQuotesRequest().withDefaultValues().build();
    final SkyscannerCheapestQuotesResponse actual =
        service.getSkyscannerCheapestQuotesResponse(request);
    assertEquals(expected, actual);

  }

  private InOutBoundLegBuilder buildOutBoundLeg(final String destination) throws ParseException {
    return aInOutBoundLeg().withCarrier(CARRIER).withOrigin(ORIGIN).withDestination(destination)
        .withDepartureDate(OUT_BOUND_DATE);
  }

  private InOutBoundLegBuilder buildInBoundLeg(final String destination) throws ParseException {
    return aInOutBoundLeg().withCarrier(CARRIER).withOrigin(destination).withDestination(ORIGIN)
        .withDepartureDate(IN_BOUND_DATE);
  }

}
