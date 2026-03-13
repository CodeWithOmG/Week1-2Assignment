import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UsernameAvlChecker {

    private ConcurrentHashMap<String, Integer> users = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();

    public boolean checkAvailability(String username) {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        return !users.containsKey(username);
    }

    public void registerUsername(String username, int userId) {
        if (checkAvailability(username)) {
            users.put(username, userId);
            System.out.println(username + " registered successfully.");
        } else {
            System.out.println(username + " is already taken.");
        }
    }

    public List<String> suggestAlternatives(String username) {
        List<String> list = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String s = username + i;
            if (!users.containsKey(s)) list.add(s);
        }

        String dot = username.replace("_", ".");
        if (!users.containsKey(dot)) list.add(dot);

        return list;
    }

    public String getMostAttempted() {
        String name = null;
        int max = 0;

        for (Map.Entry<String, Integer> e : attempts.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                name = e.getKey();
            }
        }
        return name + " (" + max + " attempts)";
    }

    public void displayUsers() {
        System.out.println("Registered Users:");
        users.forEach((k, v) -> System.out.println(k + " -> " + v));
    }

    public static void main(String[] args) {
        UsernameAvlChecker system = new UsernameAvlChecker();

        system.registerUsername("Om_Gupta", 101);
        system.registerUsername("Ansh", 102);
        system.registerUsername("admin", 1);

        System.out.println("Is Om_Gupta available? " + system.checkAvailability("Om_Gupta"));
        System.out.println("Is jane_smith available? " + system.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe:");
        System.out.println(system.suggestAlternatives("john_doe"));

        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        System.out.println("Most attempted username: " + system.getMostAttempted());
    }
}