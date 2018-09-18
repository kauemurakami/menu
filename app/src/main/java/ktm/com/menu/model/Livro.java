package ktm.com.menu.model;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import ktm.com.menu.firebase.ConfiguracaoFirebase;

public class Livro {
    private Usuario usuario;
    private String uid;
    private String nome;
    private String autor;
    private String url;


    public Livro(String nome, String autor,String url) {
        this.nome = nome;
        this.autor = autor;
        this.url = url;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public Livro(){

    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "Livro{" +
                "nome='" + nome + '\'' +
                ", autor='" + autor + '\'' +
                ", linkLivro='" + url + '\'' +
                ", usuario="+ usuario +
                '}';
    }
}
