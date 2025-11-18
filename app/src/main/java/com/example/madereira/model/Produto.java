package com.example.madereira.model;

import java.util.Date;

public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private int fkCategoria;
    private int fkStatus;
    private double quantidade;
    private String unidadeMedida;
    private double preco;
    private Date dateCadastro;

    //Campos auxiliares
    private String nomeCategoria;
    private String nomeStatus;

    // Construtor vazio
    public Produto() {
        this.dateCadastro = new Date();
        this.unidadeMedida = "un"; // unidade padrão
    }

    //contrutor completo
    public Produto (int id, String nome, String descricao, int fkCategoria, int fkStatus, double quantidade, String unidadeMedida, double preco) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.fkCategoria = fkCategoria;
        this.fkStatus = fkStatus;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.preco = preco;
        this.dateCadastro = new Date();
    }

    //Contrutor sem ID (para inserção)
    public Produto (String nome, String descricao, int fkCategoria, int fkStatus, double quantidade, String unidadeMedida, double preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.fkCategoria = fkCategoria;
        this.fkStatus = fkStatus;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.preco = preco;
        this.dateCadastro = new Date();
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getFkCategoria() {
        return fkCategoria;
    }

    public void setFkCategoria(int fkCategoria) {
        this.fkCategoria = fkCategoria;
    }

    public int getFkStatus() {
        return fkStatus;
    }

    public void setFkStatus(int fkStatus) {
        this.fkStatus = fkStatus;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Date getDataCadastro() {
        return dateCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dateCadastro = dataCadastro;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getNomeStatus() {
        return nomeStatus;
    }

    public void setNomeStatus(String nomeStatus) {
        this.nomeStatus = nomeStatus;
    }

    //Formatação do preço
    public String getPrecoFormatado() {
        return String.format("R$ %.2f", preco);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                ", preco=" + preco +
                '}';
    }

}
