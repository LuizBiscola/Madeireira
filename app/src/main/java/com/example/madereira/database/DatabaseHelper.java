package com.example.madereira.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nome e versão do banco
    private static final String DATABASE_NAME = "madeireira.db";
    private static final int DATABASE_VERSION = 2;

    // Nomes das tabelas
    public static final String TABLE_USUARIO = "usuario";
    public static final String TABLE_CATEGORIA = "categoria";
    public static final String TABLE_STATUS = "status";
    public static final String TABLE_PRODUTO = "produto";

    // Construtor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criar tabela STATUS
        String createTableStatus = "CREATE TABLE " + TABLE_STATUS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL)";
        db.execSQL(createTableStatus);

        // Criar tabela CATEGORIA
        String createTableCategoria = "CREATE TABLE " + TABLE_CATEGORIA + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL, " +
                "ativo INTEGER DEFAULT 1)";
        db.execSQL(createTableCategoria);

        // Criar tabela USUARIO
        String createTableUsuario = "CREATE TABLE " + TABLE_USUARIO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "senha TEXT NOT NULL, " +
                "tipoPerfil TEXT DEFAULT 'Cliente', " +
                "cpfCnpj TEXT, " +
                "dataCadastro TEXT, " +
                "ativo INTEGER DEFAULT 1)";
        db.execSQL(createTableUsuario);

        // Criar tabela PRODUTO
        String createTableProduto = "CREATE TABLE " + TABLE_PRODUTO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "descricao TEXT, " +
                "fkCategoria INTEGER NOT NULL, " +
                "fkStatus INTEGER NOT NULL, " +
                "quantidade REAL DEFAULT 0, " +
                "unidadeMedida TEXT DEFAULT 'UN', " +
                "preco REAL NOT NULL, " +
                "dataCadastro TEXT, " +
                "FOREIGN KEY (fkCategoria) REFERENCES " + TABLE_CATEGORIA + "(id), " +
                "FOREIGN KEY (fkStatus) REFERENCES " + TABLE_STATUS + "(id))";
        db.execSQL(createTableProduto);

        // Inserir dados iniciais
        inserirDadosIniciais(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Dropar tabelas antigas e recriar
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);
        onCreate(db);
    }

    // Inserir dados iniciais (Status e Categorias padrão)
    private void inserirDadosIniciais(SQLiteDatabase db) {
        // Inserir Status padrão
        db.execSQL("INSERT INTO " + TABLE_STATUS + " (descricao) VALUES ('Ativo')");
        db.execSQL("INSERT INTO " + TABLE_STATUS + " (descricao) VALUES ('Inativo')");
        db.execSQL("INSERT INTO " + TABLE_STATUS + " (descricao) VALUES ('Em Falta')");
        db.execSQL("INSERT INTO " + TABLE_STATUS + " (descricao) VALUES ('Descontinuado')");

        // Inserir Categorias padrão
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + " (descricao) VALUES ('Madeiras Brutas')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + " (descricao) VALUES ('Madeiras Tratadas')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + " (descricao) VALUES ('Compensados')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + " (descricao) VALUES ('MDF/MDP')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + " (descricao) VALUES ('Ferramentas')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIA + " (descricao) VALUES ('Acessórios')");
    }

    // Habilitar chaves estrangeiras
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }
}