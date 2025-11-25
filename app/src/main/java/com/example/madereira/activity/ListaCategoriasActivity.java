package com.example.madereira.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.madereira.R;
import com.example.madereira.adapter.CategoriaAdapter;
import com.example.madereira.database.DAO.CategoriaDAO;
import com.example.madereira.model.Categoria;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ListaCategoriasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategorias;
    private TextView tvMensagemVazio;
    private ImageButton btnVoltar;
    private FloatingActionButton fabAdicionar;
    private CategoriaAdapter adapter;
    private List<Categoria> listaCategorias;
    private CategoriaDAO categoriaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_categorias);

        inicializarComponentes();

        categoriaDAO = new CategoriaDAO(this);

        configurarRecyclerView();

        configurarListeners();

        carregarCategorias();
    }

    private void inicializarComponentes() {
        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);
        tvMensagemVazio = findViewById(R.id.tvMensagemVazio);
        btnVoltar = findViewById(R.id.btnVoltarListaCategorias);
        fabAdicionar = findViewById(R.id.fabAdicionarCategoria);
    }

    private void configurarRecyclerView() {
        listaCategorias = new ArrayList<>();
        adapter = new CategoriaAdapter(listaCategorias, new CategoriaAdapter.OnCategoriaClickListener() {
            @Override
            public void onCategoriaClick(Categoria categoria) {
                Intent intent = new Intent(ListaCategoriasActivity.this, CategoriaActivity.class);
                intent.putExtra("categoria_id", categoria.getId());
                startActivity(intent);
            }
        });

        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategorias.setAdapter(adapter);
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
                Intent intent = new Intent(ListaCategoriasActivity.this, CategoriaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void carregarCategorias() {
        listaCategorias = categoriaDAO.listarTodas();

        adapter.atualizarLista(listaCategorias);

        // Mostrar/esconder mensagem de lista vazia
        if (listaCategorias.isEmpty()) {
            tvMensagemVazio.setVisibility(View.VISIBLE);
            recyclerViewCategorias.setVisibility(View.GONE);
        } else {
            tvMensagemVazio.setVisibility(View.GONE);
            recyclerViewCategorias.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar categorias quando voltar para a tela
        carregarCategorias();
    }
}

