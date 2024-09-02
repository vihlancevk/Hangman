package backend.academy.game;

import backend.academy.game.user.UserInteraction;

final public class Hangman {
    private final Dictionary dictionary;
    private final UserInteraction userInteraction;
    private final Session session;

    private Hangman(UserInteraction userInteraction) {
        this.dictionary = Dictionary.getInstance();
        this.userInteraction = userInteraction;
        this.session = new Session(userInteraction.getNumberOfAttempts());
    }

    public static void run(UserInteraction userInteraction) {
        Hangman hangman = new Hangman(userInteraction);
        hangman.startGame();
        hangman.fillGameSession();
        hangman.runGameSession();
        hangman.clearGameSession();
        hangman.finishGame();
    }

    private void startGame() {
        userInteraction.start();
    }

    private void fillGameSession() {
        Level level = chooseLevelOfGame();
        String category = chooseCategoryOfWords(level);
        String word = getWord(level, category);
        session.fillSession(word);
    }

    private Level chooseLevelOfGame() {
        return userInteraction.chooseLevel();
    }

    private String chooseCategoryOfWords(Level level) {
        return userInteraction.chooseCategory(level, dictionary);
    }

    private String getWord(Level level, String category) {
        return dictionary.getWord(level, category);
    }

    private void runGameSession() {
        userInteraction.run(session);
    }

    private void clearGameSession() {
        session.clearSession();
    }

    private void finishGame() {
        userInteraction.finish();
    }
}
