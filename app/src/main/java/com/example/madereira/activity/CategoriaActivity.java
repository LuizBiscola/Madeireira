package com.example.madereira.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.madereira.R;
import com.example.madereira.database.DAO.CategoriaDAO;
import com.example.madereira.model.Categoria;

public class CategoriaActivity extends AppCompatActivity {

    // Componentes da UI
    private ImageButton btnVoltar;
    private Button btnSalvarCategoria, btnExcluirCategoria;
    private TextView tvTituloAba;
    private EditText etDescricaoCategoria;

    // DAO
    private CategoriaDAO categoriaDAO;

    // Categoria para edição (se houver)
    private Categoria categoriaEmEdicao;
    private int categoriaId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        // Inicializar DAO
        categoriaDAO = new CategoriaDAO(this);

        // Inicializar componentes
        inicializarComponentes();

        // Verificar se é edição
        verificarModoEdicao();

        // Configurar listeners
        configurarListeners();
    }

    private void inicializarComponentes() {
        btnVoltar = findViewById(R.id.btnVoltarCategoria);
        btnSalvarCategoria = findViewById(R.id.btnSalvarCategoria);
        btnExcluirCategoria = findViewById(R.id.btnExcluirCategoria);
        tvTituloAba = findViewById(R.id.tvTituloAbaCategoria);
        etDescricaoCategoria = findViewById(R.id.etDescricaoCategoria);
    }

    private void verificarModoEdicao() {
        // Verificar se foi passado um ID de categoria para edição
        categoriaId = getIntent().getIntExtra("categoria_id", -1);

        if (categoriaId != -1) {
            // Modo edição
            tvTituloAba.setText("Editar Categoria");
            btnSalvarCategoria.setText("Atualizar Categoria");
            btnExcluirCategoria.setVisibility(View.VISIBLE);
            carregarDadosCategoria(categoriaId);
        } else {
            // Modo inserção
            tvTituloAba.setText("Nova Categoria");
            btnSalvarCategoria.setText("Salvar Categoria");
            btnExcluirCategoria.setVisibility(View.GONE);
        }
    }

    private void carregarDadosCategoria(int id) {
        categoriaEmEdicao = categoriaDAO.buscarPorId(id);

        if (categoriaEmEdicao != null) {
            etDescricaoCategoria.setText(categoriaEmEdicao.getDescricao());
        }
    }

    private void configurarListeners() {
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSalvarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarCategoria();
            }
        });

        btnExcluirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarExclusaoCategoria();
            }
        });
    }

    private void salvarCategoria() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }

        try {
            // Obter dados dos campos
            String descricao = etDescricaoCategoria.getText().toString().trim();

            if (categoriaId != -1) {
                // Modo edição
                categoriaEmEdicao.setDescricao(descricao);

                int resultado = categoriaDAO.atualizar(categoriaEmEdicao);

                if (resultado > 0) {
                    Toast.makeText(this, "Categoria atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao atualizar categoria", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Modo inserção
                Categoria novaCategoria = new Categoria(descricao);

                long resultado = categoriaDAO.inserir(novaCategoria);

                if (resultado != -1) {
                    Toast.makeText(this, "Categoria cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao cadastrar categoria", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao salvar categoria: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        String descricao = etDescricaoCategoria.getText().toString().trim();

        // Validar descrição
        if (descricao.isEmpty()) {
            etDescricaoCategoria.setError("Descrição é obrigatória");
            etDescricaoCategoria.requestFocus();
            return false;
        }

        if (descricao.length() < 3) {
            etDescricaoCategoria.setError("Descrição deve ter no mínimo 3 caracteres");
            etDescricaoCategoria.requestFocus();
            return false;
        }

        return true;
    }

    private void limparCampos() {
        etDescricaoCategoria.setText("");
    }

    /**
     * Confirmar exclusão da categoria
     */
    private void confirmarExclusaoCategoria() {
        if (categoriaEmEdicao == null) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir a categoria \"" + categoriaEmEdicao.getDescricao() + "\"?\n\nAtenção: Produtos associados a esta categoria podem ser afetados.")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirCategoria();
                    }
                })
                .setNegativeButton("Não", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Excluir categoria do banco de dados
     */
    private void excluirCategoria() {
        try {
            int resultado = categoriaDAO.excluir(categoriaId);

            if (resultado > 0) {
                Toast.makeText(this, "Categoria excluída com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao excluir categoria", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao excluir categoria: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

