package backend.academy.user;

import java.io.InputStream;
import java.io.PrintStream;

public class CommandLineUserInteraction implements UserInteraction{
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
    public void finish() {
        String closingMessage = "Thank you for play. We hope you had a good time!";
        out.println(closingMessage);
    }
}
