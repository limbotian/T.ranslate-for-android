package com.tian.translate.adapter;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.tian.translate.R;
import com.tian.translate.model.TextItem;
import com.tian.translate.myInterface.ChangeItemStatus;

import org.litepal.LitePal;

public class TextItemAdapter extends RecyclerView.Adapter<TextItemAdapter.ViewHolder> {

    private List<TextItem> mTextItemList;
    private ChangeItemStatus mChangeItemStatus;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView contentText;
        TextView languageFromText;
        TextView languageToText;
        Button starBtn;
        LinearLayout cardView;
        public ViewHolder(View view){
            super(view);
            contentText = (TextView) view.findViewById(R.id.text_item);
            languageFromText = (TextView) view.findViewById(R.id.language_from_text);
            languageToText = (TextView) view.findViewById(R.id.language_to_text);
            starBtn = (Button) view.findViewById(R.id.star_btn);
            cardView = (LinearLayout)view.findViewById(R.id.outer_line);
        }
    }

    public TextItemAdapter(List<TextItem> textItemList, ChangeItemStatus changeItemStatus){
        mTextItemList = textItemList;
        mChangeItemStatus= changeItemStatus;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.starBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TextItem textItem = mTextItemList.get(holder.getAdapterPosition());
                if (!textItem.getIsFavorites()){
                    textItem.setIsFavorites(true);
                    textItem.update(textItem.getId());
                    holder.starBtn.setBackgroundResource(R.drawable.ic_yellow_star);
                }else{
                    textItem.setIsFavorites(false);
                    textItem.update(textItem.getId());
                    holder.starBtn.setBackgroundResource(R.drawable.ic_star_border_dark);
                }
                mChangeItemStatus.ItemFavorites(textItem);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundResource(R.drawable.ripple_bg);
                mChangeItemStatus.toDetail(mTextItemList.get(holder.getAdapterPosition()));
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mChangeItemStatus.deleteItem(mTextItemList.get(holder.getAdapterPosition()));
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextItem textItem = mTextItemList.get(position);
        holder.contentText.setText(textItem.getContentText());
        holder.languageFromText.setText(textItem.getLanguageFrom());
        holder.languageToText.setText(textItem.getLanguageTo());
        if (!textItem.getIsFavorites()) {
            holder.starBtn.setBackgroundResource(R.drawable.ic_star_border_dark);
        }else{
            holder.starBtn.setBackgroundResource(R.drawable.ic_yellow_star);
        }
    }

    @Override
    public int getItemCount() {
        return mTextItemList.size();
    }
}
