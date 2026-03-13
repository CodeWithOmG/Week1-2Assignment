import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FlashSaleInventoryManager {

    private ConcurrentHashMap<String, AtomicInteger> stock = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, LinkedHashMap<Integer, Integer>> waitList = new ConcurrentHashMap<>();

    public void addProduct(String productId, int quantity) {
        stock.put(productId, new AtomicInteger(quantity));
        waitList.put(productId, new LinkedHashMap<>());
    }

    public int checkStock(String productId) {
        return stock.getOrDefault(productId, new AtomicInteger(0)).get();
    }

    public synchronized String purchaseItem(String productId, int userId) {

        AtomicInteger currentStock = stock.get(productId);

        if (currentStock.get() > 0) {
            int remaining = currentStock.decrementAndGet();
            return "Success, " + remaining + " units remaining";
        } else {
            LinkedHashMap<Integer, Integer> queue = waitList.get(productId);
            int position = queue.size() + 1;
            queue.put(userId, position);
            return "Added to waiting list, position #" + position;
        }
    }

    public void showWaitingList(String productId) {
        System.out.println("Waiting List: " + waitList.get(productId));
    }

    public static void main(String[] args) {

        FlashSaleInventoryManager system = new FlashSaleInventoryManager();

        system.addProduct("IPHONE15_256GB", 100);

        System.out.println("Stock: " + system.checkStock("IPHONE15_256GB"));

        System.out.println(system.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(system.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 1; i <= 100; i++) {
            system.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(system.purchaseItem("IPHONE15_256GB", 99999));

        system.showWaitingList("IPHONE15_256GB");
    }
}