package br.com.jhonicosta.xapp_messenger.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

import br.com.jhonicosta.xapp_messenger.config.FirebaseConfig;
import br.com.jhonicosta.xapp_messenger.helper.Base64Helper;

public class Grupo implements Serializable {

    private String id;
    private String nome;
    private String foto;
    private List<Usuario> membros;

    public Grupo() {

        DatabaseReference reference = FirebaseConfig.getFirebaseDatabase();
        reference = reference.child("grupos");

        String id = reference.push().getKey();
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }

    public void salvar() {
        DatabaseReference reference = FirebaseConfig.getFirebaseDatabase();
        reference = reference.child("grupos");

        reference.child(getId()).setValue(this);

        for (Usuario usuario : getMembros()) {
            Conversa conversa = new Conversa();
            conversa.setIdRemetente(Base64Helper.encode64(usuario.getEmail()));
            conversa.setIdDestinatario(getId());
            conversa.setUltimaMensagem("");
            conversa.setIsGroup("true");
            conversa.setGrupo(this);
            conversa.salvar();
        }
    }
}
