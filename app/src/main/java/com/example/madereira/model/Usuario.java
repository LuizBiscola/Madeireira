package com.example.madereira.model;

import java.util.Date;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String endereco;
    private String telefone;
    private String senha;
    private String tipoPerfil; //"Cliente etc..."
    private Date dataCadastro;
    private boolean ativo;

    //Construtor vazio
    public Usuario(){
        this.dataCadastro = new Date();
        this.ativo = true;
    }
//construtor com todos os atributos
    public Usuario(int id, String nome, String email, String endereco, String telefone, String senha, String tipoPerfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.telefone = telefone;
        this.senha = senha;
        this.tipoPerfil = tipoPerfil;
        this.dataCadastro = new Date();
        this.ativo = true;
    }

    public Usuario(String nome, String email, String endereco, String telefone, String senha){
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.telefone = telefone;
        this.senha = senha;
        this. tipoPerfil = "Cliente"; // padrão
        this.dataCadastro = new Date();
        this.ativo = true;
    }
    //Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(String tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", tipoPerfil='" + tipoPerfil + '\'' +
                ", dataCadastro=" + dataCadastro +
                ", ativo=" + ativo +
                '}';
    }
}

