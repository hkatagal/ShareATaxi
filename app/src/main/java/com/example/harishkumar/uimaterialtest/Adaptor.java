package com.example.harishkumar.uimaterialtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Harishkumar on 4/11/2015.
 */
public class Adaptor extends RecyclerView.Adapter<Adaptor.MyViewHolder> {

    private LayoutInflater inflater;
    List<contentRecyleView> data = Collections.emptyList();


    public Adaptor(Context context, List<contentRecyleView> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view =  inflater.inflate(R.layout.recycle_custom,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        contentRecyleView current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.id);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.listText);
            icon = (ImageView)itemView.findViewById(R.id.listIcon);
        }
    }
}
