package br.com.jhonicosta.xapp_messenger.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class GrupoSelecionadoAdapter extends RecyclerView.Adapter<GrupoSelecionadoAdapter.MyViewHolder> {

    private List<Usuario> contatos;
    private Context context;

    public GrupoSelecionadoAdapter(List<Usuario> list, Context context) {
        this.contatos = list;
        this.context = context;
    }

    @NonNull
    @Override
    public GrupoSelecionadoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_grupo_selecionado, viewGroup, false);

        return new GrupoSelecionadoAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoSelecionadoAdapter.MyViewHolder myViewHolder, int i) {
        Usuario usuario = contatos.get(i);

        myViewHolder.nome.setText(usuario.getNome());

        if (usuario.getFotoUsuario() != null) {
            Glide.with(context).load(Uri.parse(usuario.getFotoUsuario())).into(myViewHolder.foto);
        } else {
            myViewHolder.foto.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.foto_membro_selecionado);
            nome = itemView.findViewById(R.id.nome_membro_selecionado);

        }
    }
}
