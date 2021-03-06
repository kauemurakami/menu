package ktm.com.menu.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import ktm.com.menu.helper.Base64Custom;
import ktm.com.menu.model.Usuario;

public class UsuarioFirebase {

    public static String getIdentificardorUsuário() {

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAuth();
        String email = usuario.getCurrentUser().getEmail();
        String identificardorUsuario = Base64Custom.codificarBase64(email);

        return identificardorUsuario;
    }

    //Recupera o Usuario Atual
    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAuth();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarPontosUsuario(int ponto){
        FirebaseUser user = getUsuarioAtual();
        Usuario usuario = new Usuario();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(usuario.getEmail())
                .build()
                ;
        return true;
    }

    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser firebaseUser = getUsuarioAtual();
        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());
        //if(firebaseUser.getPhotoUrl() != null)
        //foto usuario
        //usuario.setFoto(firebaseUser.getPhotoUrl().toString());
        //else usuario.setFoto(foto padrao)
        return usuario;
    }
}



