package backend.academy;

import backend.academy.game.Hangman;
import backend.academy.user.CommandLineUserInteraction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        Hangman.run(CommandLineUserInteraction.getInstance());
    }
}
