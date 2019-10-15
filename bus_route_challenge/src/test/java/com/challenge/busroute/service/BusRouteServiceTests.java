package com.challenge.busroute.service;

import com.challenge.busroute.dao.BusRouteRepository;
import com.challenge.busroute.dto.BusRouteDto;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class BusRouteServiceTests {
	@InjectMocks
	private BusRouteService busRouteService;
	@Mock
	private BusRouteRepository busRouteRepository;
	private static final int DEPARTURE_STATION_ID = 738;
	private static final int ARRIVAL_STATION_ID = 923;
	private static final int COMMON_ROUTE_ID = 4;
	private static final IntArrayList departureRoutes = IntArrayList.newListWith(0, 3, 4, 9);
	private static final IntArrayList arrivalRoutes = IntArrayList.newListWith(1, 2, 4, 7);
	private static final IntArrayList commonRoute = IntArrayList.newListWith(435, 999, 874, 738, 555, 923);

	@Test
	public void testEmptyDepartureRoutesDirectIsFalse() {
		when(busRouteRepository.getRoutesByStationId(DEPARTURE_STATION_ID)).thenReturn(null);
		BusRouteDto result = busRouteService.detectDirectRoute(DEPARTURE_STATION_ID, ARRIVAL_STATION_ID);
		assertThat(result.isDirectBusRoute).isFalse();
	}

	@Test
	public void testEmptyArrivalRoutesDirectIsFalse() {
		when(busRouteRepository.getRoutesByStationId(DEPARTURE_STATION_ID)).thenReturn(departureRoutes);
		when(busRouteRepository.getRoutesByStationId(ARRIVAL_STATION_ID)).thenReturn(null);
		BusRouteDto result = busRouteService.detectDirectRoute(DEPARTURE_STATION_ID, ARRIVAL_STATION_ID);
		assertThat(result.isDirectBusRoute).isFalse();
	}

	@Test
	public void testStationRightOrderDirectIsTrue() {
		when(busRouteRepository.getRoutesByStationId(DEPARTURE_STATION_ID)).thenReturn(departureRoutes);
		when(busRouteRepository.getRoutesByStationId(ARRIVAL_STATION_ID)).thenReturn(arrivalRoutes);
		when(busRouteRepository.getStationsByRouteId(COMMON_ROUTE_ID)).thenReturn(commonRoute);
		BusRouteDto result = busRouteService.detectDirectRoute(DEPARTURE_STATION_ID, ARRIVAL_STATION_ID);
		assertThat(result.isDirectBusRoute).isTrue();
	}

	@Test
	public void testStationReverseOrderDirectIsFalse() {
		when(busRouteRepository.getRoutesByStationId(DEPARTURE_STATION_ID)).thenReturn(departureRoutes);
		when(busRouteRepository.getRoutesByStationId(ARRIVAL_STATION_ID)).thenReturn(arrivalRoutes);
		when(busRouteRepository.getStationsByRouteId(COMMON_ROUTE_ID)).thenReturn(commonRoute);
		BusRouteDto result = busRouteService.detectDirectRoute(ARRIVAL_STATION_ID, DEPARTURE_STATION_ID);
		assertThat(result.isDirectBusRoute).isFalse();
	}

}
