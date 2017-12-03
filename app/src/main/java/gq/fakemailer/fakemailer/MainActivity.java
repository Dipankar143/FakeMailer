package gq.fakemailer.fakemailer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    String fake_email_txt,email_txt,subject_txt,body_txt,name_txt;
    EditText fake_email;
    EditText subject;
    EditText body;
    EditText email;
    EditText name;
    private InterstitialAd interstitialAd;
    ProgressDialog dialog;
    public AdView mAdView;
    int i =0;
    int versionCode=0;
    int vc;
    int check=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://fakemailer-280f1.firebaseio.com");


if (versionCode==0) {
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            vc = dataSnapshot.child("vc").getValue(Integer.class);
            check=1;
            showUpdate();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    try {
        PackageInfo packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        versionCode = packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
    }


}




        AdRequest newad=new AdRequest.Builder().build();
        interstitialAd=new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId("ca-app-pub-4787614537130897/6383950652");
        interstitialAd.loadAd(newad);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                displayAd();
                i++;
            }
            public void onAdClosed() {
                // Load the next interstitial.
                if (i<3) {
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });


        dialog= new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        mAdView = (AdView) findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
    }

    private void displayAd() {
        if (interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void send(View v) {
        fake_email = (EditText) findViewById(R.id.fake_email);
        email = (EditText) findViewById(R.id.email);
        subject = (EditText) findViewById(R.id.subject);
        body = (EditText) findViewById(R.id.body);
        name=(EditText) findViewById(R.id.from_name);
        dialog.show();

        fake_email_txt = fake_email.getText().toString();
        email_txt = email.getText().toString();
        subject_txt = subject.getText().toString();
        body_txt = body.getText().toString();
        name_txt=name.getText().toString();
        if (!isEmailValid(fake_email_txt)==true||!isEmailValid(email_txt)==true){
            Toast.makeText(getApplicationContext(), "invalid Email", Toast.LENGTH_LONG).show();
            TextView error_Show= (TextView) findViewById(R.id.error);
            error_Show.setVisibility(TextView.VISIBLE);
            TextView error_space= (TextView) findViewById(R.id.error_space);
            error_space.setVisibility(TextView.GONE);
            interstitialAd.loadAd(new AdRequest.Builder().build());

            dialog.dismiss();

        } else {
        if (!fake_email.equals("") &&!email_txt.equals("") &&
                !subject_txt.equals("") && !body_txt.equals("")&&!name_txt.equals("")){
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            dialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://emailbomb.co/email.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    fake_email.setText("");
                    email.setText("");
                    subject.setText("");
                    body.setText("");
                    email.setText("");
                    name.setText("");
                    TextView error_Show= (TextView) findViewById(R.id.error);
                    error_Show.setVisibility(TextView.GONE);
                    TextView error_space= (TextView) findViewById(R.id.error_space);
                    error_space.setVisibility(TextView.GONE);
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                    Toast.makeText(getApplicationContext(), "Mail sent", Toast.LENGTH_LONG).show();

                    dialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> prams = new HashMap<>();
                    prams.put("fake_email", fake_email_txt);
                    prams.put("subject", subject_txt);
                    prams.put("txt", body_txt);
                    prams.put("sender_email", email_txt);
                    prams.put("name",name_txt);
                    return prams;
                }
            };

            requestQueue.add(stringRequest);


        }
        }
        if(fake_email.equals("")||email_txt.equals("") ||
                subject_txt.equals("") || body_txt.equals("")||name_txt.equals("")) {
            Toast.makeText(getApplicationContext(),"Check error",Toast.LENGTH_LONG).show();
            TextView error_space= (TextView) findViewById(R.id.error_space);
            error_space.setVisibility(TextView.VISIBLE);
            dialog.dismiss();
        }
    }

    public void showUpdate(){
        if (versionCode!=0&&versionCode!=vc&&check==1){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Update Available")
                    .setMessage("Please Update The App For Better Experience")
                    .setNegativeButton(android.R.string.cancel,null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=gq.fakemailer.fakemailer&hl=en"));
                            startActivity(intent);
                        }
                    }).create();
            alertDialog.show();
        }
    }

    public void recive(View view){

            Intent i = new Intent(this, mail_rec.class);
            startActivity(i);
        
    }

}
