import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    int time; // minutes from start of day

    Transaction(int id, int amount, String merchant, String account, int time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class FraudTransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    public void add(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public List<String> findTwoSum(int target) {

        Map<Integer, Transaction> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction prev = map.get(complement);
                result.add("(" + prev.id + "," + t.id + ")");
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // Two-Sum with time window (minutes)
    public List<String> findTwoSumWithWindow(int target, int window) {

        List<String> result = new ArrayList<>();

        for (int i = 0; i < transactions.size(); i++) {

            Transaction a = transactions.get(i);

            for (int j = i + 1; j < transactions.size(); j++) {

                Transaction b = transactions.get(j);

                if (Math.abs(a.time - b.time) <= window &&
                        a.amount + b.amount == target) {

                    result.add("(" + a.id + "," + b.id + ")");
                }
            }
        }

        return result;
    }

    // Duplicate detection
    public List<String> detectDuplicates() {

        Map<String, List<String>> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(t.account);
        }

        for (String key : map.keySet()) {

            List<String> accounts = map.get(key);

            if (accounts.size() > 1) {
                result.add("{ " + key + " accounts:" + accounts + " }");
            }
        }

        return result;
    }

    // K-Sum (recursive)
    public List<List<Integer>> findKSum(int k, int target) {

        List<List<Integer>> result = new ArrayList<>();
        kSumHelper(0, k, target, new ArrayList<>(), result);
        return result;
    }

    private void kSumHelper(int index, int k, int target,
                            List<Integer> path,
                            List<List<Integer>> result) {

        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }

        if (k == 0 || index == transactions.size())
            return;

        Transaction t = transactions.get(index);

        path.add(t.id);
        kSumHelper(index + 1, k - 1, target - t.amount, path, result);
        path.remove(path.size() - 1);

        kSumHelper(index + 1, k, target, path, result);
    }

    public static void main(String[] args) {

        FraudTransactionAnalyzer system = new FraudTransactionAnalyzer();

        system.add(new Transaction(1, 500, "Store A", "acc1", 600));
        system.add(new Transaction(2, 300, "Store B", "acc2", 615));
        system.add(new Transaction(3, 200, "Store C", "acc3", 630));
        system.add(new Transaction(4, 500, "Store A", "acc4", 640));

        System.out.println("findTwoSum(500) → " + system.findTwoSum(500));

        System.out.println("findTwoSumWithWindow(500,60) → "
                + system.findTwoSumWithWindow(500,60));

        System.out.println("detectDuplicates() → "
                + system.detectDuplicates());

        System.out.println("findKSum(3,1000) → "
                + system.findKSum(3,1000));
    }
}