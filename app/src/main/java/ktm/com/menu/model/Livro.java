package ktm.com.menu.model;

import android.net.Uri;
import android.support.design.widget.TextInputEditText;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import ktm.com.menu.firebase.ConfiguracaoFirebase;

public class Livro {
    private Usuario usuario;
    private String nome;
    private String autor;
    private String url;
    private String uidLivro;

    public Livro(String nome, String autor, String url) {
        this.nome = nome;
        this.autor = autor;
        this.url = url.toString();
    }

    public Livro() {
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getDatabase();
        DatabaseReference livro = firebaseRef.child("arquivos").child(nome);
        //salva no database as informações do arquivo e sua url
        livro.setValue(this);
    }

    public String getUidLivro() {
        return uidLivro;
    }

    public void setUidLivro(String uidLivro) {
        this.uidLivro = uidLivro;
    }
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
