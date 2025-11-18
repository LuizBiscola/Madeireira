package com.example.madereira.model;

public class Categoria {
    private int id;
    private String descricao;
    private boolean ativo;

    // Construtor vazio
    public Categoria() {
        this.ativo = true;
    }

    // Construtor com ID
    public Categoria(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = true;
    }

    // Construtor sem ID (para inserção)
    public Categoria(String descricao) {
        this.descricao = descricao;
        this.ativo = true;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return descricao; // Para Spinners
    }
}