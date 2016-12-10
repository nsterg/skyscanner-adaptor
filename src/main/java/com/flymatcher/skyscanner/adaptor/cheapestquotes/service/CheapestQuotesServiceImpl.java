package com.flymatcher.skyscanner.adaptor.cheapestquotes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient.CheapestQuotesClient;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.transformer.CheapestQuotesResponseTransformer;
import com.flymatcher.skyscanner.adaptor.domain.CheapestQuotesRequest;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

@Component
public class CheapestQuotesServiceImpl implements CheapestQuotesService {

  private final CheapestQuotesResponseTransformer successTransformer;

  private final CheapestQuotesClient client;

  @Autowired
  public CheapestQuotesServiceImpl(final CheapestQuotesResponseTransformer successTransformer,
      final CheapestQuotesClient client) {
    this.successTransformer = successTransformer;
    this.client = client;
  }

  @Override
  public SkyscannerCheapestQuotesResponse getSkyscannerCheapestQuotesResponse(
      final CheapestQuotesRequest request) {
    final BrowseQuotesResponseAPIDto clientResponse = client.getCheapestQuotes(request);

    return successTransformer.transform(clientResponse);
  }

}
