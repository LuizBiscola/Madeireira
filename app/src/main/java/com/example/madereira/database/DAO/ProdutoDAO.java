package com.example.madereira.database.DAO;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.madereira.database.DatabaseHelper;
import com.example.madereira.model.Produto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProdutoDAO {

    private DatabaseHelper dbHelper;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public ProdutoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // CREATE
    public long inserir(Produto produto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", produto.getNome());
        values.put("descricao", produto.getDescricao());
        values.put("fkCategoria", produto.getFkCategoria());
        values.put("fkStatus", produto.getFkStatus());
        values.put("quantidade", produto.getQuantidade());
        values.put("unidadeMedida", produto.getUnidadeMedida());
        values.put("preco", produto.getPreco());
        values.put("dataCadastro", dateFormat.format(produto.getDataCadastro()));

        long id = db.insert(DatabaseHelper.TABLE_PRODUTO, null, values);
        db.close();
        return id;
    }

    // READ - Listar Todos (SEM JOIN)
    public List<Produto> listarTodos() {
        List<Produto> listaProdutos = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUTO, null, null,
                null, null, null, "nome ASC");

        if (cursor.moveToFirst()) {
            do {
                Produto produto = cursorParaProduto(cursor);
                listaProdutos.add(produto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaProdutos;
    }

    // READ - Listar Todos COM JOIN (melhor para exibição)
    public List<Produto> listarTodosComJoin() {
        List<Produto> listaProdutos = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT p.*, c.descricao as nomeCategoria, s.descricao as nomeStatus " +
                "FROM " + DatabaseHelper.TABLE_PRODUTO + " p " +
                "INNER JOIN " + DatabaseHelper.TABLE_CATEGORIA + " c ON p.fkCategoria = c.id " +
                "INNER JOIN " + DatabaseHelper.TABLE_STATUS + " s ON p.fkStatus = s.id " +
                "ORDER BY p.nome ASC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Produto produto = cursorParaProduto(cursor);
                produto.setNomeCategoria(cursor.getString(cursor.getColumnIndexOrThrow("nomeCategoria")));
                produto.setNomeStatus(cursor.getString(cursor.getColumnIndexOrThrow("nomeStatus")));
                listaProdutos.add(produto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaProdutos;
    }

    // READ - Buscar por ID
    public Produto buscarPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUTO, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Produto produto = null;
        if (cursor.moveToFirst()) {
            produto = cursorParaProduto(cursor);
        }

        cursor.close();
        db.close();
        return produto;
    }

    // READ - Buscar por Categoria
    public List<Produto> buscarPorCategoria(int categoriaId) {
        List<Produto> listaProdutos = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUTO, null, "fkCategoria = ?",
                new String[]{String.valueOf(categoriaId)}, null, null, "nome ASC");

        if (cursor.moveToFirst()) {
            do {
                Produto produto = cursorParaProduto(cursor);
                listaProdutos.add(produto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaProdutos;
    }

    // UPDATE
    public int atualizar(Produto produto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", produto.getNome());
        values.put("descricao", produto.getDescricao());
        values.put("fkCategoria", produto.getFkCategoria());
        values.put("fkStatus", produto.getFkStatus());
        values.put("quantidade", produto.getQuantidade());
        values.put("unidadeMedida", produto.getUnidadeMedida());
        values.put("preco", produto.getPreco());

        int linhasAfetadas = db.update(DatabaseHelper.TABLE_PRODUTO, values, "id = ?",
                new String[]{String.valueOf(produto.getId())});
        db.close();
        return linhasAfetadas;
    }

    // DELETE
    public int excluir(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int linhasAfetadas = db.delete(DatabaseHelper.TABLE_PRODUTO, "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return linhasAfetadas;
    }

    // Método auxiliar para converter Cursor em Produto
    private Produto cursorParaProduto(Cursor cursor) {
        Produto produto = new Produto();
        produto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        produto.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
        produto.setFkCategoria(cursor.getInt(cursor.getColumnIndexOrThrow("fkCategoria")));
        produto.setFkStatus(cursor.getInt(cursor.getColumnIndexOrThrow("fkStatus")));
        produto.setQuantidade(cursor.getDouble(cursor.getColumnIndexOrThrow("quantidade")));
        produto.setUnidadeMedida(cursor.getString(cursor.getColumnIndexOrThrow("unidadeMedida")));
        produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

        try {
            String dataStr = cursor.getString(cursor.getColumnIndexOrThrow("dataCadastro"));
            if (dataStr != null) {
                produto.setDataCadastro(dateFormat.parse(dataStr));
            }
        } catch (Exception e) {
            produto.setDataCadastro(new Date());
        }

        return produto;
    }
}