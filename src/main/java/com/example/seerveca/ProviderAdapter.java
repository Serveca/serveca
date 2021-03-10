package com.example.seerveca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProviderAdapter  extends ArrayAdapter implements Filterable {
    private Context context;
    private List<provider> providers;
    private List<provider>providerList;
    private ImageView iv;

    public ProviderAdapter(Context context, List<provider> list)
    {
        super(context,R.layout.list,list);
        this.context = context;
        this.providers=list;
        providerList=new ArrayList<>(providers);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list,parent,false);

        TextView tvName = convertView.findViewById(R.id.Name);
        TextView tvservice = convertView.findViewById(R.id.service);
        TextView tvfirm = convertView.findViewById(R.id.firm);
        iv = convertView.findViewById(R.id.iv);


        tvName.setText(providers.get(position).getName());
        tvservice.setText(providers.get(position).getService());
        tvfirm.setText(providers.get(position).getFirm());
        iv.setImageResource(R.drawable.ic_baseline_person_24);



        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return providerFilter;
    }
    private Filter providerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<provider> filterList = new ArrayList<>();
           if (constraint==null||constraint.length()==0)
           {
               filterList.addAll(providerList);
           }
           else
           {
               String filterPattern = constraint.toString().toLowerCase().trim();
               for (provider item : providerList)
               {
                   if (item.getService().toLowerCase().contains(filterPattern))
                   {
                       filterList.add(item);
                   }


               }
           }
           FilterResults results = new FilterResults();
           results.values = filterList;
           return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            providers.clear();
            providers.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
