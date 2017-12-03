package gq.fakemailer.fakemailer;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Attr;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mail_rec extends AppCompatActivity {
    String URL;
    ArrayList<data> arrayList;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    public AdView mAdView;
    int i=0;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_rec);

        interstitialAd=new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId("ca-app-pub-4787614537130897/6383950652");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (interstitialAd.isLoaded()){
                    interstitialAd.show();
                }
                i++;
            }
            public void onAdClosed() {
                // Load the next interstitial.
                if (i<2) {
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });

        arrayList = new ArrayList<>();
        dialog= new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        mAdView = (AdView) findViewById(R.id.adview2);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        i=0;

    }
    public void refresh(View v){
        EditText edit= (EditText) findViewById(R.id.edit);
        Button refresh= (Button) findViewById(R.id.refresh);
        TextView email= (TextView) findViewById(R.id.showemail);
        String check=edit.getText().toString();
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
        dialog.show();

        if (regex.matcher(check).find()||check.contains(" ")){
            email.setText("Please Enter Email Address");
            email.setVisibility(TextView.VISIBLE);
            dialog.dismiss();
        }
        else {
            refresh.setText("refresh");
            URL = edit.getText().toString();
            if (i==0){
                WebView web= (WebView) findViewById(R.id.web);
                web.getSettings().setJavaScriptEnabled(true);
                web.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        view.loadUrl(request.toString());
                        return false;
                    }
                });
                web.loadUrl("http://www.fakemailgenerator.com/inbox/gustr.com/"+URL+"/");
                i=1;
            }
            arrayList.clear();
            TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.edit_textinput);
            textInputLayout.setVisibility(TextInputLayout.GONE);
            new email().execute();
            email.setText("Your email is " + URL + "@gustr.com");
            email.setVisibility(TextView.VISIBLE);
            Button copy = (Button) findViewById(R.id.copy);
            copy.setVisibility(Button.VISIBLE);
        }
    }
    public void copy(View view){

        ClipboardManager clipboardManager=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        EditText edit= (EditText) findViewById(R.id.edit);
        String text=edit.getText().toString()+"@gustr.com";
        ClipData clipData=ClipData.newPlainText("email",text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getApplicationContext(),"Email copied",Toast.LENGTH_LONG).show();
    }
    public class email extends AsyncTask<Void,Void,Void>{
String value="",check_zero;
        data body_txt1;
        int i;
        int error=0;
        @Override
        protected Void doInBackground(Void... params) {
            dialog.show();



                    try {
                        Document document = Jsoup.connect("http://www.fakemailgenerator.com/inbox/gustr.com/"+URL+"/").get();
                        Element mail_check=document.select("div[class=col-xs-12]").get(1);
                        check_zero=mail_check.select("p").toString();
                        if (check_zero.equals("<p>Waiting for e-mails...</p>")){
                            value="no email found Please Refresh again";
                            arrayList.clear();

                        }
                        else {
                            Element element = document.select("dl[class=info-list dl-horizontal").first();
                            Element list_email=document.select("ul[class=item-list]").first();
                            Elements li=list_email.select("li");
                            Elements body= list_email.select("a");
                            Elements email_ids=list_email.select("div[class=hidden-xs hidden-sm col-md-2 col-lg-3]");
                            Elements email_subject=list_email.select("div[class=col-xs-9 col-sm-10 col-md-8 col-lg-7]");
                            for (i=0;i<=body.size()-1;i++) {
                                body_txt1 = new data("www.fakemailgenerator.com"+body.get(i).attr("href"),email_ids.get(i).select("p").text(),email_subject.get(i).select("p").text(),Integer.toString(i+1));
                                arrayList.add(body_txt1);
                            }
                          //  Document doc=Jsoup.connect(body_txt1).get();
                          //  Element text=doc.select("body").first();
                            //txt=text.html();
                        }
                    } catch (IOException e) {
                        dialog.dismiss();
                        error=1;
                        e.printStackTrace();
                    }



            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if (error == 0) {
                super.onPostExecute(aVoid);
                TextView textView = (TextView) findViewById(R.id.email_id);
                RecyclerView.Adapter adapter = new adeptor(arrayList, getApplicationContext());
                recyclerView = (RecyclerView) findViewById(R.id.body_txt);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);


                // TextView textView1= (TextView) findViewById(R.id.sender);
                //TextView textView3= (TextView) findViewById(R.id.subject_txt);
                if (!value.equals("")) {
                    textView.setText(value);
                    textView.setVisibility(TextView.VISIBLE);
                }
                dialog.dismiss();
                //textView1.setText(sender);
                //textView3.setText(subject_txt);
                //textView4.loadData(txt,"text/html",null);

            }
            else {
                Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }


}
