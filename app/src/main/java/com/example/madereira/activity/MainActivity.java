package com.example.madereira.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.madereira.R;
import com.example.madereira.utils.SessionManager;

public class MainActivity extends AppCompatActivity {
    private Button btnExplorarProduto;
    private Button btnEntrarConta;
    private Button btnCriarConta;
    private Button btnVisualizarUsuarios;
    private Button btnGerenciarCategorias;
    private Button btnGerenciarStatus;
    private Button btnLogout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();

        sessionManager = new SessionManager(this);

        atualizarUIEstadoLogin();

        configurarListeners();
    }

    private void inicializarComponentes() {
        btnExplorarProduto = findViewById(R.id.btnExplorarProduto);
        btnEntrarConta = findViewById(R.id.btnEntrarConta);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        btnVisualizarUsuarios = findViewById(R.id.btnVisualizarUsuarios);
        btnGerenciarCategorias = findViewById(R.id.btnGerenciarCategorias);
        btnGerenciarStatus = findViewById(R.id.btnGerenciarStatus);
        btnLogout = findViewById(R.id.btnLogout);
    }


    private void configurarListeners() {
        btnExplorarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaProdutosActivity.class);
                startActivity(intent);
            }
        });

        btnEntrarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UsuariosActivity.class);
                startActivity(intent);
            }
        });

        btnVisualizarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaUsuariosActivity.class);
                startActivity(intent);
            }
        });

        btnGerenciarCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaCategoriasActivity.class);
                startActivity(intent);
            }
        });

        btnGerenciarStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaStatusActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarLogout();
            }
        });
    }

    private void atualizarUIEstadoLogin() {
        if (sessionManager.isLoggedIn()) {
            // Usuário está logado
            btnEntrarConta.setVisibility(View.GONE);
            btnCriarConta.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);

            String nomeUsuario = sessionManager.getUserName();
            Toast.makeText(this, "Bem-vindo, " + nomeUsuario + "!", Toast.LENGTH_SHORT).show();
        } else {
            // Usuário NÃO está logado
            btnEntrarConta.setVisibility(View.VISIBLE);
            btnCriarConta.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    private void realizarLogout() {
        sessionManager.logout();
        Toast.makeText(this, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show();
        atualizarUIEstadoLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualizar UI quando voltar para esta tela
        atualizarUIEstadoLogin();
    }
}