package com.wposs.appkequi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutoCompleteAdapter extends ArrayAdapter<AdapterConfig>{

    private List<AdapterConfig> recommendListFull;
    private String userEmail;
    public AutoCompleteAdapter(@NonNull Context context, @NonNull List<AdapterConfig> recommendList) {
        super(context, 0, recommendList);
        recommendListFull=new ArrayList<>(recommendList);

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_config,parent,false);
        }

        TextView email=convertView.findViewById(R.id.item);

        AdapterConfig user=getItem(position);

        if(user!=null){

            email.setText(user.getEmail());
            updateEmail(user.getEmail());

        }

        return convertView;
    }

    public void updateEmail(String email){
        this.userEmail=email;
    }
    public String getUserEmail(){
        return userEmail;
    }

    private Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            List<AdapterConfig> viewSuggest=new ArrayList<>();

            if(constraint==null||constraint.length()==0){

                viewSuggest.addAll(recommendListFull);

            }else{
                String filterPattern=constraint.toString().toLowerCase().trim();

                for(AdapterConfig user:recommendListFull){
                    if(user.getEmail().toLowerCase().contains(filterPattern)) {
                        viewSuggest.add(user);
                    }
                }

            }

            results.values=viewSuggest;
            results.count=viewSuggest.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((AdapterConfig)resultValue).getEmail();
        }
    };
}
