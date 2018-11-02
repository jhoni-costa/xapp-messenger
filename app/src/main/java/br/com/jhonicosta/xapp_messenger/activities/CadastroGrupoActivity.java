package br.com.jhonicosta.xapp_messenger.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.adapter.GrupoSelecionadoAdapter;
import br.com.jhonicosta.xapp_messenger.config.FirebaseConfig;
import br.com.jhonicosta.xapp_messenger.controller.UsuarioController;
import br.com.jhonicosta.xapp_messenger.model.Grupo;
import br.com.jhonicosta.xapp_messenger.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroGrupoActivity extends AppCompatActivity {

    private static final int CAMERA_ = 100;
    private static final int GALERIA_ = 200;

    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private EditText nomeGrupo;
    private TextView txtTotalParticipantes;
    private CircleImageView imagemGrupo;
    private RecyclerView recyclerView;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private FloatingActionButton fabSalvarGrupo;
    private Grupo grupo;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Grupo");
        toolbar.setSubtitle("Defina o nome");
        setSupportActionBar(toolbar);

        grupo = new Grupo();

        fabSalvarGrupo = findViewById(R.id.fabSalvarGrupo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storageReference = FirebaseConfig.getFirebaseStorage();
        nomeGrupo = findViewById(R.id.txtNomeGrupo);
        txtTotalParticipantes = findViewById(R.id.txtTotalParticipantes);
        recyclerView = findViewById(R.id.rvGrupo);
        imagemGrupo = findViewById(R.id.imageGrupo);

        imagemGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, GALERIA_);
                }
            }
        });

        if (getIntent().getExtras() != null) {
            List<Usuario> membros = (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            this.listaMembrosSelecionados = membros;
            txtTotalParticipantes.setText("Participantes: " + listaMembrosSelecionados.size());
        }

        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerView.setLayoutManager(layoutManagerHorizontal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(grupoSelecionadoAdapter);

        fabSalvarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeGrupo.getText().toString();

                listaMembrosSelecionados.add(new UsuarioController(getParent()).getUsuario());

                grupo.setMembros(listaMembrosSelecionados);
                grupo.setNome(nome);
                grupo.salvar();

                Intent i = new Intent(CadastroGrupoActivity.this, ChatActivity.class);
                i.putExtra("chatGrupo", grupo);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;
            try {

                Uri localImagem = data.getData();
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagem);
                if (imagem != null) {
                    imagemGrupo.setImageBitmap(imagem);

                    ByteArrayOutputStream boas = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, boas);
                    byte[] dataImage = boas.toByteArray();

                    StorageReference imgRef = storageReference
                            .child("imagens")
                            .child("grupos")
                            .child(grupo.getId() + ".jpeg");

                    UploadTask uploadTask = imgRef.putBytes(dataImage);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), R.string.upload_image_error, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), R.string.upload_image_sucess, Toast.LENGTH_SHORT).show();

                            Uri url = taskSnapshot.getDownloadUrl();
                            grupo.setFoto(url.toString());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

