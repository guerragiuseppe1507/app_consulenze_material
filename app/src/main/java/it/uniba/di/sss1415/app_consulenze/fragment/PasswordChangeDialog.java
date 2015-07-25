package it.uniba.di.sss1415.app_consulenze.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;

/**
 * Created by Mary on 25/07/2015.
 * Permette all\'utente di cambiare password.
 */
public class PasswordChangeDialog extends DialogFragment {
    private String password;
    private Connection conn;
    private View v;

    private static final String TIPO_ELEMENTO="cambioPsw";
    private static final String ACCESSO = "write";

    private ChangePassTask changePassTask;
    private String oldPW;
    private String newPW;
    private String confPW;
    private MainActivity mainActivity;

    private String fieldsRequired;
    private String oldsameprevious;
    private String notmatch;
    private String timeoutMsg;
    private String passwordEdited;

    TextView oldPwd;
    EditText newPwd;
    EditText confPwd;

    public static PasswordChangeDialog newInstance(){
        PasswordChangeDialog pcd = new PasswordChangeDialog();
        return pcd;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        password = UserSessionInfo.getInstance().getPassword();
        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.password_change_dialog, null);

        fieldsRequired = getResources().getString(R.string.allFieldsNeeded);
        oldsameprevious = getResources().getString(R.string.sameOldPass);
        notmatch = getResources().getString(R.string.nothesame);
        timeoutMsg = getResources().getString(R.string.conn_timeout);
        passwordEdited = getResources().getString(R.string.passChanged);
        mainActivity = ((MainActivity)getActivity());
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)

                // Add action buttons
                .setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        // invio dati al server
                        //availabilityTask = new AvailabilityTask(exp, date, sTime, eTime, rep, until, user, change);
                        //availabilityTask.execute();
                        oldPW = oldPwd.getText().toString();
                        newPW = newPwd.getText().toString();
                        confPW = confPwd.getText().toString();

                        if(!newPW.equals("") && !confPW.equals("")){
                            //controlli sulle password inserite
                            if(!oldPW.equals(newPW)) {
                                if(newPW.equals(confPW)){
                                    changePassTask = new ChangePassTask(newPW);
                                    changePassTask.execute();
                                } else {
                                    creaMessaggio(notmatch);
                                }
                            } else {
                                creaMessaggio(oldsameprevious);
                            }
                        } else {
                            creaMessaggio(fieldsRequired);
                        }

                    }
                })
                .setNegativeButton(R.string.back_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PasswordChangeDialog.this.getDialog().cancel();
                    }
                });

        Dialog d = builder.create();

        //recupero view
        oldPwd = (TextView) v.findViewById(R.id.old_pwd);
        oldPwd.setText(UserSessionInfo.getInstance().getPassword());
        newPwd = (EditText) v.findViewById(R.id.new_pwd);
        confPwd = (EditText) v.findViewById(R.id.conf_pwd);


        return d;
    }


    // connection to server
    public class ChangePassTask extends AsyncTask<String, Void, String> {

        private String nuovaP;

        ChangePassTask(String nuovaP) {
            this.nuovaP = nuovaP;
        }

        @Override
        protected String doInBackground(String... params) {

            conn.setParametri(TIPO_ELEMENTO, ACCESSO,UserSessionInfo.getInstance().getEmail(), nuovaP);

            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            if (result.equals(ToastMsgs.CONN_TIMEOUT)) {

                creaMessaggio(timeoutMsg);

            } else {
                salvaInShared(nuovaP);
                UserSessionInfo.getInstance().setPassword(nuovaP);
                creaMessaggio(passwordEdited);
            }

            changePassTask = null;

        }



    }
    public void salvaInShared(String pass){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("password",pass).apply(); // new
        editor.commit();
    }

    public void creaMessaggio(CharSequence message) {
        Context context = v.getContext();
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }
}
