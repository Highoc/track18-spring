package ru.track.cypher;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

/**
 * Класс умеет кодировать сообщение используя шифр
 */
public class Encoder {

    /**
     * Метод шифрует символы текста в соответствие с таблицей
     * NOTE: Текст преводится в lower case!
     *
     * Если таблица: {a -> x, b -> y}
     * то текст aB -> xy, AB -> xy, ab -> xy
     *
     * @param cypherTable - таблица подстановки
     * @param text - исходный текст
     * @return зашифрованный текст
     */
    public String encode(@NotNull Map<Character, Character> cypherTable, @NotNull String text) {
        StringBuilder stringBuilder = new StringBuilder(text.length());

        for (char nowChar : text.toCharArray()) {
            if(Character.isAlphabetic(nowChar)){
                stringBuilder.append(cypherTable.get(Character.toLowerCase(nowChar)));
            } else {
                stringBuilder.append(nowChar);
            }
        }

        return stringBuilder.toString();
    }
}
