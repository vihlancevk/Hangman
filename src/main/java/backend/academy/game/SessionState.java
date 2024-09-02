package backend.academy.game;

public record SessionState(String word, String curWord, String incorrectSymbols, int numberOfUsedAttempts) {

}
