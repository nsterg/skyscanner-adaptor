package com.flymatcher.skyscanner.adaptor.cheapestquotes.rest;

import static com.flymatcher.skyscanner.adaptor.api.builders.CheapestQuotesRequestBuilder.aCheapestQuotesRequest;
import static com.flymatcher.skyscanner.adaptor.api.builders.SkyscannerCheapestQuotesResponseBuilder.aSkyscannerCheapestQuotesResponse;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpStatus.OK;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import com.flymatcher.skyscanner.adaptor.api.CheapestQuotesRequest;
import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.service.CheapestQuotesService;

public class CheapestQuotesResourceTest {

  @Mock
  private CheapestQuotesService mockService;

  private CheapestQuotesResource resource;

  @Before
  public void setUp() {
    initMocks(this);
    resource = new CheapestQuotesResource(mockService);

  }

  @Test
  public void shouldGetCheapestQuotes() {

    final SkyscannerCheapestQuotesResponse serviceResponse =
        aSkyscannerCheapestQuotesResponse().build();

    final CheapestQuotesRequest request = aCheapestQuotesRequest().withDefaultValues().build();

    given(mockService.getSkyscannerCheapestQuotesResponse(request)).willReturn(serviceResponse);

    final ResponseEntity<? extends Object> actual = resource.getCheapestQuotes(request);

    assertEquals("Incorrect status", OK, actual.getStatusCode());
    assertEquals("Incorrect body", serviceResponse, actual.getBody());
  }

}
