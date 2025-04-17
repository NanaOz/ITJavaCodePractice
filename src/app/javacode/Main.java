package app.javacode;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Integer[] numbers = {1, 2, 1, 2, 3};
        Map<Integer, Integer> map = countOccurrences(numbers);
        System.out.println(map);

    }

    public static <T> Map<T, Integer>  countOccurrences(T[] array){
        Map<T, Integer> map = new HashMap<>();

        for (T t : array) {
            map.merge(t, 1, Integer::sum);
        }

        return map;
    }
}