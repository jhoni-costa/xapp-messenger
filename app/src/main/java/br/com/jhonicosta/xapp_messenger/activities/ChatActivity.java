package br.com.jhonicosta.xapp_messenger.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.adapter.MensagensAdaptar;
import br.com.jhonicosta.xapp_messenger.config.FirebaseConfig;
import br.com.jhonicosta.xapp_messenger.controller.MensagemController;
import br.com.jhonicosta.xapp_messenger.controller.UsuarioController;
import br.com.jhonicosta.xapp_messenger.helper.Base64Helper;
import br.com.jhonicosta.xapp_messenger.model.Mensagem;
import br.com.jhonicosta.xapp_messenger.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private static final int CAMERA_ = 100;

    private TextView textViewNome;
    private CircleImageView circleImageFoto;
    private EditText editMensagem;
    private RecyclerView recyclerView;
    private ImageView imageCamera;

    private Usuario usuario;

    private UsuarioController controllerUser;
    private MensagemController controllerMsg;
    private String idRemetente;
    private String idDestinatario;

    private MensagensAdaptar adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private StorageReference storage;

    private ChildEventListener childEventListenerMensagens;

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
        storage = FirebaseConfig.getFirebaseStorage();

        textViewNome = findViewById(R.id.chat_nome);
        circleImageFoto = findViewById(R.id.chat_user_foto);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerView = findViewById(R.id.reciclerMensagem);
        imageCamera = findViewById(R.id.imageCamera);

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
        idRemetente = controllerUser.getIdUser();
        idDestinatario = Base64Helper.encode64(usuario.getEmail());
        recyclerViewConfig();

        database = FirebaseConfig.getFirebaseDatabase();
        mensagensRef = database.child("mensagens")
                .child(idRemetente)
                .child(idDestinatario);
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;
            try {
                switch (requestCode) {
                    case CAMERA_:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }
                if (imagem != null) {
                    ByteArrayOutputStream boas = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, boas);
                    byte[] dataImage = boas.toByteArray();

                    String nomeImagem = UUID.randomUUID().toString();

                    StorageReference imgRef = storage.child("imagens")
                            .child("fotos")
                            .child(idRemetente)
                            .child(nomeImagem);

                    UploadTask uploadTask = imgRef.putBytes(dataImage);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Erro", "Erro ao subir imagem");
                            Toast.makeText(ChatActivity.this, R.string.upload_image_error, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri uri = taskSnapshot.getDownloadUrl();
                            Mensagem mensagem = new Mensagem();
                            mensagem.setIdUsuario(idRemetente);
                            mensagem.setMensagem("imagem.jpeg");
                            mensagem.setImagem(uri.toString());

                            controllerMsg.enviarMensagem(idRemetente, idDestinatario, mensagem);
                            controllerMsg.enviarMensagem(idDestinatario, idRemetente, mensagem);
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void recyclerViewConfig() {
        adapter = new MensagensAdaptar(mensagens, getApplicationContext(), this);
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
            controllerMsg.enviarMensagem(Base64Helper.encode64(usuario.getEmail()), idRemetente, mensagem);
            editMensagem.setText("");

        } else {
            Toast.makeText(ChatActivity.this, "Mensagem vazia", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagem();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagem() {
        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
