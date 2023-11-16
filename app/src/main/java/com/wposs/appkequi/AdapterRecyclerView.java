package com.wposs.appkequi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {

    private List<recyclerView> data;
    private LayoutInflater inflater; //description or of what archive receive
    private Context context;

    public AdapterRecyclerView(List<recyclerView>elements,Context context){
        this.inflater=LayoutInflater.from(context);
        this.context=context;
        this.data=elements;
    }

    @Override
    public int getItemCount(){
        return data.size();
    }

    @Override
    public AdapterRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View see=inflater.inflate(R.layout.list_recycler_lobby,null);
        return new AdapterRecyclerView.ViewHolder(see);
    }

    @Override
    public void onBindViewHolder(final AdapterRecyclerView.ViewHolder holder, final int position){
        holder.bindData(data.get(position));
    }

    public void setItems(List<recyclerView> elements){
        data=elements;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView viewStatus;
        TextView name,numberPhone, cash, message, status;

        ViewHolder(View itemView){
            super(itemView);
            viewStatus=itemView.findViewById(R.id.imageStatus);
            name=itemView.findViewById(R.id.nameUser);
            numberPhone=itemView.findViewById(R.id.numberPhoneUser);
            cash=itemView.findViewById(R.id.cashUser);
            message=itemView.findViewById(R.id.messageUser);
            status=itemView.findViewById(R.id.status);
        }

        void bindData(final recyclerView item){
            viewStatus.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.SRC_IN);
            name.setText(item.getName());
            numberPhone.setText(item.getNumberPhone());
            cash.setText(item.getCash());
            message.setText(item.getMessage());
            status.setText(item.getStatus());
        }

    }
}
