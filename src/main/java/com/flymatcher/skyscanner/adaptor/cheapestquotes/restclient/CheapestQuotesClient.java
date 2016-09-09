package com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient;

import com.flymatcher.skyscanner.adaptor.api.CheapestQuotesRequest;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

public interface CheapestQuotesClient {

  BrowseQuotesResponseAPIDto getCheapestQuotes(CheapestQuotesRequest request);

}
