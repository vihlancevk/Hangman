package backend.academy.game.user;

import backend.academy.game.dictionary.Dictionary;
import backend.academy.game.session.Session;

public interface UserInteraction {
    int getNumberOfAttempts();

    String getWord(Dictionary dictionary);

    void run(Session session);
}
