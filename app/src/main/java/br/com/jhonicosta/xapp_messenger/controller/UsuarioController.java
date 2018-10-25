package br.com.jhonicosta.xapp_messenger.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.activities.LoginActivity;
import br.com.jhonicosta.xapp_messenger.activities.MainActivity;
import br.com.jhonicosta.xapp_messenger.config.FirebaseConfig;
import br.com.jhonicosta.xapp_messenger.helper.Base64Helper;
import br.com.jhonicosta.xapp_messenger.model.Usuario;

public class UsuarioController {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Activity context;

    public UsuarioController(Activity context) {
        this.context = context;
        this.firebaseAuth = FirebaseConfig.getFirebaseAuth();
        this.databaseReference = FirebaseConfig.getFirebaseDatabase();
        this.storageReference = FirebaseConfig.getFirebaseStorage();
    }

    public void cadastrar(final Usuario usuario) {
        this.firebaseAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, R.string.cadastro_sucesso, Toast.LENGTH_SHORT).show();
                            context.finish();
                            try {
                                usuario.setId(Base64Helper.encode64(usuario.getEmail()));
                                salvar(usuario);
                            } catch (Exception e) {

                            }
                        } else {
                            int exception;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                exception = R.string.exception_senha_fraca;
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                exception = R.string.exception_email_invalido;
                            } catch (FirebaseAuthUserCollisionException e) {
                                exception = R.string.exception_usuario_ja_cadastrado;
                            } catch (Exception e) {
                                exception = R.string.cadastro_erro;
                                e.printStackTrace();
                            }
                            Toast.makeText(context, exception, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logar(Usuario usuario) {
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToMainActivity();
                        } else {
                            int exception;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                exception = R.string.exception_email_senha;
                            } catch (FirebaseAuthInvalidUserException e) {
                                exception = R.string.exception_usuario_nao_cadastrado;
                            } catch (Exception e) {
                                exception = R.string.exception_usuario_nao_cadastrado;
                                e.printStackTrace();
                            }
                            Toast.makeText(context, exception, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void salvar(Usuario usuario) {
        DatabaseReference usuarioFB = databaseReference.child("usuarios")
                .child(usuario.getId());
        usuarioFB.setValue(usuario);
    }

    public void salvarImagem(Bitmap imagem) {

        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 70, boas);
        byte[] dataImage = boas.toByteArray();

        StorageReference imgRef = storageReference
                .child("imagens")
                .child("perfil")
                .child(getIdUser() + ".jpeg");

        UploadTask uploadTask = imgRef.putBytes(dataImage);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, R.string.upload_image_error, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, R.string.upload_image_sucess, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getIdUser() {
        return Base64Helper.encode64(firebaseAuth.getCurrentUser().getEmail());
    }

    public void deslogar() {
        try {
            firebaseAuth.signOut();
            context.startActivity(new Intent(context, LoginActivity.class));
            context.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verificaAutentificacao() {
        if (firebaseAuth.getCurrentUser() != null) {
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        context.startActivity(new Intent(context, MainActivity.class));
        context.finish();
    }
}
