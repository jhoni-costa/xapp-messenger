package br.com.jhonicosta.xapp_messenger.model;

public class Mensagem {

    private String idUsuario, mensagem, imagem, nome;

    public Mensagem() {
        this.setNome("");
    }

    public Mensagem(String idUsuario, String mensagem, String imagem) {
        this.idUsuario = idUsuario;
        this.mensagem = mensagem;
        this.imagem = imagem;
        this.setNome("");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
