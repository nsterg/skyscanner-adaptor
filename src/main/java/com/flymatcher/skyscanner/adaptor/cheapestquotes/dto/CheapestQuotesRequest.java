package com.flymatcher.skyscanner.adaptor.cheapestquotes.dto;

import static java.time.LocalDate.parse;
import static org.apache.commons.lang.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang.builder.ToStringBuilder.reflectionToString;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class CheapestQuotesRequest {

  @JsonPropertyOrder({"country", "city", "currency", "locale", "outboundPartialDate",
      "inboundPartialDate"})

  private String country;

  private String city;

  private String currency;

  private String locale;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate outboundPartialDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate inboundPartialDate;


  public static CheapestQuotesRequest valueOf(final String country, final String city,
      final String currency, final String locale, final String outboundPartialDate,
      final String inboundPartialDate) {
    final CheapestQuotesRequest request = new CheapestQuotesRequest();
    request.country = country;
    request.city = city;
    request.currency = currency;
    request.locale = locale;
    request.outboundPartialDate = parse(outboundPartialDate);
    request.inboundPartialDate = parse(inboundPartialDate);
    return request;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(final String currency) {
    this.currency = currency;
  }


  public String getLocale() {
    return locale;
  }

  public void setLocale(final String locale) {
    this.locale = locale;
  }

  public LocalDate getOutboundPartialDate() {
    return outboundPartialDate;
  }

  public void setOutboundPartialDate(final LocalDate outboundPartialDate) {
    this.outboundPartialDate = outboundPartialDate;
  }

  public LocalDate getInboundPartialDate() {
    return inboundPartialDate;
  }

  public void setInboundPartialDate(final LocalDate inboundPartialDate) {
    this.inboundPartialDate = inboundPartialDate;
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(this);
  }

  @Override
  public boolean equals(final Object obj) {
    return reflectionEquals(this, obj);
  }

  @Override
  public String toString() {
    return reflectionToString(this);
  }

}
