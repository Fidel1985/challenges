package com.challenge.busroute.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class BusRouteDto {
	@JsonProperty("dep_sid")
	public final int departureStationId;
	@JsonProperty("arr_sid")
	public final int arrivalStationId;
	@JsonProperty("direct_bus_route")
	public final boolean isDirectBusRoute;
}
