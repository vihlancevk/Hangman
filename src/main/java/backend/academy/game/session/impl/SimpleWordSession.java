package backend.academy.game.session.impl;

import backend.academy.game.session.Session;
import backend.academy.game.session.SessionState;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import static backend.academy.game.session.SimpleWordSessionUtils.convert;
import static backend.academy.game.session.SimpleWordSessionUtils.isCorrectWord;
import static backend.academy.game.session.SimpleWordSessionUtils.isSymbol;
import static backend.academy.game.session.SimpleWordSessionUtils.periodWithNewLine;
import static backend.academy.game.session.SimpleWordSessionUtils.toUpperCase;

public final class SimpleWordSession implements Session {
    private final boolean isCorrectSession;

    private static final int MAX_ATTEMPTS = 6;
    private final String[] views = new String[] {
        "  -----\n  |   |\n  |\n  |\n  |\n  |\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |\n  |\n  |\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |   |\n  |   |\n  |\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |  -|\n  |   |\n  |\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |  -|-\n  |   |\n  |\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |  -|-\n  |   |\n  |  /\n  |\n  |\n ---",
        "  -----\n  |   |\n  |   0\n  |  -|-\n  |   |\n  |  / \\\n  |\n  |\n ---"
    };
    private int numberOfUsedAttempts;

    private final String targetWord;
    private final char[] wordAsSymbols;

    private final Set<Character> correctSymbols = new TreeSet<>();
    private final Set<Character> incorrectSymbols = new TreeSet<>();

    private final SessionState incorrectSessionState = new SimpleWordSessionState(
        true,
        "Incorrect session!"
    );

    private SimpleWordSession(boolean isCorrectSession, String targetWord, char[] wordAsSymbols) {
        this.isCorrectSession = isCorrectSession;
        this.targetWord = targetWord;
        this.wordAsSymbols = wordAsSymbols;
    }

    public static SimpleWordSession getInstance(String word) {
        if (!isCorrectWord(word)) {
            return new SimpleWordSession(false, "", new char[0]);
        }

        String upperCaseWord = toUpperCase(word);

        char[] curWord = new char[word.length()];
        Arrays.fill(curWord, '_');

        return new SimpleWordSession(true, upperCaseWord, curWord);
    }

    @Override
    public SessionState getSessionState() {
        if (isCorrectSession) {
            return getSessionStateForCorrectSession(UpdateInfo.NO_UPDATE);
        } else {
            return incorrectSessionState;
        }
    }

    private SessionState getSessionStateForCorrectSession(UpdateInfo updateInfo) {
        String wordAsString = convert(wordAsSymbols);
        boolean isFinished = isFinished(wordAsString);
        return new SimpleWordSessionState(
            isFinished,
            generateMessage(isFinished, updateInfo, wordAsString)
        );
    }

    private boolean isFinished(String word) {
        return !hasAttempts() || isGuessed(word);
    }

    private boolean hasAttempts() {
        return numberOfUsedAttempts < MAX_ATTEMPTS;
    }

    private boolean isGuessed(String word) {
        return targetWord.equalsIgnoreCase(word);
    }

    private String generateMessage(boolean isFinished, UpdateInfo updateInfo, String word) {
        StringBuilder message = new StringBuilder();
        message.append(generateBaseMessage(updateInfo, word));
        if (isFinished) {
            message.append('\n');
            message.append(isGuessed(word) ? "Victory!" : generateDefeatMessage());
        }
        return message.toString();
    }

    private String generateBaseMessage(UpdateInfo updateInfo, String word) {
        return generateAttemptsMessage()
            + generateViewMessage()
            + generateUpdateInfoMessage(updateInfo)
            + generateSymbolsMessage(word);
    }

    private String generateAttemptsMessage() {
        return "Maximum attempts: " + MAX_ATTEMPTS + periodWithNewLine()
            + "Number of used attempts: " + numberOfUsedAttempts + periodWithNewLine();
    }

    private String generateViewMessage() {
        return views[numberOfUsedAttempts] + "\n";
    }

    private String generateUpdateInfoMessage(UpdateInfo updateInfo) {
        return updateInfo == UpdateInfo.NO_UPDATE ? "" : updateInfo.getInfo() + "\n";
    }

    private String generateSymbolsMessage(String word) {
        return word + "\n" + convert(incorrectSymbols);
    }

    private String generateDefeatMessage() {
        return "Target word: " + targetWord + periodWithNewLine() + "Defeat!";
    }

    @Override
    public SessionState updateState(String symbol) {
        if (canUpdate()) {
            return updateStateIfCan(symbol);
        }

        return getSessionState();
    }

    private boolean canUpdate() {
        return isCorrectSession && !isFinished(convert(wordAsSymbols));
    }

    private SessionState updateStateIfCan(String symbol) {
        if (!isSymbol(symbol)) {
            return getSessionStateForCorrectSession(UpdateInfo.INCORRECT_INPUT);
        }

        char upperCaseSymbol = toUpperCase(symbol.charAt(0));

        if (correctSymbols.contains(upperCaseSymbol)) {
            return getSessionStateForCorrectSession(UpdateInfo.ALREADY_GUESSED);
        }

        if (incorrectSymbols.contains(upperCaseSymbol)) {
            return getSessionStateForCorrectSession(UpdateInfo.ALREADY_NOT_GUESSED);
        }

        boolean isCorrectSymbol = false;

        int n = targetWord.length();
        for (int i = 0; i < n; i++) {
            if (upperCaseSymbol == targetWord.charAt(i)) {
                wordAsSymbols[i] = upperCaseSymbol;
                isCorrectSymbol = true;
            }
        }

        UpdateInfo updateInfo;
        if (isCorrectSymbol) {
            correctSymbols.add(upperCaseSymbol);
            updateInfo = UpdateInfo.GUESSED;
        } else {
            incorrectSymbols.add(upperCaseSymbol);
            numberOfUsedAttempts++;
            updateInfo = UpdateInfo.NOT_GUESSED;
        }

        return getSessionStateForCorrectSession(updateInfo);
    }

    private enum UpdateInfo {
        NO_UPDATE(""),
        INCORRECT_INPUT("Incorrect input."),
        GUESSED("You guess the letter."),
        NOT_GUESSED("You doesn't guess the letter."),
        ALREADY_GUESSED("This letter already guess."),
        ALREADY_NOT_GUESSED("This letter already doesn't guess.");

        private final String info;

        UpdateInfo(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }
}
