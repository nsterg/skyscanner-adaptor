package com.flymatcher.skyscanner.adaptor.cheapestquotes.service;

import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.dto.CheapestQuotesRequest;

public interface CheapestQuotesService {

  SkyscannerCheapestQuotesResponse getSkyscannerCheapestQuotesResponse(
      CheapestQuotesRequest request);

}
