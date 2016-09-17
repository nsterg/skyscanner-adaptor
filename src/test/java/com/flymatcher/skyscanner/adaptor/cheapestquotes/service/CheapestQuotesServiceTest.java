package com.flymatcher.skyscanner.adaptor.cheapestquotes.service;

import static com.flymatcher.skyscanner.adaptor.api.builders.LegBuilder.aLeg;
import static com.flymatcher.skyscanner.adaptor.api.builders.SkyscannerCheapestQuotesResponseBuilder.aSkyscannerCheapestQuotesResponse;
import static com.flymatcher.skyscanner.adaptor.api.builders.SkyscannerQuoteBuilder.aSkyscannerQuote;
import static com.flymatcher.skyscanner.adaptor.cheapestquotes.dto.builders.CheapestQuotesRequestBuilder.aCheapestQuotesRequest;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.adaptor.api.builders.LegBuilder;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.dto.CheapestQuotesRequest;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient.CheapestQuotesClient;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.transformer.CheapestQuotesResponseTransformer;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;
import com.flymatcher.skyscanner.cheapestquotes.builders.BrowseQuotesResponseAPIDtoBuilder;

public class CheapestQuotesServiceTest {

  private static final String OUT_BOUND_DATE = "2016-10-10T00:00:00";
  private static final String IN_BOUND_DATE = "2016-10-20T00:00:00";
  private static final String CARRIER = "easyjet";
  private static final String ORIGIN = "ATH";
  private static final String DESTINATION1 = "LND";
  private static final String DESTINATION2 = "MAD";


  @Mock
  private CheapestQuotesResponseTransformer mockTransformer;
  @Mock
  private CheapestQuotesClient mockClient;


  private CheapestQuotesService service;

  @Before
  public void setUp() {
    initMocks(this);
    service = new CheapestQuotesServiceImpl(mockTransformer, mockClient);
  }

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
    final CheapestQuotesRequest request = aCheapestQuotesRequest().withDefaultValues().build();

    final BrowseQuotesResponseAPIDto clientResponse =
        BrowseQuotesResponseAPIDtoBuilder.aBrowseQuotesResponseAPIDto().build();

    given(mockClient.getCheapestQuotes(request)).willReturn(clientResponse);
    given(mockTransformer.transform(clientResponse)).willReturn(expected);

    final SkyscannerCheapestQuotesResponse actual =
        service.getSkyscannerCheapestQuotesResponse(request);
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

}
