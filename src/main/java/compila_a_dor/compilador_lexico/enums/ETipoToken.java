package compila_a_dor.compilador_lexico.enums;

public enum ETipoToken {
    IDENTIFICADOR,
    NUMERO,
    ERRO,
    OPERADOR,
    PONTUACAO,
    INTEIRO,
    RESERVADO,  // Alterado de RESERVED para RESERVADO
    CONTINUA;
}