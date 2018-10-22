package br.com.jhonicosta.xapp_messenger;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FloatingActionButton fabCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.cadastroNome);
        campoEmail = findViewById(R.id.loginEmail);
        campoSenha = findViewById(R.id.cadastroSenha);
        fabCadastrar = findViewById(R.id.fabCadastrar);

        fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "App em construção", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
