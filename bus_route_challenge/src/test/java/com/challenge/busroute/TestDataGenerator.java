package com.challenge.busroute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestDataGenerator {

    public static void main(String[] args) {
        PrintWriter pw;
        try {
            pw = new PrintWriter(new File("VolumeData"));
			int routeCount = 100_000;
			pw.write(routeCount + "\n");
            for (int i = 100_000; i > 0; i--) {
            //for (int i = 0; i < routeCount; i++) {
                pw.write(buildRow(i));
            }
            pw.close();
            System.out.println("done!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String buildRow(int routeId) {
        StringBuilder row = new StringBuilder();
        row.append(routeId).append(" ");
        int count = 0;
        Set<Integer> stations = new HashSet<>(1001, 1);
        Random random = new Random();
        do {
            int station = random.nextInt(1000_000);
            if (stations.add(station)) {
                count++;
                row.append(station).append(" ");
            }
        } while (count <= 1000);
        return row.append("\n").toString();
    }

}
