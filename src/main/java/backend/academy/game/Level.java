package backend.academy.game;

public enum Level {
    EASY("easy", "e"),
    MEDIUM("medium", "m"),
    HARD("hard", "h");

    private final String name;
    private final String shortName;

    Level(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
}
