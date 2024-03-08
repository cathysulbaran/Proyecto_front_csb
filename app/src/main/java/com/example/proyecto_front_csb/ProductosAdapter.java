package com.example.proyecto_front_csb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_front_csb.model.Productos;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder> implements View.OnClickListener{
    private List <Productos> productos;
    private View.OnClickListener listener;
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Productos produto);
    }

    public ProductosAdapter(List<Productos> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos,parent, false);
        view.setOnClickListener(this);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Productos producto = productos.get(position);
        holder.nombre.setText(producto.getNombre());
        holder.ean.setText(producto.getEan());
        String cantidad = Integer.toString(producto.getUnidades());
        holder.cantidad.setText("Cantidad: "+cantidad);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder{
        TextView ean, nombre, cantidad;
        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            ean = itemView.findViewById(R.id.textEan);
            nombre = itemView.findViewById(R.id.textNombre);
            cantidad = itemView.findViewById(R.id.textCantidad);
        }
    }

}
