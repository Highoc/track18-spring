package ru.track;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


/**
 * Задание 1: Реализовать два метода
 *
 * Формат файла: текстовый, на каждой его строке есть (или/или)
 * - целое число (int)
 * - текстовая строка
 * - пустая строка (пробелы)
 *
 * Числа складываем, строки соединяем через пробел, пустые строки пропускаем
 *
 *
 * Пример файла - words.txt в корне проекта
 *
 * ******************************************************************************************
 *  Пожалуйста, не меняйте сигнатуры методов! (название, аргументы, возвращаемое значение)
 *
 *  Можно дописывать новый код - вспомогательные методы, конструкторы, поля
 *
 * ******************************************************************************************
 *
 */
public class CountWords {

    String skipWord;

    public CountWords(String skipWord) {
        this.skipWord = skipWord;
    }

//    public static void main(String[] args) throws Exception {
//       countNumbers(new File("L2-objects/words.txt"));
//    }
    /**
     * Метод на вход принимает объект File, изначально сумма = 0
     * Нужно пройти по всем строкам файла, и если в строке стоит целое число,
     * то надо добавить это число к сумме
     * @param file - файл с данными
     * @return - целое число - сумма всех чисел из файла
     */
    public long countNumbers(File file) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        long result = 0;
        String nowLine = "";

        while ((nowLine = bufferedReader.readLine()) != null) {

            try {
                result += Integer.parseInt(nowLine);
            }
            catch (NumberFormatException e) {}
        }

        return result;
    }


    /**
     * Метод на вход принимает объект File, изначально результат= ""
     * Нужно пройти по всем строкам файла, и если в строка не пустая и не число
     * то надо присоединить ее к результату через пробел
     * @param file - файл с данными
     * @return - результирующая строка
     */
    public String concatWords(File file) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String nowLine = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((nowLine = bufferedReader.readLine()) != null) {

            try {
                Integer.parseInt(nowLine);
            }
            catch (NumberFormatException e) {
                if (!skipWord.equals(nowLine)) {
                    stringBuilder.append(nowLine);
                    stringBuilder.append(' ');
                }
            }
        }

        return stringBuilder.toString();
    }

}

