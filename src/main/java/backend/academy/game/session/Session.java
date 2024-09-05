package backend.academy.game.session;

import backend.academy.game.SessionState;

public interface Session {
    boolean isCorrectSession();

    SessionState getSessionState();

    SessionState updateState(char symbol);
}
