package com.lorem.simpledictionary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by CHEVALIER-11 on 10-Aug-18.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewholder>{
    private ArrayList<Word> listWords = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;

    public WordAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Word> getListWords() {
        return listWords;
    }

    public void setListWords(ArrayList<Word> listWords) {
        this.listWords = listWords;
    }

    @Override
    public WordViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new WordViewholder(view);
    }

    public void addItem(ArrayList<Word> listWords) {
        this.listWords = listWords;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(WordViewholder holder, int position) {
        holder.tvTitle.setText(getListWords().get(position).getWord());
    }

    @Override
    public int getItemCount() {
        return getListWords().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class WordViewholder extends RecyclerView.ViewHolder{
        TextView tvTitle;

        public WordViewholder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.word_id);
        }
    }
}
