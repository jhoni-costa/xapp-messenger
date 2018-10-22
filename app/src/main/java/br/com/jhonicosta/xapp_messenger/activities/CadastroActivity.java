package br.com.jhonicosta.xapp_messenger.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.controller.UsuarioController;
import br.com.jhonicosta.xapp_messenger.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FloatingActionButton fabCadastrar;

    private UsuarioController controller;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        controller = new UsuarioController(CadastroActivity.this);

        campoNome = findViewById(R.id.cadastroNome);
        campoEmail = findViewById(R.id.cadastroEmail);
        campoSenha = findViewById(R.id.cadastroSenha);
        fabCadastrar = findViewById(R.id.fabCadastrar);

        fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = campoNome.getText().toString();
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();
                if (!nome.isEmpty()) {
                    if (!email.isEmpty()) {
                        if (!senha.isEmpty()) {
                            controller.cadastrar(new Usuario(nome, email, senha));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.senha_vazia, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.email_vazio, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.nome_vazio, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
