import java.util.concurrent.*;
import java.util.*;

class TokenBucket {
    int tokens;
    int maxTokens;
    double refillRate;
    long lastRefill;

    TokenBucket(int maxTokens) {
        this.tokens = maxTokens;
        this.maxTokens = maxTokens;
        this.refillRate = maxTokens / 3600.0;
        this.lastRefill = System.currentTimeMillis();
    }

    synchronized boolean allowRequest() {
        refill();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        double add = (now - lastRefill) / 1000.0 * refillRate;

        if (add > 0) {
            tokens = Math.min(maxTokens, tokens + (int) add);
            lastRefill = now;
        }
    }

    int remaining() {
        refill();
        return tokens;
    }

    long retryAfter() {
        return (long)((maxTokens - tokens) / refillRate);
    }
}

public class DistributedRateLimiter {

    private ConcurrentHashMap<String, TokenBucket> clients = new ConcurrentHashMap<>();
    private final int LIMIT = 1000;

    public String checkRateLimit(String clientId) {

        TokenBucket bucket = clients.computeIfAbsent(clientId,
                k -> new TokenBucket(LIMIT));

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.remaining() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, retry after "
                    + bucket.retryAfter() + "s)";
        }
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        int used = LIMIT - bucket.remaining();
        long reset = (bucket.lastRefill / 1000) + 3600;

        return "{used: " + used + ", limit: " + LIMIT + ", reset: " + reset + "}";
    }

    public static void main(String[] args) {

        DistributedRateLimiter limiter = new DistributedRateLimiter();

        System.out.println("checkRateLimit(clientId=\"abc123\") → "
                + limiter.checkRateLimit("abc123"));

        System.out.println("checkRateLimit(clientId=\"abc123\") → "
                + limiter.checkRateLimit("abc123"));

        for(int i=0;i<998;i++)
            limiter.checkRateLimit("abc123");

        System.out.println("checkRateLimit(clientId=\"abc123\") → "
                + limiter.checkRateLimit("abc123"));

        System.out.println("getRateLimitStatus(\"abc123\") → "
                + limiter.getRateLimitStatus("abc123"));
    }
}