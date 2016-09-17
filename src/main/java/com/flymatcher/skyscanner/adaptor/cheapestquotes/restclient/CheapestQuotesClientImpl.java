package com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient;

import static javax.ws.rs.core.UriBuilder.fromPath;
import static org.springframework.http.HttpMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.flymatcher.skyscanner.adaptor.cheapestquotes.dto.CheapestQuotesRequest;
import com.flymatcher.skyscanner.adaptor.exception.SkyscannerBadRequestException;
import com.flymatcher.skyscanner.adaptor.exception.SkyscannerServerException;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

@Component
public class CheapestQuotesClientImpl implements CheapestQuotesClient {

  private static final String CHEAPEST_QUOTES_PATH_URL =
      "/{country}/{currency}/{locale}/{city}/anywhere/{outboundPartialDate}/{inboundPartialDate}";

  private final RestTemplate skyscannerRestTemplate;
  private final String cheapestQuotesUrl;
  private final String apiKey;

  private static final String ERROR_MESSAGE = "Could not get a valid skyscanner quote response.";

  @Autowired
  public CheapestQuotesClientImpl(final RestTemplate restTemplate,
      @Value("${skyscanner.cheapest-quotes-base-url}") final String cheapestQuotesBaseUrl,
      @Value("${skyscanner.api-key}") final String apiKey) {
    this.cheapestQuotesUrl = cheapestQuotesBaseUrl + CHEAPEST_QUOTES_PATH_URL;
    this.apiKey = apiKey;
    this.skyscannerRestTemplate = restTemplate;
  }



  @Override
  public BrowseQuotesResponseAPIDto getCheapestQuotes(final CheapestQuotesRequest request) {

    try {
      final ResponseEntity<BrowseQuotesResponseAPIDto> responseEntity = skyscannerRestTemplate
          .exchange(buildUrl(request), GET, null, BrowseQuotesResponseAPIDto.class);
      return responseEntity.getBody();

    } catch (final HttpStatusCodeException e) {
      // final String errorpayload = e.getResponseBodyAsString();
      if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
        throw new SkyscannerBadRequestException(ERROR_MESSAGE + " Error: Bad Request.");
      }

      throw new SkyscannerServerException(ERROR_MESSAGE + " Error: Internal Server Error.");
    }
  }

  private String buildUrl(final CheapestQuotesRequest request) {
    // @formatter:off
    return fromPath(cheapestQuotesUrl).build(request.getCountry(), 
                                             request.getCurrency(),
                                             request.getLocale(),
                                             request.getCity(),
                                             request.getOutboundPartialDate().toString(),
                                             request.getInboundPartialDate().toString())
        .toString() + "?apiKey=" + apiKey;
    // @formatter:on

  }


}
