package com.ss174h.amsa.APKScanner;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss174h.amsa.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

public class CheckAppCertActivity extends AppCompatActivity {

    String issuer = "";
    String expiry = "";
    String valid = "Certificate is valid.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_app_cert);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String version = intent.getStringExtra("version");
        String installedOn = intent.getStringExtra("installedOn");
        String packName = intent.getStringExtra("packName");

        try {
            Drawable icon = getPackageManager().getApplicationIcon(packName);
            ImageView imageView = (ImageView) findViewById(R.id.app_icon);
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException nnf) {

        }

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packName,PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            for(Signature signature : signatures) {
                byte[] rawCert = signature.toByteArray();
                InputStream certStream = new ByteArrayInputStream(rawCert);
                try {
                    CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
                    X509Certificate certificate = (X509Certificate)certificateFactory.generateCertificate(certStream);

                    issuer = "Issuer: " + certificate.getIssuerDN();
                    expiry = "Expires on: " + certificate.getNotAfter();

                    certificate.checkValidity();
                } catch (CertificateExpiredException exp) {
                    valid = "Certificate is not valid!";
                } catch (CertificateNotYetValidException not) {
                    valid = "Certificate is not valid!";
                } catch (CertificateException ce) {

                }
            }
        } catch (PackageManager.NameNotFoundException nnf) {

        }


        TextView t1 = (TextView) findViewById(R.id.app_name);
        t1.setText(name);

        TextView t2 = (TextView) findViewById(R.id.version);
        t2.setText(version);

        TextView t3 = (TextView) findViewById(R.id.installedOn);
        t3.setText(installedOn);

        TextView t4 = (TextView) findViewById(R.id.certIssuer);
        t4.setText(issuer);

        TextView t5 = (TextView) findViewById(R.id.expiry);
        t5.setText(expiry);

        TextView t6 = (TextView) findViewById(R.id.validity);
        t6.setText(valid);
        if(valid.equals("Certificate is valid.")) {
            t6.setTextColor(getResources().getColor(R.color.goodColor));
        } else {
            t6.setTextColor(getResources().getColor(R.color.errorColor));
        }
    }
}
