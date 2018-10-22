package br.com.jhonicosta.xapp_messenger.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.config.FirebaseConfig;
import br.com.jhonicosta.xapp_messenger.model.Usuario;

public class UsuarioController {

    private FirebaseAuth firebaseAuth;
    private Context context;

    public UsuarioController(Context context) {
        this.context = context;
        this.firebaseAuth = FirebaseConfig.getFirebaseAuth();
    }

    public void cadastrar(Usuario usuario) {
        this.firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, R.string.cadastro_sucesso, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, R.string.cadastro_erro, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
