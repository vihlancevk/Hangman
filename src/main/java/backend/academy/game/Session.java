package backend.academy.game;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Session {
    private char[] word;
    private char[] curWord;
    private final Set<Character> correctSymbols;
    private final Set<Character> incorrectSymbols;
    private final int numberOfAttempts;
    private int numberOfUsedAttempts;

    public Session(int numberOfAttempts) {
        this.word = new char[0];
        this.curWord = new char[0];
        this.correctSymbols = new TreeSet<>();
        this.incorrectSymbols = new TreeSet<>();
        this.numberOfAttempts = numberOfAttempts;
        this.numberOfUsedAttempts = 0;
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

    public void fillSession(String word) {
        this.word = toUpperCase(word).toCharArray();
        this.curWord = new char[word.length()];
        Arrays.fill(curWord, '_');
    }

    private String toUpperCase(String string) {
        return string.toUpperCase();
    }

    public void clearSession() {
        this.word = new char[0];
        this.curWord = new char[0];
        this.numberOfUsedAttempts = 0;
    }

    public boolean isContinue() {
        return !isGuessed() && numberOfUsedAttempts < numberOfAttempts;
    }

    @SuppressFBWarnings({"PL_PARALLEL_LISTS"})
    public boolean isGuessed() {
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

    private char toUpperCase(char symbol) {
        return Character.toUpperCase(symbol);
    }
}
