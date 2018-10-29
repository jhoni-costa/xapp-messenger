package br.com.jhonicosta.xapp_messenger.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageFoto;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = findViewById(R.id.chat_nome);
        circleImageFoto = findViewById(R.id.chat_user_foto);

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
    }

}
