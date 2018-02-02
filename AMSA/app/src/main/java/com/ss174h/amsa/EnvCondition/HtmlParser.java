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

            //pageDocument.getElementsByTag("hi");
            elements = pageDocument.select("td");

            //pageDocument.select

            //articleElements = pageDocument.select(".infobox vevent .tbody");
            //articleElements = pageDocument.select(".wrap .cols .col-1of2 p");
            //articleElements = pageDocument.select(".mw-parser-output .\"infobox vevent\" .summary tbody tr");
            //pageDocument.getElementsByAttribute()
            //pageDocument.
            articleElements = pageDocument.select("th");
            headline = "test";

            /*for(Element element: articleElements){
                article = article + element.text() + "\n\n";
            }*/

            Elements testEl = pageDocument.select("tr");

            for(Element e: testEl){

                if(e.text().contains("Latest release"))
                {
                    Log.d("TEST", e.text().substring(15));
                    article = e.text().substring(15);
                }
            }

            /*for(int i = 0; i < elements.size(); i++){
                //article = article + element.text() + "\n\n";
                if(articleElements.get(i).text().equalsIgnoreCase("Latest release")){
                    Log.d("TEST", elements.get(i).text());
                }
            }*/

            articleModel = new ArticleModel(headline, article);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){

        }

        return articleModel;
    }

    @Override
    protected void onPostExecute(ArticleModel articleModel) {
        super.onPostExecute(articleModel);

        parserResponseInterface.onParsingDone(articleModel);
    }

}
