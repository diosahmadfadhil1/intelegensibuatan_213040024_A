package RoutingApp;

import java.util.*;

public class RoutePlanner {
    private Map<String, Map<String, Integer>> cityMap;

    public RoutePlanner() {
        cityMap = new HashMap<>();
        // Definisi jarak antara kota-kota di Indonesia
        addCity("Jakarta", "Bandung", 150);
        addCity("Jakarta", "Yogyakarta", 550);
        addCity("Jakarta", "Surabaya", 700);
        addCity("Bandung", "Yogyakarta", 400);
        addCity("Bandung", "Surabaya", 800);
        addCity("Yogyakarta", "Surabaya", 500);
    }

    private void addCity(String city1, String city2, int distance) {
        cityMap.putIfAbsent(city1, new HashMap<>());
        cityMap.get(city1).put(city2, distance);

        cityMap.putIfAbsent(city2, new HashMap<>());
        cityMap.get(city2).put(city1, distance);
    }

    public List<String> findShortestPath(String startCity, String endCity) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previousCities = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        distances.put(startCity, 0);

        for (String city : cityMap.keySet()) {
            if (!city.equals(startCity)) {
                distances.put(city, Integer.MAX_VALUE);
            }
            queue.offer(city);
        }

        while (!queue.isEmpty()) {
            String currentCity = queue.poll();

            if (currentCity.equals(endCity)) {
                List<String> path = new ArrayList<>();
                while (previousCities.containsKey(currentCity)) {
                    path.add(currentCity);
                    currentCity = previousCities.get(currentCity);
                }
                path.add(startCity);
                Collections.reverse(path);
                return path;
            }

            if (distances.get(currentCity) == Integer.MAX_VALUE) {
                break;
            }

            for (String neighbor : cityMap.get(currentCity).keySet()) {
                int potentialDistance = distances.get(currentCity) + cityMap.get(currentCity).get(neighbor);

                if (potentialDistance < distances.get(neighbor)) {
                    distances.put(neighbor, potentialDistance);
                    previousCities.put(neighbor, currentCity);
                    queue.remove(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        return null; // Tidak ada jalur yang tersedia
    }

    public static void main(String[] args) {
        RoutePlanner routePlanner = new RoutePlanner();

        String startCity = "Jakarta";
        String endCity = "Bandung";

        List<String> shortestPath = routePlanner.findShortestPath(startCity, endCity);

        if (shortestPath != null) {
            System.out.println("Jalur terpendek dari " + startCity + " ke " + endCity + ": " + shortestPath);
        } else {
            System.out.println("Tidak ada jalur yang tersedia dari " + startCity + " ke " + endCity);
        }
    }
}
