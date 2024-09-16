package backend.academy.game.session;

import backend.academy.game.dictionary.DictionaryWord;
import backend.academy.game.dictionary.impl.LevelBasedDictionaryWord;

public final class SimpleWordSessionUtils {
    private SimpleWordSessionUtils() {

    }

    public static boolean isCorrectWord(DictionaryWord dictionaryWord) {
        String word = dictionaryWord.word();
        String clue = dictionaryWord.clue();

        if (word == null || clue == null) {
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

    public static char toUpperCase(char symbol) {
        return Character.toUpperCase(symbol);
    }

    public static DictionaryWord toUpperCase(DictionaryWord dictionaryWord) {
        return new LevelBasedDictionaryWord(
            dictionaryWord.word().toUpperCase(),
            dictionaryWord.clue().toUpperCase()
        );
    }

    public static String convert(char[] array) {
        StringBuilder sb = new StringBuilder();

        for (char symbol : array) {
            sb.append(symbol);
        }

        return sb.toString();
    }

    public static <T> String convert(Iterable<T> iterable) {
        StringBuilder sb = new StringBuilder();

        for (T t : iterable) {
            sb.append(t);
        }

        return sb.toString();
    }

    public static boolean isSymbol(String symbol) {
        return symbol.length() == 1 && Character.isLetter(symbol.charAt(0));
    }
}
