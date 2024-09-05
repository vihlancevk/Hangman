package backend.academy.game;

public record SessionState(int numberOfAttempts,
                           int numberOfUsedAttempts,
                           String word,
                           String info,
                           String curWord,
                           String incorrectSymbols) {
    public boolean isFinished() {
        return isGuessed() || !hasAttempts();
    }

    public boolean isGuessed() {
        return hasAttempts() && word.equalsIgnoreCase(curWord);
    }

    private boolean hasAttempts() {
        return numberOfUsedAttempts < numberOfAttempts;
    }
}
