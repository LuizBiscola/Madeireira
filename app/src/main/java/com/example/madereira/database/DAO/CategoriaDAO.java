package com.example.madereira.database.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.madereira.database.DatabaseHelper;
import com.example.madereira.model.Categoria;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private DatabaseHelper dbHelper;

    public CategoriaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // CREATE
    public long inserir(Categoria categoria) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descricao", categoria.getDescricao());

        long id = db.insert(DatabaseHelper.TABLE_CATEGORIA, null, values);
        db.close();
        return id;
    }

    // READ - Listar Todas (somente ativas)
    public List<Categoria> listarTodas() {
        List<Categoria> listaCategorias = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORIA, null, "ativo = 1",
                null, null, null, "descricao ASC");

        if (cursor.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();
                categoria.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                categoria.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                listaCategorias.add(categoria);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaCategorias;
    }

    // READ - Buscar por ID
    public Categoria buscarPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORIA, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Categoria categoria = null;
        if (cursor.moveToFirst()) {
            categoria = new Categoria();
            categoria.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            categoria.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
        }

        cursor.close();
        db.close();
        return categoria;
    }

    // UPDATE
    public int atualizar(Categoria categoria) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descricao", categoria.getDescricao());

        int linhasAfetadas = db.update(DatabaseHelper.TABLE_CATEGORIA, values, "id = ?",
                new String[]{String.valueOf(categoria.getId())});
        db.close();
        return linhasAfetadas;
    }

    // DELETE
    public int excluir(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int linhasAfetadas = db.delete(DatabaseHelper.TABLE_CATEGORIA, "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return linhasAfetadas;
    }
}