package compila_a_dor.compilador_lexico;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import compila_a_dor.compilador_lexico.enums.ETipoToken;

public class AnalisadorLexico {

    public static final String[] PALAVRAS_RESERVADAS_RUST = {
        "fn", "let", "mut", "if", "else", "while", "return",
        "match", "enum", "struct", "impl", "use", "mod"
    };

    private char[] conteudo;
    private int posicao;

    public AnalisadorLexico(String nomeArquivo) {
        try {
            byte[] bConteudo = Files.readAllBytes(new File(
                    getClass().getClassLoader().getResource(nomeArquivo).getFile()
            ).toPath());
            this.conteudo = new String(bConteudo).toCharArray();
            this.posicao = 0;
        } catch (IOException ex) {
            System.err.println("Erro ao ler arquivo");
        }
    }

    private boolean fimArquivo() {
        return posicao == conteudo.length;
    }

    private char proximoCaractere() {
        return conteudo[posicao++];
    }

    private boolean ehLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean ehEspacoEmBranco(char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }

    private boolean ehOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '=';
    }

    private boolean ehDigito(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean ehDoisPontos(char c) {
        return c == ':';
    }

    private boolean ehPontuacao(char c) {
        return c == ',' || c == '.' || c == '(' || c == ')' || c == ':' || c == ';' || c == '!' || c == '{' || c == '}';
    }

    private void retroceder() {
        posicao--;
    }

    private boolean isCaractereEspecial(char c) {
        return c == '!' || c == '\"';
    }

    public Token proximoToken() {
        int s = 0;
        int pontoInicial = posicao;
        ETipoToken tokenParaRetorno = ETipoToken.CONTINUA;
        String valorToken = "";

        while (tokenParaRetorno == ETipoToken.CONTINUA) {
            switch (s) {
                case 0:
                    char c = proximoCaractere();
                    if (ehDoisPontos(c)) {
                        s = 3;
                    } else if (ehLetra(c)) {
                        s = 1;
                    } else if (ehOperador(c)) {
                        s = 2;
                    } else if (ehEspacoEmBranco(c)) {
                        s = 0;
                    } else if (ehPontuacao(c)) {
                        tokenParaRetorno = ETipoToken.PONTUACAO;
                    } else if (ehDigito(c)) {
                        s = 4;
                    } else {
                        tokenParaRetorno = ETipoToken.ERRO;
                    }
                    break;
                case 1:
                    c = proximoCaractere();
                    if (ehLetra(c) || ehDigito(c)) {
                        s = 1;
                    } else if (ehEspacoEmBranco(c)) {
                        tokenParaRetorno = ETipoToken.IDENTIFICADOR;
                    } else if (ehOperador(c) || ehPontuacao(c)) {
                        retroceder();
                        tokenParaRetorno = ETipoToken.IDENTIFICADOR;
                    } else {
                        tokenParaRetorno = ETipoToken.ERRO;
                    }
                    break;
                case 2:
                    tokenParaRetorno = ETipoToken.OPERADOR;
                    break;
                case 3:
                    c = proximoCaractere();
                    if (ehOperador(c))
                        tokenParaRetorno = ETipoToken.OPERADOR;
                    if (ehEspacoEmBranco(c)) {
                        retroceder();
                        tokenParaRetorno = ETipoToken.PONTUACAO;
                    } else if (isCaractereEspecial(c)) {
                        tokenParaRetorno = ETipoToken.PONTUACAO;
                        StringBuilder strBuilder = new StringBuilder();
                        strBuilder.append(c);
                        while (isCaractereEspecial(c)) {
                            strBuilder.append(c);
                            c = proximoCaractere();
                        }
                        valorToken = strBuilder.toString();
                        retroceder();
                    } else
                        tokenParaRetorno = ETipoToken.ERRO;
                    break;
                case 4:
                    c = proximoCaractere();
                    if (ehDigito(c)) {
                        s = 4;
                    } else if (ehEspacoEmBranco(c)) {
                        tokenParaRetorno = ETipoToken.INTEIRO;
                    } else if (ehOperador(c) || ehPontuacao(c)) {
                        retroceder();
                        tokenParaRetorno = ETipoToken.INTEIRO;
                    } else {
                        tokenParaRetorno = ETipoToken.ERRO;
                    }
                    break;
                case 5:
                    c = proximoCaractere();
                    if (ehEspacoEmBranco(c)) {
                        tokenParaRetorno = ETipoToken.PONTUACAO;
                    } else {
                        tokenParaRetorno = ETipoToken.ERRO;
                    }
                    break;
            }
        }

        if (tokenParaRetorno == ETipoToken.ERRO) {
            while (true) {
                char c = proximoCaractere();
                if (ehEspacoEmBranco(c) || ehPontuacao(c) || ehOperador(c))
                    break;
            }
            retroceder();
        }

        int pontoFinal = posicao;
        valorToken = new String(conteudo);
        valorToken = valorToken.substring(pontoInicial, pontoFinal).trim();

        if (tokenParaRetorno == ETipoToken.IDENTIFICADOR) {
            if (Arrays.asList(PALAVRAS_RESERVADAS_RUST).contains(valorToken))
                tokenParaRetorno = ETipoToken.RESERVADO;
        }

        return new Token(tokenParaRetorno, valorToken);
    }
}
