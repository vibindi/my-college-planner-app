package com.bindi.collegeplanner.helperClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bindi.collegeplanner.MainActivity;
import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;

public class WebviewActivity extends AppCompatActivity {

    private WebView webView;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_webview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String data=null;
        boolean other = true;
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(!bundle.getString("resourceWebsite").equals(null)){
                data= bundle.getString("resourceWebsite");
                url = data;
            }
        }

        webView = (WebView) findViewById(R.id.webView1);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else{
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.resourcesDirection);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
