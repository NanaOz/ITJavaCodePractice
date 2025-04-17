package app.javacode;

import java.lang.reflect.Array;

public class Main {
    public static void main(String[] args) {
        String[] str = {"kdsjUSH", "HyscnkHk", "HKSNosmcl"};
        Filter f = o -> o.toString().toLowerCase();
        Object[] filteredStr = filter(str, f);
        for (Object o : filteredStr) {
            System.out.println(o);
        }

        Integer[] nums = {1, 2, 3, 4, 5};
        Filter f2 = o -> (Integer) o * (Integer) o;
        Integer[] nums2 = filter(nums, f2);
        for (Object o : nums2) {
            System.out.println(o);
        }
    }

    public static <T> T[] filter(T[] array, Filter filter) {
        T[] filtered = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
        for (int i = 0; i < array.length; i++) {
            filtered[i] = (T) filter.apply(array[i]);
        }
        return filtered;
    }
}