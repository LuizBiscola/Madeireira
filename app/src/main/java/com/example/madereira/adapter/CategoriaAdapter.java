package com.example.madereira.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.madereira.R;
import com.example.madereira.model.Categoria;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private List<Categoria> listaCategorias;
    private OnCategoriaClickListener listener;

    public interface OnCategoriaClickListener {
        void onCategoriaClick(Categoria categoria);
    }

    public CategoriaAdapter(List<Categoria> listaCategorias, OnCategoriaClickListener listener) {
        this.listaCategorias = listaCategorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = listaCategorias.get(position);
        holder.bind(categoria, listener);
    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    public void atualizarLista(List<Categoria> novaLista) {
        this.listaCategorias = novaLista;
        notifyDataSetChanged();
    }

    static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvDescricao;
        private TextView tvIdCategoria;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewCategoria);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoCategoria);
            tvIdCategoria = itemView.findViewById(R.id.tvIdCategoria);
        }

        public void bind(final Categoria categoria, final OnCategoriaClickListener listener) {
            tvDescricao.setText(categoria.getDescricao());
            tvIdCategoria.setText("ID: " + categoria.getId());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCategoriaClick(categoria);
                    }
                }
            });
        }
    }
}

