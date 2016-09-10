package com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.flymatcher.skyscanner.adaptor.api.CheapestQuotesRequest;
import com.flymatcher.skyscanner.adaptor.exception.SkyscannerBadRequestException;
import com.flymatcher.skyscanner.adaptor.exception.SkyscannerServerException;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

public class CheapestQuotesClientImpl implements CheapestQuotesClient {

  private static final String CHEAPEST_QUOTES_PATH_URL =
      "/[country]/[currency]/[locale]/[city]/anywhere/[outboundPartialDate]/[inboundPartialDate]";

  private final RestTemplate skyscannerRestTemplate;
  private final String cheapestQuotesUrl;

  private static final String ERROR_MESSAGE = "Could not get a valid scyscanner quote response.";

  public CheapestQuotesClientImpl(final RestTemplate restTemplate,
      final String cheapestQuotesBaseUrl, final String apiKey) {
    this.cheapestQuotesUrl = cheapestQuotesBaseUrl + CHEAPEST_QUOTES_PATH_URL + "?apiKey=" + apiKey;
    this.skyscannerRestTemplate = restTemplate;
  }



  @Override
  public BrowseQuotesResponseAPIDto getCheapestQuotes(final CheapestQuotesRequest request) {

    try {
      final ResponseEntity<BrowseQuotesResponseAPIDto> responseEntity = skyscannerRestTemplate
          .exchange(buildUrl(request), HttpMethod.GET, null, BrowseQuotesResponseAPIDto.class);
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
    return cheapestQuotesUrl
              .replaceAll("[country]", request.getCountry())
              .replaceAll("[currency]", request.getCurrency())
              .replaceAll("[city]", request.getCity())
              .replaceAll("[locale]", request.getLocale())
              .replaceAll("[outboundPartialDate]", request.getOutboundPartialDate().toString())
              .replaceAll("[inboundPartialDate]", request.getInboundPartialDate().toString());
    // @formatter:on

  }


}
