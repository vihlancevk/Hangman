package backend.academy.game.user;

import backend.academy.game.Dictionary;
import backend.academy.game.Level;
import backend.academy.game.Session;
import backend.academy.game.SessionState;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

final public class CommandLineUserInteraction implements UserInteraction {
    private static final int NUMBER_OF_ATTEMPTS = 6;
    private final String[] states = new String[] {
        "  -----\n  |   |\n  |\n  |\n  |\n  |\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |\n  |\n  |\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |   |\n  |   |\n  |\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |  -|\n  |   |\n  |\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |  -|-\n  |   |\n  |\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |  -|-\n  |   |\n  |  /\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |  -|-\n  |   |\n  |  / \\\n  |\n  |\n ---"
    };

    private final PrintStream out;
    private final Scanner scanner;

    private CommandLineUserInteraction() {
        out = System.out;
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    public static UserInteraction getInstance() {
        return new CommandLineUserInteraction();
    }

    @Override
    public void start() {
        String greetingMessage = "Hello, let's start play on Hangman.";
        println(greetingMessage);
    }

    @Override
    public Level chooseLevel() {
        StringBuilder message = new StringBuilder();
        message.append("Choose level of game:\n");
        for (Level level : Level.values()) {
            message
                .append(level.getName())
                .append(' ')
                .append('(').append(level.getShortName()).append(')')
                .append('\n');
        }
        print(message.toString());

        String level = nextLineInLowerCase();
        switch (level) {
            case "easy", "e" -> {
                println("You choose easy level.");
                return Level.EASY;
            }
            case "medium", "m" -> {
                println("You choose medium level.");
                return Level.MEDIUM;
            }
            case "hard", "h" -> {
                println("You choose hard level.");
                return Level.HARD;
            }
            default -> {
                println("Incorrect input. The default game level is medium.");
                return Level.MEDIUM;
            }
        }
    }

    @Override
    public String chooseCategory(Level level, Dictionary dictionary) {
        List<String> categories = handleCategories(dictionary.getCategoriesByLevel(level));

        StringBuilder message = new StringBuilder();
        message.append("Choose category of words:\n");
        int i = 1;
        for (String category : categories) {
            message
                .append(i++).append(". ")
                .append(category).append('\n');
        }
        print(message.toString());

        String category = nextLineInLowerCase();
        if (categories.contains(category)) {
            println("You choose category: " + category + ".");
            return category;
        } else {
            println("Incorrect input.");
            String randomCategory = dictionary.getRandomCategoryByLevel(level);
            println("Randomly selected category: " + randomCategory + ".");
            return randomCategory;
        }
    }

    private List<String> handleCategories(Set<String> categories) {
        return new ArrayList<>(categories).stream()
            .sorted()
            .map(String::toLowerCase)
            .toList();
    }

    @Override
    public void run(Session session) {
        while (session.isContinue()) {
            draw(session);
            addSymbol(session);
        }
        stop(session);
    }

    private void draw(Session session) {
        SessionState sessionState = session.getSessionState();

        println("Number of attempts: " + NUMBER_OF_ATTEMPTS + ".");
        println("Number of used attempts: " + (sessionState.numberOfUsedAttempts()) + ".");

        println(states[sessionState.numberOfUsedAttempts()]);

        println(sessionState.curWord());
        colorPrintln(sessionState.incorrectSymbols(), AnsiColor.RED);
    }

    private void addSymbol(Session session) {
        char symbol = inputSymbol();
        session.addSymbol(symbol);
    }

    private char inputSymbol() {
        print("Input symbol: ");

        String symbol = nextLineInLowerCase();
        while (symbol.length() != 1 || !Character.isLetter(symbol.charAt(0))) {
            print("You input incorrect symbol. Please, try again: ");
            symbol = nextLineInLowerCase();
        }

        return symbol.charAt(0);
    }

    private void stop(Session session) {
        draw(session);

        SessionState sessionState = session.getSessionState();
        if (session.isGuessed()) {
            colorPrintln("Victory!", AnsiColor.GREEN);
        } else {
            println("Target word: " + sessionState.word());
            colorPrintln("Defeat!", AnsiColor.RED);
        }
    }

    @Override
    public void finish() {
        String closingMessage = "Thank you for play. We hope you had a good time!";
        println(closingMessage);
    }

    @Override
    public int getNumberOfAttempts() {
        return NUMBER_OF_ATTEMPTS;
    }

    private String nextLineInLowerCase() {
        return scanner.nextLine().toLowerCase();
    }

    private void println(String string) {
        print(string + "\n");
    }

    private void print(String string) {
        out.print(string);
    }

    private void colorPrintln(String string, AnsiColor color) {
        colorPrint(string + "\n", color);
    }

    private void colorPrint(String string, AnsiColor color) {
        String ansiReset = "\033[0m";
        out.print(color.getCode() + string + ansiReset);
    }

    private enum AnsiColor {
        RED("\033[0;31m"),
        GREEN("\033[0;32m");

        private final String code;

        AnsiColor(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
