package com.challenge.busroute.dao;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Solution is based on 2 data structures:
 * 1) stationToRoutesMap contains int key, IntArrayList value, where key - stationId, value - collection of routeId's
 *    which are crossing through this stationId. It allows us fast querying of routes by stationId.
 * 2) routeToStationsMap also contains int key, IntArrayList value. This map just reproduce input file data structure.
 *    It help us to detect whether departure stationId is preceding arrival stationId or not.
 *
 * I also perform sorting for routes collections to achieve O(n) complexity instead of O(n2) during intersection calculation.
 * Both maps are built on top of the fastutil library to avoid primitive autoboxing by standard java data structures.
 * It allows us to save a lot of heap space.
 *
 * For 100_000 routes x 1000 stations data size(file size 660Mb) application consumes 1.2Gb of heap size and starts for around 1 minute.
 * Response time is below 30 ms for subsequent local requests
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusRouteRepository implements SmartInitializingSingleton {
	private Int2ReferenceMap<IntArrayList> stationToRoutesMap = new Int2ReferenceOpenHashMap<>();
	private Int2ReferenceMap<IntList> routeToStationsMap = new Int2ReferenceOpenHashMap<>();
	private final ApplicationArguments args;

	@Override
	public void afterSingletonsInstantiated() {
		if (args.getSourceArgs().length < 1) {
			log.error("Could not find bus route data filename in args line, interrupting!");
			return;
		}
		String file_path = args.getSourceArgs()[0];
		try {
			File file = new File(file_path);
			if (!file.exists()) {
				log.error("File with name: {} doesn't exists, interrupting!", file_path);
				return;
			}
			Scanner scanner = new Scanner(file);
			int numberOfRoutes = scanner.nextInt();
			log.info("Routes count: {}", numberOfRoutes);
			scanner.nextLine();
			int routeId;
			int stationId;
			for (int i = 0; i < numberOfRoutes; i++) {
				IntArrayList route = new IntArrayList();
				String[] data = scanner.nextLine().split(" ");
				routeId = Integer.parseInt(data[0]);
				for (int j = 1; j < data.length; j++) {
					stationId = Integer.parseInt(data[j]);
					stationToRoutesMap.computeIfAbsent(stationId, station -> new IntArrayList()).add(routeId);
					route.add(stationId);
				}
				routeToStationsMap.put(routeId, route);
			}
			stationToRoutesMap.values().forEach(x -> x.sort(null));
		} catch (IOException ioe) {
			log.error("Could not read bus route data from the file: {}", file_path);
		}
	}

	public int[] getRoutesByStationId(int stationId) {
		IntArrayList routes = stationToRoutesMap.get(stationId);
		return routes == null ? null : routes.elements();
	}

	public IntList getStationsByRouteId(int routeId) {
		return routeToStationsMap.get(routeId);
	}
}
