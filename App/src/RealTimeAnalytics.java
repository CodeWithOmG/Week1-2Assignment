import java.util.*;

class PageEvent {
    String url;
    String userId;
    String source;

    PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class RealTimeAnalytics {

    private Map<String, Integer> pageViews = new HashMap<>();
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();
    private Map<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(PageEvent e) {

        pageViews.put(e.url, pageViews.getOrDefault(e.url, 0) + 1);

        uniqueVisitors
                .computeIfAbsent(e.url, k -> new HashSet<>())
                .add(e.userId);

        trafficSources.put(e.source,
                trafficSources.getOrDefault(e.source, 0) + 1);
    }

    public void getDashboard() {

        System.out.println("Top Pages:");

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        int rank = 1;
        while (!pq.isEmpty() && rank <= 10) {
            Map.Entry<String, Integer> e = pq.poll();
            int unique = uniqueVisitors.get(e.getKey()).size();

            System.out.println(rank + ". " + e.getKey()
                    + " - " + e.getValue() + " views (" + unique + " unique)");
            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = trafficSources.values().stream().mapToInt(i -> i).sum();

        for (String source : trafficSources.keySet()) {
            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;

            System.out.println(source + ": "
                    + String.format("%.1f", percent) + "%");
        }
    }

    public static void main(String[] args) {

        RealTimeAnalytics system = new RealTimeAnalytics();

        system.processEvent(new PageEvent("/article/breaking-news", "user_123", "google"));
        system.processEvent(new PageEvent("/article/breaking-news", "user_456", "facebook"));
        system.processEvent(new PageEvent("/sports/championship", "user_789", "direct"));
        system.processEvent(new PageEvent("/article/breaking-news", "user_123", "google"));

        system.getDashboard();
    }
}