package com.example.madereira.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.madereira.database.DatabaseHelper;
import com.example.madereira.database.DAO.CategoriaDAO;
import com.example.madereira.database.DAO.ProdutoDAO;
import com.example.madereira.database.DAO.StatusDAO;
import com.example.madereira.database.DAO.UsuarioDAO;
import com.example.madereira.model.Categoria;
import com.example.madereira.model.Produto;
import com.example.madereira.model.Status;
import com.example.madereira.model.Usuario;

import java.util.List;

/**
 * Classe utilitária para facilitar o acesso e visualização do banco de dados
 */
public class DatabaseUtil {

    private static final String TAG = "DatabaseUtil";
    private Context context;
    private DatabaseHelper dbHelper;

    public DatabaseUtil(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    // ==================== MÉTODOS DE CONSULTA DIRETA ====================

    /**
     * Executa uma query SQL personalizada
     * @param query SQL query para executar
     * @return Cursor com os resultados
     */
    public Cursor executarQuery(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    /**
     * Imprime todos os dados de uma tabela no Logcat
     * @param tableName Nome da tabela
     */
    public void imprimirTabela(String tableName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        Log.d(TAG, "========================================");
        Log.d(TAG, "Tabela: " + tableName);
        Log.d(TAG, "Total de registros: " + cursor.getCount());
        Log.d(TAG, "========================================");

        if (cursor.moveToFirst()) {
            // Imprimir nomes das colunas
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                header.append(cursor.getColumnName(i)).append(" | ");
            }
            Log.d(TAG, header.toString());
            Log.d(TAG, "----------------------------------------");

            // Imprimir dados
            do {
                StringBuilder row = new StringBuilder();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row.append(cursor.getString(i)).append(" | ");
                }
                Log.d(TAG, row.toString());
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    /**
     * Imprime todas as tabelas do banco
     */
    public void imprimirTodasTabelas() {
        imprimirTabela(DatabaseHelper.TABLE_STATUS);
        imprimirTabela(DatabaseHelper.TABLE_CATEGORIA);
        imprimirTabela(DatabaseHelper.TABLE_USUARIO);
        imprimirTabela(DatabaseHelper.TABLE_PRODUTO);
    }

    // ==================== MÉTODOS USANDO DAOs ====================

    /**
     * Lista todos os produtos com informações completas (JOIN)
     */
    public void listarProdutosCompleto() {
        ProdutoDAO produtoDAO = new ProdutoDAO(context);
        List<Produto> produtos = produtoDAO.listarTodosComJoin();

        Log.d(TAG, "========================================");
        Log.d(TAG, "PRODUTOS CADASTRADOS (COM JOIN)");
        Log.d(TAG, "Total: " + produtos.size());
        Log.d(TAG, "========================================");

        for (Produto p : produtos) {
            Log.d(TAG, String.format(
                "ID: %d | Nome: %s | Categoria: %s | Status: %s | Qtd: %.2f %s | Preço: %s",
                p.getId(),
                p.getNome(),
                p.getNomeCategoria(),
                p.getNomeStatus(),
                p.getQuantidade(),
                p.getUnidadeMedida(),
                p.getPrecoFormatado()
            ));
        }
    }

    /**
     * Lista todas as categorias
     */
    public void listarCategorias() {
        CategoriaDAO categoriaDAO = new CategoriaDAO(context);
        List<Categoria> categorias = categoriaDAO.listarTodas();

        Log.d(TAG, "========================================");
        Log.d(TAG, "CATEGORIAS CADASTRADAS");
        Log.d(TAG, "Total: " + categorias.size());
        Log.d(TAG, "========================================");

        for (Categoria c : categorias) {
            Log.d(TAG, String.format("ID: %d | Descrição: %s", c.getId(), c.getDescricao()));
        }
    }

    /**
     * Lista todos os status
     */
    public void listarStatus() {
        StatusDAO statusDAO = new StatusDAO(context);
        List<Status> statusList = statusDAO.listarTodos();

        Log.d(TAG, "========================================");
        Log.d(TAG, "STATUS CADASTRADOS");
        Log.d(TAG, "Total: " + statusList.size());
        Log.d(TAG, "========================================");

        for (Status s : statusList) {
            Log.d(TAG, String.format("ID: %d | Descrição: %s", s.getId(), s.getDescricao()));
        }
    }

    /**
     * Lista todos os usuários
     */
    public void listarUsuarios() {
        UsuarioDAO usuarioDAO = new UsuarioDAO(context);
        List<Usuario> usuarios = usuarioDAO.listarTodos();

        Log.d(TAG, "========================================");
        Log.d(TAG, "USUÁRIOS CADASTRADOS");
        Log.d(TAG, "Total: " + usuarios.size());
        Log.d(TAG, "========================================");

        for (Usuario u : usuarios) {
            Log.d(TAG, String.format(
                "ID: %d | Nome: %s | Email: %s | Perfil: %s | Telefone: %s",
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getTipoPerfil(),
                u.getTelefone()
            ));
        }
    }

    // ==================== MÉTODOS DE INSERÇÃO RÁPIDA ====================

    /**
     * Insere um produto de teste
     */
    public long inserirProdutoTeste(String nome, String descricao, double quantidade, double preco) {
        ProdutoDAO produtoDAO = new ProdutoDAO(context);

        // Usar primeira categoria e status ativo
        Produto produto = new Produto(nome, descricao, 1, 1, quantidade, "un", preco);
        long id = produtoDAO.inserir(produto);

        Log.d(TAG, "Produto inserido com ID: " + id);
        return id;
    }

    /**
     * Busca produto por ID e exibe informações
     */
    public void buscarProdutoPorId(int id) {
        ProdutoDAO produtoDAO = new ProdutoDAO(context);
        Produto produto = produtoDAO.buscarPorId(id);

        if (produto != null) {
            Log.d(TAG, "========================================");
            Log.d(TAG, "PRODUTO ENCONTRADO");
            Log.d(TAG, "========================================");
            Log.d(TAG, "ID: " + produto.getId());
            Log.d(TAG, "Nome: " + produto.getNome());
            Log.d(TAG, "Descrição: " + produto.getDescricao());
            Log.d(TAG, "Quantidade: " + produto.getQuantidade() + " " + produto.getUnidadeMedida());
            Log.d(TAG, "Preço: " + produto.getPrecoFormatado());
            Log.d(TAG, "Categoria ID: " + produto.getFkCategoria());
            Log.d(TAG, "Status ID: " + produto.getFkStatus());
        } else {
            Log.d(TAG, "Produto não encontrado com ID: " + id);
        }
    }

    /**
     * Busca produtos por categoria
     */
    public void buscarProdutosPorCategoria(int categoriaId) {
        ProdutoDAO produtoDAO = new ProdutoDAO(context);
        List<Produto> produtos = produtoDAO.buscarPorCategoria(categoriaId);

        Log.d(TAG, "========================================");
        Log.d(TAG, "PRODUTOS DA CATEGORIA ID: " + categoriaId);
        Log.d(TAG, "Total: " + produtos.size());
        Log.d(TAG, "========================================");

        for (Produto p : produtos) {
            Log.d(TAG, String.format(
                "ID: %d | Nome: %s | Qtd: %.2f %s | Preço: %s",
                p.getId(),
                p.getNome(),
                p.getQuantidade(),
                p.getUnidadeMedida(),
                p.getPrecoFormatado()
            ));
        }
    }

    // ==================== INFORMAÇÕES DO BANCO ====================

    /**
     * Retorna o caminho do arquivo do banco de dados
     */
    public String getCaminhoBanco() {
        return context.getDatabasePath("madeireira.db").getAbsolutePath();
    }

    /**
     * Imprime informações gerais do banco
     */
    public void imprimirInfoBanco() {
        Log.d(TAG, "========================================");
        Log.d(TAG, "INFORMAÇÕES DO BANCO DE DADOS");
        Log.d(TAG, "========================================");
        Log.d(TAG, "Nome: madeireira.db");
        Log.d(TAG, "Caminho: " + getCaminhoBanco());
        Log.d(TAG, "Versão: 1");
        Log.d(TAG, "========================================");
    }

    /**
     * Fecha a conexão com o banco
     */
    public void fechar() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

