import java.util.*;

public class PlagiarismDetector {

    private static final int N = 5;

    private Map<String, Set<String>> ngramIndex = new HashMap<>();
    private Map<String, List<String>> documentNgrams = new HashMap<>();

    public void addDocument(String docId, String text) {

        List<String> ngrams = generateNgrams(text);
        documentNgrams.put(docId, ngrams);

        for (String ngram : ngrams) {
            ngramIndex.computeIfAbsent(ngram, k -> new HashSet<>()).add(docId);
        }
    }

    private List<String> generateNgrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> list = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            list.add(sb.toString().trim());
        }
        return list;
    }

    public void analyzeDocument(String docId) {

        List<String> ngrams = documentNgrams.get(docId);
        Map<String, Integer> matches = new HashMap<>();

        for (String ngram : ngrams) {
            Set<String> docs = ngramIndex.getOrDefault(ngram, new HashSet<>());

            for (String other : docs) {
                if (!other.equals(docId)) {
                    matches.put(other, matches.getOrDefault(other, 0) + 1);
                }
            }
        }

        for (String otherDoc : matches.keySet()) {
            int matchCount = matches.get(otherDoc);
            double similarity = (matchCount * 100.0) / ngrams.size();

            System.out.println("Matches with " + otherDoc + ": " + matchCount +
                    " → Similarity: " + String.format("%.2f", similarity) + "%");
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector system = new PlagiarismDetector();

        system.addDocument("essay_089",
                "machine learning is a field of artificial intelligence that focuses on learning from data");

        system.addDocument("essay_092",
                "machine learning is a field of artificial intelligence that focuses on learning from data and patterns");

        system.addDocument("essay_123",
                "machine learning is a field of artificial intelligence that focuses on learning from data");

        system.analyzeDocument("essay_123");
    }
}