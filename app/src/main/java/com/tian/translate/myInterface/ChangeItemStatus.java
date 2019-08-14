package com.tian.translate.myInterface;

import com.tian.translate.model.TextItem;

public interface ChangeItemStatus {
    public void chooseItem(TextItem textItem);
    public void ItemFavorites(TextItem textItem);
    public void deleteItem(TextItem textItem);
    public void toDetail(TextItem textItem);
}
