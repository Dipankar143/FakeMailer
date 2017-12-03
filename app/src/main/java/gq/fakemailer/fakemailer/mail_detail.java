package gq.fakemailer.fakemailer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class mail_detail extends AppCompatActivity {
    String url;
    public AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_detail);
        url=getIntent().getStringExtra("url");
        mAdView = (AdView) findViewById(R.id.adview3);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        new email_detail().execute();

    }
    public class email_detail extends AsyncTask<Void,Void,Void>{
        String from_emails,subject,time,html,html2;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document= Jsoup.connect(url).get();
                Element element=document.select("dl[class=info-list dl-horizontal]").first();
                Elements element1=element.select("dd");
                from_emails=element1.get(1).text();
                subject=element1.get(2).text();
                time=element1.get(3).text();
                String s=url;
                s = s.substring(s.indexOf("/mess") + 1);
                html2="http://www.fakemailgenerator.com/email/gustr.com/dss/"+s;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TextView from_email= (TextView) findViewById(R.id.from_email);
            TextView email_subject= (TextView) findViewById(R.id.email_subject);
            TextView timer= (TextView) findViewById(R.id.email_times);
            WebView webView= (WebView) findViewById(R.id.email_body);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(html2);
            from_email.setText('"'+from_emails+'"');
            timer.setText(time);
            email_subject.setText(subject);

        }
    }
}
