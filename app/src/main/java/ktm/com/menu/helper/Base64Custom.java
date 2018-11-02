package ktm.com.menu.helper;

import android.util.Base64;

import ktm.com.menu.model.Livro;

public class Base64Custom {
    //Converte email do usuario para a base 64 para ser usada como um Identificador unico
    //Por o email ser unico isso nos da essa opção
    public static String codificarBase64(String texto){
        //classe Base65 android.util @param1 string a ser convertida em bites(email) @param2 Base 64 padrao
        //método repleceAll substitui todos os caracteres e vazios antes e depois por nada
        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    //método para decodificar da base64
    public static String decodificarBase64(String textoCodificado){
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }

}
