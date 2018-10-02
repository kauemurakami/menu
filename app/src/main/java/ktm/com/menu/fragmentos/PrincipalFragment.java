package ktm.com.menu.fragmentos;


import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import ktm.com.menu.R;
import ktm.com.menu.adapter.LivroAdapter;
import ktm.com.menu.firebase.ConfiguracaoFirebase;
import ktm.com.menu.firebase.UsuarioFirebase;
import ktm.com.menu.helper.RecyclerItemClickListener;
import ktm.com.menu.model.Livro;
import ktm.com.menu.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {
    //Recycle view
    private RecyclerView recyclerView;
    //adapter
    private LivroAdapter adapter;
    private ArrayList<Livro> listaLivros = new ArrayList<>();
    //livros para listagem
    private DatabaseReference livrosRef;
    //Variavel do evento de recuperação dos livros
    private ValueEventListener valueEventListener;
    private FloatingActionButton botaoPesquisa;
    private EditText editTextPesquisa;

    //WebView
    WebView webView;

    public PrincipalFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        //Recycle View
        recyclerView = view.findViewById(R.id.recycle_view_livros);

        //livrosl istagem
        livrosRef = ConfiguracaoFirebase.getDatabase().child("arquivos");

        //Configurar Adapter
        adapter = new LivroAdapter(listaLivros,getContext());

        //WebView

        //Configurar Recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        //Criando evento de touch no recicler view
        recyclerView.addOnItemTouchListener(
                //@param1 context @param2 recycler view @param3 onItemclickListener
                new RecyclerItemClickListener(
                        getContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //recpera livro da posição clicada
                                Livro livroSelecionado = listaLivros.get(position);
                                try {
                                    Usuario usuario = new Usuario();
                                    //verifica se o usuario possui pontos suficientes para fazer o download
                                    if(usuario.getPontos() > 0){
                                        Toast.makeText(getContext(),"Baixando "+livroSelecionado.getNome().toString(),Toast.LENGTH_SHORT).show();
                                        //chamada do método de download
                                        downloadFile(livroSelecionado.getUrl());
                                        //retira um ponto do usuario
                                        usuario.setPontos(usuario.getPontos()-1);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        botaoPesquisa = view.findViewById(R.id.botao_pesquisar);
        editTextPesquisa = view.findViewById(R.id.edit_text_pesquisar);

        //Firebase Usuario atual
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();

        botaoPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Pesquisar",Toast.LENGTH_SHORT).show();


            }
        });
        //chama o método responsavel por recuperar e inflar o recycler view
        return view;
    }
    //realiza download do arquivo
    private Long downloadFile(String url) throws IOException {
//Trago a variável url de uma consulta no firebase

        DownloadManager downloadManeger = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        return downloadManeger.enqueue(request);
    }



    @Override
    public void onStart() {
        super.onStart();
        recuperarLivros();
    }

    //Quand não estivermos usando o fragment ele para
    @Override
    public void onStop() {
        super.onStop();
        livrosRef.removeEventListener(valueEventListener);
    }

    public void recuperarLivros() {
        listaLivros.clear();//limpa a lista atual antes inflar a proxima, caso não haja esse método os itens se repetem a cada chamada
        valueEventListener = livrosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //recupera os livros
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Livro livro = dados.getValue(Livro.class);
                    listaLivros.add(livro);
                }
                //notificar que os dados do adapter mudaram
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}