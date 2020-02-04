package com.wong.utils;

public class ObjectUtils {

    /**
     * Returns {@code true} if the provided reference is {@code null} otherwise
     * returns {@code false}.
     *
     * @param obj a reference to be checked against {@code null}
     * @return {@code true} if the provided reference is {@code null} otherwise
     * {@code false}
     *
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }
}
