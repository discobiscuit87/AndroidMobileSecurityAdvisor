package com.ss174h.amsa.EnvCondition;

/**
 * Created by jianwen on 9/1/18.
 */

public class ArticleModel {

    private String headline;
    private String article;

    public ArticleModel(String headline, String article) {
        this.headline = headline;
        this.article = article;
    }

    public String getHeadline() {
        return headline;
    }

    public String getArticle() {
        return article;
    }
}
