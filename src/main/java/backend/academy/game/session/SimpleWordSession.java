package backend.academy.game.session;

import backend.academy.game.SessionState;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public final class SimpleWordSession implements Session {
    private final boolean isCorrectSession;

    private final int numberOfAttempts;
    private int numberOfUsedAttempts;

    private final String word;
    private final char[] curWord;

    private final Set<Character> correctSymbols;
    private final Set<Character> incorrectSymbols;

    private SimpleWordSession(boolean isCorrectSession, int numberOfAttempts, String word, char[] curWord) {
        this.isCorrectSession = isCorrectSession;
        this.numberOfAttempts = numberOfAttempts;
        this.numberOfUsedAttempts = 0;
        this.word = word;
        this.curWord = curWord;
        this.correctSymbols = new TreeSet<>();
        this.incorrectSymbols = new TreeSet<>();
    }

    public static SimpleWordSession getInstance(int numberOfAttempts, String word) {
        boolean isCorrectSession = isCorrectNumberOfAttempts(numberOfAttempts) && isCorrectWord(word);
        if (!isCorrectSession) {
            return new SimpleWordSession(false, 0, "", new char[0]);
        }

        String upperCaseWord = word.toUpperCase();

        char[] curWord = new char[word.length()];
        Arrays.fill(curWord, '_');

        return new SimpleWordSession(true, numberOfAttempts, upperCaseWord, curWord);
    }

    private static boolean isCorrectNumberOfAttempts(int numberOfAttempts) {
        return numberOfAttempts > 0;
    }

    private static boolean isCorrectWord(String word) {
        if (word == null) {
            return false;
        }

        if (word.isBlank()) {
            return false;
        }

        int n = word.length();
        for (int i = 0; i < n; i++) {
            if (!Character.isLetter(word.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isCorrectSession() {
        return isCorrectSession;
    }

    @Override
    public SessionState getSessionState() {
        return getSessionState(Info.NO_INFO);
    }

    @Override
    public SessionState updateState(char symbol) {
        if (isCorrectSession) {
            return updateStateIfCorrectSession(symbol);
        }

        return getSessionState();
    }

    private SessionState updateStateIfCorrectSession(char symbol) {
        char upperCaseSymbol = toUpperCase(symbol);

        if (correctSymbols.contains(upperCaseSymbol)) {
            return getSessionState(Info.ALREADY_CORRECT);
        }

        if (incorrectSymbols.contains(upperCaseSymbol)) {
            return getSessionState(Info.ALREADY_INCORRECT);
        }

        boolean isCorrectSymbol = false;

        int n = word.length();
        for (int i = 0; i < n; i++) {
            if (upperCaseSymbol == word.charAt(i)) {
                curWord[i] = upperCaseSymbol;
                isCorrectSymbol = true;
            }
        }

        if (isCorrectSymbol) {
            correctSymbols.add(upperCaseSymbol);
            return getSessionState(Info.CORRECT);
        } else {
            incorrectSymbols.add(upperCaseSymbol);
            numberOfUsedAttempts++;
            return getSessionState(Info.INCORRECT);
        }
    }

    private static char toUpperCase(char symbol) {
        return Character.toUpperCase(symbol);
    }

    private SessionState getSessionState(Info info) {
        return new SessionState(
            numberOfAttempts,
            numberOfUsedAttempts,
            word,
            info.getMessage(),
            convert(curWord),
            convert(incorrectSymbols)
        );
    }

    private String convert(char[] array) {
        StringBuilder sb = new StringBuilder();

        for (char symbol : array) {
            sb.append(symbol);
        }

        return sb.toString();
    }

    private <T> String convert(Iterable<T> iterable) {
        StringBuilder sb = new StringBuilder();

        for (T t : iterable) {
            sb.append(t);
        }

        return sb.toString();
    }

    private enum Info {
        NO_INFO(""),
        CORRECT("The symbol is entered correctly."),
        INCORRECT("The symbol is entered incorrectly."),
        ALREADY_CORRECT("This character has already been entered correctly."),
        ALREADY_INCORRECT("This character has already been entered incorrectly.");

        private final String message;

        Info(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
