package com.challenge.busroute.controller;

import com.challenge.busroute.dto.BusRouteDto;
import com.challenge.busroute.service.BusRouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BusRouteController {
	private final BusRouteService busRouteService;

	@GetMapping(path = "/direct", produces = MediaType.APPLICATION_JSON_VALUE)
	public BusRouteDto isRouteDirect(@RequestParam("dep_sid") int departureStationId, @RequestParam("arr_sid") int arrivalStationId) {
		log.info("Received route direct request with departure id: {} and arrival id: {}", departureStationId, arrivalStationId);
		return busRouteService.detectDirectRoute(departureStationId, arrivalStationId);
	}

}
