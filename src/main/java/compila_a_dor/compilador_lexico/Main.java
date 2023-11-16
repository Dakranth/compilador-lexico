package compila_a_dor.compilador_lexico;

public class Main {
    public static void main(String[] args) {
        AnalisadorLexico l;
        l = new AnalisadorLexico("prog.in");

        while (true) {
            try {
                Token token = l.nextToken();
                System.out.println(token.getText() + " - " + token.getId().toString());
            } catch (Exception e) {
                break;
            }
        }
    }
}
