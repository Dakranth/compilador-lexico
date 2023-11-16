package compila_a_dor.compilador_lexico;

public class Main {
    public static void main(String[] args) {
        AnalisadorLexico analisadorLexico = new AnalisadorLexico("prog.in");

        while (true) {
            try {
                Token token = analisadorLexico.proximoToken();
                System.out.println(token.getTexto() + " - " + token.getId().toString());
            } catch (Exception e) {
                break;
            }
        }
    }
}
