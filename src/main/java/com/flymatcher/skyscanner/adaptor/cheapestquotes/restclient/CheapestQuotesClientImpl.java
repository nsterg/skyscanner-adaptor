package com.flymatcher.skyscanner.adaptor.cheapestquotes.restclient;

import static javax.ws.rs.core.UriBuilder.fromPath;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flymatcher.skyscanner.adaptor.domain.CheapestQuotesRequest;
import com.flymatcher.skyscanner.adaptor.exception.SkyscannerBadRequestException;
import com.flymatcher.skyscanner.adaptor.exception.SkyscannerServerException;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;

@Component
public class CheapestQuotesClientImpl implements CheapestQuotesClient {

  private static final String CHEAPEST_QUOTES_PATH_URL =
      "/{market}/{currency}/{locale}/{originPlace}/{destinationPlace}/{outboundPartialDate}/{inboundPartialDate}";

  private final RestTemplate skyscannerRestTemplate;
  private final String cheapestQuotesUrl;
  private final String apiKey;
  private final ObjectMapper objectMapper;

  private static final String ERROR_MESSAGE = "Could not get a valid skyscanner quote response.";
  private static final String VALIDATION_MESSAGE =
      "Skyscanner quote response included validation errors.";

  @Autowired
  public CheapestQuotesClientImpl(final RestTemplate restTemplate,
      @Value("${skyscanner.cheapest-quotes-base-url}") final String cheapestQuotesBaseUrl,
      @Value("${skyscanner.api-key}") final String apiKey, final ObjectMapper objectMapper) {
    this.cheapestQuotesUrl = cheapestQuotesBaseUrl + CHEAPEST_QUOTES_PATH_URL;
    this.apiKey = apiKey;
    this.skyscannerRestTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }



  @Override
  public BrowseQuotesResponseAPIDto getCheapestQuotes(final CheapestQuotesRequest request) {

    ResponseEntity<BrowseQuotesResponseAPIDto> responseEntity = null;
    try {
      responseEntity = skyscannerRestTemplate.exchange(buildUrl(request), GET, null,
          BrowseQuotesResponseAPIDto.class);
      return responseEntity.getBody();

    } catch (final HttpStatusCodeException e) {
      if (e.getStatusCode() == BAD_REQUEST) {
        BrowseQuotesResponseAPIDto response = null;
        try {
          response =
              objectMapper.readValue(e.getResponseBodyAsString(), BrowseQuotesResponseAPIDto.class);
        } catch (final Throwable throwable) {
          throw new SkyscannerServerException(
              ERROR_MESSAGE + " Error: Could not unmarshal error response. Response was: "
                  + e.getResponseBodyAsString());

        }
        throw new SkyscannerBadRequestException(VALIDATION_MESSAGE, response.getValidationErrors());
      }

      throw new SkyscannerServerException(ERROR_MESSAGE + " Error: Internal Server Error.");
    }
  }

  private String buildUrl(final CheapestQuotesRequest request) {
    // @formatter:off
    return fromPath(cheapestQuotesUrl).build(request.getMarket(), 
                                             request.getCurrency(),
                                             request.getLocale(),
                                             request.getOriginCity(),
                                             request.getDestinationCountry(),
                                             request.getOutboundPartialDate().toString(),
                                             request.getInboundPartialDate().toString())
        .toString() + "?apiKey=" + apiKey;
    // @formatter:on

  }


}
