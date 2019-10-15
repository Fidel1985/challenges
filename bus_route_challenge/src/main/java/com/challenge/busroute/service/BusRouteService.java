package com.challenge.busroute.service;

import com.challenge.busroute.dao.BusRouteRepository;
import com.challenge.busroute.dto.BusRouteDto;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BusRouteService {
	private final BusRouteRepository busRouteRepository;

	public BusRouteDto detectDirectRoute(int departureStationId, int arrivalStationId) {
		return BusRouteDto.builder()
				.departureStationId(departureStationId)
				.arrivalStationId(arrivalStationId)
				.isDirectBusRoute(isDirect(departureStationId, arrivalStationId))
				.build();
	}

	private boolean isDirect(int departureStationId, int arrivalStationId) {
		int[] departureStationRoutes = busRouteRepository.getRoutesByStationId(departureStationId);
		if (departureStationRoutes == null) {
			return false;
		}
		log.info("departureStationRoutes :{}", departureStationRoutes);
		int[] arrivalStationRoutes = busRouteRepository.getRoutesByStationId(arrivalStationId);
		if (arrivalStationRoutes == null) {
			return false;
		}
		log.info("arrivalStationRoutes :{}", arrivalStationRoutes);
		int i = 0;
		int j = 0;
		while (i < departureStationRoutes.length && j < arrivalStationRoutes.length) {
			if (departureStationRoutes[i] > arrivalStationRoutes[j])
				j++;
			else if (departureStationRoutes[i] < arrivalStationRoutes[j])
				i++;
			else {
				if (isDirectionRight(departureStationRoutes[i], departureStationId, arrivalStationId)) {
					return true;
				}
				i++;
				j++;
			}
		}
		return false;
	}

	private boolean isDirectionRight(int routeId, int departureStationId, int arrivalStationId) {
		IntList route = busRouteRepository.getStationsByRouteId(routeId);
		for (int stationId : route) {
			if (stationId == arrivalStationId) {
				return false;
			}
			if (stationId == departureStationId) {
				return true;
			}
		}
		return false;
	}
}
