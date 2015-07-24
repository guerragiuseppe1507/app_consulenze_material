package it.uniba.di.sss1415.app_consulenze.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Random;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.ServerMsgs;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;


public class RegistrazioneActivity extends Activity {

    SharedPreferences prefs ; //new
    SharedPreferences.Editor editor ; //new

    private Connection conn;
    private String parametriServer;
    private UserRegisterTask mRegTask = null;

    private static final String TIPO_ELEMENTO = "registrazione";
    private static final String ACCESSO = "write";


    public EditText editEmail;
    private EditText editAnnoIscrizione;
    private EditText editNumero;
    private Spinner spinnerProvince;
    private Button regButton;
    private Button loginButton;

    //per la generazione della password
    private static final String _CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STR_LENGTH = 16;

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

        regButton = (Button) findViewById(R.id.btnRegister);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrazioneUtente();
            }
        });

        loginButton = (Button) findViewById(R.id.link_to_login);
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

    //evento per registrare l'utente
    public void registrazioneUtente(){
        //recupero tutti i dati inseriti dall'utente e genero la stringa dei parametri da inviare memorizzando tutto nei membri interni
        String password = generaPass();//generazione della password

        //recuperiamo i dati inseriti dall'utente
        String email = editEmail.getText().toString();
        String anno = editAnnoIscrizione.getText().toString();
        String numero = editNumero.getText().toString();
        String provincia = spinnerProvince.getSelectedItem().toString();

        mRegTask = new UserRegisterTask(email, numero, anno, provincia, password);
        mRegTask.execute();

    }

    public class UserRegisterTask extends AsyncTask<String, Void, String> {

        private final String mEmail;
        private final String mNumero;
        private final String mAnno;
        private final String mProvincia;
        private final String mPassword;



        UserRegisterTask(String email, String numero, String anno , String provincia, String password) {
            mEmail = email;
            mNumero = numero;
            mAnno = anno;
            mProvincia = provincia;
            mPassword = password;
        }

        @Override
        protected String doInBackground(String... params) {

            conn.setParametri(TIPO_ELEMENTO, ACCESSO, mEmail, mNumero, mAnno, mProvincia, mPassword);
            Log.i("PARAMETRI = ", conn.getParametri());

            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            if (result.equals(ToastMsgs.CONN_TIMEOUT)){

                creaMessaggio(getApplicationContext().getResources().getString(R.string.conn_timeout));

            } else if (result.equals(ServerMsgs.NO_USER_FOUND)) {
                creaMessaggio(getApplicationContext().getResources().getString(R.string.fail_login));
            } else {
                UserSessionInfo u = UserSessionInfo.getInstance();
                u.setEmail(mEmail);
                u.setNome("Not set yet");
                u.setCognome("Not set yet");
                u.setProvincia(mProvincia);
                u.setAnnoIscr(mAnno);
                u.setNumIscr(mNumero);
                u.setExp("Not set yet");
                u.setPassword(mPassword);


                salvaInPref(mEmail,mPassword);
                toLogin(); // new prima c'era toMain
                creaMessaggio(getApplicationContext().getResources().getString(R.string.success_register));

            }

            mRegTask = null;

        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
        }
    }

    public void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void toMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void salvaInPref(String mail , String pass){

        prefs = PreferenceManager.getDefaultSharedPreferences(this); //new
        editor = prefs.edit(); //new
        editor.clear();
        editor.putString("email",mail).apply(); // new
        editor.putString("password",pass).apply(); // new
        editor.commit();
    }

    public void creaMessaggio(CharSequence message){
        Context context = getApplicationContext();
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }

}
