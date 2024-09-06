package backend.academy.game.session.impl;

import backend.academy.game.session.SessionState;

public record SimpleWordSessionState(boolean isFinished, String message) implements SessionState {
}
