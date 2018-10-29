package br.com.jhonicosta.xapp_messenger.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.adapter.MensagensAdaptar;
import br.com.jhonicosta.xapp_messenger.controller.MensagemController;
import br.com.jhonicosta.xapp_messenger.controller.UsuarioController;
import br.com.jhonicosta.xapp_messenger.helper.Base64Helper;
import br.com.jhonicosta.xapp_messenger.model.Mensagem;
import br.com.jhonicosta.xapp_messenger.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageFoto;
    private EditText editMensagem;
    private RecyclerView recyclerView;

    private Usuario usuario;

    private UsuarioController controllerUser;
    private MensagemController controllerMsg;
    private String idRemetente;

    private MensagensAdaptar adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        controllerUser = new UsuarioController(this);
        controllerMsg = new MensagemController();

        idRemetente = controllerUser.getIdUser();

        textViewNome = findViewById(R.id.chat_nome);
        circleImageFoto = findViewById(R.id.chat_user_foto);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerView = findViewById(R.id.reciclerMensagem);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuario = (Usuario) bundle.getSerializable("chatContato");
            textViewNome.setText(usuario.getNome());

            String foto = usuario.getFotoUsuario();
            if (foto != null) {
                Uri uri = Uri.parse(usuario.getFotoUsuario());
                Glide.with(ChatActivity.this).load(uri).into(circleImageFoto);
            } else {
                circleImageFoto.setImageResource(R.drawable.padrao);
            }
        }

        recyclerViewConfig();
    }

    private void recyclerViewConfig() {
        adapter = new MensagensAdaptar(mensagens, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public void enviarMensagem(View view) {
        String txtMsg = editMensagem.getText().toString();
        if (!txtMsg.isEmpty()) {
            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario(idRemetente);
            mensagem.setMensagem(txtMsg);

            controllerMsg.enviarMensagem(idRemetente, Base64Helper.encode64(usuario.getEmail()), mensagem);
            editMensagem.setText("");

        } else {
            Toast.makeText(ChatActivity.this, "Mensagem vazia", Toast.LENGTH_SHORT).show();
        }
    }

}
