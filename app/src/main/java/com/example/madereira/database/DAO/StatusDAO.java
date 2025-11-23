package com.example.madereira.database.DAO;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.madereira.database.DatabaseHelper;
import com.example.madereira.model.Status;
import java.util.ArrayList;
import java.util.List;

public class StatusDAO {

    private DatabaseHelper dbHelper;

    public StatusDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // CREATE
    public long inserir(Status status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descricao", status.getDescricao());

        long id = db.insert(DatabaseHelper.TABLE_STATUS, null, values);
        db.close();
        return id;
    }

    // READ - Listar Todos
    public List<Status> listarTodos() {
        List<Status> listaStatus = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_STATUS, null, null, null, null, null, "descricao ASC");

        if (cursor.moveToFirst()) {
            do {
                Status status = new Status();
                status.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                status.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                listaStatus.add(status);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaStatus;
    }

    // READ - Buscar por ID
    public Status buscarPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_STATUS, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Status status = null;
        if (cursor.moveToFirst()) {
            status = new Status();
            status.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            status.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
        }

        cursor.close();
        db.close();
        return status;
    }

    // UPDATE
    public int atualizar(Status status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descricao", status.getDescricao());

        int linhasAfetadas = db.update(DatabaseHelper.TABLE_STATUS, values, "id = ?",
                new String[]{String.valueOf(status.getId())});
        db.close();
        return linhasAfetadas;
    }

    // DELETE
    public int excluir(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int linhasAfetadas = db.delete(DatabaseHelper.TABLE_STATUS, "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return linhasAfetadas;
    }
}