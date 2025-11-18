package com.example.madereira.model;

public class Status {
    private int id;
    private String descricao;

    // Construtor vazio
    public Status() {
    }

    // Construtor com ID
    public Status(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // Construtor sem ID (para inserção)
    public Status(String descricao) {
        this.descricao = descricao;
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

    @Override
    public String toString() {
        return descricao; //para Spinners
    }
}