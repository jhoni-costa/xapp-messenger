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
import br.com.jhonicosta.xapp_messenger.model.Conversa;
import br.com.jhonicosta.xapp_messenger.model.Grupo;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;

    public ConversasAdapter(List<Conversa> conversas, Context context) {
        this.conversas = conversas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_conversas, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    public List<Conversa> getConversas() {
        return conversas;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Conversa conversa = conversas.get(i);
        if (conversa.getIsGroup().equals("true")) {
            myViewHolder.nome.setText(conversa.getGrupo().getNome());
        } else {
            myViewHolder.nome.setText(conversa.getUsuarioExibicao().getNome());
        }
        myViewHolder.ultima.setText(conversa.getUltimaMensagem());

        if (conversa.getIsGroup().equals("true")) {
            Grupo grupo = conversa.getGrupo();
            myViewHolder.nome.setText(grupo.getNome());
            if (grupo.getFoto() != null) {
                Glide.with(context).load(Uri.parse(grupo.getFoto())).into(myViewHolder.foto);
            } else {
                myViewHolder.foto.setImageResource(R.drawable.padrao);
            }
        } else {
            if (conversa.getUsuarioExibicao().getFotoUsuario() != null) {
                Glide.with(context).load(Uri.parse(conversa.getUsuarioExibicao().getFotoUsuario())).into(myViewHolder.foto);
            } else {
                myViewHolder.foto.setImageResource(R.drawable.padrao);
            }
        }


    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome, ultima;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.adapter_foto_conversas);
            nome = itemView.findViewById(R.id.adapter_nome_conversas);
            ultima = itemView.findViewById(R.id.adapter_ultima_conversas);
        }
    }
}
