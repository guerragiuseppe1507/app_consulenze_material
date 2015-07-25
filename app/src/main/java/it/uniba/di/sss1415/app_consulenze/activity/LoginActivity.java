package it.uniba.di.sss1415.app_consulenze.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.istances.DatiUtente;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.ServerMsgs;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity{

    SharedPreferences pref;
    SharedPreferences.Editor editor ;


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] CREDENTIALS = new String[]{
            "asdsd@foo.com", "4XSRG08ZP2IHQUX"
    };

    private String credEmail;   // new
    private String credPassword;// new

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private Connection conn;
    private DatiUtente datiUser;

    private static final String ACCESSO = "write";
    private static final String TIPO_ELEMENTO = "accediSistema";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        //editor = pref.edit();
        //editor.putString("email", CREDENTIALS[0]); // new
        //editor.putString("password", CREDENTIALS[1]); // new
       //editor.commit();
        try {

            credEmail = pref.getString("email", "");
            credPassword = pref.getString("password", "");

        }catch (NullPointerException noCred){
            credEmail = "";
            credPassword = "";

        }


        conn = new Connection(getApplicationContext().getResources().getString(R.string.serverQuery));
        System.out.println(getApplicationContext().getResources().getString(R.string.serverQuery));

        mAuthTask = new UserLoginTask();
        mAuthTask.execute();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mEmailView.setText(credEmail);
        //mEmailView.setText(CREDENTIALS[0]);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setText(credPassword);
       // mPasswordView.setText(CREDENTIALS[1]);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button registraButton = (Button) findViewById(R.id.link_to_signup);
        registraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegistraUtente();
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        //return password.length() > 4;
        return true;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, String> {

        private final String mEmail;
        private final String mPassword;
        private boolean brancheMediche;



        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            brancheMediche=false;
        }

        UserLoginTask() {
            mEmail = null;
            mPassword = null;
            brancheMediche=true;
        }
        @Override
        protected String doInBackground(String... params) {


            if(brancheMediche){
                System.out.println("ciao");
                conn.setParametri("accesso:read, elemento:brancheMediche, jsonDaScrivere:");
            }else{
                conn.setParametri(TIPO_ELEMENTO, ACCESSO, mEmail, mPassword);
            }


            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);
            if(brancheMediche){
                if (result.equals(ToastMsgs.CONN_TIMEOUT )){

                    UserSessionInfo.getInstance().setBranche(pref.getString("branche", ""));
                }else{
                    UserSessionInfo.getInstance().setBranche(result);
                    salvaBrancheInPref(result);
                }
            }else{
                if (result.equals(ToastMsgs.CONN_TIMEOUT )&& !brancheMediche){

                    creaMessaggio(getApplicationContext().getResources().getString(R.string.conn_timeout));

                } else if (result.equals(ServerMsgs.NO_USER_FOUND)&& !brancheMediche) {

                    creaMessaggio(getApplicationContext().getResources().getString(R.string.fail_login));
                } else if(!brancheMediche) {
                    //datiUser = new DatiUtente(result);
                    creaIstanza(result);
                    salvaInPref(mEmail, mPassword);
                    toMain();
                    creaMessaggio(getApplicationContext().getResources().getString(R.string.success_login));

                }
            }



            mAuthTask = null;

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void creaIstanza(String res){
        UserSessionInfo.getInstance().setParams(this,res);
    }

    public void creaMessaggio(CharSequence message){
        Context context = getApplicationContext();
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }

    public void toMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toRegistraUtente(){
        Intent intent = new Intent(this, RegistrazioneActivity.class);
        startActivity(intent);
    }

    public void salvaInPref(String mail , String pass){

        pref = PreferenceManager.getDefaultSharedPreferences(this); //new
        editor = pref.edit(); //new
        editor.putString("email",mail).apply(); // new
        editor.putString("password",pass).apply(); // new
        editor.commit();
    }

    public void salvaBrancheInPref(String result){

        pref = PreferenceManager.getDefaultSharedPreferences(this); //new
        editor = pref.edit(); //new
        editor.putString("branche",result).apply();
        editor.commit();
    }

}

