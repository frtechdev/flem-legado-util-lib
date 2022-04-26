/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.flem.util.helper;

/**
 *
 * @author tscortes
 */
public class CoreUtil {

    /**
     * Caso o primeiro parametro seja nulo, o segundo é retornado
     *
     * @author Thiago Côrtes
     * @param one Object
     * @param other Object
     * @return Object
     */
    public static <T> T isNull(T one, T other) {
        if (one == null) {
            return other;
        }
        return one;
    }

    public static boolean isAnyNull(Object... objs) {
        if (objs == null) {
            return true;
        }
        for (Object obj : objs) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

}
