
package utilitarios;


public class Biblioteca {

    public static boolean validaCpfCnpj(String s_aux) {
        switch (s_aux.length()) {
            case 11:
                int d1, d2;
                int digito1, digito2, resto;
                int digitoCPF;
                String nDigResult;
                d1 = d2 = 0;
                digito1 = digito2 = resto = 0;
                for (int n_Count = 1; n_Count < s_aux.length() - 1; n_Count++) {
                    digitoCPF = Integer.valueOf(s_aux.substring(n_Count - 1, n_Count)).intValue();
                    d1 = d1 + (11 - n_Count) * digitoCPF;
                    d2 = d2 + (12 - n_Count) * digitoCPF;
                }   resto = (d1 % 11);
                if (resto < 2) {
                    digito1 = 0;
                } else {
                    digito1 = 11 - resto;
                }   d2 += 2 * digito1;
                resto = (d2 % 11);
                if (resto < 2) {
                    digito2 = 0;
                } else {
                    digito2 = 11 - resto;
                }   String nDigVerific = s_aux.substring(s_aux.length() - 2, s_aux.length());
                nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
                return nDigVerific.equals(nDigResult);
            case 14:
                int soma = 0, aux, dig;
                String cnpj_calc = s_aux.substring(0, 12);
                char[] chr_cnpj = s_aux.toCharArray();
                for (int i = 0; i < 4; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
                    }
                }   for (int i = 0; i < 8; i++) {
                    if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                        soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
                    }
                }   dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11)
                        ? "0" : Integer.toString(dig);
                soma = 0;
                for (int i = 0; i < 5; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
                    }
                }   for (int i = 0; i < 8; i++) {
                    if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                        soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
                    }
                }   dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11)
                        ? "0" : Integer.toString(dig);
                return s_aux.equals(cnpj_calc);
            default:
                return false;
        }
    }


}