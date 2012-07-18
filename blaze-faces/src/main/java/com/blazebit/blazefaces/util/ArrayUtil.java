/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.util;

public class ArrayUtil {

    private ArrayUtil() {
    }

    public static boolean contains(String[] array, String searchedText) {

        if (array == null || array.length == 0) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i].equalsIgnoreCase(searchedText)) {
                return true;
            }
        }

        return false;
    }

    public static String[] concat(String[] array1, String[] array2) {
        int length1 = array1.length;
        int length2 = array2.length;
        int length = length1 + length2;

        String[] dest = new String[length];

        System.arraycopy(array1, 0, dest, 0, length1);
        System.arraycopy(array2, 0, dest, length1, length2);

        return dest;
    }

    public static String[] concat(String[]... arrays) {
        int destSize = 0;

        for (int i = 0; i < arrays.length; i++) {
            destSize += arrays[i].length;
        }

        String[] dest = new String[destSize];
        int lastIndex = 0;

        for (int i = 0; i < arrays.length; i++) {
            String[] array = arrays[i];
            System.arraycopy(array, 0, dest, lastIndex, array.length);
            lastIndex += array.length;
        }

        return dest;
    }
}
