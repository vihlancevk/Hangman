package backend.academy.game;

import backend.academy.game.user.UserInteraction;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Set;

final public class Hangman {
    private final Random random;
    private final Dictionary dictionary;
    private final UserInteraction userInteraction;

    private Hangman(UserInteraction userInteraction) {
        this.random = new SecureRandom();
        this.dictionary = Dictionary.getInstance();
        this.userInteraction = userInteraction;
    }

    public static void run(UserInteraction userInteraction) {
        Hangman hangman = new Hangman(userInteraction);
        hangman.startGame();
        Level level = hangman.chooseLevelOfGame();
        String category = hangman.chooseCategoryOfWords(level);
        String word = hangman.getWord(level, category);
        hangman.finishGame();
    }

    private void startGame() {
        userInteraction.start();
    }

    private Level chooseLevelOfGame() {
        return userInteraction.chooseLevel();
    }

    private String chooseCategoryOfWords(Level level) {
        Set<String> categories = dictionary.getCategoriesByLevel(level);
        return userInteraction.chooseCategory(random, categories);
    }

    private String getWord(Level level, String category) {
        return dictionary.getWord(random, level, category);
    }

    private void finishGame() {
        userInteraction.finish();
    }
}
