package backend.academy.game;

import backend.academy.user.UserInteraction;

public class Hangman {
    private final UserInteraction userInteraction;

    private Hangman(UserInteraction userInteraction) {
        this.userInteraction = userInteraction;
    }

    public static void run(UserInteraction userInteraction) {
        Hangman hangman = new Hangman(userInteraction);
        hangman.startGame();
        hangman.finishGame();
    }

    private void startGame() {
        userInteraction.start();
    }

    private void finishGame() {
        userInteraction.finish();
    }
}
