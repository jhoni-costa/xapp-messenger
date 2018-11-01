package br.com.jhonicosta.xapp_messenger.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.adapter.GrupoSelecionadoAdapter;
import br.com.jhonicosta.xapp_messenger.helper.RecyclerItemClickListener;
import br.com.jhonicosta.xapp_messenger.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroGrupoActivity extends AppCompatActivity {

    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private EditText nomeGrupo;
    private TextView txtTotalParticipantes;
    private CircleImageView imagemGrupo;
    private RecyclerView recyclerView;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Grupo");
        toolbar.setSubtitle("Defina o nome");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeGrupo = findViewById(R.id.txtNomeGrupo);
        txtTotalParticipantes = findViewById(R.id.txtTotalParticipantes);
        imagemGrupo = findViewById(R.id.imageGrupo);
        recyclerView = findViewById(R.id.rvGrupo);

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


    }
}

