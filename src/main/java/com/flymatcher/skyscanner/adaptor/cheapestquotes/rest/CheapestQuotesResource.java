package com.flymatcher.skyscanner.adaptor.cheapestquotes.rest;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flymatcher.skyscanner.adaptor.api.CheapestQuotesRequest;
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

  @RequestMapping(value = "/v1/cheapest-quotes/", method = GET)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success",
      response = SkyscannerCheapestQuotesResponse.class)})
  public ResponseEntity<? extends Object> getCheapestQuotes(
      @RequestBody final CheapestQuotesRequest request) {

    final SkyscannerCheapestQuotesResponse response =
        service.getSkyscannerCheapestQuotesResponse(request);
    return new ResponseEntity<SkyscannerCheapestQuotesResponse>(response, OK);

  }

}
