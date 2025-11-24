package com.example.madereira.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.madereira.R;
import com.example.madereira.database.DAO.CategoriaDAO;
import com.example.madereira.database.DAO.ProdutoDAO;
import com.example.madereira.model.Categoria;
import com.example.madereira.model.Produto;
import java.util.List;

public class ProdutoActivity extends AppCompatActivity {

    // Componentes da UI
    private Button btnSalvarProduto, btnExcluirProduto;
    private ImageButton btnVoltar;
    private TextView tvTituloAba;
    private EditText etNomeProduto, etDescricao, etQuantidade, etPreco;
    private Spinner spinnerCategoria;

    // DAOs
    private ProdutoDAO produtoDAO;
    private CategoriaDAO categoriaDAO;

    // Lista de categorias
    private List<Categoria> listaCategorias;

    // Produto para edição (se houver)
    private Produto produtoEmEdicao;
    private int produtoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_produto);

        // Inicializar DAOs
        produtoDAO = new ProdutoDAO(this);
        categoriaDAO = new CategoriaDAO(this);

        // Inicializar componentes
        inicializarComponentes();

        // Carregar categorias no Spinner
        carregarCategorias();

        // Verificar se é edição
        verificarModoEdicao();

        // Configurar listeners
        configurarListeners();
    }

    private void inicializarComponentes() {
        btnVoltar = findViewById(R.id.btnVoltar);
        btnSalvarProduto = findViewById(R.id.btnSalvarProduto);
        btnExcluirProduto = findViewById(R.id.btnExcluirProduto);
        tvTituloAba = findViewById(R.id.tvTituloAba);
        etNomeProduto = findViewById(R.id.etNomeProduto);
        etDescricao = findViewById(R.id.etDescricao);
        etQuantidade = findViewById(R.id.etQuantidade);
        etPreco = findViewById(R.id.etPreco);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
    }

    private void carregarCategorias() {
        listaCategorias = categoriaDAO.listarTodas();

        if (listaCategorias.isEmpty()) {
            Toast.makeText(this, "Nenhuma categoria disponível", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaCategorias
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    private void verificarModoEdicao() {
        // Verificar se foi passado um ID de produto para edição
        produtoId = getIntent().getIntExtra("produto_id", -1);

        if (produtoId != -1) {
            // Modo edição
            tvTituloAba.setText("Editar Produto");
            btnSalvarProduto.setText("Atualizar Produto");
            btnExcluirProduto.setVisibility(View.VISIBLE);
            carregarDadosProduto(produtoId);
        } else {
            // Modo inserção
            tvTituloAba.setText("Novo Produto");
            btnSalvarProduto.setText("Salvar Produto");
            btnExcluirProduto.setVisibility(View.GONE);
        }
    }

    private void carregarDadosProduto(int id) {
        produtoEmEdicao = produtoDAO.buscarPorId(id);

        if (produtoEmEdicao != null) {
            etNomeProduto.setText(produtoEmEdicao.getNome());
            etDescricao.setText(produtoEmEdicao.getDescricao());
            etQuantidade.setText(String.valueOf((int)produtoEmEdicao.getQuantidade()));
            etPreco.setText(String.format("%.2f", produtoEmEdicao.getPreco()));

            // Selecionar categoria no Spinner
            for (int i = 0; i < listaCategorias.size(); i++) {
                if (listaCategorias.get(i).getId() == produtoEmEdicao.getFkCategoria()) {
                    spinnerCategoria.setSelection(i);
                    break;
                }
            }
        }
    }

    private void configurarListeners() {
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSalvarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarProduto();
            }
        });

        btnExcluirProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarExclusaoProduto();
            }
        });
    }

    private void salvarProduto() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }

        // Obter dados dos campos
        String nome = etNomeProduto.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();
        double quantidade = Double.parseDouble(etQuantidade.getText().toString().trim());
        String precoStr = etPreco.getText().toString().trim().replace(",", ".");
        double preco = Double.parseDouble(precoStr);

        Categoria categoriaSelecionada = (Categoria) spinnerCategoria.getSelectedItem();
        int fkCategoria = categoriaSelecionada.getId();

        // Status padrão: Ativo (id = 1)
        int fkStatus = 1;

        // Verificar estoque e ajustar status
        if (quantidade <= 0) {
            fkStatus = 3; // Em Falta
        }

        if (produtoId != -1) {
            // Modo edição
            produtoEmEdicao.setNome(nome);
            produtoEmEdicao.setDescricao(descricao);
            produtoEmEdicao.setFkCategoria(fkCategoria);
            produtoEmEdicao.setFkStatus(fkStatus);
            produtoEmEdicao.setQuantidade(quantidade);
            produtoEmEdicao.setPreco(preco);

            int resultado = produtoDAO.atualizar(produtoEmEdicao);

            if (resultado > 0) {
                Toast.makeText(this, "Produto atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar produto", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Modo inserção
            Produto novoProduto = new Produto(nome, descricao, fkCategoria, fkStatus, quantidade, "un", preco);

            long resultado = produtoDAO.inserir(novoProduto);

            if (resultado != -1) {
                Toast.makeText(this, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                limparCampos();
                finish();
            } else {
                Toast.makeText(this, "Erro ao cadastrar produto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validarCampos() {
        String nome = etNomeProduto.getText().toString().trim();
        String quantidadeStr = etQuantidade.getText().toString().trim();
        String precoStr = etPreco.getText().toString().trim();

        if (nome.isEmpty()) {
            etNomeProduto.setError("Nome é obrigatório");
            etNomeProduto.requestFocus();
            return false;
        }

        if (quantidadeStr.isEmpty()) {
            etQuantidade.setError("Quantidade é obrigatória");
            etQuantidade.requestFocus();
            return false;
        }

        if (precoStr.isEmpty()) {
            etPreco.setError("Preço é obrigatório");
            etPreco.requestFocus();
            return false;
        }

        try {
            double quantidade = Double.parseDouble(quantidadeStr);
            if (quantidade < 0) {
                etQuantidade.setError("Quantidade deve ser maior ou igual a zero");
                etQuantidade.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etQuantidade.setError("Quantidade inválida");
            etQuantidade.requestFocus();
            return false;
        }

        try {
            String precoLimpo = precoStr.replace(",", ".");
            double preco = Double.parseDouble(precoLimpo);
            if (preco <= 0) {
                etPreco.setError("Preço deve ser maior que zero");
                etPreco.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etPreco.setError("Preço inválido");
            etPreco.requestFocus();
            return false;
        }

        if (spinnerCategoria.getSelectedItem() == null) {
            Toast.makeText(this, "Selecione uma categoria", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void limparCampos() {
        etNomeProduto.setText("");
        etDescricao.setText("");
        etQuantidade.setText("");
        etPreco.setText("");
        spinnerCategoria.setSelection(0);
    }

    /**
     * Confirmar exclusão do produto
     */
    private void confirmarExclusaoProduto() {
        if (produtoEmEdicao == null) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir o produto \"" + produtoEmEdicao.getNome() + "\"?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirProduto();
                    }
                })
                .setNegativeButton("Não", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Excluir produto do banco de dados
     */
    private void excluirProduto() {
        try {
            int resultado = produtoDAO.excluir(produtoId);

            if (resultado > 0) {
                Toast.makeText(this, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao excluir produto", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao excluir produto: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
