package br.com.jhonicosta.xapp_messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.controller.UsuarioController;
import br.com.jhonicosta.xapp_messenger.model.Mensagem;

public class MensagensAdaptar extends RecyclerView.Adapter<MensagensAdaptar.MyViewHolder> {

    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    private List<Mensagem> mensagens;
    private Context context;
    private Activity activity;

    public MensagensAdaptar(List<Mensagem> lista, Context c, Activity activity) {
        this.mensagens = lista;
        this.context = c;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = null;
        if (i == TIPO_REMETENTE) {
            item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_msg_remetente, viewGroup, false);
        } else if (i == TIPO_DESTINATARIO) {
            item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_msg_destinatario, viewGroup, false);
        }
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensagem();
        String image = mensagem.getImagem();
        if (image != null) {
            Uri url = Uri.parse(image);
            Glide.with(context).load(url).into(myViewHolder.image);

            String nome = mensagem.getNome();
            if (!nome.isEmpty()) {
                myViewHolder.nome.setText(nome);
            } else {
                myViewHolder.nome.setVisibility(View.GONE);
            }
            myViewHolder.mensagem.setVisibility(View.GONE);
        } else {
            myViewHolder.mensagem.setText(msg);
            String nome = mensagem.getNome();
            if (!nome.isEmpty()) {
                myViewHolder.nome.setText(nome);
            } else {
                myViewHolder.nome.setVisibility(View.GONE);
            }
            myViewHolder.image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {

        Mensagem mensagem = mensagens.get(position);
        String idUsuario = new UsuarioController(this.activity).getIdUser();
        if (idUsuario.equals(mensagem.getIdUsuario())) {
            return TIPO_REMETENTE;
        }
        return TIPO_DESTINATARIO;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mensagem, nome;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.nomeExibicao);
            mensagem = itemView.findViewById(R.id.txtMensagem);
            image = itemView.findViewById(R.id.imageMsgFoto);
        }
    }
}
