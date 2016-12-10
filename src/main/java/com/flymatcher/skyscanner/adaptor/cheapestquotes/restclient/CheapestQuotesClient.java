package com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient;

import com.flymatcher.skyscanner.adaptor.domain.CheapestQuotesRequest;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

public interface CheapestQuotesClient {

  BrowseQuotesResponseAPIDto getCheapestQuotes(CheapestQuotesRequest request);

}
