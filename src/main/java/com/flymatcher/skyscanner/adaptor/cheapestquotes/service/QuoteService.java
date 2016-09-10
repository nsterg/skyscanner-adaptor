package com.flymatcher.skyscanner.adaptor.cheapestquotes.service;

import com.flymatcher.skyscanner.adaptor.api.CheapestQuotesRequest;
import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;

public interface QuoteService {

  SkyscannerCheapestQuotesResponse getSkyscannerCheapestQuotesResponse(
      CheapestQuotesRequest request);

}
