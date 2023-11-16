package compila_a_dor.compilador_lexico;

import compila_a_dor.compilador_lexico.enums.ETipoToken;

public class Token {

    private ETipoToken id;
    private String texto;

    public Token() {
        id = ETipoToken.CONTINUA;
        texto = "";
    }

    public Token(ETipoToken tokenParaRetorno, String texto) {
        this.id = tokenParaRetorno;
        this.texto = texto;
    }

    public ETipoToken getId() {
        return this.id;
    }

    public void setId(ETipoToken id) {
        this.id = id;
    }

    public void setTexto(String str) {
        this.texto = str;
    }

    public String getTexto() {
        return this.texto;
    }

    public String toString(){
        return "<"+id+","+texto+">";
    }
}
