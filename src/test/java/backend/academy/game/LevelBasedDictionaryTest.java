package backend.academy.game;

import backend.academy.game.dictionary.LevelBasedDictionary;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

public class LevelBasedDictionaryTest {
    private final LevelBasedDictionary levelBasedDictionary = LevelBasedDictionary.getInstance();

    @Test
    public void getLevels() {
        Set<Level> availableLevels = levelBasedDictionary.getLevels();

        assertThat(availableLevels).containsOnly(Level.EASY, Level.MEDIUM, Level.HARD);
    }

    @Test
    public void getDefaultLevel() {
        Level defaultLevel = levelBasedDictionary.getDefaultLevel();

        assertThat(defaultLevel).isEqualTo(Level.MEDIUM);
    }

    @Test
    public void getCategoriesByEasyLevel() {
        Set<String> easyCategories = levelBasedDictionary.getCategoriesByLevel(Level.EASY);

        assertThat(easyCategories).containsOnly("weather", "appearance");
    }

    @Test
    public void getCategoriesByMediumLevel() {
        Set<String> easyCategories = levelBasedDictionary.getCategoriesByLevel(Level.MEDIUM);

        assertThat(easyCategories).containsOnly("food", "character");
    }

    @Test
    public void getCategoriesByHardLevel() {
        Set<String> easyCategories = levelBasedDictionary.getCategoriesByLevel(Level.HARD);

        assertThat(easyCategories).containsOnly("culture", "politics");
    }

    @Test
    public void getCategoriesByNull() {
        Set<String> easyCategories = levelBasedDictionary.getCategoriesByLevel(null);

        assertThat(easyCategories).isEmpty();
    }

    @Test
    public void getCategoryByEasyLevel() {
        Optional<String> categoryByEasyLevel = levelBasedDictionary.getCategoryByLevel(Level.EASY);

        assertThat(categoryByEasyLevel.orElseThrow()).isIn(List.of("weather", "appearance"));
    }

    @Test
    public void getCategoryByMediumLevel() {
        Optional<String> categoryByMediumLevel = levelBasedDictionary.getCategoryByLevel(Level.MEDIUM);

        assertThat(categoryByMediumLevel.orElseThrow()).isIn(List.of("food", "character"));
    }

    @Test
    public void getCategoryByHardLevel() {
        Optional<String> categoryByHardLevel = levelBasedDictionary.getCategoryByLevel(Level.HARD);

        assertThat(categoryByHardLevel.orElseThrow()).isIn(List.of("culture", "politics"));
    }

    @Test
    public void getCategoryByNullLevel() {
        Optional<String> categoryByNullLevel = levelBasedDictionary.getCategoryByLevel(null);

        assertThat(categoryByNullLevel).isEmpty();
    }

    @Test
    public void getDefaultCategory() {
        String defaultCategory = levelBasedDictionary.getDefaultCategory();

        assertThat(defaultCategory).isIn(List.of("food", "character"));
    }

    @Test
    public void getWordByBadLevel() {
        Optional<String> word = levelBasedDictionary.getWord(null, null);

        assertThat(word).isEmpty();
    }

    @Test
    public void getWordByGoodLevelAndBadCategory() {
        Level level = levelBasedDictionary.getDefaultLevel();
        Optional<String> word = levelBasedDictionary.getWord(level, null);

        assertThat(word).isEmpty();
    }

    @Test
    public void getWordByGoodLevelAndGoodCategory() {
        Level level = levelBasedDictionary.getDefaultLevel();
        Optional<String> category = levelBasedDictionary.getCategoryByLevel(level);
        Optional<String> word = levelBasedDictionary.getWord(level, category.orElseThrow());

        assertThat(word).isPresent();
    }

    @Test
    public void getDefaultWord() {
        String defaultWord = levelBasedDictionary.getDefaultWord();

        assertThat(defaultWord).isNotNull();
    }
}
