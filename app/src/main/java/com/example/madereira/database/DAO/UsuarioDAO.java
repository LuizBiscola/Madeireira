package com.example.madereira.database.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.madereira.database.DatabaseHelper;
import com.example.madereira.model.Usuario;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UsuarioDAO {

    private DatabaseHelper dbHelper;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public UsuarioDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // CREATE
    public long inserir(Usuario usuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("endereco", usuario.getEndereco());
        values.put("telefone", usuario.getTelefone());
        values.put("senha", usuario.getSenha());
        values.put("tipoPerfil", usuario.getTipoPerfil());
        values.put("dataCadastro", dateFormat.format(usuario.getDataCadastro()));
        values.put("ativo", usuario.isAtivo() ? 1 : 0);

        long id = db.insert(DatabaseHelper.TABLE_USUARIO, null, values);
        db.close();
        return id;
    }

    // READ - Listar Todos (somente ativos)
    public List<Usuario> listarTodos() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_USUARIO, null, "ativo = 1",
                null, null, null, "nome ASC");

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = cursorParaUsuario(cursor);
                listaUsuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaUsuarios;
    }

    // READ - Buscar por ID
    public Usuario buscarPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USUARIO, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = cursorParaUsuario(cursor);
        }

        cursor.close();
        db.close();
        return usuario;
    }

    // READ - Buscar por Email
    public Usuario buscarPorEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USUARIO, null, "email = ?",
                new String[]{email}, null, null, null);

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = cursorParaUsuario(cursor);
        }

        cursor.close();
        db.close();
        return usuario;
    }

    // UPDATE
    public int atualizar(Usuario usuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("endereco", usuario.getEndereco());
        values.put("telefone", usuario.getTelefone());
        values.put("senha", usuario.getSenha());
        values.put("tipoPerfil", usuario.getTipoPerfil());
        values.put("ativo", usuario.isAtivo() ? 1 : 0);

        int linhasAfetadas = db.update(DatabaseHelper.TABLE_USUARIO, values, "id = ?",
                new String[]{String.valueOf(usuario.getId())});
        db.close();
        return linhasAfetadas;
    }

    // DELETE
    public int excluir(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int linhasAfetadas = db.delete(DatabaseHelper.TABLE_USUARIO, "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return linhasAfetadas;
    }

    // LOGIN - Validar credenciais
    public Usuario validarLogin(String email, String senha) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USUARIO, null,
                "email = ? AND senha = ? AND ativo = 1",
                new String[]{email, senha}, null, null, null);

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = cursorParaUsuario(cursor);
        }

        cursor.close();
        db.close();
        return usuario;
    }

    // Método auxiliar para converter Cursor em Usuario
    private Usuario cursorParaUsuario(Cursor cursor) {
        Usuario usuario = new Usuario();
        usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        usuario.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow("endereco")));
        usuario.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
        usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow("senha")));
        usuario.setTipoPerfil(cursor.getString(cursor.getColumnIndexOrThrow("tipoPerfil")));
        usuario.setAtivo(cursor.getInt(cursor.getColumnIndexOrThrow("ativo")) == 1);

        try {
            String dataStr = cursor.getString(cursor.getColumnIndexOrThrow("dataCadastro"));
            if (dataStr != null) {
                usuario.setDataCadastro(dateFormat.parse(dataStr));
            }
        } catch (Exception e) {
            usuario.setDataCadastro(new Date());
        }

        return usuario;
    }
}