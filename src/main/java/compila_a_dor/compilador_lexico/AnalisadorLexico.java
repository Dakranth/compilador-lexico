package compila_a_dor.compilador_lexico;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import compila_a_dor.compilador_lexico.enums.ETipoToken;

public class AnalisadorLexico {

    public static final String[] PALAVRAS_RESERVADAS = {"fn", "let", "mut", "if", "else", "while", "return"};

    private char[] content;
    private int pos;

    public AnalisadorLexico(String filename) {
        try {
            byte[] bContent = Files.readAllBytes(new File(
                    getClass().getClassLoader().getResource(filename).getFile()
            ).toPath());
            this.content = new String(bContent).toCharArray();
            this.pos = 0;
        } catch (IOException ex) {
            System.err.println("Erro ao ler arquivo");
        }
    }

    private boolean eof() {
        return pos == content.length;
    }

    private char nextChar() {
        return content[pos++];
    }

    private boolean isLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '=';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isColon(char c) {
        return c == ':';
    }

    private boolean isPunctuation(char c) {
        return c == ',' || c == '.' || c == '(' || c == ')' || c == ':';
    }

    private void retroceder() {
        pos--;
    }

    public Token nextToken() {
        int s = 0;
        int startingPoint = pos;
        ETipoToken tokenToReturn = ETipoToken.CONTINUA;

        while (tokenToReturn == ETipoToken.CONTINUA) {
            switch (s) {
                case 0:
                    char c = nextChar();
                    if (isColon(c)) {
                        s = 3;
                    } else if (isLetter(c)) {
                        s = 1;
                    } else if (isOperator(c)) {
                        s = 2;
                    } else if (isWhitespace(c)) {
                        s = 0;
                    } else if (isPunctuation(c)) {
                        tokenToReturn = ETipoToken.PONTUACAO;
                    } else if (isDigit(c)) {
                        s = 4;
                    } else {
                        tokenToReturn = ETipoToken.ERRO;
                    }
                    break;
                case 1:
                    c = nextChar();
                    if (isLetter(c) || isDigit(c)) {
                        s = 1;
                    } else if (isWhitespace(c)) {
                        tokenToReturn = ETipoToken.IDENTIFICADOR;
                    } else if (isOperator(c) || isPunctuation(c)) {
                        retroceder();
                        tokenToReturn = ETipoToken.IDENTIFICADOR;
                    } else {
                        tokenToReturn = ETipoToken.ERRO;
                    }
                    break;
                case 2:
                    tokenToReturn = ETipoToken.OPERADOR;
                    break;
                case 3:
                    c = nextChar();
                    if (isOperator(c))
                        tokenToReturn = ETipoToken.OPERADOR;
                    if (isWhitespace(c)) {
                        retroceder();
                        tokenToReturn = ETipoToken.PONTUACAO;
                    } else
                        tokenToReturn = ETipoToken.ERRO;
                    break;
                case 4:
                    c = nextChar();
                    if (isDigit(c)) {
                        s = 4;
                    } else if (isWhitespace(c)) {
                        tokenToReturn = ETipoToken.INTEIRO;
                    } else if (isOperator(c) || isPunctuation(c)) {
                        retroceder();
                        tokenToReturn = ETipoToken.INTEIRO;
                    } else {
                        tokenToReturn = ETipoToken.ERRO;
                    }
                    break;
            }
        }

        if (tokenToReturn == ETipoToken.ERRO) {
            while (true) {
                char c = nextChar();
                if (isWhitespace(c) || isPunctuation(c) || isOperator(c))
                    break;
            }
            retroceder();
        }

        int endingPoint = pos;
        String tokenValue = new String(content);
        tokenValue = tokenValue.substring(startingPoint, endingPoint).trim();

        if (tokenToReturn == ETipoToken.IDENTIFICADOR) {
            if (Arrays.asList(PALAVRAS_RESERVADAS).contains(tokenValue))
                tokenToReturn = ETipoToken.RESERVADO;
        }

        return new Token(tokenToReturn, tokenValue);
    }
}
