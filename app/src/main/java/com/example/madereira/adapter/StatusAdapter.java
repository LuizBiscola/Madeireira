package com.example.madereira.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.madereira.R;
import com.example.madereira.model.Status;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

    private List<Status> listaStatus;
    private OnStatusClickListener listener;

    public interface OnStatusClickListener {
        void onStatusClick(Status status);
    }

    public StatusAdapter(List<Status> listaStatus, OnStatusClickListener listener) {
        this.listaStatus = listaStatus;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        Status status = listaStatus.get(position);
        holder.bind(status, listener);
    }

    @Override
    public int getItemCount() {
        return listaStatus.size();
    }

    public void atualizarLista(List<Status> novaLista) {
        this.listaStatus = novaLista;
        notifyDataSetChanged();
    }

    static class StatusViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvDescricao;
        private TextView tvIdStatus;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewStatus);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoStatus);
            tvIdStatus = itemView.findViewById(R.id.tvIdStatus);
        }

        public void bind(final Status status, final OnStatusClickListener listener) {
            tvDescricao.setText(status.getDescricao());
            tvIdStatus.setText("ID: " + status.getId());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onStatusClick(status);
                    }
                }
            });
        }
    }
}

