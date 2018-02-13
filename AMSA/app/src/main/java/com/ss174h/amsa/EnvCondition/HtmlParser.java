package com.ss174h.amsa.EnvCondition;

import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by jianwen on 9/1/18.
 */

public class HtmlParser extends AsyncTask<String, Void, ArticleModel>{

    private ParserResponseInterface parserResponseInterface;

    public HtmlParser(ParserResponseInterface parserResponseInterface){
        this.parserResponseInterface = parserResponseInterface;
    }

    @Override
    protected ArticleModel doInBackground(String... params) {

        String url = params[0];
        ArticleModel articleModel = null;

        String headline;
        String article = "";

        Document pageDocument;
        Elements elements;
        Elements articleElements;

        try {
            pageDocument = Jsoup.connect(url).get();
            elements = pageDocument.select("td");
            articleElements = pageDocument.select("th");
            headline = "test";
            Elements testEl = pageDocument.select("tr");

            for(Element e: testEl){

                if(e.text().contains("Latest release")) {
                    Log.d("TEST", e.text().substring(15));
                    article = e.text().substring(15);
                }
            }
            articleModel = new ArticleModel(headline, article);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){

        }

        return articleModel;
    }

    @Override
    protected void onPostExecute(ArticleModel articleModel) {
        super.onPostExecute(articleModel);

        parserResponseInterface.onParsingDone(articleModel);
    }
}
