package backend.academy.game.user;

import backend.academy.game.Level;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

final public class CommandLineUserInteraction implements UserInteraction {
    private final PrintStream out;
    private final InputStream in;

    private CommandLineUserInteraction() {
        out = System.out;
        in = System.in;
    }

    public static UserInteraction getInstance() {
        return new CommandLineUserInteraction();
    }

    @Override
    public void start() {
        String greetingMessage = "Hello, let's start play on Hangman.";
        out.println(greetingMessage);
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
        out.print(message);

        String level = new Scanner(in, StandardCharsets.UTF_8).nextLine();
        switch (level) {
            case "easy", "e" -> {
                out.println("You choose easy level.");
                return Level.EASY;
            }
            case "medium", "m" -> {
                out.println("You choose medium level.");
                return Level.MEDIUM;
            }
            case "hard", "h" -> {
                out.println("You choose hard level.");
                return Level.HARD;
            }
            default -> {
                out.println("Incorrect input. The default game level is medium.");
                return Level.MEDIUM;
            }
        }
    }

    @Override
    public String chooseCategory(Random random, Set<String> categories) {
        List<String> sortedCategories = copyAndSortCategories(categories);

        StringBuilder message = new StringBuilder();
        message.append("Choose category of words:\n");
        int i = 1;
        for (String category : sortedCategories) {
            message
                .append(i++).append(". ")
                .append(category).append('\n');
        }
        out.print(message);

        String category = new Scanner(in, StandardCharsets.UTF_8).nextLine();
        if (categories.contains(category)) {
            out.println("You choose category: " + category + ".");
            return category;
        } else {
            out.println("Incorrect input.");
            String randomCategory = chooseCategory(random, sortedCategories);
            out.println("Randomly selected category: " + randomCategory + ".");
            return randomCategory;
        }
    }

    private List<String> copyAndSortCategories(Collection<String> categories) {
        List<String> sortedCategories = new ArrayList<>(categories);
        Collections.sort(sortedCategories);
        return sortedCategories;
    }

    private String chooseCategory(Random random, List<String> categories) {
        int randomIndex = random.nextInt(categories.size());
        return categories.get(randomIndex);
    }

    @Override
    public void finish() {
        String closingMessage = "Thank you for play. We hope you had a good time!";
        out.println(closingMessage);
    }
}
