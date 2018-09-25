package ktm.com.menu.fragmentos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import ktm.com.menu.firebase.ConfiguracaoFirebase;
import ktm.com.menu.R;
import ktm.com.menu.firebase.UsuarioFirebase;
import ktm.com.menu.helper.Base64Custom;
import ktm.com.menu.helper.Upload;
import ktm.com.menu.model.Livro;
import ktm.com.menu.model.Usuario;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment {
    //Arquivo
    private Uri pdfUri;
    private StorageReference storageRef;
    private TextView textView;
    private TextInputEditText nomeArquivo;
    private TextInputEditText nomeAutor;
    private TextInputEditText numeroDePaginas;
    private Spinner categorias;
    private Button botaoProcurarArquivo;
    private Button botaoEnviarArquivo;
    //permissões
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private final int SELECAO_PDF = 234;
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //chamando a classe e o metodo statico responsavel pelas permissoes
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        nomeArquivo = view.findViewById(R.id.edit_nome_arquivo);
        nomeAutor = view.findViewById(R.id.edit_autor_arquivo);
        numeroDePaginas = view.findViewById(R.id.edit_numero_paginas);
        categorias = view.findViewById(R.id.spinner_categorias);
        botaoEnviarArquivo = view.findViewById(R.id.botao_enviarUpload);
        botaoProcurarArquivo = view.findViewById(R.id.busca_arquivo);

        storageRef = ConfiguracaoFirebase.getFirebaseStorage();
        textView = view.findViewById(R.id.textView);

        //Botão que procura por um arquivo no celualr
        botaoProcurarArquivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPDF();
                } else {
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        botaoEnviarArquivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfUri != null) {
                    uploadFile();
                    textView.setText(pdfUri.toString());
                } else
                    Toast.makeText(getContext(), "Selecione um arquivo", Toast.LENGTH_SHORT).show();
                textView.setText(pdfUri.toString());
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //checka se o usuario selecionou um arquivo ou nao
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();//retorna o uid do pdf
            textView.setText(pdfUri.toString());
        } else
            Toast.makeText(getContext(), "Por favor,selecione um arquivo", Toast.LENGTH_SHORT).show();
    }

    //Tratando negação das permissoes
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPDF();
        } else
            Toast.makeText(getContext(), "Por favor, permita o acesso", Toast.LENGTH_SHORT).show();
    }

    //metodo para fazer o upload
    private void uploadFile(){
        final String nomeAutorArquivo = nomeAutor.getText().toString();
        final String fileName = nomeArquivo.getText().toString();

        Toast.makeText(getContext(), "Enviado", Toast.LENGTH_SHORT).show();
        Uri file = Uri.fromFile(new File(pdfUri.toString()));
        final StorageReference riversRefTTT = storageRef
                .child("arquivos").child(fileName);//nós referencia
        UploadTask uploadTask1 = riversRefTTT.putFile(pdfUri);
        uploadTask1.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Falha ao enviar!",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getContext(),"Enviado!",Toast.LENGTH_SHORT).show();
            }
        });

        Task<Uri> urlTask = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return riversRefTTT.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUrl = task.getResult();
                    Livro livro = new Livro(fileName,nomeAutorArquivo,downloadUrl.toString());
                    livro.salvar();
                }else Log.d("erro","a");
            }
        });
    }


    //metodo para selecionar o arquivo
    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);//pegando arquivos

        startActivityForResult(intent, 86);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
