package backend.academy.game.user;

import backend.academy.game.Level;
import java.util.Random;
import java.util.Set;

public interface UserInteraction {
    void start();

    Level chooseLevel();

    String chooseCategory(Random random, Set<String> categories);

    void finish();
}
