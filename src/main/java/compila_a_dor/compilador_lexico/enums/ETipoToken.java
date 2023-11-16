package compila_a_dor.compilador_lexico.enums;

public enum ETipoToken {
    IDENTIFICADOR,
    NUMERO,
    ERRO,
    OPERADOR,
    PONTUACAO,
    INTEIRO,
    RESERVADO,
    LIFETIME,       // Exemplo de um novo tipo para lifetimes
    ATRIBUTO,       // Exemplo de um novo tipo para atributos
    CHAR_LITERAL,   // Exemplo de um novo tipo para literais de caractere
    CONTINUA;
}
