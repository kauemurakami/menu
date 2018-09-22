package ktm.com.menu.fragmentos;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ktm.com.menu.R;
import ktm.com.menu.adapter.LivroAdapter;
import ktm.com.menu.firebase.ConfiguracaoFirebase;
import ktm.com.menu.firebase.UsuarioFirebase;
import ktm.com.menu.model.Livro;

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

    public PrincipalFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        //Recycle View
        recyclerView = view.findViewById(R.id.recycle_view_livros);

        //livrosl istagem
        livrosRef = ConfiguracaoFirebase.getDatabase().child("arquivos");

        //Configurar Adapter
        adapter = new LivroAdapter(listaLivros,getContext());

        //Configurar Recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        //Criando evento de touch no recicler view

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