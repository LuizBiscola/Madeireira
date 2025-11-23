package com.example.madereira.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.madereira.R;
import com.example.madereira.adapter.ProdutoAdapter;
import com.example.madereira.database.DAO.ProdutoDAO;
import com.example.madereira.model.Produto;
import com.example.madereira.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class InventarioProdutosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProdutos;
    private TextView tvTotalProdutos;
    private TextView tvMensagemVazio;
    private Button btnVoltar;
    private FloatingActionButton fabAdicionar;

    private ProdutoAdapter adapter;
    private List<Produto> listaProdutos;
    private ProdutoDAO produtoDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_produtos);

        // Inicializar SessionManager
        sessionManager = new SessionManager(this);

        // Verificar se está logado
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Você precisa fazer login primeiro!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(InventarioProdutosActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Inicializar componentes
        inicializarComponentes();

        // Inicializar DAO
        produtoDAO = new ProdutoDAO(this);

        // Configurar RecyclerView
        configurarRecyclerView();

        // Configurar listeners
        configurarListeners();

        // Carregar produtos
        carregarProdutos();
    }

    private void inicializarComponentes() {
        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);
        tvTotalProdutos = findViewById(R.id.tvTotalProdutos);
        tvMensagemVazio = findViewById(R.id.tvMensagemVazio);
        btnVoltar = findViewById(R.id.btnVoltarInventario);
        fabAdicionar = findViewById(R.id.fabAdicionarProduto);
    }

    private void configurarRecyclerView() {
        listaProdutos = new ArrayList<>();
        adapter = new ProdutoAdapter(listaProdutos, new ProdutoAdapter.OnProdutoClickListener() {
            @Override
            public void onProdutoClick(Produto produto) {
                // Abrir tela de edição do produto
                Intent intent = new Intent(InventarioProdutosActivity.this, ProdutoActivity.class);
                intent.putExtra("produto_id", produto.getId());
                startActivity(intent);
            }
        });

        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProdutos.setAdapter(adapter);
    }

    private void configurarListeners() {
        // Botão voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Botão adicionar produto
        fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventarioProdutosActivity.this, ProdutoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void carregarProdutos() {
        // Buscar produtos do banco com JOIN (para ter nome da categoria e status)
        listaProdutos = produtoDAO.listarTodosComJoin();

        // Atualizar adapter
        adapter.atualizarLista(listaProdutos);

        // Atualizar contador
        atualizarContador();

        // Mostrar/esconder mensagem de lista vazia
        if (listaProdutos.isEmpty()) {
            tvMensagemVazio.setVisibility(View.VISIBLE);
            recyclerViewProdutos.setVisibility(View.GONE);
        } else {
            tvMensagemVazio.setVisibility(View.GONE);
            recyclerViewProdutos.setVisibility(View.VISIBLE);
        }
    }

    private void atualizarContador() {
        int total = listaProdutos.size();
        if (total == 0) {
            tvTotalProdutos.setText("Nenhum produto cadastrado");
        } else if (total == 1) {
            tvTotalProdutos.setText("1 produto cadastrado");
        } else {
            tvTotalProdutos.setText(total + " produtos cadastrados");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            realizarLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void realizarLogout() {
        sessionManager.logout();
        Toast.makeText(this, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(InventarioProdutosActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar produtos ao voltar para a tela
        carregarProdutos();
    }
}

