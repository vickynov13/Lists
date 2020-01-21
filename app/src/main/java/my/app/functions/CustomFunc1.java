package my.app.functions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class CustomFunc1 {

    public static StringBuilder getresponsebody(InputStream caInput, String endpoint, String typemethod, JSONObject jsonObject) {
        StringBuilder sb = new StringBuilder();
        CertificateFactory cf = null;
        Certificate ca = null;
        SSLContext context = null;
        try {
            cf = CertificateFactory.getInstance("x.509");
            ca = cf.generateCertificate(caInput);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    //return hv.verify("gulunodejs.myvnc.com", session);
                    //return false;
                    return true;
                }
            });
            //InputStream caInput = getAssets().open("server.cert");

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null,null);
            keyStore.setCertificateEntry("ca", ca);
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        URL url = null;
        HttpsURLConnection urlConnection = null;
        try {
            url = new URL(endpoint);
            urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            if(typemethod.equalsIgnoreCase("PUT")) {
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(jsonObject.toString());
                wr.flush();
                wr.close();
            }
            urlConnection.connect();

            //urlConnection.getResponseCode();
            InputStream in = urlConnection.getInputStream();
            //to work on//InputStream inh = urlConnection.getHeaderField(field name);
            //InputStreamReader responseBodyReader = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String read;
            while ((read=br.readLine()) != null) {
                sb.append(read);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            sb.append("{"+'"'+"message"+'"'+":"+'"'+"serverdown"+'"'+"}");
            System.out.println(sb.toString());
        }
        return sb;
    }
}
