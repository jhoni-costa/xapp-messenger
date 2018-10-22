package br.com.jhonicosta.xapp_messenger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.controller.UsuarioController;
import br.com.jhonicosta.xapp_messenger.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;
    private TextView textCadastrar;
    private Button botaoLogar;

    private UsuarioController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        controller = new UsuarioController(this);

        campoEmail = findViewById(R.id.loginEmail);
        campoSenha = findViewById(R.id.loginSenha);
        textCadastrar = findViewById(R.id.loginCadastrar);
        botaoLogar = findViewById(R.id.loginButton);

        textCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
            }
        });

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();
                if (!email.isEmpty()) {
                    if (!senha.isEmpty()) {
                        controller.logar(new Usuario(null, email, senha));
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.senha_vazia, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.email_vazio, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
