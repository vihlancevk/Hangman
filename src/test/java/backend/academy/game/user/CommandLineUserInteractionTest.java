package backend.academy.game.user;

import backend.academy.game.session.Session;
import backend.academy.game.session.SimpleWordSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommandLineUserInteractionTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void getNumberOfAttempts() {
        CommandLineUserInteraction commandLineUserInteraction = CommandLineUserInteraction.getInstance();

        int actual = commandLineUserInteraction.getNumberOfAttempts();

        assertThat(actual).isEqualTo(6);
    }

    @Test
    public void runIncorrectIncorrectSession() {
        CommandLineUserInteraction commandLineUserInteraction = CommandLineUserInteraction.getInstance();

        Session incorrectSession = SimpleWordSession.getInstance(
            -1,
            "word"
        );
        assertThat(incorrectSession.isCorrectSession()).isEqualTo(false);
        commandLineUserInteraction.run(incorrectSession);

        String actual = outputStream.toString();

        assertThat(actual).isEqualTo(createRedMessage("Incorrect session state.\n"));
    }

    @Test
    public void runIncorrectCorrectSession() {
        CommandLineUserInteraction commandLineUserInteraction = CommandLineUserInteraction.getInstance();

        Session incorrectSession = SimpleWordSession.getInstance(
            8,
            "word"
        );
        assertThat(incorrectSession.isCorrectSession()).isEqualTo(true);
        commandLineUserInteraction.run(incorrectSession);

        String actual = outputStream.toString();

        assertThat(actual).isEqualTo(createRedMessage("Incorrect session state.\n"));
    }

    private String createRedMessage(String message) {
        return "\033[0;31m" + message + "\033[0m";
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
