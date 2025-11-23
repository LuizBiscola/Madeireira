package com.example.madereira.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.madereira.R;
import com.example.madereira.model.Produto;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private List<Produto> listaProdutos;
    private OnProdutoClickListener listener;

    // Interface para clicks nos itens
    public interface OnProdutoClickListener {
        void onProdutoClick(Produto produto);
    }

    public ProdutoAdapter(List<Produto> listaProdutos, OnProdutoClickListener listener) {
        this.listaProdutos = listaProdutos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produto = listaProdutos.get(position);
        holder.bind(produto, listener);
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    // Método para atualizar a lista
    public void atualizarLista(List<Produto> novaLista) {
        this.listaProdutos = novaLista;
        notifyDataSetChanged();
    }

    // ViewHolder
    static class ProdutoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNome;
        private TextView tvCategoria;
        private TextView tvQuantidade;
        private TextView tvPreco;
        private TextView tvStatus;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeProdutoItem);
            tvCategoria = itemView.findViewById(R.id.tvCategoriaProdutoItem);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidadeProdutoItem);
            tvPreco = itemView.findViewById(R.id.tvPrecoProdutoItem);
            tvStatus = itemView.findViewById(R.id.tvStatusProdutoItem);
        }

        public void bind(final Produto produto, final OnProdutoClickListener listener) {
            tvNome.setText(produto.getNome());

            // Mostrar categoria (se tiver)
            if (produto.getNomeCategoria() != null) {
                tvCategoria.setText(produto.getNomeCategoria());
            } else {
                tvCategoria.setText("Categoria: " + produto.getFkCategoria());
            }

            // Quantidade
            tvQuantidade.setText(String.format("Qtd: %.0f %s",
                produto.getQuantidade(),
                produto.getUnidadeMedida()));

            // Preço formatado
            tvPreco.setText(produto.getPrecoFormatado());

            // Status com cores
            if (produto.getNomeStatus() != null) {
                tvStatus.setText(produto.getNomeStatus());

                // Colorir de acordo com o status
                switch (produto.getNomeStatus()) {
                    case "Ativo":
                        tvStatus.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
                        break;
                    case "Inativo":
                        tvStatus.setBackgroundColor(Color.parseColor("#9E9E9E")); // Cinza
                        break;
                    case "Em Falta":
                        tvStatus.setBackgroundColor(Color.parseColor("#FF9800")); // Laranja
                        break;
                    case "Descontinuado":
                        tvStatus.setBackgroundColor(Color.parseColor("#F44336")); // Vermelho
                        break;
                    default:
                        tvStatus.setBackgroundColor(Color.parseColor("#2196F3")); // Azul
                }
            } else {
                tvStatus.setText("Status: " + produto.getFkStatus());
                tvStatus.setBackgroundColor(Color.parseColor("#757575"));
            }

            // Click no item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onProdutoClick(produto);
                    }
                }
            });
        }
    }
}
