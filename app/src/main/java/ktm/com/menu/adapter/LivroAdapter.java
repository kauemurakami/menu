package ktm.com.menu.adapter;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ktm.com.menu.R;
import ktm.com.menu.model.Livro;

public class LivroAdapter extends RecyclerView.Adapter<LivroAdapter.MyViewHolder> {

    private List<Livro> arquivos; //contatos//
    private Context context;

    public LivroAdapter(List<Livro> listaLivros, Context context) {

        this.arquivos = listaLivros;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                                                                //adapter_contatos
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view ,parent ,false);

         MyViewHolder vHolder = new MyViewHolder(itemLista);
        return vHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Livro livro = arquivos.get(position);

        holder.nomeLivro.setText(livro.getNome());
        holder.nomeAutor.setText(livro.getAutor());
        //holder.imageView.setImageResource(R.android/ic_menu_view");

        //recupera uri do arquivo
        //if(livro.get)
    }

    @Override
    public int getItemCount() {

        return arquivos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView nomeLivro,nomeAutor;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewPDF);
            nomeAutor = itemView.findViewById(R.id.autor_livro);
            nomeLivro = itemView.findViewById(R.id.nome_livro);

        }
    }
}
