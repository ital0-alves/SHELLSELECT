package pdv;

public class Validador {
    public static boolean isCPFValido(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int soma = 0, peso = 10;
            for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * peso--;
            int r = 11 - (soma % 11);
            char d1 = (r == 10 || r == 11) ? '0' : (char) (r + '0');

            soma = 0; peso = 11;
            for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * peso--;
            r = 11 - (soma % 11);
            char d2 = (r == 10 || r == 11) ? '0' : (char) (r + '0');

            return (d1 == cpf.charAt(9)) && (d2 == cpf.charAt(10));
        } catch (Exception e) { return false; }
    }
}