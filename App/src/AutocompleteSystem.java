import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> queries = new ArrayList<>();
}

public class AutocompleteSystem {

    private TrieNode root = new TrieNode();
    private Map<String, Integer> frequency = new HashMap<>();

    public void addQuery(String query) {
        frequency.put(query, frequency.getOrDefault(query, 0) + 1);

        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
            node.queries.add(query);
        }
    }

    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c))
                return new ArrayList<>();
            node = node.children.get(c);
        }

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequency.get(a) - frequency.get(b)
        );

        for (String q : node.queries) {
            pq.offer(q);
            if (pq.size() > 10)
                pq.poll();
        }

        List<String> result = new ArrayList<>();

        while (!pq.isEmpty())
            result.add(pq.poll());

        Collections.reverse(result);
        return result;
    }

    public void updateFrequency(String query) {
        addQuery(query);
        System.out.println(query + " → Frequency: " + frequency.get(query));
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java download");
        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java tutorial");

        System.out.println("search(\"jav\") →");

        List<String> suggestions = system.search("jav");

        int rank = 1;
        for (String s : suggestions) {
            System.out.println(rank++ + ". \"" + s + "\" ("
                    + system.frequency.get(s) + " searches)");
        }

        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
    }
}