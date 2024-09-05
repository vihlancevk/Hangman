package backend.academy.game.dictionary;

import backend.academy.game.Level;
import java.util.Optional;
import java.util.Set;

public interface Dictionary {
    Set<Level> getLevels();

    Level getDefaultLevel();

    Set<String> getCategoriesByLevel(Level level);

    Optional<String> getCategoryByLevel(Level level);

    String getDefaultCategory();

    Optional<String> getWord(Level level, String category);

    String getDefaultWord();
}
