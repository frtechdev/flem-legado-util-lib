package br.org.flem.util.helper;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

/**
 *
 * @author tscortes
 */
public class StringUtil {

    private static final Logger LOGGER = Logger.getLogger(StringUtil.class);

    private static final String MASK_CPF = "^\\d{11}$";
    private static final String MASK_FORMATED_CPF = "^\\d{3}.\\d{3}.\\d{3}-\\d{2}$";
    private static final String MASK_CNPJ = "^\\d{14}$";
    private static final String MASK_FORMATED_CNPJ = "^\\d{2}.\\d{3}.\\d{3}/\\d{4}-\\d{2}$";

    /**
     * Método verifica se o CPF passado por parâmetro é válido
     *
     * @param cpf
     * @return
     */
    public static Boolean validarCPF(String cpf) {
        try {
            if (!GenericValidator.isBlankOrNull(cpf)) {
                if (!GenericValidator.matchRegexp(cpf, MASK_CPF)
                        && !GenericValidator.matchRegexp(cpf, MASK_FORMATED_CPF)) {

                    return false;
                }
                int d1, d2;
                int digito1, digito2, resto;
                int digitoCPF;
                String nDigResult;

                String unformatedCpf = removeFormatacao(cpf);

                if (isSameCharacter(unformatedCpf)) {
                    return false;
                }

                d1 = d2 = 0;
                digito1 = digito2 = resto = 0;

                for (int nCount = 1; nCount < unformatedCpf.length() - 1; nCount++) {
                    digitoCPF = Integer.valueOf(unformatedCpf.substring(nCount - 1, nCount))
                            .intValue();
                    // multiplique a ultima casa por 2 a seguinte por 3 a
                    // seguinte por 4
                    // e assim por diante.
                    d1 = d1 + (11 - nCount) * digitoCPF;

                    // para o segundo digito repita o procedimento incluindo
                    // o primeiro
                    // digito calculado no passo anterior.
                    d2 = d2 + (12 - nCount) * digitoCPF;
                }

                // Primeiro resto da divisÃ£o por 11.
                resto = (d1 % 11);

                // Se o resultado for 0 ou 1 o digito Ã© 0 caso contrÃ¡rio o
                // digito Ã© 11
                // menos o resultado anterior.
                if (resto < 2) {
                    digito1 = 0;
                } else {
                    digito1 = 11 - resto;
                }
                d2 += 2 * digito1;
                // Segundo resto da divisÃ£o por 11.
                resto = (d2 % 11);

                // Se o resultado for 0 ou 1 o digito Ã© 0 caso contrÃ¡rio o
                // digito Ã© 11
                // menos o resultado anterior.
                if (resto < 2) {
                    digito2 = 0;
                } else {
                    digito2 = 11 - resto;
                }
                // Digito verificador do CPF que estÃ¡ sendo validado.
                String nDigVerific = unformatedCpf.substring(unformatedCpf
                        .length() - 2, unformatedCpf.length());
                // Concatenando o primeiro resto com o segundo.
                nDigResult = String.valueOf(digito1)
                        + String.valueOf(digito2);
                // comparar o digito verificador do cpf com o primeiro resto
                // + o segundo
                // resto.
                return nDigVerific.equals(nDigResult);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("Erro na validação do CPF: " + cpf, e);

        }
        return false;
    }

    /**
     * Método verifica se o CNPJ passado por parâmetro é válido
     *
     * @param cnpj
     * @return
     */
    public static Boolean validarCNPJ(String cnpj) {
        try {
            if (!GenericValidator.isBlankOrNull(cnpj)) {
                if (!GenericValidator.matchRegexp(cnpj,
                        MASK_CNPJ)
                        && !GenericValidator.matchRegexp(cnpj,
                                MASK_FORMATED_CNPJ)) {
                    return false;
                }
                int soma = 0, dig;

                String unformatedCnpj = removeFormatacao(cnpj);

                if (isSameCharacter(unformatedCnpj)) {
                    return false;
                }

                String cnpj_calc = unformatedCnpj.substring(0, 12);

                char[] chr_cnpj = unformatedCnpj.toCharArray();

                /* Primeira parte */
                for (int i = 0; i < 4; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
                    }
                }
                for (int i = 0; i < 8; i++) {
                    if (chr_cnpj[i + 4] - 48 >= 0
                            && chr_cnpj[i + 4] - 48 <= 9) {
                        soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);

                cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer
                        .toString(dig);

                /* Segunda parte */
                soma = 0;
                for (int i = 0; i < 5; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
                    }
                }
                for (int i = 0; i < 8; i++) {
                    if (chr_cnpj[i + 5] - 48 >= 0
                            && chr_cnpj[i + 5] - 48 <= 9) {
                        soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer
                        .toString(dig);

                return unformatedCnpj.equals(cnpj_calc);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("Erro na validação do CNPJ: " + cnpj, e);
        }
        return false;
    }

    /**
     * Método utilizado para remover a formatação de Strings contendo números.
     * Retira pontos, barras, traços e espaços em branco.
     *
     * @param s string a ser analisada.
     * @return apeas os números da string informada.
     */
    private static String removeFormatacao(String s) {
        if (s != null) {
            s = s.replace(".", "");
            s = s.replace("/", "");
            s = s.replace("-", "");
            s = s.replace(" ", "");
        }
        return s;
    }

    /**
     * Método utilizado para verificar se uma determinada String é composta
     * sempre do mesmo caracter. Ex: "SSSSSS" - true "SSSXSS" - false
     *
     * @param s a ser analisada.
     * @return true - caso a String seja composta sempre do mesmo caracter.
     * false - caso contrário
     */
    public static boolean isSameCharacter(String s) {
        if (s != null && s.length() > 0) {
            char first = s.charAt(0);

            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != first) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static String removerAcentosECaracteresEspecias(String texto) {
        texto = texto.replaceAll("[áàâäãªÁÀÄÃÂ]", "A");
        texto = texto.replaceAll("[èéêëÈÉÊË]", "E");
        texto = texto.replaceAll("[íìïîÍÌÏÎ]", "I");
        texto = texto.replaceAll("óòöôÓÒÖÔº", "O");
        texto = texto.replaceAll("[úùûüÚÙÛÜ]", "U");
        texto = texto.replaceAll("[çÇ]", "C");
        return texto;
    }

    /**
     * Recebe um CNPJ e o retorna com mï¿½scara correta.
     *
     * @param cnpj
     * @return String CNPJ mascarada
     */
    public static String formataCNPJ(String cnpj) {
        if (cnpj == null) {
            return null;
        }
        if (cnpj.length() == 18) {
            return cnpj;
        }
        if (cnpj.length() != 18 && !NumberUtil.isNumber(cnpj)) {
            return "";
        }
        if (cnpj.length() > 18 || cnpj.length() < 13) {
            return "";
        }
        if (cnpj.length() == 13) {
            return "0" + cnpj.substring(0, 1) + "." + cnpj.substring(1, 4) + "." + cnpj.substring(4, 7) + "/" + cnpj.substring(7, 11) + "-" + cnpj.substring(11);
        }
        if (cnpj.length() == 14) {
            return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
        }
        return "";
    }

    /**
     * Recebe somente os numeros de um CPF e o retorna com a mascara correta.
     *
     * @param strCPF
     * @return String CPF mascarado
     */
    public static String formataCPF(String strCPF) {
        if (strCPF == null) {
            return null;
        }
        if (strCPF.length() == 14) {
            return strCPF;
        }
        if (strCPF.length() != 14 && !NumberUtil.isNumber(strCPF)) {
            return "";
        }
        if (strCPF.length() > 14 || strCPF.length() < 10) {
            return "";
        }
        if (strCPF.length() == 10) {
            return "0" + strCPF.substring(0, 2) + "." + strCPF.substring(2, 5) + "." + strCPF.substring(6, 8) + "-" + strCPF.substring(8);
        }
        if (strCPF.length() == 11) {
            return strCPF.substring(0, 3) + "." + strCPF.substring(3, 6) + "." + strCPF.substring(6, 9) + "-" + strCPF.substring(9);
        }
        return "";
    }

}
