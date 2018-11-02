package ktm.com.menu.model;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import ktm.com.menu.firebase.ConfiguracaoFirebase;

public class Livro implements Serializable{
    private Usuario usuario;
    private String nome;
    private String autor;
    private String url;
    private String categoria;
    private String uidLivro;

    public Livro(String nome, String autor, String url,String categoria) {
        this.nome = nome;
        this.autor = autor;
        this.url = url.toString();
        this.categoria = categoria;
    }

    public Livro() {
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getDatabase();
        DatabaseReference livro = firebaseRef.child("arquivos").child(nome);
        //salva no database as informações do arquivo e sua url
        livro.setValue(this);
    }
    //@param context used to check the device version and DownloadManager information
    //@return true if the download manager is available

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
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
