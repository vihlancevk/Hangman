package backend.academy.game.user;

import backend.academy.game.Level;
import backend.academy.game.SessionState;
import backend.academy.game.dictionary.Dictionary;
import backend.academy.game.session.Session;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public final class CommandLineUserInteraction implements UserInteraction {
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
    public int getNumberOfAttempts() {
        return NUMBER_OF_ATTEMPTS;
    }

    @Override
    public String getWord(Dictionary dictionary) {
        Level level = chooseLevel(dictionary);
        String category = chooseCategory(level, dictionary);
        return getWord(level, category, dictionary);
    }

    private Level chooseLevel(Dictionary dictionary) {
        Set<Level> levels = dictionary.getLevels();

        String message = createNumberedMessage("Choose level of game:\n", levels);
        print(message);

        String chosenLevel = scanner.nextLine();
        Optional<Level> optionalLevel = levels.stream()
            .filter(level -> level.compareToWithIgnoringCase(chosenLevel))
            .findFirst();

        if (optionalLevel.isPresent()) {
            Level level = optionalLevel.orElseThrow();
            println("You choose " + level + " level.");
            return level;
        } else {
            reportWarning();
            Level level = dictionary.getDefaultLevel();
            println("The default game level is used: " + level + ".");
            return level;
        }
    }

    private String chooseCategory(Level level, Dictionary dictionary) {
        List<String> categories = handleCategories(dictionary.getCategoriesByLevel(level));

        String message = createNumberedMessage("Choose category of words:\n", categories);
        print(message);

        String category = nextLineInLowerCase();
        if (categories.contains(category)) {
            println("You choose category: " + category + ".");
            return category;
        } else {
            reportWarning();
            Optional<String> optionalCategory = dictionary.getCategoryByLevel(level);
            String chosenCategory = optionalCategory.isPresent()
                ? optionalCategory.orElseThrow()
                : dictionary.getDefaultCategory();
            println("We choose category for you: " + chosenCategory + ".");
            return chosenCategory;
        }
    }

    private List<String> handleCategories(Set<String> categories) {
        return new ArrayList<>(categories).stream()
            .sorted()
            .map(String::toLowerCase)
            .toList();
    }

    private String nextLineInLowerCase() {
        return nextLine().toLowerCase();
    }

    private <T> String createNumberedMessage(String info, Iterable<T> iterable) {
        StringBuilder message = new StringBuilder();
        message.append(info);
        int i = 1;
        for (T t : iterable) {
            message
                .append(i++).append(". ")
                .append(t).append('\n');
        }
        return message.toString();
    }

    private void reportWarning() {
        println("Incorrect input.");
    }

    private String getWord(Level level, String category, Dictionary dictionary) {
        Optional<String> optionalWord = dictionary.getWord(level, category);
        return optionalWord.isPresent() ? optionalWord.orElseThrow() : dictionary.getDefaultWord();
    }

    @Override
    public void run(Session session) {
        SessionState sessionState = session.getSessionState();
        while (!sessionState.isFinished()) {
            draw(sessionState);
            sessionState = updateState(session);
        }
        stop(sessionState);
    }

    private void draw(SessionState sessionState) {
        println("Number of attempts: " + sessionState.numberOfAttempts() + ".");
        println("Number of used attempts: " + sessionState.numberOfUsedAttempts() + ".");

        println(states[sessionState.numberOfUsedAttempts()]);

        println(sessionState.info());
        println(sessionState.curWord());
        colorPrintln(sessionState.incorrectSymbols(), AnsiColor.RED);
    }

    private SessionState updateState(Session session) {
        char symbol = inputSymbol();
        return session.updateState(symbol);
    }

    private char inputSymbol() {
        print("Input symbol: ");

        String symbol = nextLine();
        while (symbol.length() != 1 || !Character.isLetter(symbol.charAt(0))) {
            print("You input incorrect symbol. Please, try again: ");
            symbol = nextLine();
        }

        return symbol.charAt(0);
    }

    private String nextLine() {
        return scanner.nextLine();
    }

    private void stop(SessionState sessionState) {
        draw(sessionState);

        if (sessionState.isGuessed()) {
            colorPrintln("Victory!", AnsiColor.GREEN);
        } else {
            println("Target word: " + sessionState.word());
            colorPrintln("Defeat!", AnsiColor.RED);
        }
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
