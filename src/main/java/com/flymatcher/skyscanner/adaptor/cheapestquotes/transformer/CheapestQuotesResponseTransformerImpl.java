package com.flymatcher.skyscanner.adaptor.cheapestquotes.transformer;

import static java.time.LocalDateTime.parse;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flymatcher.skyscanner.adaptor.api.InOutBoundLeg;
import com.flymatcher.skyscanner.adaptor.api.SkyscannerCheapestQuotesResponse;
import com.flymatcher.skyscanner.adaptor.api.SkyscannerQuote;
import com.flymatcher.skyscanner.cheapestquotes.BrowseQuotesResponseAPIDto;
import com.flymatcher.skyscanner.cheapestquotes.OutboundLeg;
import com.flymatcher.skyscanner.cheapestquotes.carrier.CarriersDto;
import com.flymatcher.skyscanner.cheapestquotes.place.PlaceDto;

public class CheapestQuotesResponseTransformerImpl implements CheapestQuotesResponseTransformer {

  @Override
  public SkyscannerCheapestQuotesResponse transform(final BrowseQuotesResponseAPIDto response) {
    final SkyscannerCheapestQuotesResponse cheapestQuotesResponse =
        new SkyscannerCheapestQuotesResponse();

    final List<SkyscannerQuote> quotes = new ArrayList<>();

    response.getQuotes().forEach(q -> {
      final SkyscannerQuote quote = new SkyscannerQuote();
      quote.setDirect(q.getDirect());
      quote.setPrice(q.getMinPrice());

      final InOutBoundLeg inboundLeg = new InOutBoundLeg();
      // inboundLeg.setType(LegType.INBOUND);

      final OutboundLeg inLeg = q.getInboundLeg();
      inboundLeg.setDepartureDate(parse(inLeg.getDepartureDate()));
      inboundLeg.setCarrier(carrierLookUp(response.getCarriers(), inLeg.getCarrierIds().get(0)));
      inboundLeg.setOrigin(placeLookUp(response.getPlaces(), inLeg.getOriginId()));
      inboundLeg.setDestination(placeLookUp(response.getPlaces(), inLeg.getDestinationId()));

      quote.setInboundLeg(inboundLeg);

      final InOutBoundLeg outboundLeg = new InOutBoundLeg();
      // outboundLeg.setType(LegType.OUTBOUND);

      final OutboundLeg outLeg = q.getOutboundLeg();
      outboundLeg.setDepartureDate(parse(outLeg.getDepartureDate()));
      outboundLeg.setCarrier(carrierLookUp(response.getCarriers(), outLeg.getCarrierIds().get(0)));
      outboundLeg.setOrigin(placeLookUp(response.getPlaces(), outLeg.getOriginId()));
      outboundLeg.setDestination(placeLookUp(response.getPlaces(), outLeg.getDestinationId()));

      quote.setOutboundLeg(outboundLeg);

      quotes.add(quote);
    });

    cheapestQuotesResponse.setQuotes(quotes);
    return cheapestQuotesResponse;
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
