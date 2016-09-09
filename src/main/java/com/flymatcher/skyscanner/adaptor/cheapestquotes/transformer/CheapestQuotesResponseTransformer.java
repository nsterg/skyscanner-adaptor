package com.flymatcher.skyscanner.adaptor.cheapestquotes.transformer;

import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

public interface CheapestQuotesResponseTransformer {

  SkyscannerCheapestQuotesResponse transform(BrowseQuotesResponseAPIDto response);

}
