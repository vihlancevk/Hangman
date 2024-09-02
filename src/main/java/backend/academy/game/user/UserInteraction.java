package backend.academy.game.user;

import backend.academy.game.Dictionary;
import backend.academy.game.Level;
import backend.academy.game.Session;

public interface UserInteraction {
    void start();

    Level chooseLevel();

    String chooseCategory(Level level, Dictionary dictionary);

    void run(Session session);

    void finish();

    int getNumberOfAttempts();
}
