package ktm.com.menu.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import ktm.com.menu.R;
import ktm.com.menu.firebase.ConfiguracaoFirebase;
import ktm.com.menu.fragmentos.PrincipalFragment;
import ktm.com.menu.fragmentos.UploadFragment;
import ktm.com.menu.helper.Base64Custom;
import ktm.com.menu.model.Usuario;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static ktm.com.menu.firebase.ConfiguracaoFirebase.verificaConexao;

public class LoginActivity extends FragmentActivity {

    private TextInputEditText textEmail ,textSenha;
    private SignInButton gmailLogin;
    private Button botaoEntrar,botaoRegistrar;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //campos
        botaoEntrar = findViewById(R.id.btn_entrar);
        botaoRegistrar = findViewById(R.id.btn_registrar);
        textEmail = findViewById(R.id.emailLogin);
        textSenha = findViewById(R.id.passwordLogin);
        gmailLogin = findViewById(R.id.login_button_gmail);
        //firebase
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        //#############login com gmail
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        };
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"Erro",Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        gmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        //########end login com gmail
    }

    //##############login gmail
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else{

            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Autenticação falhou",Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(getApplicationContext(),"Autenticado",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //################end login gmail


    //Recebe um Objeto usuario como parametro para efetuar o cadastro
    public void cadastrarUsuario(final Usuario usuario){
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        mAuth.createUserWithEmailAndPassword(
            usuario.getEmail(),usuario.getSenha()
                ).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    //Verifica exceções e se o usuário se cadastrou
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Usuário cadastrado com sucesso!",Toast.LENGTH_SHORT).show();
                    finish();
                    //Convertendo email em base 64 para ser usado como uidPessoa
                    try {
                        String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                        usuario.setUidPessoa(identificadorUsuario);
                        usuario.salvar();
                        abrirTelaPrincipal();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {String excecao = "";
                    try{
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor, digite um e-mail válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "esta conta já está cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                 Toast.makeText(getApplicationContext(),excecao,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Método que válida se os campos estão vazios e chama o método cadastrarUsuario
    public void validarCadastroUsuario(View view) {
        //verifica conexao
        if (verificaConexao(this)) {
            //Recuperando os textos dos campos
            String email = textEmail.getText().toString();
            String senha = textSenha.getText().toString();
            if (!textEmail.getText().toString().isEmpty()) {
                if (!textSenha.getText().toString().isEmpty()) {
                    //Instnacia um novo usuario
                    Usuario usuario = new Usuario(email,senha);
                    //Chama a função cadastrarUsuario enviando comoargumento um Objeto @usuario
                    cadastrarUsuario(usuario);
                    logarUsuario(usuario);
                } else
                    Toast.makeText(getApplicationContext(), "Digite sua senha", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Digite seu email", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getApplicationContext(),"Sem conexão",Toast.LENGTH_SHORT).show();
    }


    //Método para validar a autenticação usuário
    public void validarAutenticacaoUsuario(View view){
        if (verificaConexao(this)) {
            //Recuperando os textos dos campos
            String email = textEmail.getText().toString();
            String senha = textSenha.getText().toString();
            if (!textEmail.getText().toString().isEmpty()) {
                if (!textSenha.getText().toString().isEmpty()) {
                    //configurando objeto no construtor
                    Usuario usuario = new Usuario(email,senha);
                    //cahama o método logarUsuario
                    logarUsuario(usuario);
                } else
                    Toast.makeText(getApplicationContext(), "Digite sua senha", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Digite seu email", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getApplicationContext(),"Sem conexão",Toast.LENGTH_SHORT).show();
    }
    //Método para realizar o login do usuario
    public void logarUsuario(Usuario usuario) {
        mAuth.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                }else {
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não está cadastrado!";
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Email e/ou senha errados!";
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"Erro ao Logar!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Função pra abrir a tela inicial
    public void abrirTelaPrincipal(){

        PrincipalFragment principalFragment = new PrincipalFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, principalFragment);
    }
}