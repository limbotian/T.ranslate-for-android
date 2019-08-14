package com.tian.translate.utils;

import android.util.Log;

import com.tian.translate.model.ResultDetail;
import com.tian.translate.model.TextItem;
import com.tian.translate.model.TranslateHttpSendParm;
import com.tian.translate.model.TranslateHttpSendResult;
import com.tian.translate.translateCore.TransApi;


import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetTranslateResult {

    public static TextItem translate(TranslateHttpSendParm sendParm) {
        TextItem textItem;
        TransApi transApi = new TransApi("20190315000277316", "PU9I_mPuBSWmPaCSoD5X");
        String transResult = transApi.getTransResult(sendParm);
        TranslateHttpSendResult translateHttpSendResult = GsonUtil.GsonToBean(transResult, TranslateHttpSendResult.class);
        if (null != translateHttpSendResult && null != translateHttpSendResult.getError_code()) {
            textItem = new TextItem();
            textItem.setErrorCode(translateHttpSendResult.getError_code());
            textItem.setErrorMsg(translateHttpSendResult.getError_msg());
        } else {
            textItem = new TextItem();
            textItem.setLanguageFrom(translateHttpSendResult.getFrom());
            textItem.setLanguageTo(translateHttpSendResult.getTo());
            StringBuilder contentText = new StringBuilder();
            StringBuilder srcText = new StringBuilder();
            List<ResultDetail> resultDetails = translateHttpSendResult.getTrans_result();
            for (ResultDetail resultDetail : resultDetails) {
                srcText.append(resultDetail.getSrc()).append("\n");
                contentText.append(resultDetail.getDst()).append("\n");
            }
            textItem.setSrcText(srcText.toString());
            textItem.setContentText(contentText.toString());
        }
        return textItem;
    }

    public static String rndNum(int n) {
        String rnd = "";
        for (int i = 0; i < n; i++)
            rnd += Math.floor(Math.random() * 10);
        return rnd;
    }


    /**
     * 32位MD5加密
     *
     * @param content -- 待加密内容
     * @return
     */
    public static String md5Decode(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) { // 过滤空的key
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }
        return builder.toString();
    }

    public static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }
}
