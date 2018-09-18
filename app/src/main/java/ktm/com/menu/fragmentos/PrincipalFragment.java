package ktm.com.menu.fragmentos;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ktm.com.menu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {

    FloatingActionButton botaoPesquisa;
    EditText editTextPesquisa;

    public PrincipalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        botaoPesquisa = view.findViewById(R.id.botao_pesquisar);
        editTextPesquisa = view.findViewById(R.id.edit_text_pesquisar);

        botaoPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Pesquisar",Toast.LENGTH_SHORT).show();

                PrincipalFragment principalFragment = new PrincipalFragment();
                                                        //getSupportFragmentManagener em Activitys
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, principalFragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
