package com.example.madereira.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.madereira.R;
import com.example.madereira.model.Usuario;
import java.util.List;

/**
 * Adapter para exibir lista de usuários em RecyclerView
 */
public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<Usuario> listaUsuarios;
    private OnUsuarioClickListener listener;

    // Interface para clicks nos itens
    public interface OnUsuarioClickListener {
        void onUsuarioClick(Usuario usuario);
    }

    public UsuarioAdapter(List<Usuario> listaUsuarios, OnUsuarioClickListener listener) {
        this.listaUsuarios = listaUsuarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.bind(usuario, listener);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    // Método para atualizar a lista
    public void atualizarLista(List<Usuario> novaLista) {
        this.listaUsuarios = novaLista;
        notifyDataSetChanged();
    }

    // ViewHolder
    static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNome;
        private TextView tvEmail;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeUsuarioItem);
            tvEmail = itemView.findViewById(R.id.tvEmailUsuarioItem);
        }

        public void bind(final Usuario usuario, final OnUsuarioClickListener listener) {
            tvNome.setText(usuario.getNome());
            tvEmail.setText(usuario.getEmail());

            // Click no item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onUsuarioClick(usuario);
                    }
                }
            });
        }
    }
}
