package backend.academy.game.session;

import backend.academy.game.SessionState;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SimpleWordSessionTest {
    @Test
    public void isCorrectSessionBadNumberOfAttempts() {
        List<SimpleWordSession> simpleWordSession = List.of(
            SimpleWordSession.getInstance(-1, "word"),
            SimpleWordSession.getInstance(0, "word")
        );

        List<Boolean> actual = simpleWordSession.stream()
            .map(SimpleWordSession::isCorrectSession)
            .toList();

        actual.forEach(isCorrect -> assertThat(isCorrect).isEqualTo(false));
    }

    @Test
    public void isCorrectSessionBadWord() {
        List<SimpleWordSession> simpleWordSessions = List.of(
            SimpleWordSession.getInstance(1000, null),
            SimpleWordSession.getInstance(1000, ""),
            SimpleWordSession.getInstance(1000, "          "),
            SimpleWordSession.getInstance(1000, "\t\n"),
            SimpleWordSession.getInstance(1000, "word_word"),
            SimpleWordSession.getInstance(1000, "word-word"),
            SimpleWordSession.getInstance(1000, "w1o2r3d")
        );

        List<Boolean> actual = simpleWordSessions.stream()
            .map(SimpleWordSession::isCorrectSession)
            .toList();

        actual.forEach(isCorrect -> assertThat(isCorrect).isEqualTo(false));
    }

    @Test
    public void isCorrectSessionGoodInput() {
        List<SimpleWordSession> simpleWordSessions = List.of(
            SimpleWordSession.getInstance(1, "word"),
            SimpleWordSession.getInstance(2, "word"),
            SimpleWordSession.getInstance(3, "word"),
            SimpleWordSession.getInstance(4, "word"),
            SimpleWordSession.getInstance(5, "word"),
            SimpleWordSession.getInstance(8, "application")
        );

        List<Boolean> actual = simpleWordSessions.stream()
            .map(SimpleWordSession::isCorrectSession)
            .toList();

        actual.forEach(isCorrect -> assertThat(isCorrect).isEqualTo(true));
    }

    @Test
    public void getSessionStateIfIncorrectSession() {
        SimpleWordSession incorrectSession = SimpleWordSession.getInstance(-1, "");
        assertThat(incorrectSession.isCorrectSession()).isEqualTo(false);

        SessionState actual = incorrectSession.getSessionState();

        SessionState expected = new SessionState(
            0,
            0,
            "",
            "",
            "",
            ""
        );
        checkSessionState(actual, expected, true, false);
    }

    @Test
    public void getSessionStateIfCorrectSession() {
        List<SimpleWordSession> simpleWordSessions = List.of(
            SimpleWordSession.getInstance(4, "word"),
            SimpleWordSession.getInstance(4, "Word"),
            SimpleWordSession.getInstance(4, "wOrd"),
            SimpleWordSession.getInstance(4, "woRd"),
            SimpleWordSession.getInstance(4, "worD"),
            SimpleWordSession.getInstance(4, "WOrd"),
            SimpleWordSession.getInstance(4, "WoRd"),
            SimpleWordSession.getInstance(4, "WorD"),
            SimpleWordSession.getInstance(4, "wORd"),
            SimpleWordSession.getInstance(4, "wOrD"),
            SimpleWordSession.getInstance(4, "woRD"),
            SimpleWordSession.getInstance(4, "WORd"),
            SimpleWordSession.getInstance(4, "WOrD"),
            SimpleWordSession.getInstance(4, "wORD"),
            SimpleWordSession.getInstance(4, "WORD")
        );

        List<SessionState> actual = simpleWordSessions.stream()
            .map(SimpleWordSession::getSessionState)
            .toList();

        SessionState expected = new SessionState(
            4,
            0,
            "WORD",
            "",
            "____",
            ""
        );
        actual.forEach(sessionState -> checkSessionState(sessionState, expected, false, false));
    }

    @Test
    public void updateStateLowerCaseCorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(4, "word");

        SessionState actual = simpleWordSession.updateState('w');

        SessionState expected = new SessionState(
            4,
            0,
            "WORD",
            "The symbol is entered correctly.",
            "W___",
            ""
        );
        checkSessionState(actual, expected, false, false);
    }

    @Test
    public void updateStateUpperCaseCorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(4, "word");

        SessionState actual = simpleWordSession.updateState('W');

        SessionState expected = new SessionState(
            4,
            0,
            "WORD",
            "The symbol is entered correctly.",
            "W___",
            ""
        );
        checkSessionState(actual, expected, false, false);
    }

    @Test
    public void updateStateLowerCaseIncorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(4, "word");

        SessionState actual = simpleWordSession.updateState('e');

        SessionState expected = new SessionState(
            4,
            1,
            "WORD",
            "The symbol is entered incorrectly.",
            "____",
            "E"
        );
        checkSessionState(actual, expected, false, false);
    }

    @Test
    public void updateStateUpperCaseIncorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(4, "word");

        SessionState actual = simpleWordSession.updateState('E');

        SessionState expected = new SessionState(
            4,
            1,
            "WORD",
            "The symbol is entered incorrectly.",
            "____",
            "E"
        );
        checkSessionState(actual, expected, false, false);
    }

    @Test
    public void updateStateLowerCaseAlreadyCorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(4, "word");

        simpleWordSession.updateState('o');
        SessionState actual = simpleWordSession.updateState('o');

        SessionState expected = new SessionState(
            4,
            0,
            "WORD",
            "This character has already been entered correctly.",
            "_O__",
            ""
        );
        checkSessionState(actual, expected, false, false);
    }

    @Test
    public void updateStateUpperCaseAlreadyCorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(4, "word");

        simpleWordSession.updateState('r');
        SessionState actual = simpleWordSession.updateState('R');

        SessionState expected = new SessionState(
            4,
            0,
            "WORD",
            "This character has already been entered correctly.",
            "__R_",
            ""
        );
        checkSessionState(actual, expected, false, false);
    }

    @Test
    public void updateStateLowerCaseAlreadyIncorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(4, "word");

        simpleWordSession.updateState('w');
        simpleWordSession.updateState('o');
        simpleWordSession.updateState('p');
        simpleWordSession.updateState('a');
        SessionState actual = simpleWordSession.updateState('a');

        SessionState expected = new SessionState(
            4,
            2,
            "WORD",
            "This character has already been entered incorrectly.",
            "WO__",
            "AP"
        );
        checkSessionState(actual, expected, false, false);
    }

    @Test
    public void updateStateUpperCaseAlreadyIncorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(4, "word");

        simpleWordSession.updateState('w');
        simpleWordSession.updateState('o');
        simpleWordSession.updateState('p');
        simpleWordSession.updateState('a');
        SessionState actual = simpleWordSession.updateState('A');

        SessionState expected = new SessionState(
            4,
            2,
            "WORD",
            "This character has already been entered incorrectly.",
            "WO__",
            "AP"
        );
        checkSessionState(actual, expected, false, false);
    }

    @Test
    public void updateStateGuessed() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(4, "word");

        simpleWordSession.updateState('w');
        simpleWordSession.updateState('O');
        simpleWordSession.updateState('P');
        simpleWordSession.updateState('a');
        simpleWordSession.updateState('r');
        simpleWordSession.updateState('T');
        SessionState actual = simpleWordSession.updateState('D');

        SessionState expected = new SessionState(
            4,
            3,
            "WORD",
            "The symbol is entered correctly.",
            "WORD",
            "APT"
        );
        checkSessionState(actual, expected, true, true);
    }

    @Test
    public void updateStateFinishedTrying() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(5, "word");

        simpleWordSession.updateState('w');
        simpleWordSession.updateState('D');
        simpleWordSession.updateState('b');
        simpleWordSession.updateState('C');
        simpleWordSession.updateState('a');
        simpleWordSession.updateState('x');
        SessionState actual = simpleWordSession.updateState('Y');

        SessionState expected = new SessionState(
            5,
            5,
            "WORD",
            "The symbol is entered incorrectly.",
            "W__D",
            "ABCXY"
        );
        checkSessionState(actual, expected, true, false);
    }

    @Test
    public void updateStateFinishedTryingOverLimitDefault() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(5, "word");

        simpleWordSession.updateState('a');
        simpleWordSession.updateState('b');
        simpleWordSession.updateState('c');
        simpleWordSession.updateState('d');
        simpleWordSession.updateState('e');
        simpleWordSession.updateState('g');
        SessionState actual = simpleWordSession.updateState('h');

        SessionState expected = new SessionState(
            5,
            6,
            "WORD",
            "The symbol is entered incorrectly.",
            "___D",
            "ABCEGH"
        );
        checkSessionState(actual, expected, true, false);
    }

    @Test
    public void updateStateFinishedTryingOverLimitVictory() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(5, "word");

        simpleWordSession.updateState('a');
        simpleWordSession.updateState('b');
        simpleWordSession.updateState('c');
        simpleWordSession.updateState('d');
        simpleWordSession.updateState('e');
        simpleWordSession.updateState('g');
        simpleWordSession.updateState('w');
        simpleWordSession.updateState('o');
        simpleWordSession.updateState('r');
        simpleWordSession.updateState('d');
        simpleWordSession.updateState('h');
        SessionState actual = simpleWordSession.updateState('d');

        SessionState expected = new SessionState(
            5,
            6,
            "WORD",
            "This character has already been entered correctly.",
            "WORD",
            "ABCEGH"
        );
        checkSessionState(actual, expected, true, false);
    }

    private void checkSessionState(SessionState actual, SessionState expected, boolean isFinished, boolean isGuessed) {
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.isFinished()).isEqualTo(isFinished);
        assertThat(expected.isGuessed()).isEqualTo(isGuessed);
    }
}
