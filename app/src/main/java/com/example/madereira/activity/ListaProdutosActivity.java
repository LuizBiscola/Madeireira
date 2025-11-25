package com.example.madereira.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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

public class ListaProdutosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProdutos;
    private TextView tvMensagemVazio;
    private ImageButton btnVoltar;
    private FloatingActionButton fabAdicionar;

    private ProdutoAdapter adapter;
    private List<Produto> listaProdutos;
    private ProdutoDAO produtoDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        // Inicializar SessionManager
        sessionManager = new SessionManager(this);

        // Verificar se está logado
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Você precisa fazer login primeiro!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListaProdutosActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }

        inicializarComponentes();

        produtoDAO = new ProdutoDAO(this);

        configurarRecyclerView();

        configurarListeners();

        carregarProdutos();
    }

    private void inicializarComponentes() {
        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);
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
                Intent intent = new Intent(ListaProdutosActivity.this, ProdutoActivity.class);
                intent.putExtra("produto_id", produto.getId());
                startActivity(intent);
            }
        });

        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProdutos.setAdapter(adapter);
    }

    private void configurarListeners() {
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaProdutosActivity.this, ProdutoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void carregarProdutos() {
        listaProdutos = produtoDAO.listarTodosComJoin();

        adapter.atualizarLista(listaProdutos);

        // Mostrar/esconder mensagem de lista vazia
        if (listaProdutos.isEmpty()) {
            tvMensagemVazio.setVisibility(View.VISIBLE);
            recyclerViewProdutos.setVisibility(View.GONE);
        } else {
            tvMensagemVazio.setVisibility(View.GONE);
            recyclerViewProdutos.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar produtos ao voltar para a tela
        carregarProdutos();
    }
}

