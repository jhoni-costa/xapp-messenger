package br.com.jhonicosta.xapp_messenger.controller;

import com.google.firebase.database.DatabaseReference;

import br.com.jhonicosta.xapp_messenger.config.FirebaseConfig;
import br.com.jhonicosta.xapp_messenger.model.Mensagem;

public class MensagemController {

    private DatabaseReference reference;

    public MensagemController() {
        this.reference = FirebaseConfig.getFirebaseDatabase();
    }

    public void enviarMensagem(String remetente, String destinatario, Mensagem mensagem) {
        DatabaseReference msgRef = reference.child("mensagens");


        msgRef.child(remetente).child(destinatario).push().setValue(mensagem);
    }
}
