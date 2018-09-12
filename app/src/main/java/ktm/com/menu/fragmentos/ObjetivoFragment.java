package ktm.com.menu.fragmentos;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ktm.com.menu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObjetivoFragment extends Fragment {


    public ObjetivoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_objetivo, container, false);
    }

}
