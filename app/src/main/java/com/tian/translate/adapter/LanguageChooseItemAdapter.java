package com.tian.translate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tian.translate.R;
import com.tian.translate.model.Language;
import com.tian.translate.myInterface.RefreshLanguage;

import java.util.List;

public class LanguageChooseItemAdapter extends RecyclerView.Adapter<LanguageChooseItemAdapter.ViewHolder> {

    private List<Language> mLanguageList;
    private RefreshLanguage mRefreshLanguage;


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentText;

        public ViewHolder(View view) {
            super(view);
            contentText = (TextView) view.findViewById(R.id.language_name);
        }
    }

    public LanguageChooseItemAdapter(List<Language> languageList, RefreshLanguage refreshLanguage) {
        mLanguageList = languageList;
        mRefreshLanguage = refreshLanguage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_choose_item_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.contentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefreshLanguage.refreshActivity(mLanguageList.get(holder.getAdapterPosition()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Language language = mLanguageList.get(position);
        holder.contentText.setText(language.getName());
    }

    @Override
    public int getItemCount() {
        return mLanguageList.size();
    }
}
