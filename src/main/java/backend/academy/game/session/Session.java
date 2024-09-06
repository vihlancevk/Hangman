package backend.academy.game.session;

public interface Session {
    SessionState getSessionState();

    SessionState updateState(String symbol);
}
