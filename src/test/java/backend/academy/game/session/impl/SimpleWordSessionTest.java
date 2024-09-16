package backend.academy.game.session.impl;

import backend.academy.game.dictionary.impl.LevelBasedDictionaryWord;
import backend.academy.game.session.SessionState;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SimpleWordSessionTest {
    @Test
    public void getSessionStateIfIncorrectSession() {
        // Arrange
        List<SimpleWordSession> incorrectSessions = List.of(
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord(null, null)),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", null)),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord(null, "clue")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("\t\n", ""))
        );

        // Act
        List<SessionState> incorrectSessionStates = incorrectSessions.stream()
            .map(SimpleWordSession::getSessionState)
            .toList();

        // Assert
        SessionState expected = new SimpleWordSessionState(true,"Incorrect session!");
        incorrectSessionStates.forEach(actual -> assertThat(actual).isEqualTo(expected));
    }

    @Test
    public void getSessionStateIfCorrectSession() {
        // Arrange
        List<SimpleWordSession> simpleWordSessions = List.of(
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("Word", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("wOrd", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("woRd", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("worD", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("WOrd", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("WoRd", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("WorD", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("wORd", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("wOrD", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("woRD", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("WORd", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("WOrD", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("wORD", "")),
            SimpleWordSession.getInstance(new LevelBasedDictionaryWord("WORD", ""))
        );

        // Act
        List<SessionState> simpleWordSessionStates = simpleWordSessions.stream()
            .map(SimpleWordSession::getSessionState)
            .toList();

        // Assert
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
        simpleWordSessionStates.forEach(actual -> assertThat(actual).isEqualTo(expected));
    }

    @Test
    public void updateSessionIncorrectInput() {
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
        simpleWordSession.updateState("w");
        simpleWordSession.updateState("A");
        simpleWordSession.updateState("D");
        simpleWordSession.updateState("h");
        SessionState actual = simpleWordSession.updateState("biba");

        // Assert
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
Incorrect input.
W__D
AH"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateLowerCaseCorrect() {
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
        SessionState actual = simpleWordSession.updateState("w");

        // Assert
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
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
        SessionState actual = simpleWordSession.updateState("W");

        // Assert
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
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
        SessionState actual = simpleWordSession.updateState("e");

        // Assert
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
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
        SessionState actual = simpleWordSession.updateState("E");

        // Assert
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
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
        simpleWordSession.updateState("o");
        SessionState actual = simpleWordSession.updateState("o");

        // Assert
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
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
        simpleWordSession.updateState("r");
        SessionState actual = simpleWordSession.updateState("R");

        // Assert
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
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
        simpleWordSession.updateState("w");
        simpleWordSession.updateState("o");
        simpleWordSession.updateState("p");
        simpleWordSession.updateState("a");
        SessionState actual = simpleWordSession.updateState("a");

        // Assert
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
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
        simpleWordSession.updateState("w");
        simpleWordSession.updateState("o");
        simpleWordSession.updateState("p");
        simpleWordSession.updateState("a");
        SessionState actual = simpleWordSession.updateState("A");

        // Assert
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
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", "World but short."));

        // Act
        simpleWordSession.updateState("w");
        simpleWordSession.updateState("O");
        simpleWordSession.updateState("P");
        simpleWordSession.updateState("a");
        simpleWordSession.updateState("r");
        simpleWordSession.updateState("T");
        SessionState actual = simpleWordSession.updateState("D");

        // Assert
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
Clue: WORLD BUT SHORT.
WORD
APT
Victory!"""
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStateFinishedTryingOverLimitDefault() {
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
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

        // Assert
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
        // Arrange
        SimpleWordSession simpleWordSession = SimpleWordSession.getInstance(new LevelBasedDictionaryWord("word", ""));

        // Act
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

        // Assert
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
