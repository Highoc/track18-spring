package ru.track.cypher;

import java.util.*;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

public class Decoder {

    // Расстояние между A-Z -> a-z
    public static final int SYMBOL_DIST = 32;

    private Map<Character, Character> cypher;

    /**
     * Конструктор строит гистограммы открытого домена и зашифрованного домена
     * Сортирует буквы в соответствие с их частотой и создает обратный шифр Map<Character, Character>
     *
     * @param domain - текст, по которому строим гистограмму языка
     */
    public Decoder(@NotNull String domain, @NotNull String encryptedDomain) {
        Map<Character, Integer> domainHist = createHist(domain);
        Map<Character, Integer> encryptedDomainHist = createHist(encryptedDomain);

        cypher = new LinkedHashMap<>();

        Iterator<Entry<Character, Integer>> domainIterator = domainHist.entrySet().iterator();
        Iterator<Entry<Character, Integer>> encryptedDomainIterator = encryptedDomainHist.entrySet().iterator();

        while(domainIterator.hasNext() && encryptedDomainIterator.hasNext()) {
            cypher.put(encryptedDomainIterator.next().getKey(), domainIterator.next().getKey());
        }
    }

    public Map<Character, Character> getCypher() {
        return cypher;
    }

    /**
     * Применяет построенный шифр для расшифровки текста
     *
     * @param encoded зашифрованный текст
     * @return расшифровка
     */
    @NotNull
    public String decode(@NotNull String encoded) {
        StringBuilder stringBuilder = new StringBuilder(encoded.length());

        for (char nowChar : encoded.toCharArray()) {
            if(Character.isAlphabetic(nowChar)){
                stringBuilder.append(getCypher().get(Character.toLowerCase(nowChar)));
            } else {
                stringBuilder.append(nowChar);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Считывает входной текст посимвольно, буквы сохраняет в мапу.
     * Большие буквы приводит к маленьким
     *
     *
     * @param text - входной текст
     * @return - мапа с частотой вхождения каждой буквы (Ключ - буква в нижнем регистре)
     * Мапа отсортирована по частоте. При итерировании на первой позиции наиболее частая буква
     */
    @NotNull
    Map<Character, Integer> createHist(@NotNull String text) {
        Map<Character, Integer> hist = new HashMap<>();

        for (char nowChar : text.toCharArray()) {
            Character lowerCaseChar = Character.toLowerCase(nowChar);
            if(Character.isAlphabetic(nowChar)){
                if (hist.containsKey(lowerCaseChar)) {
                    hist.put(lowerCaseChar, hist.get(lowerCaseChar) + 1);
                } else {
                    hist.put(lowerCaseChar, 1);
                }
            }
        }

        List<Entry<Character, Integer>> entryList = new ArrayList<>(hist.entrySet());
        Collections.sort(entryList, (o1, o2) -> { return o2.getValue() - o1.getValue(); });

        hist = new LinkedHashMap<>();

        for (Entry<Character, Integer> now: entryList) {
            hist.put(now.getKey(), now.getValue());
        }

        return hist;
    }

}
