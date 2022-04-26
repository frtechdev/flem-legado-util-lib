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
public class NumberUtil {

    /**
     * Verifica se uma String e um Numero.
     *
     * @param str
     * @return boolean true se for Numero
     */
    public static boolean isNumber(String str) {
        try {
            for (int i = 0; i < str.length(); ++i) {
                Integer.parseInt(str.substring(i, i + 1));
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
