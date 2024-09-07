package backend.academy.game.user;

import backend.academy.game.dictionary.Dictionary;
import backend.academy.game.dictionary.DictionaryWord;
import backend.academy.game.session.Session;

public interface UserInteraction {
    DictionaryWord getDictionaryWord(Dictionary dictionary);

    void run(Session session);
}
