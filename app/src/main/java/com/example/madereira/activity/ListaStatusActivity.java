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
import com.example.madereira.adapter.StatusAdapter;
import com.example.madereira.database.DAO.StatusDAO;
import com.example.madereira.model.Status;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ListaStatusActivity extends AppCompatActivity {

    private RecyclerView recyclerViewStatus;
    private TextView tvMensagemVazio;
    private ImageButton btnVoltar;
    private FloatingActionButton fabAdicionar;
    private StatusAdapter adapter;
    private List<Status> listaStatus;
    private StatusDAO statusDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_status);

        // Inicializar componentes
        inicializarComponentes();

        // Inicializar DAO
        statusDAO = new StatusDAO(this);

        // Configurar RecyclerView
        configurarRecyclerView();

        // Configurar listeners
        configurarListeners();

        // Carregar status
        carregarStatus();
    }

    private void inicializarComponentes() {
        recyclerViewStatus = findViewById(R.id.recyclerViewStatus);
        tvMensagemVazio = findViewById(R.id.tvMensagemVazio);
        btnVoltar = findViewById(R.id.btnVoltarListaStatus);
        fabAdicionar = findViewById(R.id.fabAdicionarStatus);
    }

    private void configurarRecyclerView() {
        listaStatus = new ArrayList<>();
        adapter = new StatusAdapter(listaStatus, new StatusAdapter.OnStatusClickListener() {
            @Override
            public void onStatusClick(Status status) {
                // Abrir tela de edição do status
                Intent intent = new Intent(ListaStatusActivity.this, StatusActivity.class);
                intent.putExtra("status_id", status.getId());
                startActivity(intent);
            }
        });

        recyclerViewStatus.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStatus.setAdapter(adapter);
    }

    private void configurarListeners() {
        // Botão voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Botão adicionar status
        fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaStatusActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        });
    }

    private void carregarStatus() {
        // Buscar status do banco
        listaStatus = statusDAO.listarTodos();

        // Atualizar adapter
        adapter.atualizarLista(listaStatus);

        // Mostrar/esconder mensagem de lista vazia
        if (listaStatus.isEmpty()) {
            tvMensagemVazio.setVisibility(View.VISIBLE);
            recyclerViewStatus.setVisibility(View.GONE);
        } else {
            tvMensagemVazio.setVisibility(View.GONE);
            recyclerViewStatus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar status quando voltar para esta tela
        carregarStatus();
    }
}

