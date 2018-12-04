package com.becasipn.util;

/**
 * Clase con la funcionalidad de generar password aleatorio
 *
 * @author Patricia Benitezárez Parra <joseluis.juarez@fit.com.mx>
 * @version $Rev: 1169 $
 * @since 1.0
 */
public class PasswordGenerator {

    /**
     * Elige un caracter aleatorio de una cadena
     *
     * @param str Cadena de donde se obtiene el caracter
     * @return String con un caracter aleatorio
     */
    private static char randomChar(String str) {
        int i = (int) (str.length() * Math.random());
        if (i < 0) {
            i = 0;
        } else if (i >= str.length()) {
            i = str.length() - 1;
        }
        return str.charAt(i);
    }

    /**
     * Metodo estatico que genera un password aleatorio de 8 caracteres con la
     * combinacion de numeros, letras mayusculas y minusculas
     *
     * @return String Password aleatorio
     */
    public static String generatePassword() {
        String alpha = "ABCDEFGHIJKLMNOPQPRSTUVWXYZ";
        String num = "0123456789";
        String pass = "" + randomChar(alpha);
        pass += randomChar(num);
        pass += ("" + randomChar(alpha)).toLowerCase();
        pass += randomChar(num);
        pass += randomChar(alpha);
        pass += randomChar(num);
        pass += ("" + randomChar(alpha)).toLowerCase();
        pass += randomChar(num);
        return pass;
    }

}
