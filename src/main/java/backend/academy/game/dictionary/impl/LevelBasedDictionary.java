package backend.academy.game.dictionary.impl;

import backend.academy.game.Level;
import backend.academy.game.dictionary.Dictionary;
import backend.academy.game.dictionary.DictionaryWord;
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
        Map<String, List<DictionaryWord>> category2words = new HashMap<>();
        category2words.put(
            "weather",
            List.of(
                new LevelBasedDictionaryWord("cold", "Low temperature, winter."),
                new LevelBasedDictionaryWord("hot", "High temperature, summer, spicy food."),
                new LevelBasedDictionaryWord("warm", "Mild temperature, comfortable, spring day."),
                new LevelBasedDictionaryWord("rain", "Water droplets that fall from the sky."),
                new LevelBasedDictionaryWord("wind", "Movement of air")
            )
        );
        category2words.put(
            "appearance",
            List.of(
                new LevelBasedDictionaryWord("lips", "Speak, kiss."),
                new LevelBasedDictionaryWord("teeth", "Food, smile."),
                new LevelBasedDictionaryWord("ears", "Hearing, two."),
                new LevelBasedDictionaryWord("forehead", "Hard as a f...d."),
                new LevelBasedDictionaryWord("neck", "Connects the head to the body.")
            )
        );
        map.put(Level.EASY, new SimpleDictionary(category2words));
    }

    private static void putMedium(Map<Level, SimpleDictionary> map) {
        Map<String, List<DictionaryWord>> category2words = new HashMap<>();
        category2words.put(
            "food",
            List.of(
                new LevelBasedDictionaryWord("omelette", "Dish made from beaten eggs."),
                new LevelBasedDictionaryWord("sausages", "Dog from multiplication \"Hunting season\"."),
                new LevelBasedDictionaryWord("fry", "Cooking method that involves cooking food in hot oil."),
                new LevelBasedDictionaryWord("cocoa", "Powder made from roasted cocoa beans."),
                new LevelBasedDictionaryWord("wine", "Alcoholic drink made from grapes.")
            )
        );
        category2words.put(
            "character",
            List.of(
                new LevelBasedDictionaryWord("careful", "Showing caution and attention to detail."),
                new LevelBasedDictionaryWord("careless", "Not giving sufficient attention or thought."),
                new LevelBasedDictionaryWord("ambitious", "Having a strong desire to achieve success."),
                new LevelBasedDictionaryWord("calm", "State of tranquility or peace."),
                new LevelBasedDictionaryWord("cruel", "Characterized by a lack of kindness or compassion.")
            )
        );
        map.put(Level.MEDIUM, new SimpleDictionary(category2words));
    }

    private static void putHard(Map<Level, SimpleDictionary> map) {
        Map<String, List<DictionaryWord>> category2words = new HashMap<>();
        category2words.put(
            "culture",
            List.of(
                new LevelBasedDictionaryWord("affect", "To have an influence on."),
                new LevelBasedDictionaryWord("greed", "Desire for wealth; often depicted as a vice in fables."),
                new LevelBasedDictionaryWord("piece", "Part of a whole."),
                new LevelBasedDictionaryWord("shadow", "Dark."),
                new LevelBasedDictionaryWord("sheer", "Often used to describe something very steep.")
            )
        );
        category2words.put(
            "politics",
            List.of(
                new LevelBasedDictionaryWord("opponent", "Person who competes against another in a election, debate."),
                new LevelBasedDictionaryWord("opposition", "Party that disagrees with the current government."),
                new LevelBasedDictionaryWord("corruption", "Abuse of power for personal gain."),
                new LevelBasedDictionaryWord("conservative", "Tradition, limited government, free markets."),
                new LevelBasedDictionaryWord("election", "Formal decision-making process.")
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
    public Optional<DictionaryWord> getDictionaryWord(Level level, String category) {
        SimpleDictionary simpleDictionary = level2simpleDictionary.get(level);
        if (simpleDictionary == null) {
            return Optional.empty();
        }

        return simpleDictionary.getDictionaryWord(secureRandom, category);
    }

    @Override
    public DictionaryWord getDefaultDictionaryWord() {
        return getDictionaryWord(getDefaultLevel(), getDefaultCategory()).orElseThrow();
    }

    private record SimpleDictionary(Map<String, List<DictionaryWord>> category2words) {
        public Set<String> getCategories() {
            return category2words.keySet();
        }

        public Optional<DictionaryWord> getDictionaryWord(SecureRandom secureRandom, String category) {
            List<DictionaryWord> words = category2words.get(category);
            if (words == null || words.isEmpty()) {
                return Optional.empty();
            }

            int randomIndex = secureRandom.nextInt(words.size());
            return Optional.of(words.get(randomIndex));
        }
    }
}
