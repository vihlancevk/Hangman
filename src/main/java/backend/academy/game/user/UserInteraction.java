package backend.academy.game.user;

import backend.academy.game.Session;
import backend.academy.game.dictionary.Dictionary;

public interface UserInteraction {
    int getNumberOfAttempts();

    String getWord(Dictionary dictionary);

    void run(Session session);
}
