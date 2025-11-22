package com.example.madereira.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.madereira.R;
import com.example.madereira.database.DAO.CategoriaDAO;
import com.example.madereira.database.DAO.StatusDAO;
import com.example.madereira.database.DAO.UsuarioDAO;
import com.example.madereira.database.DAO.ProdutoDAO;
import com.example.madereira.model.Categoria;
import com.example.madereira.model.Status;
import com.example.madereira.model.Usuario;
import com.example.madereira.model.Produto;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TESTE_MADEIREIRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Executar testes
        testarBancoDeDados();
    }

    private void testarBancoDeDados() {
        Log.d(TAG, "========================================");
        Log.d(TAG, "INICIANDO TESTES DO BANCO DE DADOS");
        Log.d(TAG, "========================================");

        // Teste 1: Listar Status (já inseridos automaticamente)
        testarStatus();

        // Teste 2: Listar Categorias (já inseridas automaticamente)
        testarCategorias();

        // Teste 3: Inserir e Listar Usuários
        testarUsuarios();

        // Teste 4: Inserir e Listar Produtos
        testarProdutos();

        Log.d(TAG, "========================================");
        Log.d(TAG, "TESTES FINALIZADOS!");
        Log.d(TAG, "========================================");

        Toast.makeText(this, "Testes concluídos! Veja o Logcat", Toast.LENGTH_LONG).show();
    }

    // ==================== TESTE 1: STATUS ====================
    private void testarStatus() {
        Log.d(TAG, "\n--- TESTE 1: STATUS ---");
        StatusDAO statusDAO = new StatusDAO(this);

        // Listar todos os status
        List<Status> listaStatus = statusDAO.listarTodos();
        Log.d(TAG, "Total de Status cadastrados: " + listaStatus.size());

        for (Status status : listaStatus) {
            Log.d(TAG, "Status ID: " + status.getId() + " | Descrição: " + status.getDescricao());
        }
    }

    // ==================== TESTE 2: CATEGORIAS ====================
    private void testarCategorias() {
        Log.d(TAG, "\n--- TESTE 2: CATEGORIAS ---");
        CategoriaDAO categoriaDAO = new CategoriaDAO(this);

        // Listar todas as categorias
        List<Categoria> listaCategorias = categoriaDAO.listarTodas();
        Log.d(TAG, "Total de Categorias cadastradas: " + listaCategorias.size());

        for (Categoria categoria : listaCategorias) {
            Log.d(TAG, "Categoria ID: " + categoria.getId() + " | Descrição: " + categoria.getDescricao());
        }
    }

    // ==================== TESTE 3: USUÁRIOS ====================
    private void testarUsuarios() {
        Log.d(TAG, "\n--- TESTE 3: USUÁRIOS ---");
        UsuarioDAO usuarioDAO = new UsuarioDAO(this);

        // Verificar se já existe usuário para não duplicar
        Usuario usuarioExistente = usuarioDAO.buscarPorEmail("joao@email.com");

        if (usuarioExistente == null) {
            // Inserir novo usuário
            Usuario novoUsuario = new Usuario("João Silva", "joao@email.com",
                    "Rua das Madeiras, 123", "11987654321", "senha123");
            long id = usuarioDAO.inserir(novoUsuario);
            Log.d(TAG, "✅ Usuário inserido com ID: " + id);
        } else {
            Log.d(TAG, "⚠️ Usuário já existe no banco: " + usuarioExistente.getNome());
        }

        // Inserir mais um usuário
        Usuario usuarioExistente2 = usuarioDAO.buscarPorEmail("maria@email.com");
        if (usuarioExistente2 == null) {
            Usuario usuario2 = new Usuario("Maria Santos", "maria@email.com",
                    "Av. Principal, 456", "11912345678", "senha456");
            usuario2.setTipoPerfil("Funcionario");
            long id2 = usuarioDAO.inserir(usuario2);
            Log.d(TAG, "✅ Usuário inserido com ID: " + id2);
        }

        // Listar todos os usuários
        List<Usuario> listaUsuarios = usuarioDAO.listarTodos();
        Log.d(TAG, "\nTotal de Usuários cadastrados: " + listaUsuarios.size());

        for (Usuario usuario : listaUsuarios) {
            Log.d(TAG, "Usuario ID: " + usuario.getId() +
                    " | Nome: " + usuario.getNome() +
                    " | Email: " + usuario.getEmail() +
                    " | Perfil: " + usuario.getTipoPerfil());
        }

        // Testar Login
        Log.d(TAG, "\n--- Testando Login ---");
        Usuario usuarioLogado = usuarioDAO.validarLogin("joao@email.com", "senha123");
        if (usuarioLogado != null) {
            Log.d(TAG, "✅ Login bem-sucedido! Bem-vindo: " + usuarioLogado.getNome());
        } else {
            Log.d(TAG, "❌ Login falhou!");
        }
    }

    // ==================== TESTE 4: PRODUTOS ====================
    private void testarProdutos() {
        Log.d(TAG, "\n--- TESTE 4: PRODUTOS ---");
        ProdutoDAO produtoDAO = new ProdutoDAO(this);

        // Buscar IDs de categoria e status para usar no produto
        CategoriaDAO categoriaDAO = new CategoriaDAO(this);
        StatusDAO statusDAO = new StatusDAO(this);

        List<Categoria> categorias = categoriaDAO.listarTodas();
        List<Status> statusList = statusDAO.listarTodos();

        if (categorias.size() > 0 && statusList.size() > 0) {
            // Pegar primeira categoria (Madeiras Brutas) e primeiro status (Ativo)
            int idCategoria = categorias.get(0).getId();
            int idStatus = statusList.get(0).getId();

            // Verificar se produto já existe (buscar por nome seria ideal, mas vamos inserir sempre para teste)
            // Inserir produtos de teste
            Produto produto1 = new Produto("Tábua de Pinus", "Tábua de pinus tratada",
                    idCategoria, idStatus, 100, "M2", 50.00);
            long idProduto1 = produtoDAO.inserir(produto1);
            Log.d(TAG, "✅ Produto inserido com ID: " + idProduto1);

            // Inserir segundo produto
            Produto produto2 = new Produto("Compensado Naval", "Compensado naval 15mm",
                    idCategoria, idStatus, 50, "UN", 120.00);
            produto2.setUnidadeMedida("UN");
            long idProduto2 = produtoDAO.inserir(produto2);
            Log.d(TAG, "✅ Produto inserido com ID: " + idProduto2);

            // Listar produtos SEM JOIN
            Log.d(TAG, "\n--- Listando produtos SEM JOIN ---");
            List<Produto> produtosSemJoin = produtoDAO.listarTodos();
            Log.d(TAG, "Total de Produtos: " + produtosSemJoin.size());

            for (Produto p : produtosSemJoin) {
                Log.d(TAG, "Produto: " + p.getNome() +
                        " | Preço: " + p.getPrecoFormatado() +
                        " | Qtd: " + p.getQuantidade() + " " + p.getUnidadeMedida() +
                        " | FK Categoria: " + p.getFkCategoria() +
                        " | FK Status: " + p.getFkStatus());
            }

            // Listar produtos COM JOIN (melhor visualização)
            Log.d(TAG, "\n--- Listando produtos COM JOIN ---");
            List<Produto> produtosComJoin = produtoDAO.listarTodosComJoin();

            for (Produto p : produtosComJoin) {
                Log.d(TAG, "Produto: " + p.getNome() +
                        " | Preço: " + p.getPrecoFormatado() +
                        " | Qtd: " + p.getQuantidade() + " " + p.getUnidadeMedida() +
                        " | Categoria: " + p.getNomeCategoria() + // ← Veja a diferença!
                        " | Status: " + p.getNomeStatus());        // ← Nome ao invés de ID
            }

            // Teste de UPDATE
            Log.d(TAG, "\n--- Testando UPDATE ---");
            if (produtosComJoin.size() > 0) {
                Produto produtoParaAtualizar = produtosComJoin.get(0);
                produtoParaAtualizar.setPreco(55.00);
                produtoParaAtualizar.setQuantidade(150);
                int linhasAfetadas = produtoDAO.atualizar(produtoParaAtualizar);
                Log.d(TAG, "✅ Produto atualizado! Linhas afetadas: " + linhasAfetadas);

                // Buscar novamente para confirmar
                Produto produtoAtualizado = produtoDAO.buscarPorId(produtoParaAtualizar.getId());
                Log.d(TAG, "Novo preço: " + produtoAtualizado.getPrecoFormatado());
                Log.d(TAG, "Nova quantidade: " + produtoAtualizado.getQuantidade());
            }

        } else {
            Log.d(TAG, "❌ Não há categorias ou status cadastrados!");
        }
    }
}