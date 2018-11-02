package ktm.com.menu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ktm.com.menu.R;
import ktm.com.menu.firebase.ConfiguracaoFirebase;
import ktm.com.menu.fragmentos.ObjetivoFragment;
import ktm.com.menu.fragmentos.PrincipalFragment;
import ktm.com.menu.fragmentos.UploadFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , UploadFragment.OnFragmentInteractionListener{

    //Firebase Authentication dados
    private FirebaseAuth firebaseAuth;
    //Text View Header
    TextView textViewEmail;
    TextView textViewPontos;
    ImageView imageView;
    //Frames Layouts
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recebe o FrameLayout principal ###################
        frameLayout = findViewById(R.id.frame_container);

        //Verifica se o usuário está logado, caso não esteja ele força a tela de login
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       // firebaseUser.getDisplayName()


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Clube do Livro");
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.botao_upload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadFragment uploadFragment = new UploadFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, uploadFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_quem_somos) {
            // Handle actions
            //Chamada do fragment
            ObjetivoFragment objetivoFragment = new ObjetivoFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, objetivoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }else if (id == R.id.nav_meus_livros) {
            Toast.makeText(this,"meus livros",Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_home) {
            //realizando chamada do fragment
            PrincipalFragment principalFragment = new PrincipalFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, principalFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (id == R.id.nav_rank) {
            Toast.makeText(this,"rank",Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_compartilhar) {
            Toast.makeText(this,"Email",Toast.LENGTH_SHORT).show();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String texto = "Olá diga o que quiser!";
            sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }else if (id == R.id.nav_sair) {
            Toast.makeText(this,"Até breve!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            //Botao voltar
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //desconecta o usuario
            FirebaseAuth.getInstance().signOut();
            //redireciona para a pagina de login
            startActivity(new Intent(this,LoginActivity.class));
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Métodos da Interface OnFragmentInteractionListener

    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}