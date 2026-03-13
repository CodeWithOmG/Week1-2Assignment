import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String id, String content) {
        this.videoId = id;
        this.content = content;
    }
}

public class MultiLevelCache {

    // L1 Cache (Memory) - LRU
    private LinkedHashMap<String, VideoData> L1 =
            new LinkedHashMap<>(10000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> e) {
                    return size() > 10000;
                }
            };

    // L2 Cache (SSD)
    private LinkedHashMap<String, VideoData> L2 =
            new LinkedHashMap<>(100000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> e) {
                    return size() > 100000;
                }
            };

    // L3 Database simulation
    private HashMap<String, VideoData> database = new HashMap<>();

    private int l1Hits = 0, l2Hits = 0, l3Hits = 0;

    public MultiLevelCache() {
        for (int i = 1; i <= 1000; i++) {
            database.put("video_" + i,
                    new VideoData("video_" + i, "VideoContent" + i));
        }
    }

    public VideoData getVideo(String videoId) {

        // L1 lookup
        if (L1.containsKey(videoId)) {
            l1Hits++;
            System.out.println("→ L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("→ L1 Cache MISS");

        // L2 lookup
        if (L2.containsKey(videoId)) {
            l2Hits++;
            System.out.println("→ L2 Cache HIT (5ms)");
            VideoData video = L2.get(videoId);

            // promote to L1
            L1.put(videoId, video);
            System.out.println("→ Promoted to L1");
            return video;
        }

        System.out.println("→ L2 Cache MISS");

        // L3 lookup
        VideoData video = database.get(videoId);

        if (video != null) {
            l3Hits++;
            System.out.println("→ L3 Database HIT (150ms)");

            // add to L2 first
            L2.put(videoId, video);

            System.out.println("→ Added to L2 (access count:1)");
        }

        return video;
    }

    public void invalidate(String videoId) {
        L1.remove(videoId);
        L2.remove(videoId);
        database.remove(videoId);
    }

    public void getStatistics() {

        int total = l1Hits + l2Hits + l3Hits;

        double l1Rate = total == 0 ? 0 : (l1Hits * 100.0 / total);
        double l2Rate = total == 0 ? 0 : (l2Hits * 100.0 / total);
        double l3Rate = total == 0 ? 0 : (l3Hits * 100.0 / total);

        System.out.println("\nCache Statistics:");

        System.out.println("L1: Hit Rate " + String.format("%.1f", l1Rate)
                + "%, Avg Time: 0.5ms");

        System.out.println("L2: Hit Rate " + String.format("%.1f", l2Rate)
                + "%, Avg Time: 5ms");

        System.out.println("L3: Hit Rate " + String.format("%.1f", l3Rate)
                + "%, Avg Time: 150ms");

        System.out.println("Overall Hit Rate: "
                + String.format("%.1f", (l1Rate + l2Rate)) + "%");
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        System.out.println("getVideo(\"video_123\")");

        cache.getVideo("video_123");

        System.out.println("\ngetVideo(\"video_123\") [second request]");
        cache.getVideo("video_123");

        System.out.println("\ngetVideo(\"video_999\")");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}