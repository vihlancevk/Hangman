package backend.academy.game.dictionary;

import backend.academy.game.Level;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * <ul>
 *     <li>Beginner</li>
 *     <li>Intermediate</li>
 *     <li>Advanced</li>
 * </ul>
 * <a href="https://langeek.co/en/vocab/level-based">Resource with english words by categories.</a>
 */
public final class LevelBasedDictionary implements Dictionary {
    private final SecureRandom secureRandom;
    private final Map<Level, SimpleDictionary> level2simpleDictionary;

    private LevelBasedDictionary(SecureRandom secureRandom, Map<Level, SimpleDictionary> level2simpleDictionary) {
        this.secureRandom = secureRandom;
        this.level2simpleDictionary = level2simpleDictionary;
    }

    public static LevelBasedDictionary getInstance() {
        SecureRandom secureRandom = new SecureRandom();

        Map<Level, SimpleDictionary> level2simpleDictionary = new HashMap<>();
        fill(level2simpleDictionary);

        return new LevelBasedDictionary(secureRandom, level2simpleDictionary);
    }

    private static void fill(Map<Level, SimpleDictionary> level2simpleDictionary) {
        putEasy(level2simpleDictionary);
        putMedium(level2simpleDictionary);
        putHard(level2simpleDictionary);
    }

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

    @Override
    public Set<Level> getLevels() {
        return level2simpleDictionary.keySet();
    }

    @Override
    public Level getDefaultLevel() {
        return Level.MEDIUM;
    }

    @Override
    public Set<String> getCategoriesByLevel(Level level) {
        SimpleDictionary simpleDictionary = level2simpleDictionary.get(level);
        if (simpleDictionary == null) {
            return Collections.emptySet();
        } else {
            return simpleDictionary.getCategories();
        }
    }

    @Override
    public Optional<String> getCategoryByLevel(Level level) {
        Set<String> categories = getCategoriesByLevel(level);
        if (categories.isEmpty()) {
            return Optional.empty();
        }

        int index = secureRandom.nextInt(categories.size());

        int i = 0;
        for (String category : categories) {
            if (i == index) {
                return Optional.of(category);
            } else {
                i++;
            }
        }

        return Optional.empty();
    }

    @Override
    public String getDefaultCategory() {
        return getCategoryByLevel(getDefaultLevel()).orElseThrow();
    }

    @Override
    public Optional<String> getWord(Level level, String category) {
        SimpleDictionary simpleDictionary = level2simpleDictionary.get(level);
        if (simpleDictionary == null) {
            return Optional.empty();
        }

        return simpleDictionary.getWord(secureRandom, category);
    }

    @Override
    public String getDefaultWord() {
        return getWord(getDefaultLevel(), getDefaultCategory()).orElseThrow();
    }

    private record SimpleDictionary(Map<String, List<String>> category2words) {
        public Set<String> getCategories() {
            return category2words.keySet();
        }

        public Optional<String> getWord(SecureRandom secureRandom, String category) {
            List<String> words = category2words.get(category);
            if (words == null || words.isEmpty()) {
                return Optional.empty();
            }

            int randomIndex = secureRandom.nextInt(words.size());
            return Optional.of(words.get(randomIndex));
        }
    }
}
