package ktm.com.menu.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ktm.com.menu.firebase.ConfiguracaoFirebase;
import ktm.com.menu.firebase.UsuarioFirebase;
import ktm.com.menu.helper.Base64Custom;

public class Usuario {
    private String uidPessoa;
    private String nome;
    private int pontos;
    private String email;
    private String senha;

    public Usuario(String email,String senha){
        this.email = email;
        this.senha = senha;
    }
    public Usuario() {

    }

    public void salvar(){
        DatabaseReference firebaseRef= ConfiguracaoFirebase.getDatabase();
        DatabaseReference usuario = firebaseRef.child("usuarios").child(getUidPessoa());
        usuario.setValue(this);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUidPessoa() {
        return uidPessoa;
    }

    public void setUidPessoa(String uidPessoa) {
        this.uidPessoa = uidPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
}
