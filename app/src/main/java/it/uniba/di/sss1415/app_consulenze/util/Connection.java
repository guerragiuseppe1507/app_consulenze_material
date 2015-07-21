package it.uniba.di.sss1415.app_consulenze.util;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import it.uniba.di.sss1415.app_consulenze.util.JsonHandler;
import it.uniba.di.sss1415.app_consulenze.util.ServerMsgs;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;

/**
 * Created by Giuseppe on 03/06/2015.
 */
public class Connection {

    private String registrationUrl;
    private String parametriServer;
    private String tipoElem;


    public Connection(String url){
        this.registrationUrl = url;
    }

    //Tipi di accesso: read, write, change, delete
    //Tipo elem: vedi classe util/StrutturaJson
    public void setParametri(String tipoElem, String accesso, String... valori){
        this.tipoElem = tipoElem;
        String jsonDaInviare;
        try {
            jsonDaInviare = JsonHandler.generaJson(this.tipoElem, valori).toString();
            //parametri = 'accesso:' + accesso + ', elemento:' + this.tipoElemento + ', jsonDaScrivere:' + jsonDaInviare;
            parametriServer = "accesso:" + accesso + ", elemento:" + this.tipoElem + ", jsonDaScrivere:" + jsonDaInviare;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e){
            parametriServer = "accesso:" + accesso + ", elemento:" + this.tipoElem + ", jsonDaScrivere:";
        }

    }
    public void setParametri(String parametri){
        parametriServer = parametri;

    }

    public String getParametri(){
        return parametriServer;
    }


    public String newConnect() {

        URL url;
        HttpURLConnection conn = null;
        try {

            //url
            url = new URL(registrationUrl);
            //apro la connessione
            conn = (HttpURLConnection) url.openConnection();

            byte[] postData = getParametri().toString().getBytes("UTF-8");

            //creazione di una richiesta post a partire dall'url
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));

            //Setta il method a POST
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(1000);

            //send POST request
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(getParametri());
            wr.flush();
            wr.close();

            //GET response
            InputStream in = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = rd.readLine()) != null) {
                response.append(line);
                //response.append('\r');
            }

            rd.close();
            Log.i("STRINGA RIC = ", response.toString());
            return response.toString();

        } catch (UnknownHostException e) {
            return ToastMsgs.CONN_PROBLEM;
        } catch (SocketTimeoutException e) {
            return ToastMsgs.CONN_TIMEOUT;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }

}
