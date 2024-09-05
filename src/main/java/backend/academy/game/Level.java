package backend.academy.game;

public enum Level {
    EASY,
    MEDIUM,
    HARD;

    public boolean compareToWithIgnoringCase(String level) {
        return name().equalsIgnoreCase(level);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
