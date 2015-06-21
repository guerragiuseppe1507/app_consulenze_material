package it.uniba.di.sss1415.app_consulenze.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Random;

import androidhive.info.materialdesign.R;
import it.uniba.di.sss1415.app_consulenze.istances.Connection;
import it.uniba.di.sss1415.app_consulenze.istances.DatiUtente;
import it.uniba.di.sss1415.app_consulenze.util.ServerMsgs;


public class RegistrazioneActivity extends Activity {

    private Connection conn;
    private String parametriServer;

    private static final String TIPO_ELEMENTO = "appuntamenti";
    private static final String ACCESSO = "read";


    public EditText editEmail;
    private EditText editAnnoIscrizione;
    private EditText editNumero;
    private Spinner spinnerProvince;

    //per la generazione della password
    private static final String _CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STR_LENGTH = 16;
    private String password; //password dda generare random

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        //TODO implementazione

        conn = new Connection(getApplicationContext().getResources().getString(R.string.serverQuery));

        editEmail = (EditText) findViewById(R.id.reg_email_et);
        editAnnoIscrizione = (EditText) findViewById(R.id.reg_anno_et);
        editNumero = (EditText) findViewById(R.id.reg_num_et);

        // Populate the Spinner with the specialties array
        spinnerProvince = (Spinner) findViewById(R.id.province_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arrayProvince, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerProvince.setAdapter(adapter);

        Button loginButton = (Button) findViewById(R.id.link_to_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLogin();
            }
        });

    }

    //generatore della password
    public String generaPass(){
        char[] chars = _CHAR.toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < RANDOM_STR_LENGTH; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }

    //prende i dati inseriti e genera la stringa di parametri
    public void prendiDati() {
        password = generaPass();//generazione della password

        //recuperiamo i dati inseriti dall'utente
        String email = editEmail.getText().toString();
        String anno = editAnnoIscrizione.getText().toString();
        String numero = editNumero.getText().toString();
        String provincia = spinnerProvince.getSelectedItem().toString();

        //genero la stringa di parametri
        conn.setParametri(TIPO_ELEMENTO, ACCESSO, email, numero, anno, provincia, password);
        Log.i("PARAMETRI = ", conn.getParametri());
    }

    //evento per registrare l'utente
    public void registrazioneUtente(View v){
        //recupero tutti i dati inseriti dall'utente e genero la stringa dei parametri da inviare memorizzando tutto nei membri interni
        prendiDati();
        inviaRichiesta();

        mostraHome();

    }

    public class UserRegisterTask extends AsyncTask<String, Void, String> {

        private final String mEmail;
        private final String mPassword;



        UserRegisterTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(String... params) {

            conn.setParametri(TIPO_ELEMENTO, ACCESSO, mEmail, mPassword);

            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            if (result.equals(ServerMsgs.CONN_TIMEOUT)){

                creaMessaggio(getApplicationContext().getResources().getString(R.string.conn_timeout));

            } else if (result.equals(ServerMsgs.NO_USER_FOUND)) {

                creaMessaggio(getApplicationContext().getResources().getString(R.string.fail_login));
            } else {
                //datiUser = new DatiUtente(result);
                datiUser = new DatiUtente(result);
                toMain();
                creaMessaggio(getApplicationContext().getResources().getString(R.string.success_login));

            }

            mAuthTask = null;

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }



}
