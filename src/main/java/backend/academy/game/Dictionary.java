package backend.academy.game;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <a href="https://englishinn.ru/izuchenie-angliyskogo-yazyika-po-temam/">Resource with english words by categories.</a>
 */
final public class Dictionary {
    private final SecureRandom secureRandom;
    private final Map<Level, SimpleDictionary> level2simpleDictionary;

    private Dictionary(SecureRandom secureRandom, Map<Level, SimpleDictionary> level2simpleDictionary) {
        this.secureRandom = secureRandom;
        this.level2simpleDictionary = level2simpleDictionary;
    }

    public static Dictionary getInstance() {
        SecureRandom secureRandom = new SecureRandom();

        Map<Level, SimpleDictionary> level2simpleDictionary = new HashMap<>();
        fill(level2simpleDictionary);

        return new Dictionary(secureRandom, level2simpleDictionary);
    }

    private static void fill(Map<Level, SimpleDictionary> level2simpleDictionary) {
        putEasy(level2simpleDictionary);
        putMedium(level2simpleDictionary);
        putHard(level2simpleDictionary);
    }

    @SuppressFBWarnings({"MultipleStringLiterals"})
    private static void putEasy(Map<Level, SimpleDictionary> map) {
        Map<String, List<String>> category2words = new HashMap<>();
        category2words.put(
            "weather",
            List.of(
                "fine",
                "terrible",
                "cold",
                "hot",
                "warm",
                "rain",
                "wind",
                "cloud",
                "snow"
            )
        );
        category2words.put(
            "appearance",
            List.of(
                "lips",
                "teeth",
                "ears",
                "forehead",
                "neck",
                "body",
                "arms",
                "hands",
                "knees"
            )
        );
        map.put(Level.EASY, new SimpleDictionary(category2words));
    }

    private static void putMedium(Map<Level, SimpleDictionary> map) {
        Map<String, List<String>> category2words = new HashMap<>();
        category2words.put(
            "food",
            List.of(
                "omelette",
                "sausages",
                "boil",
                "fry",
                "cocoa",
                "letice",
                "wine",
                "dessert",
                "cream",
                "buckwheat"
            )
        );
        category2words.put(
            "character",
            List.of(
                "understanding",
                "obedient",
                "generous",
                "cheerful",
                "careful",
                "careless",
                "selfish",
                "ambitious",
                "calm",
                "cruel"
            )
        );
        map.put(Level.MEDIUM, new SimpleDictionary(category2words));
    }

    private static void putHard(Map<Level, SimpleDictionary> map) {
        Map<String, List<String>> category2words = new HashMap<>();
        category2words.put(
            "culture",
            List.of(
                "affect",
                "arrange",
                "discord",
                "gasp",
                "greed",
                "masterpiece",
                "piece",
                "poverty",
                "shadow",
                "sheer"
            )
        );
        category2words.put(
            "politics",
            List.of(
                "opponent",
                "opposition",
                "corruption",
                "liberal",
                "communist",
                "labour",
                "conservative",
                "politician",
                "person",
                "election"
            )
        );
        map.put(Level.HARD, new SimpleDictionary(category2words));
    }

    public String getRandomCategoryByLevel(Level level) {
        Set<String> categories = getCategoriesByLevel(level);

        int index = secureRandom.nextInt(categories.size());

        int i = 0;
        for (String category : categories) {
            if (i == index) {
                return category;
            } else {
                i++;
            }
        }

        return "";
    }

    public Set<String> getCategoriesByLevel(Level level) {
        return level2simpleDictionary.get(level).getCategories();
    }

    public String getWord(Level level, String category) {
        return level2simpleDictionary.get(level).getWord(secureRandom, category);
    }

    private record SimpleDictionary(Map<String, List<String>> category2words) {
        public Set<String> getCategories() {
            return category2words.keySet();
        }

        public String getWord(SecureRandom secureRandom, String category) {
            List<String> words = category2words.get(category);
            int randomIndex = secureRandom.nextInt(words.size());
            return words.get(randomIndex);
        }
    }
}
