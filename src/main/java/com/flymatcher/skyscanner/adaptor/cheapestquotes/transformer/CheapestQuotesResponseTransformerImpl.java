package com.flymatcher.skyscanner.adaptor.cheapestquotes.transformer;

import static java.time.LocalDateTime.parse;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.flymatcher.skyscanner.adaptor.api.Leg;
import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.adaptor.api.SkyscannerQuote;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;
import com.flymatcher.skyscanner.cheapestquotes.QuoteDto;
import com.flymatcher.skyscanner.cheapestquotes.SkyscannerLeg;
import com.flymatcher.skyscanner.cheapestquotes.carrier.CarriersDto;
import com.flymatcher.skyscanner.cheapestquotes.place.PlaceDto;

@Component
public class CheapestQuotesResponseTransformerImpl implements CheapestQuotesResponseTransformer {

  @Override
  public SkyscannerCheapestQuotesResponse transform(final BrowseQuotesResponseAPIDto response) {
    final SkyscannerCheapestQuotesResponse cheapestQuotesResponse =
        new SkyscannerCheapestQuotesResponse();

    final List<SkyscannerQuote> quotes = new ArrayList<>();

    response.getQuotes().forEach(q -> {
      final SkyscannerQuote quote = createQuote(response, q);
      quotes.add(quote);
    });

    cheapestQuotesResponse.setQuotes(quotes);
    return cheapestQuotesResponse;
  }

  private SkyscannerQuote createQuote(final BrowseQuotesResponseAPIDto response, final QuoteDto q) {
    final SkyscannerQuote quote = new SkyscannerQuote();
    quote.setDirect(q.getDirect());
    quote.setPrice(q.getMinPrice());
    quote.setQuoteDate(parse(q.getQuoteDateTime()));

    quote.setInboundLeg(createInOutBoundLeg(response, q.getInboundLeg()));
    quote.setOutboundLeg(createInOutBoundLeg(response, q.getOutboundLeg()));
    return quote;
  }

  private Leg createInOutBoundLeg(final BrowseQuotesResponseAPIDto response,
      final SkyscannerLeg inoutLeg) {
    final Leg leg = new Leg();

    leg.setDepartureDate(parse(inoutLeg.getDepartureDate()));

    final List<Integer> carrierIds = inoutLeg.getCarrierIds();

    if (carrierIds != null && !carrierIds.isEmpty()) {
      leg.setCarrier(carrierLookUp(response.getCarriers(), carrierIds.get(0)));
    }

    leg.setOrigin(placeLookUp(response.getPlaces(), inoutLeg.getOriginId()));
    leg.setDestination(placeLookUp(response.getPlaces(), inoutLeg.getDestinationId()));
    return leg;
  }


  private String placeLookUp(final List<PlaceDto> places, final int placeId) {
    final Map<Integer, String> placesMap =
        places.stream().collect(toMap(PlaceDto::getPlaceId, PlaceDto::getName));
    return placesMap.get(placeId);
  }

  private String carrierLookUp(final List<CarriersDto> carriers, final int carrierId) {
    final Map<Integer, String> carriersMap =
        carriers.stream().collect(toMap(CarriersDto::getCarrierId, CarriersDto::getName));
    return carriersMap.get(carrierId);
  }


}
