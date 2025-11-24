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
import com.example.madereira.database.DAO.StatusDAO;
import com.example.madereira.model.Status;

public class StatusActivity extends AppCompatActivity {

    // Componentes da UI
    private ImageButton btnVoltar;
    private Button btnSalvarStatus, btnExcluirStatus;
    private TextView tvTituloAba;
    private EditText etDescricaoStatus;

    // DAO
    private StatusDAO statusDAO;

    // Status para edição (se houver)
    private Status statusEmEdicao;
    private int statusId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Inicializar DAO
        statusDAO = new StatusDAO(this);

        // Inicializar componentes
        inicializarComponentes();

        // Verificar se é edição
        verificarModoEdicao();

        // Configurar listeners
        configurarListeners();
    }

    private void inicializarComponentes() {
        btnVoltar = findViewById(R.id.btnVoltarStatus);
        btnSalvarStatus = findViewById(R.id.btnSalvarStatus);
        btnExcluirStatus = findViewById(R.id.btnExcluirStatus);
        tvTituloAba = findViewById(R.id.tvTituloAbaStatus);
        etDescricaoStatus = findViewById(R.id.etDescricaoStatus);
    }

    private void verificarModoEdicao() {
        // Verificar se foi passado um ID de status para edição
        statusId = getIntent().getIntExtra("status_id", -1);

        if (statusId != -1) {
            // Modo edição
            tvTituloAba.setText("Editar Status");
            btnSalvarStatus.setText("Atualizar Status");
            btnExcluirStatus.setVisibility(View.VISIBLE);
            carregarDadosStatus(statusId);
        } else {
            // Modo inserção
            tvTituloAba.setText("Novo Status");
            btnSalvarStatus.setText("Salvar Status");
            btnExcluirStatus.setVisibility(View.GONE);
        }
    }

    private void carregarDadosStatus(int id) {
        statusEmEdicao = statusDAO.buscarPorId(id);

        if (statusEmEdicao != null) {
            etDescricaoStatus.setText(statusEmEdicao.getDescricao());
        }
    }

    private void configurarListeners() {
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSalvarStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarStatus();
            }
        });

        btnExcluirStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarExclusaoStatus();
            }
        });
    }

    private void salvarStatus() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }

        try {
            // Obter dados dos campos
            String descricao = etDescricaoStatus.getText().toString().trim();

            if (statusId != -1) {
                // Modo edição
                statusEmEdicao.setDescricao(descricao);

                int resultado = statusDAO.atualizar(statusEmEdicao);

                if (resultado > 0) {
                    Toast.makeText(this, "Status atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao atualizar status", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Modo inserção
                Status novoStatus = new Status(descricao);

                long resultado = statusDAO.inserir(novoStatus);

                if (resultado != -1) {
                    Toast.makeText(this, "Status cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao cadastrar status", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao salvar status: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        String descricao = etDescricaoStatus.getText().toString().trim();

        // Validar descrição
        if (descricao.isEmpty()) {
            etDescricaoStatus.setError("Descrição é obrigatória");
            etDescricaoStatus.requestFocus();
            return false;
        }

        if (descricao.length() < 3) {
            etDescricaoStatus.setError("Descrição deve ter no mínimo 3 caracteres");
            etDescricaoStatus.requestFocus();
            return false;
        }

        return true;
    }

    private void limparCampos() {
        etDescricaoStatus.setText("");
    }

    /**
     * Confirmar exclusão do status
     */
    private void confirmarExclusaoStatus() {
        if (statusEmEdicao == null) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir o status \"" + statusEmEdicao.getDescricao() + "\"?\n\nAtenção: Produtos associados a este status podem ser afetados.")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirStatus();
                    }
                })
                .setNegativeButton("Não", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Excluir status do banco de dados
     */
    private void excluirStatus() {
        try {
            int resultado = statusDAO.excluir(statusId);

            if (resultado > 0) {
                Toast.makeText(this, "Status excluído com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao excluir status", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao excluir status: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

