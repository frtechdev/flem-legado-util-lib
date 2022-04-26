package br.org.flem.util.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

	private EmailUtil() {
		throw new IllegalStateException("Utility class");
	}
	
	
	/**
	 * Verifica se o e-mail está em formato valido
	 * @param email
	 * @return
	 */
	public static boolean isValid(String email) {
		boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
	}
}
