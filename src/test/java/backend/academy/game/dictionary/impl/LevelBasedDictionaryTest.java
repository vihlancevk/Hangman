package backend.academy.game.dictionary.impl;

import backend.academy.game.Level;
import backend.academy.game.dictionary.DictionaryWord;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

public class LevelBasedDictionaryTest {
    @Test
    public void getLevels() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Set<Level> availableLevels = levelBasedDictionary.getLevels();

        // Assert
        assertThat(availableLevels).containsOnly(Level.EASY, Level.MEDIUM, Level.HARD);
    }

    @Test
    public void getDefaultLevel() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Level defaultLevel = levelBasedDictionary.getDefaultLevel();

        // Assert
        assertThat(defaultLevel).isEqualTo(Level.MEDIUM);
    }

    @Test
    public void getCategoriesByEasyLevel() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Set<String> easyCategories = levelBasedDictionary.getCategoriesByLevel(Level.EASY);

        // Assert
        assertThat(easyCategories).containsOnly("weather", "appearance");
    }

    @Test
    public void getCategoriesByMediumLevel() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Set<String> easyCategories = levelBasedDictionary.getCategoriesByLevel(Level.MEDIUM);

        // Assert
        assertThat(easyCategories).containsOnly("food", "character");
    }

    @Test
    public void getCategoriesByHardLevel() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Set<String> easyCategories = levelBasedDictionary.getCategoriesByLevel(Level.HARD);

        // Assert
        assertThat(easyCategories).containsOnly("culture", "politics");
    }

    @Test
    public void getCategoriesByNull() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Set<String> easyCategories = levelBasedDictionary.getCategoriesByLevel(null);

        // Assert
        assertThat(easyCategories).isEmpty();
    }

    @Test
    public void getCategoryByEasyLevel() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Optional<String> categoryByEasyLevel = levelBasedDictionary.getCategoryByLevel(Level.EASY);

        // Assert
        assertThat(categoryByEasyLevel.orElseThrow()).isIn(List.of("weather", "appearance"));
    }

    @Test
    public void getCategoryByMediumLevel() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Optional<String> categoryByMediumLevel = levelBasedDictionary.getCategoryByLevel(Level.MEDIUM);

        // Assert
        assertThat(categoryByMediumLevel.orElseThrow()).isIn(List.of("food", "character"));
    }

    @Test
    public void getCategoryByHardLevel() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Optional<String> categoryByHardLevel = levelBasedDictionary.getCategoryByLevel(Level.HARD);

        // Assert
        assertThat(categoryByHardLevel.orElseThrow()).isIn(List.of("culture", "politics"));
    }

    @Test
    public void getCategoryByNullLevel() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Optional<String> categoryByNullLevel = levelBasedDictionary.getCategoryByLevel(null);

        // Assert
        assertThat(categoryByNullLevel).isEmpty();
    }

    @Test
    public void getDefaultCategory() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        String defaultCategory = levelBasedDictionary.getDefaultCategory();

        // Assert
        assertThat(defaultCategory).isIn(List.of("food", "character"));
    }

    @Test
    public void getWordByBadLevel() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Optional<DictionaryWord> dictionaryWord = levelBasedDictionary.getDictionaryWord(null, null);

        // Assert
        assertThat(dictionaryWord).isEmpty();
    }

    @Test
    public void getWordByGoodLevelAndBadCategory() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Level level = levelBasedDictionary.getDefaultLevel();
        Optional<DictionaryWord> dictionaryWord = levelBasedDictionary.getDictionaryWord(level, null);

        // Assert
        assertThat(dictionaryWord).isEmpty();
    }

    @Test
    public void getWordByGoodLevelAndGoodCategory() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        Level level = levelBasedDictionary.getDefaultLevel();
        Optional<String> category = levelBasedDictionary.getCategoryByLevel(level);
        Optional<DictionaryWord> dictionaryWord = levelBasedDictionary.getDictionaryWord(level, category.orElseThrow());

        // Assert
        assertThat(dictionaryWord).isPresent();
    }

    @Test
    public void getDefaultDictionaryWord() {
        // Arrange
        LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

        // Act
        DictionaryWord defaultDictionaryWord = levelBasedDictionary.getDefaultDictionaryWord();

        // Assert
        assertThat(defaultDictionaryWord).isNotNull();
    }
}
