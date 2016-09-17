package com.flymatcher.skyscanner.adaptor.cheapestquotes.rest;

import static com.flymatcher.skyscanner.adaptor.cheapestquotes.dto.CheapestQuotesRequest.valueOf;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.adaptor.cheapestquotes.service.CheapestQuotesService;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class CheapestQuotesResource {
  private final CheapestQuotesService service;

  @Autowired
  public CheapestQuotesResource(final CheapestQuotesService service) {
    this.service = service;
  }

  @RequestMapping(
      value = "/v1/cheapest-quotes/{country}/{city}/{currency}/{locale}/{outboundPartialDate}/{inboundPartialDate}",
      method = GET)
  // @formatter:off
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Success", response = SkyscannerCheapestQuotesResponse.class)})
  // @formatter:on
  public ResponseEntity<? extends Object> getCheapestQuotes(@PathVariable final String country,
      @PathVariable final String city, @PathVariable final String currency,
      @PathVariable final String locale, @PathVariable final String outboundPartialDate,
      @PathVariable final String inboundPartialDate) {

    final SkyscannerCheapestQuotesResponse response = service.getSkyscannerCheapestQuotesResponse(
        valueOf(country, city, currency, locale, outboundPartialDate, inboundPartialDate));
    return new ResponseEntity<SkyscannerCheapestQuotesResponse>(response, OK);

  }

}
