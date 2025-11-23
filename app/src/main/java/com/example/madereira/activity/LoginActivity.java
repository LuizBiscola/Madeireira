package com.example.madereira.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.madereira.R;
import com.example.madereira.database.DAO.UsuarioDAO;
import com.example.madereira.model.Usuario;
import com.example.madereira.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnEntrar, btnCriarConta, btnVoltar;
    private UsuarioDAO usuarioDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar componentes
        inicializarComponentes();

        // Inicializar DAO e SessionManager
        usuarioDAO = new UsuarioDAO(this);
        sessionManager = new SessionManager(this);

        // Verificar se já está logado
        if (sessionManager.isLoggedIn()) {
            irParaInventario();
            return;
        }

        // Configurar listeners
        configurarListeners();
    }

    private void inicializarComponentes() {
        etEmail = findViewById(R.id.etEmailLogin);
        etSenha = findViewById(R.id.etSenhaLogin);
        btnEntrar = findViewById(R.id.btnEntrarLogin);
        btnCriarConta = findViewById(R.id.btnCriarContaLogin);
        btnVoltar = findViewById(R.id.btnVoltarLogin);
    }

    private void configurarListeners() {
        // Botão Entrar
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarLogin();
            }
        });

        // Botão Criar Conta
        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UsuariosActivity.class);
                startActivity(intent);
            }
        });

        // Botão Voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void realizarLogin() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        // Validações
        if (email.isEmpty()) {
            etEmail.setError("Digite seu email");
            etEmail.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            etSenha.setError("Digite sua senha");
            etSenha.requestFocus();
            return;
        }

        // Validar login no banco de dados
        Usuario usuario = usuarioDAO.validarLogin(email, senha);

        if (usuario != null) {
            // Login bem-sucedido
            sessionManager.createLoginSession(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getTipoPerfil()
            );

            Toast.makeText(this, "Bem-vindo, " + usuario.getNome() + "!", Toast.LENGTH_SHORT).show();
            irParaInventario();
        } else {
            // Login falhou
            Toast.makeText(this, "Email ou senha incorretos!", Toast.LENGTH_SHORT).show();
        }
    }

    private void irParaInventario() {
        Intent intent = new Intent(LoginActivity.this, InventarioProdutosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificar se voltou da tela de cadastro e tentar logar automaticamente
        if (sessionManager.isLoggedIn()) {
            irParaInventario();
        }
    }
}

