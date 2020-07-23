package com.example.httpurlconnection.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.httpurlconnection.utils.MyUtils;
import com.example.httpurlconnection.R;
import com.example.httpurlconnection.models.Country;

import java.util.ArrayList;


public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Country> countryArrayList;


    public CountriesAdapter(Context context, ArrayList<Country> countryArrayList) {
        this.context = context;
        this.countryArrayList = countryArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myCountryView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.countries_list_view, parent, false);
        RecyclerView.LayoutParams countryParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myCountryView.setLayoutParams(countryParams);

        return new MyViewHolder(myCountryView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.countryNameTv.setText(countryArrayList.get(position).getCountryName());
        holder.countryCapitalTv.setText(countryArrayList.get(position).getCountryCapital());
        holder.callingCodeTv.setText(countryArrayList.get(position).getCallingCode());
        holder.alphaCodeTv.setText(countryArrayList.get(position).getAlpha2Code());
        holder.regionTv.setText(countryArrayList.get(position).getRegion());
        holder.populationTv.setText(countryArrayList.get(position).getPopulation());
        holder.circleImageView.setImageURI(Uri.parse(countryArrayList.get(position).getCountryFlagUrl()));
        MyUtils.fetchSvg(context, countryArrayList.get(position).getCountryFlagUrl(), holder.circleImageView);

    }

    @Override
    public int getItemCount() {
        return countryArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView circleImageView;
        TextView countryNameTv;
        TextView countryCapitalTv;
        TextView callingCodeTv;
        TextView regionTv;
        TextView populationTv;
        TextView alphaCodeTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.country_flag);
            countryNameTv = itemView.findViewById(R.id.country_name);
            countryCapitalTv = itemView.findViewById(R.id.country_capital);
            callingCodeTv = itemView.findViewById(R.id.calling_code);
            regionTv = itemView.findViewById(R.id.country_region);
            populationTv = itemView.findViewById(R.id.country_population);
            alphaCodeTv = itemView.findViewById(R.id.alpha_code);
        }
    }

}
