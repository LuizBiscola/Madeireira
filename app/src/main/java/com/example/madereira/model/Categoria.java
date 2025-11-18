package com.example.madereira.model;

public class Categoria {
    private int id;
    private String descricao;


    // Construtor vazio
    public Categoria() {

    }

    // Construtor com ID
    public Categoria(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // Construtor sem ID (para inserção)
    public Categoria(String descricao) {
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
        return descricao; // Para Spinners
    }
}