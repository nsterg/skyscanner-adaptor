package com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.flymatcher.skyscanner.adaptor.api.CheapestQuotesRequest;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

public class CheapestQuotesClientImpl implements CheapestQuotesClient {

  private static final String CHEAPEST_QUOTES_PATH_URL =
      "/{country}/{currency}/{locale}/{city}/anywhere/{outboundPartialDate}/{inboundPartialDate}";

  private final RestTemplate skyscannerRestTemplate;
  private final String cheapestQuotesUrl;

  public CheapestQuotesClientImpl(final RestTemplate restTemplate,
      final String cheapestQuotesBaseUrl, final String apiKey) {
    this.cheapestQuotesUrl = cheapestQuotesBaseUrl + CHEAPEST_QUOTES_PATH_URL + "?apiKey=" + apiKey;
    this.skyscannerRestTemplate = restTemplate;
  }



  @Override
  public BrowseQuotesResponseAPIDto getCheapestQuotes(final CheapestQuotesRequest request) {
    return skyscannerRestTemplate.getForObject(cheapestQuotesUrl, BrowseQuotesResponseAPIDto.class,
        buildParametersMap(request));
  }

  private Map<String, String> buildParametersMap(final CheapestQuotesRequest request) {
    final Map<String, String> vars = new HashMap<>();
    vars.put("country", request.getCountry());
    vars.put("currency", request.getCurrency());
    vars.put("city", request.getCity());
    vars.put("locale", request.getLocale());
    vars.put("outboundPartialDate", request.getOutboundPartialDate().toString());
    vars.put("inboundPartialDate", request.getInboundPartialDate().toString());

    return vars;
  }


}
