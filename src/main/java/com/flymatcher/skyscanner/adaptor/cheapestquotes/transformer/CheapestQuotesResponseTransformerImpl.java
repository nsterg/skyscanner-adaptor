package com.flymatcher.skyscanner.adaptor.cheapestquotes.transformer;

import static java.time.LocalDateTime.parse;

import java.util.ArrayList;
import java.util.HashMap;
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

    final List<PlaceDto> places = response.getPlaces();

    leg.setOrigin(placeLookUp(places, inoutLeg.getOriginId()));
    leg.setOriginCode(airportLookUp(places, inoutLeg.getOriginId()));
    leg.setDestination(placeLookUp(places, inoutLeg.getDestinationId()));
    leg.setDestinationCode(airportLookUp(places, inoutLeg.getDestinationId()));

    final String country = countryLookUp(places, inoutLeg.getDestinationId());
    leg.setCountry(country);
    leg.setCountryCode(countryCodeLookUp(places, country));

    return leg;
  }


  private String placeLookUp(final List<PlaceDto> places, final int placeId) {
    final Map<Integer, String> placesMap = new HashMap<>();
    places.forEach(p -> placesMap.put(p.getPlaceId(), p.getCityName()));

    return placesMap.get(placeId);
  }

  private String airportLookUp(final List<PlaceDto> places, final int placeId) {
    final Map<Integer, String> placesMap = new HashMap<>();
    places.forEach(p -> placesMap.put(p.getPlaceId(), p.getIataCode()));
    return placesMap.get(placeId);
  }

  private String countryLookUp(final List<PlaceDto> places, final int placeId) {
    final Map<Integer, String> placesMap = new HashMap<>();
    places.forEach(p -> placesMap.put(p.getPlaceId(), p.getCountryName()));

    return placesMap.get(placeId);
  }

  private String countryCodeLookUp(final List<PlaceDto> places, final String country) {
    final Map<String, String> placesMap = new HashMap<>();
    places.stream().filter(t -> t.getType().equals("Country"))
        .forEach(p -> placesMap.put(p.getName(), p.getSkyscannerCode()));

    return placesMap.get(country);
  }

  private String carrierLookUp(final List<CarriersDto> carriers, final int carrierId) {
    final Map<Integer, String> carriersMap = new HashMap<>();
    carriers.forEach(c -> carriersMap.put(c.getCarrierId(), c.getName()));
    return carriersMap.get(carrierId);
  }


}
