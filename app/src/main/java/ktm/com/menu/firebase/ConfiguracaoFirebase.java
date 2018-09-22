package ktm.com.menu.firebase;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {
    //objetos firebase qe serão utilizados pelo aplicativo
    //Devem ser staticos pois não vamos instânciar novamente, vamos apenas reutilizar
    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;
    private static StorageReference firebaseStorage;
    //Recupera a instancia do storage
    public static StorageReference getFirebaseStorage(){
        if(firebaseStorage == null){
            firebaseStorage = FirebaseStorage.getInstance().getReference();
        }
        return firebaseStorage;
    }

    //Devem ser staticos pois não vamos instânciar a classe, apenas chamar o método
    //Retorna instancia do Firebase (retorna um tipo DatabaseReference)
    public static DatabaseReference getDatabase(){
        if(databaseReference == null){
            //Objeto que nos permite gerenciar o nosso banco de dados @databaseReference
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    //Devem ser staticos pois não vamos instânciar a classe, apenas chamar o método
    //Retorna instância do FirebaseAuth (retorna um tipo FirebaseAuth)
    public static FirebaseAuth getFirebaseAuth(){
        if (firebaseAuth == null){
            //Objeto que nos permitira gerenciar os usuários @firebaseAuth
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    //método ue verifica a conexao com a internet
    public static boolean verificaConexao(Context cont){
        boolean conectado = false;
        ConnectivityManager conmag;
        conmag = (ConnectivityManager)cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        conmag.getActiveNetworkInfo();
        //Verifica o WIFI
        if(conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
            conectado = true;
        }
        //Verifica o 3G
        else if(conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
            conectado = true;
        }
        else{
            conectado = false;
        }
        return conectado;
    }
}
