package backend.academy.game.user;

import backend.academy.game.Level;
import backend.academy.game.dictionary.Dictionary;
import backend.academy.game.session.Session;
import backend.academy.game.session.SessionState;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public final class CommandLineUserInteraction implements UserInteraction {
    private final PrintStream out;
    private final Scanner scanner;

    private CommandLineUserInteraction() {
        out = System.out;
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    public static CommandLineUserInteraction getInstance() {
        return new CommandLineUserInteraction();
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
            display(sessionState);
            sessionState = updateState(session);
        }
        display(sessionState);
    }

    private void display(SessionState sessionState) {
        println(sessionState.message());
    }

    private SessionState updateState(Session session) {
        String symbol = inputSymbol();
        return session.updateState(symbol);
    }

    private String inputSymbol() {
        print("Input symbol: ");
        return nextLine();
    }

    private String nextLine() {
        return scanner.nextLine();
    }

    private void println(String string) {
        print(string + "\n");
    }

    private void print(String string) {
        out.print(string);
    }
}
