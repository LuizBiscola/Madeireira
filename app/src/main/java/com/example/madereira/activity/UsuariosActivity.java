package com.example.madereira.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.madereira.R;
import com.example.madereira.database.DAO.UsuarioDAO;
import com.example.madereira.model.Usuario;

public class UsuariosActivity extends AppCompatActivity {

    // Componentes da UI
    private Button btnVoltar, btnSalvarUsuario;
    private TextView tvTituloAba;
    private EditText etNome, etEmail, etTelefone, etEndereco, etCpfCnpj, etSenha, etConfirmarSenha;
    private Spinner spinnerTipoPerfil;

    // DAO
    private UsuarioDAO usuarioDAO;

    // Usuário para edição (se houver)
    private Usuario usuarioEmEdicao;
    private int usuarioId = -1;

    // Tipos de perfil
    private String[] tiposPerfil = {"Cliente", "Funcionario", "Administrador"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        // Inicializar DAO
        usuarioDAO = new UsuarioDAO(this);

        // Inicializar componentes
        inicializarComponentes();

        // Carregar tipos de perfil no Spinner
        carregarTiposPerfil();

        // Verificar se é edição
        verificarModoEdicao();

        // Configurar listeners
        configurarListeners();
    }

    private void inicializarComponentes() {
        btnVoltar = findViewById(R.id.btnVoltarUsuario);
        btnSalvarUsuario = findViewById(R.id.btnSalvarUsuario);
        tvTituloAba = findViewById(R.id.tvTituloAbaUsuario);
        etNome = findViewById(R.id.etNomeUsuario);
        etEmail = findViewById(R.id.etEmailUsuario);
        etTelefone = findViewById(R.id.etTelefoneUsuario);
        etEndereco = findViewById(R.id.etEnderecoUsuario);
        etCpfCnpj = findViewById(R.id.etCpfCnpjUsuario);
        etSenha = findViewById(R.id.etSenhaUsuario);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenhaUsuario);
        spinnerTipoPerfil = findViewById(R.id.spinnerTipoPerfil);
    }

    private void carregarTiposPerfil() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                tiposPerfil
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoPerfil.setAdapter(adapter);
    }

    private void verificarModoEdicao() {
        // Verificar se foi passado um ID de usuário para edição
        usuarioId = getIntent().getIntExtra("usuario_id", -1);

        if (usuarioId != -1) {
            // Modo edição
            tvTituloAba.setText("Editar Usuário");
            btnSalvarUsuario.setText("Atualizar Usuário");
            carregarDadosUsuario(usuarioId);

            // Campos de senha não obrigatórios na edição
            etSenha.setHint("Deixe vazio para manter a senha atual");
            etConfirmarSenha.setHint("Deixe vazio para manter a senha atual");
        } else {
            // Modo inserção
            tvTituloAba.setText("Novo Usuário");
            btnSalvarUsuario.setText("Salvar Usuário");
        }
    }

    private void carregarDadosUsuario(int id) {
        usuarioEmEdicao = usuarioDAO.buscarPorId(id);

        if (usuarioEmEdicao != null) {
            etNome.setText(usuarioEmEdicao.getNome());
            etEmail.setText(usuarioEmEdicao.getEmail());
            etTelefone.setText(usuarioEmEdicao.getTelefone());
            etEndereco.setText(usuarioEmEdicao.getEndereco());
            etCpfCnpj.setText(usuarioEmEdicao.getCpfCnpj());

            // Selecionar tipo de perfil no Spinner
            String tipoPerfil = usuarioEmEdicao.getTipoPerfil();
            for (int i = 0; i < tiposPerfil.length; i++) {
                if (tiposPerfil[i].equals(tipoPerfil)) {
                    spinnerTipoPerfil.setSelection(i);
                    break;
                }
            }

            // Limpar campos de senha
            etSenha.setText("");
            etConfirmarSenha.setText("");
        }
    }

    private void configurarListeners() {
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSalvarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarUsuario();
            }
        });
    }

    private void salvarUsuario() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }

        // Obter dados dos campos
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();
        String endereco = etEndereco.getText().toString().trim();
        String cpfCnpj = etCpfCnpj.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String tipoPerfil = spinnerTipoPerfil.getSelectedItem().toString();

        if (usuarioId != -1) {
            // Modo edição
            usuarioEmEdicao.setNome(nome);
            usuarioEmEdicao.setEmail(email);
            usuarioEmEdicao.setTelefone(telefone);
            usuarioEmEdicao.setEndereco(endereco);
            usuarioEmEdicao.setCpfCnpj(cpfCnpj);
            usuarioEmEdicao.setTipoPerfil(tipoPerfil);

            // Atualizar senha apenas se foi informada
            if (!senha.isEmpty()) {
                usuarioEmEdicao.setSenha(senha);
            }

            int resultado = usuarioDAO.atualizar(usuarioEmEdicao);

            if (resultado > 0) {
                Toast.makeText(this, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar usuário", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Modo inserção

            // Verificar se email já existe
            Usuario usuarioExistente = usuarioDAO.buscarPorEmail(email);
            if (usuarioExistente != null) {
                etEmail.setError("Este email já está cadastrado");
                etEmail.requestFocus();
                return;
            }

            Usuario novoUsuario = new Usuario(nome, email, endereco, telefone, senha);
            novoUsuario.setTipoPerfil(tipoPerfil);
            novoUsuario.setCpfCnpj(cpfCnpj);

            long resultado = usuarioDAO.inserir(novoUsuario);

            if (resultado != -1) {
                Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                limparCampos();
                finish();
            } else {
                Toast.makeText(this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validarCampos() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        // Validar nome
        if (nome.isEmpty()) {
            etNome.setError("Nome é obrigatório");
            etNome.requestFocus();
            return false;
        }

        // Validar email
        if (email.isEmpty()) {
            etEmail.setError("Email é obrigatório");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            etEmail.requestFocus();
            return false;
        }

        // Validar telefone
        if (telefone.isEmpty()) {
            etTelefone.setError("Telefone é obrigatório");
            etTelefone.requestFocus();
            return false;
        }

        // Validar senha (apenas no modo inserção ou se informada no modo edição)
        if (usuarioId == -1) {
            // Modo inserção - senha obrigatória
            if (senha.isEmpty()) {
                etSenha.setError("Senha é obrigatória");
                etSenha.requestFocus();
                return false;
            }

            if (senha.length() < 6) {
                etSenha.setError("Senha deve ter no mínimo 6 caracteres");
                etSenha.requestFocus();
                return false;
            }

            if (confirmarSenha.isEmpty()) {
                etConfirmarSenha.setError("Confirme a senha");
                etConfirmarSenha.requestFocus();
                return false;
            }

            if (!senha.equals(confirmarSenha)) {
                etConfirmarSenha.setError("As senhas não coincidem");
                etConfirmarSenha.requestFocus();
                return false;
            }
        } else {
            // Modo edição - validar senha apenas se foi informada
            if (!senha.isEmpty()) {
                if (senha.length() < 6) {
                    etSenha.setError("Senha deve ter no mínimo 6 caracteres");
                    etSenha.requestFocus();
                    return false;
                }

                if (!senha.equals(confirmarSenha)) {
                    etConfirmarSenha.setError("As senhas não coincidem");
                    etConfirmarSenha.requestFocus();
                    return false;
                }
            }
        }

        return true;
    }

    private void limparCampos() {
        etNome.setText("");
        etEmail.setText("");
        etTelefone.setText("");
        etEndereco.setText("");
        etCpfCnpj.setText("");
        etSenha.setText("");
        etConfirmarSenha.setText("");
        spinnerTipoPerfil.setSelection(0);
    }
}
