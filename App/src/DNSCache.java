import java.util.*;

class DNSEntry {
    String ip;
    long expiry;

    DNSEntry(String ip, int ttl) {
        this.ip = ip;
        this.expiry = System.currentTimeMillis() + ttl * 1000;
    }

    boolean expired() {
        return System.currentTimeMillis() > expiry;
    }
}

public class DNSCache {

    private final int MAX_SIZE = 5;

    private LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> e) {
                    return size() > MAX_SIZE;
                }
            };

    private int hits = 0, misses = 0;

    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.expired()) {
            hits++;
            return "Cache HIT → " + entry.ip;
        }

        misses++;
        String ip = queryUpstream(domain);
        cache.put(domain, new DNSEntry(ip, 5)); // TTL = 5 sec
        return "Cache MISS → " + ip;
    }

    private String queryUpstream(String domain) {
        return "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);
    }

    public void getCacheStats() {
        int total = hits + misses;
        double rate = total == 0 ? 0 : (hits * 100.0 / total);
        System.out.println("Hit Rate: " + rate + "%");
    }

    public static void main(String[] args) throws Exception {

        DNSCache dns = new DNSCache();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(6000);

        System.out.println(dns.resolve("google.com"));

        dns.getCacheStats();
    }
}