package compila_a_dor.compilador_sintatico;

import compila_a_dor.compilador_lexico.AnalisadorLexico;
import compila_a_dor.compilador_lexico.Token;
import compila_a_dor.compilador_lexico.enums.ETipoToken;

public class AnalisadorSintatico {
    private AnalisadorLexico lexico;
    private Token tokenAtual;

    public AnalisadorSintatico(String nomeArquivo) {
        this.lexico = new AnalisadorLexico(nomeArquivo);
        this.tokenAtual = lexico.proximoToken();
    }

    // Método para consumir o próximo token
    private void consumirToken() {
        this.tokenAtual = lexico.proximoToken();
    }

    // Método de erro sintático
    private void erroSintatico(String mensagem) {
        System.err.println("Erro Sintático: " + mensagem);
        System.exit(1);
    }

    // Método para análise sintática do programa
    public void analisarPrograma() {
        while (tokenAtual.getId() != ETipoToken.CONTINUA) {
            analisarFuncao();
        }
    }

    // Método para análise sintática de uma função
    private void analisarFuncao() {
        // Espera-se encontrar "fn"
        if (tokenAtual.getId() == ETipoToken.RESERVADO && tokenAtual.getTexto().equals("fn")) {
            consumirToken(); // Consumir "fn"
            
            // Espera-se um identificador
            if (tokenAtual.getId() == ETipoToken.IDENTIFICADOR) {
                consumirToken(); // Consumir o identificador
                
                // Espera-se "("
                if (tokenAtual.getId() == ETipoToken.PONTUACAO && tokenAtual.getTexto().equals("(")) {
                    consumirToken(); // Consumir "("
                    
                    // Aqui você analisaria os parâmetros da função

                    // Espera-se ")"
                    if (tokenAtual.getId() == ETipoToken.PONTUACAO && tokenAtual.getTexto().equals(")")) {
                        consumirToken(); // Consumir ")"
                        
                        // Aqui você analisaria o corpo da função
                    } else {
                        erroSintatico("Esperado ')' após os parâmetros da função.");
                    }
                } else {
                    erroSintatico("Esperado '(' após o identificador da função.");
                }
            } else {
                erroSintatico("Esperado um identificador após 'fn'.");
            }
        } else {
            erroSintatico("Esperado 'fn' para iniciar uma função.");
        }
    }

    public static void main(String[] args) {
        AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico("prog.in");
        analisadorSintatico.analisarPrograma();
        System.out.println("Análise sintática concluída com sucesso.");
    }
}
