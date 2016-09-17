package com.flymatcher.skyscanner.adaptor.cheapestquotes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.dto.CheapestQuotesRequest;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient.CheapestQuotesClient;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.transformer.CheapestQuotesResponseTransformer;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

@Component
public class CheapestQuotesServiceImpl implements CheapestQuotesService {

  private final CheapestQuotesResponseTransformer transformer;
  private final CheapestQuotesClient client;

  @Autowired
  public CheapestQuotesServiceImpl(final CheapestQuotesResponseTransformer transformer,
      final CheapestQuotesClient client) {
    this.transformer = transformer;
    this.client = client;
  }

  @Override
  public SkyscannerCheapestQuotesResponse getSkyscannerCheapestQuotesResponse(
      final CheapestQuotesRequest request) {
    final BrowseQuotesResponseAPIDto clientResponse = client.getCheapestQuotes(request);
    return transformer.transform(clientResponse);
  }

}
