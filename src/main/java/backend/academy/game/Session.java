package backend.academy.game;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

@SuppressFBWarnings({"PL_PARALLEL_LISTS"})
public final class Session {
    private final int numberOfAttempts;
    private int numberOfUsedAttempts;

    private final char[] word;
    private final char[] curWord;

    private final Set<Character> correctSymbols;
    private final Set<Character> incorrectSymbols;

    private Session(int numberOfAttempts, char[] word, char[] curWord) {
        this.numberOfAttempts = numberOfAttempts;
        this.numberOfUsedAttempts = 0;
        this.word = word;
        this.curWord = curWord;
        this.correctSymbols = new TreeSet<>();
        this.incorrectSymbols = new TreeSet<>();
    }

    public static Session getInstance(int numberOfAttempts, String word) {
        char[] wordAsSymbols = toUpperCase(word).toCharArray();

        char[] curWordAsSymbols = new char[word.length()];
        Arrays.fill(curWordAsSymbols, '_');

        return new Session(numberOfAttempts, wordAsSymbols, curWordAsSymbols);
    }

    public SessionState getSessionState() {
        return new SessionState(convert(word), convert(curWord), getIncorrectSymbols(), numberOfUsedAttempts);
    }

    private String convert(char[] array) {
        StringBuilder sb = new StringBuilder();

        for (char symbol : array) {
            sb.append(symbol);
        }

        return sb.toString();
    }

    private String getIncorrectSymbols() {
        StringBuilder sb = new StringBuilder();

        for (char symbol : incorrectSymbols) {
            sb.append(symbol).append(' ');
        }

        return sb.toString();
    }

    public boolean isContinue() {
        return !isSuccessfulFinished() && numberOfUsedAttempts < numberOfAttempts;
    }

    public boolean isSuccessfulFinished() {
        int n = word.length;
        for (int i = 0; i < n; i++) {
            if (word[i] != curWord[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean addSymbol(char symbol) {
        char upperCaseSymbol = toUpperCase(symbol);

        if (correctSymbols.contains(upperCaseSymbol)) {
            return false;
        }

        if (incorrectSymbols.contains(upperCaseSymbol)) {
            return false;
        }

        boolean isCorrectSymbol = false;

        int n = word.length;
        for (int i = 0; i < n; i++) {
            if (upperCaseSymbol == word[i]) {
                curWord[i] = upperCaseSymbol;
                isCorrectSymbol = true;
            }
        }

        if (isCorrectSymbol) {
            correctSymbols.add(upperCaseSymbol);
        } else {
            incorrectSymbols.add(upperCaseSymbol);
            numberOfUsedAttempts++;
        }

        return isCorrectSymbol;
    }

    private static String toUpperCase(String string) {
        return string.toUpperCase();
    }

    private static char toUpperCase(char symbol) {
        return Character.toUpperCase(symbol);
    }
}
