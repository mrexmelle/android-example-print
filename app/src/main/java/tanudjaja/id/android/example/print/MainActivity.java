package tanudjaja.id.android.example.print;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private final String TAG=getClass().getSimpleName();

    @BindView(R.id.cl_main)
    CoordinatorLayout mClMain;

    @BindView(R.id.et_input)
    EditText mEtInput;

    @BindView(R.id.btn_print)
    Button mBtnPrint;

    private WebView mWebview;
    private PrintJob mPrintJob;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBtnPrint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        final String input=mEtInput.getText().toString();

        if(input.length()==0)
        {
            Snackbar.make(mClMain, "Text cannot be empty", Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            mWebview = new WebView(MainActivity.this);
            mWebview.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req)
                {
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url)
                {
                    PrintManager pm=(PrintManager)(MainActivity.this.getSystemService(Context.PRINT_SERVICE));
                    PrintDocumentAdapter pda=mWebview.createPrintDocumentAdapter("Android Sample Print Document");
                    if(mPrintJob != null)
                    {
                        mPrintJob.cancel();
                    }
                    mPrintJob=pm.print("Android Sample Print Document", pda, new PrintAttributes.Builder().build());
                    mWebview=null;
                }
            });

            final String html="<html><head></head><body><p>"+ input + "</p></body></html>";
            mWebview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        }
    }
}
