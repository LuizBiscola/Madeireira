package com.example.madereira.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.madereira.R;
import com.example.madereira.adapter.UsuarioAdapter;
import com.example.madereira.database.DAO.UsuarioDAO;
import com.example.madereira.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ListaUsuariosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsuarios;
    private TextView tvTotalUsuarios;
    private TextView tvMensagemVazio;
    private ImageButton btnVoltar;
    private FloatingActionButton fabAdicionar;
    private UsuarioAdapter adapter;
    private List<Usuario> listaUsuarios;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        inicializarComponentes();

        usuarioDAO = new UsuarioDAO(this);

        configurarRecyclerView();

        configurarListeners();

        carregarUsuarios();
    }

    private void inicializarComponentes() {
        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);
        tvMensagemVazio = findViewById(R.id.tvMensagemVazio);
        btnVoltar = findViewById(R.id.btnVoltarListaUsuarios);
        fabAdicionar = findViewById(R.id.fabAdicionarUsuario);
    }

    private void configurarRecyclerView() {
        listaUsuarios = new ArrayList<>();
        adapter = new UsuarioAdapter(listaUsuarios, new UsuarioAdapter.OnUsuarioClickListener() {
            @Override
            public void onUsuarioClick(Usuario usuario) {
                // Abrir tela de edição do usuário
                Intent intent = new Intent(ListaUsuariosActivity.this, UsuariosActivity.class);
                intent.putExtra("usuario_id", usuario.getId());
                startActivity(intent);
            }
        });

        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsuarios.setAdapter(adapter);
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
                Intent intent = new Intent(ListaUsuariosActivity.this, UsuariosActivity.class);
                startActivity(intent);
            }
        });
    }

    private void carregarUsuarios() {
        listaUsuarios = usuarioDAO.listarTodos();

        adapter.atualizarLista(listaUsuarios);

        atualizarInterface();
    }

    private void atualizarInterface() {
        int total = listaUsuarios.size();

        // Mostrar ou ocultar mensagem de lista vazia
        if (total == 0) {
            tvMensagemVazio.setVisibility(View.VISIBLE);
            recyclerViewUsuarios.setVisibility(View.GONE);
        } else {
            tvMensagemVazio.setVisibility(View.GONE);
            recyclerViewUsuarios.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar a lista quando voltar para a activity
        carregarUsuarios();
    }
}

