package com.challenge.busroute.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
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
 * Both maps are built on top of the eclipse collections library to avoid primitive autoboxing by standard java data structures.
 * It allows us to save a lot of heap space.
 *
 * For 100_000 routes x 1000 stations data size(file size 660Mb) application consumes 1.2Gb of heap size and starts for around 1 minute.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusRouteRepository implements SmartInitializingSingleton {
	private IntObjectHashMap<IntArrayList> stationToRoutesMap = new IntObjectHashMap<>();
	private IntObjectHashMap<IntList> routeToStationsMap = new IntObjectHashMap<>();
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
					IntArrayList routesList = stationToRoutesMap.get(stationId);
					if (routesList != null) {
						routesList.add(routeId);
					} else {
						routesList = new IntArrayList();
						routesList.add(routeId);
						stationToRoutesMap.put(stationId, routesList);
					}
					route.add(stationId);
				}
				routeToStationsMap.put(routeId, route);
			}
			stationToRoutesMap.values().forEach(IntArrayList::sortThis);
		} catch (IOException ioe) {
			log.error("Could not read bus route data from the file: {}", file_path);
		}
	}

	public IntList getRoutesByStationId(int stationId) {
		return stationToRoutesMap.get(stationId);
	}

	public IntList getStationsByRouteId(int routeId) {
		return routeToStationsMap.get(routeId);
	}
}
