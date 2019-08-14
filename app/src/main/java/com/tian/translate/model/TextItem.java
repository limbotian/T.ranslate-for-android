package com.tian.translate.model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Date;

public class TextItem extends LitePalSupport implements Serializable{

    @Column
    private Long id;
    @Column
    private String languageFrom;
    @Column
    private String languageTo;
    @Column
    private String contentText;
    @Column
    private String srcText;
    @Column
    private Date createDate;
    @Column(defaultValue = "false")
    private Boolean isFavorites;
    private String errorCode;
    private String errorMsg;

    public TextItem() {

    }

    public Boolean isError(){
        return errorCode!=null;
    }


    public TextItem(String languageFrom, String languageTo, String contentText) {
        this.languageFrom = languageFrom;
        this.languageTo = languageTo;
        this.contentText = contentText;
    }

    public String getLanguageFrom() {
        return languageFrom;
    }

    public void setLanguageFrom(String languageFrom) {
        this.languageFrom = languageFrom;
    }

    public String getLanguageTo() {
        return languageTo;
    }

    public void setLanguageTo(String languageTo) {
        this.languageTo = languageTo;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public String getSrcText() {
        return srcText;
    }

    public void setSrcText(String srcText) {
        this.srcText = srcText;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Boolean getIsFavorites() {
        return isFavorites;
    }

    public void setIsFavorites(Boolean favorites) {
        isFavorites = favorites;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TextItem{" +
                "id=" + id +
                ", languageFrom='" + languageFrom + '\'' +
                ", languageTo='" + languageTo + '\'' +
                ", contentText='" + contentText + '\'' +
                ", srcText='" + srcText + '\'' +
                ", createDate=" + createDate +
                ", isFavorites=" + isFavorites +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
