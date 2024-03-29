package com.tian.translate.utils;

import android.content.Context;

import com.tian.translate.R;
import com.tian.translate.model.Language;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    public static void initializeData() {
        List<Language> languages = getLanguageFromList();
        languages.addAll(getLanguageFromTo());
        LitePal.saveAll(languages);
    }

    private static List<Language> getLanguageFromList() {
        List<Language> languageFrom = new ArrayList<>();
        Language auto = new Language();
        auto.setCode("auto");
        auto.setName("自动");
        languageFrom.add(auto);
        Language zh = new Language();
        zh.setCode("zh");
        zh.setName("中文");
        languageFrom.add(zh);
        Language en = new Language();
        en.setCode("en");
        en.setName("英文");
        languageFrom.add(en);
        Language jp = new Language();
        jp.setCode("jp");
        jp.setName("日语");
        languageFrom.add(jp);
        Language cht = new Language();
        cht.setCode("cht");
        cht.setName("繁体中文");
        languageFrom.add(cht);
        Language kor = new Language();
        kor.setCode("kor");
        kor.setName("韩语");
        languageFrom.add(kor);
        Language fra = new Language();
        fra.setCode("fra");
        fra.setName("法语");
        languageFrom.add(fra);
        Language de = new Language();
        de.setCode("de");
        de.setName("德语");
        languageFrom.add(de);
        for (Language language : languageFrom) {
            language.setType(1);
        }

        return languageFrom;
    }


    private static List<Language> getLanguageFromTo() {
        List<Language> languageTo = new ArrayList<>();
        Language zh = new Language();
        zh.setCode("zh");
        zh.setName("中文");
        languageTo.add(zh);
        Language en = new Language();
        en.setCode("en");
        en.setName("英文");
        languageTo.add(en);
        Language jp = new Language();
        jp.setCode("jp");
        jp.setName("日语");
        languageTo.add(jp);
        Language cht = new Language();
        cht.setCode("cht");
        cht.setName("繁体中文");
        languageTo.add(cht);
        Language kor = new Language();
        kor.setCode("kor");
        kor.setName("韩语");
        languageTo.add(kor);
        Language fra = new Language();
        fra.setCode("fra");
        fra.setName("法语");
        languageTo.add(fra);
        Language de = new Language();
        de.setCode("de");
        de.setName("德语");
        languageTo.add(de);
        for (Language language : languageTo) {
            language.setType(2);
        }

        return languageTo;
    }


    public static String codeToString(Context context, String code) {
        switch (code) {
            case "auto":
                return context.getString(R.string.auto);
            case "zh":
                return context.getString(R.string.zh);
            case "en":
                return context.getString(R.string.en);
            case "jp":
                return context.getString(R.string.jp);
            case "cht":
                return context.getString(R.string.cht);
            case "kor":
                return context.getString(R.string.kor);
            case "fra":
                return context.getString(R.string.fra);
            case "de":
                return context.getString(R.string.de);
            default:

        }
        return null;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
