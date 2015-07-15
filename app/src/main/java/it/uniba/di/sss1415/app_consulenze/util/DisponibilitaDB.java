package it.uniba.di.sss1415.app_consulenze.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;


/**
 * Classe che si occupa della disponibilita'
 */
public class DisponibilitaDB {
    private String urlString = null;

    private static final String TIPO_ELEMENTO = "dispon";
    private static final String ACCESSO_READ = "read";
    private static final String ACCESSO_WRITE = "write";
    private static final String ACCESSO_CHANGE = "change";
    private static final String ACCESSO_DELETE = "delete";
    //json ricevuto dal server
    private JSONObject json;
    //prendo gli elementi che sono tra le parentesi [] dell'oggetto JSON
    private JSONArray dispArray;
    private ArrayList<MieDisp> dispList = new ArrayList<MieDisp>();

    public volatile boolean parsingComplete = true;

    private Connection conn;


    public DisponibilitaDB(String url){
        this.urlString = url;
    }

        public DisponibilitaDB(String url, String id, String data, String ora_inizio, String ora_fine, String specializzazione){

        conn = new Connection(url);

        this.urlString = url;
        json = new JSONObject();
        try{
            json.put("id", id);
            json.put("data", data);
            json.put("oraInizio", ora_inizio);
            json.put("oraFine", ora_fine);
            json.put("intervento", specializzazione);
            json.put("ripetizione", "");
            json.put("fineRipetizione", "0000-00-00");
            json.put("utente", "roger@gmail.com");

            //genero la stringa di parametri


        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }


    public String generaParametri(String tipoElemento, String accesso, String jsonDaInviare){
        //parametri = 'accesso:' + accesso + ', elemento:' + this.tipoElemento + ', jsonDaScrivere:' + jsonDaInviare;
        String stringaP;
        stringaP = "accesso:" + accesso + ", elemento:" + tipoElemento + ", jsonDaScrivere:" + jsonDaInviare;
        return stringaP;
    }


    public void inviaRichiestaScrittura(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                    String parametri = generaParametri(TIPO_ELEMENTO,ACCESSO_WRITE,json.toString());
                try {
                    conn.setParametri(TIPO_ELEMENTO, ACCESSO_WRITE, json.get("id").toString(),
                            json.get("data").toString(),
                            json.get("oraInizio").toString(),
                            json.get("oraFine").toString(),
                            json.get("intervento").toString(),
                            json.get("ripetizione").toString(),
                            json.get("fineRipetizione").toString(),
                            json.get("utente").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                conn.newConnect();
                    parsingComplete = false;

            }
        });
        thread.start();
    }

    public void inviaRichiestaModifica(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //NON OSO TOCCARE DA QUA
                try{

                    String parametri = generaParametri(TIPO_ELEMENTO,ACCESSO_CHANGE,json.toString());
                    System.out.println(parametri);
                    byte[] postData = parametri.toString().getBytes("UTF-8");
                    int postDataLength = postData.length;
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setInstanceFollowRedirects(false);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                    conn.setUseCaches(false);
                    conn.getOutputStream().write(postData);
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    String data = convertStreamToString(stream);
                    //A QUA

                    Log.i("RISULTATO " + data, new StringBuilder().toString()); //QUA PUO USCIRE IL JSON O LA STRINGA
                    stream.close();
                    //ASSOCIO ALLA VARIABILE IL RISULTATO DELLA RICHIESTA
                    Log.i("STRINGA RIC = ", data.toString());
                    parsingComplete = false;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void inviaRichiestaCancella(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //NON OSO TOCCARE DA QUA
                try{

                    String parametri = generaParametri(TIPO_ELEMENTO,ACCESSO_DELETE,json.toString());
                    System.out.println(parametri);
                    byte[] postData = parametri.toString().getBytes("UTF-8");
                    int postDataLength = postData.length;
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setInstanceFollowRedirects(false);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                    conn.setUseCaches(false);
                    conn.getOutputStream().write(postData);
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    String data = convertStreamToString(stream);
                    //A QUA

                    Log.i("RISULTATO " + data, new StringBuilder().toString()); //QUA PUO USCIRE IL JSON O LA STRINGA
                    stream.close();
                    //ASSOCIO ALLA VARIABILE IL RISULTATO DELLA RICHIESTA
                    Log.i("STRINGA RIC = ", data.toString());
                    parsingComplete = false;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void inviaRichiestaLettura(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //NON OSO TOCCARE DA QUA
                try{

                    String urlParameters = generaParametri(TIPO_ELEMENTO, ACCESSO_READ, "");
                    byte[] postData = urlParameters.toString().getBytes("UTF-8");
                    int postDataLength = postData.length;
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setInstanceFollowRedirects(false);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                    conn.setUseCaches(false);
                    conn.getOutputStream().write(postData);
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    String data = convertStreamToString(stream);
                    //A QUA

                    Log.i("RISULTATO " + data, new StringBuilder().toString()); //QUA PUO USCIRE IL JSON O LA STRINGA
                    //elaboro il JSON
                    elaboraJson(data);
                    stream.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public void inviaRichiestaSpecifica(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //NON OSO TOCCARE DA QUA
                try{

                    String urlParameters = generaParametri(TIPO_ELEMENTO, ACCESSO_READ, json.toString());
                    byte[] postData = urlParameters.toString().getBytes("UTF-8");
                    int postDataLength = postData.length;
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setInstanceFollowRedirects(false);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                    conn.setUseCaches(false);
                    conn.getOutputStream().write(postData);
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    String data = convertStreamToString(stream);
                    //A QUA

                    Log.i("RISULTATO " + data, new StringBuilder().toString()); //QUA PUO USCIRE IL JSON O LA STRINGA
                    //elaboro il JSON
                    elaboraJson(data);
                    stream.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    private void elaboraJson(String result){
        json = null;
        try {
            //creo un nuovo oggetto Json dal risultato ottenuto dal server
            json = new JSONObject(result);
            //visualizzo il JSON
            System.out.println("JSON = " + json.toString());

            //prendo l'array degli appuntamenti dando in input la chiave di accesso al file
            dispArray = json.getJSONArray("disponibilita");

            //ora devo prendere i singoli appuntamenti nell'array
            for (int i = 0; i< dispArray.length(); i++){
                JSONObject disp = dispArray.getJSONObject(i);
                dispList.add(new MieDisp(
                        disp.getString("id"),
                        disp.getString("data"),
                        disp.getString("oraInizio"),
                        disp.getString("oraFine"),
                        disp.getString("intervento"),
                        disp.getString("ripetizione"),
                        disp.getString("fineRipetizione")
                ));


            }
            parsingComplete = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public String lastId() {
        return dispList.get(dispList.size()-1).getId();
    }

    public ArrayList<MieDisp> getdispList(){
        return dispList;
    }

}
