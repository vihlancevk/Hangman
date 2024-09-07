package backend.academy.game;

import backend.academy.game.dictionary.Dictionary;
import backend.academy.game.dictionary.DictionaryWord;
import backend.academy.game.dictionary.impl.LevelBasedDictionary;
import backend.academy.game.session.Session;
import backend.academy.game.session.impl.SimpleWordSession;
import backend.academy.game.user.UserInteraction;

public final class Hangman {
    private final UserInteraction userInteraction;
    private final Session session;

    private Hangman(UserInteraction userInteraction, Session session) {
        this.userInteraction = userInteraction;
        this.session = session;
    }

    public static void create(UserInteraction userInteraction) {
        Hangman hangman = Hangman.getInstance(userInteraction);
        hangman.run();
    }

    private static Hangman getInstance(UserInteraction userInteraction) {
        Dictionary dictionary = LevelBasedDictionary.getInstance();

        DictionaryWord dictionaryWord = userInteraction.getDictionaryWord(dictionary);
        Session session = SimpleWordSession.getInstance(dictionaryWord);

        return new Hangman(userInteraction, session);
    }

    private void run() {
        userInteraction.run(session);
    }
}
