package backend.academy.game.session.impl;

import backend.academy.game.session.SessionState;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SimpleWordSessionTest {
    @Test
    public void getSessionStateIfIncorrectSession() {
        SimpleWordSession incorrectSession = SimpleWordSession.getInstance("");

        SessionState actual = incorrectSession.getSessionState();

        SessionState expected = new SimpleWordSessionState(true,"Incorrect session!");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getSessionStateIfCorrectSession() {
        List<SimpleWordSession> simpleWordSessions = List.of(
            SimpleWordSession.getInstance("word"),
            SimpleWordSession.getInstance("Word"),
            SimpleWordSession.getInstance("wOrd"),
            SimpleWordSession.getInstance("woRd"),
            SimpleWordSession.getInstance("worD"),
            SimpleWordSession.getInstance("WOrd"),
            SimpleWordSession.getInstance("WoRd"),
            SimpleWordSession.getInstance("WorD"),
            SimpleWordSession.getInstance("wORd"),
            SimpleWordSession.getInstance("wOrD"),
            SimpleWordSession.getInstance("woRD"),
            SimpleWordSession.getInstance("WORd"),
            SimpleWordSession.getInstance("WOrD"),
            SimpleWordSession.getInstance("wORD"),
            SimpleWordSession.getInstance("WORD")
        );

        List<SessionState> actuals = simpleWordSessions.stream()
            .map(SimpleWordSession::getSessionState)
            .toList();

        SessionState expected = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 0.
  -----
  |   |
  |
  |
  |
  |
  |
  |
 ---
____
"""
        );
        actuals.forEach(actual -> assertThat(actual).isEqualTo(expected));
    }

    @Test
    public void updateSessionIncorrectInput() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");
        simpleWordSession.updateState("w");
        simpleWordSession.updateState("A");
        simpleWordSession.updateState("D");
        SessionState actual = simpleWordSession.updateState("h");
        SessionState expectedBefore = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 2.
  -----
  |   |
  |   0
  |   |
  |   |
  |
  |
  |
 ---
You doesn't guess the letter.
W__D
AH"""
        );
        assertThat(actual).isEqualTo(expectedBefore);

        actual = simpleWordSession.updateState("biba");

        SessionState expectedAfter = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 2.
  -----
  |   |
  |   0
  |   |
  |   |
  |
  |
  |
 ---
Incorrect input.
W__D
AH"""
        );
        assertThat(actual).isEqualTo(expectedAfter);
    }

    @Test
    public void updateStateLowerCaseCorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        SessionState actual = simpleWordSession.updateState("w");

        SessionState expected = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 0.
  -----
  |   |
  |
  |
  |
  |
  |
  |
 ---
You guess the letter.
W___
"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateUpperCaseCorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        SessionState actual = simpleWordSession.updateState("W");

        SessionState expected = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 0.
  -----
  |   |
  |
  |
  |
  |
  |
  |
 ---
You guess the letter.
W___
"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateLowerCaseIncorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        SessionState actual = simpleWordSession.updateState("e");

        SessionState expected = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 1.
  -----
  |   |
  |   0
  |
  |
  |
  |
  |
 ---
You doesn't guess the letter.
____
E"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateUpperCaseIncorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        SessionState actual = simpleWordSession.updateState("E");

        SessionState expected = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 1.
  -----
  |   |
  |   0
  |
  |
  |
  |
  |
 ---
You doesn't guess the letter.
____
E"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateLowerCaseAlreadyCorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        simpleWordSession.updateState("o");
        SessionState actual = simpleWordSession.updateState("o");

        SessionState expected = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 0.
  -----
  |   |
  |
  |
  |
  |
  |
  |
 ---
This letter already guess.
_O__
"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateUpperCaseAlreadyCorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        simpleWordSession.updateState("r");
        SessionState actual = simpleWordSession.updateState("R");

        SessionState expected = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 0.
  -----
  |   |
  |
  |
  |
  |
  |
  |
 ---
This letter already guess.
__R_
"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateLowerCaseAlreadyIncorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        simpleWordSession.updateState("w");
        simpleWordSession.updateState("o");
        simpleWordSession.updateState("p");
        simpleWordSession.updateState("a");
        SessionState actual = simpleWordSession.updateState("a");

        SessionState expected = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 2.
  -----
  |   |
  |   0
  |   |
  |   |
  |
  |
  |
 ---
This letter already doesn't guess.
WO__
AP"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateUpperCaseAlreadyIncorrect() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        simpleWordSession.updateState("w");
        simpleWordSession.updateState("o");
        simpleWordSession.updateState("p");
        simpleWordSession.updateState("a");
        SessionState actual = simpleWordSession.updateState("A");

        SessionState expected = new SimpleWordSessionState(
            false,
            """
Maximum attempts: 6.
Number of used attempts: 2.
  -----
  |   |
  |   0
  |   |
  |   |
  |
  |
  |
 ---
This letter already doesn't guess.
WO__
AP"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateGuessed() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        simpleWordSession.updateState("w");
        simpleWordSession.updateState("O");
        simpleWordSession.updateState("P");
        simpleWordSession.updateState("a");
        simpleWordSession.updateState("r");
        simpleWordSession.updateState("T");
        SessionState actual = simpleWordSession.updateState("D");

        SessionState expected = new SimpleWordSessionState(
            true,
            """
Maximum attempts: 6.
Number of used attempts: 3.
  -----
  |   |
  |   0
  |  -|
  |   |
  |
  |
  |
 ---
You guess the letter.
WORD
APT
Victory!"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateFinishedTryingOverLimitDefault() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        simpleWordSession.updateState("a");
        simpleWordSession.updateState("b");
        simpleWordSession.updateState("c");
        simpleWordSession.updateState("d");
        simpleWordSession.updateState("e");
        simpleWordSession.updateState("g");
        simpleWordSession.updateState("w");
        simpleWordSession.updateState("o");
        simpleWordSession.updateState("d");
        SessionState actual = simpleWordSession.updateState("h");

        SessionState expected = new SimpleWordSessionState(
            true,
            """
Maximum attempts: 6.
Number of used attempts: 6.
  -----
  |   |
  |   0
  |  -|-
  |   |
  |  / \\
  |
  |
 ---
You doesn't guess the letter.
WO_D
ABCEGH
Target word: WORD.
Defeat!"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateFinishedTryingOverLimitVictory() {
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance("word");

        simpleWordSession.updateState("a");
        simpleWordSession.updateState("b");
        simpleWordSession.updateState("c");
        simpleWordSession.updateState("d");
        simpleWordSession.updateState("e");
        simpleWordSession.updateState("g");
        simpleWordSession.updateState("w");
        simpleWordSession.updateState("o");
        simpleWordSession.updateState("r");
        simpleWordSession.updateState("d");
        simpleWordSession.updateState("h");
        SessionState actual = simpleWordSession.updateState("d");

        SessionState expected = new SimpleWordSessionState(
            true,
            """
Maximum attempts: 6.
Number of used attempts: 5.
  -----
  |   |
  |   0
  |  -|-
  |   |
  |  /
  |
  |
 ---
WORD
ABCEG
Victory!"""
        );
        assertThat(actual).isEqualTo(expected);
    }
}
