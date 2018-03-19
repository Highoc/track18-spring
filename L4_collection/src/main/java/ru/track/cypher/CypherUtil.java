package ru.track.cypher;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import sun.awt.Symbol;

/**
 * Вспомогательные методы шифрования/дешифрования
 */
public class CypherUtil {

    public static final String SYMBOLS = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Генерирует таблицу подстановки - то есть каждой буква алфавита ставится в соответствие другая буква
     * Не должно быть пересечений (a -> x, b -> x). Маппинг уникальный
     *
     * @return таблицу подстановки шифра
     */
    @NotNull
    public static Map<Character, Character> generateCypher() {

        Map<Character, Character> chypherMap = new HashMap<>();
        List<Character> characterList = new ArrayList<>();

        for (char nowChar: SYMBOLS.toCharArray()) {
            characterList.add(nowChar);
        }

        Collections.shuffle(characterList);

        for (int i = 0; i < SYMBOLS.length(); i++) {
            chypherMap.put(SYMBOLS.charAt(i), characterList.get(i));
        }

        return chypherMap;
    }

}
