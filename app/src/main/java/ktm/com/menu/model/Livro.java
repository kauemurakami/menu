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
    //@param context used to check the device version and DownloadManager information
    //@return true if the download manager is available

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }

    public void baixar(){
        try{
            //DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
            FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReferenceFromUrl(url);

        final StorageReference storageRef = storage
                .getReferenceFromUrl("gs://the-piraty-book-alpha1.appspot.com/arquivos/")
                .child(nome);

            File storagePath = new File( Environment.getExternalStorageDirectory(),"TPB");
            if(!storagePath.exists()) {
                storagePath.mkdirs();
            }

            File localFile = null;
            localFile = File.createTempFile(nome,"pdf");

            final File finalLocalFile = localFile;
            final FileDownloadTask task = storageRef.getFile(localFile);
            task.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage();
                    Log.e("firebase ",";local tem file created  created " + finalLocalFile.toString());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("firebase ",";local tem file not created  created " +exception.toString());
                }
            });

        }catch (IOException e){

        }
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
