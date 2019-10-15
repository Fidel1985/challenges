package com.challenge.busroute.service;

import com.challenge.busroute.dao.BusRouteRepository;
import com.challenge.busroute.dto.BusRouteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.iterator.IntIterator;
import org.eclipse.collections.api.list.primitive.IntList;
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
		IntList departureStationRoutes = busRouteRepository.getRoutesByStationId(departureStationId);
		if (departureStationRoutes == null) {
			return false;
		}
		IntList arrivalStationRoutes = busRouteRepository.getRoutesByStationId(arrivalStationId);
		if (arrivalStationRoutes == null) {
			return false;
		}
		int i = 0;
		int j = 0;
		while (i < departureStationRoutes.size() && j < arrivalStationRoutes.size()) {
			if (departureStationRoutes.get(i) > arrivalStationRoutes.get(j))
				j++;
			else if (departureStationRoutes.get(i) < arrivalStationRoutes.get(j))
				i++;
			else {
				if (isDirectionRight(departureStationRoutes.get(i), departureStationId, arrivalStationId)) {
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
		IntIterator routeIterator = route.intIterator();
		int stationId;
		while (routeIterator.hasNext()) {
			stationId = routeIterator.next();
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
