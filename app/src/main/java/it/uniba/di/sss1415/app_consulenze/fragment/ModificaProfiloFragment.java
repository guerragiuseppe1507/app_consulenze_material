package it.uniba.di.sss1415.app_consulenze.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.Connection;
import it.uniba.di.sss1415.app_consulenze.util.JsonHandler;
import it.uniba.di.sss1415.app_consulenze.util.ToastMsgs;

/**
 * Created by Giuseppe on 15/07/2015.
 */

public class ModificaProfiloFragment extends Fragment {

    private boolean login=false;
    private TextView ETemail, ScoreValue;
    private EditText ETnome, ETcognome, ETanno, ETnumero;
    private Spinner lista_province;
    private Spinner lista_branche_mediche;
    private Button btnupdate;
    private Button btnModPwd;
    PasswordChangeDialog pwdChangeDialog;
    private String password="";
    private String spec="";
    private ArrayList<String> query_spec;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<String> med_adapter;


    private static final String TIPO_ELEMENTO="modificaP";
    private static final String ACCESSO = "write";

    private Connection conn;

    private EditProfileTask editTask;

    public ModificaProfiloFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conn = new Connection(getActivity().getApplicationContext().getResources().getString(R.string.serverQuery));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_modificaprofilo, container, false);

        //inizializza campi
        ETemail= (TextView) v.findViewById(R.id.etModMail);
        ETnome= (EditText) v.findViewById(R.id.etModNome);
        ETcognome= (EditText) v.findViewById(R.id.etModCognome);
        ETanno= (EditText) v.findViewById(R.id.etModAnno);
        ETnumero= (EditText) v.findViewById(R.id.etModNumero);
        ScoreValue = (TextView) v.findViewById(R.id.etModScore);

        ETemail.setText(UserSessionInfo.getInstance().getEmail());
        ETnome.setText(UserSessionInfo.getInstance().getNome());
        ETcognome.setText(UserSessionInfo.getInstance().getCognome());
        ETanno.setText(UserSessionInfo.getInstance().getAnnoIscr());
        ETnumero.setText(UserSessionInfo.getInstance().getNumIscr());
        ScoreValue.setText(UserSessionInfo.getInstance().getScore());
        // inizializzazione spinner province
        lista_province = (Spinner) v.findViewById(R.id.spinnerModProvince);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.arrayProvince,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lista_province.setAdapter(adapter);
        lista_province.setSelection(adapter.getPosition(UserSessionInfo.getInstance().getProvincia()));
                // inizializzazione spinner branche mediche
                lista_branche_mediche = (Spinner) v.findViewById(R.id.spinnerModSpec);

        med_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, UserSessionInfo.getInstance().getBranche());
        med_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lista_branche_mediche.setAdapter(med_adapter);

        String exp = UserSessionInfo.getInstance().getExp();

        if(!exp.equals("Not set yet")){
            lista_branche_mediche.setSelection(med_adapter.getPosition(exp));
        }

        btnupdate = (Button) v.findViewById(R.id.btnConfermaModifiche);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifica_profilo();
            }
        });

        btnModPwd = (Button) v.findViewById(R.id.btnModificaPassword);
        btnModPwd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //TODO far partire la dialog
                pwdChangeDialog = PasswordChangeDialog.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                pwdChangeDialog.show(ft, "changePwd");

            }
        });

        return v;
    }

    public void modifica_profilo(){

        String nome =  ETnome.getText().toString();
        String cognome= ETcognome.getText().toString();
        String anno=  ETanno.getText().toString();
        String numero = ETnumero.getText().toString();
        String spec = lista_branche_mediche.getSelectedItem().toString();

        System.out.println(spec);

        String error = "Edit done";

        if      (nome.compareTo("")==0) {error = getActivity().getResources().getString(R.string.insertName); ETnome.setError(error);}
        else if (nome.contains(",")) {error = getActivity().getResources().getString(R.string.InvalidValue); ETnome.setError(error);}
        else if (cognome.compareTo("")==0) {error = getActivity().getResources().getString(R.string.insertSurname);ETcognome.setError(error);}
        else if (cognome.contains(",")) {error = getActivity().getResources().getString(R.string.InvalidValue); ETcognome.setError(error);}
        else if (anno.compareTo("")==0) {error = getActivity().getResources().getString(R.string.insertYear);ETanno.setError(error);}
        else if (anno.contains(",")) {error = getActivity().getResources().getString(R.string.InvalidValue); ETanno.setError(error);}
        else if (numero.compareTo("")==0) {error = getActivity().getResources().getString(R.string.insertNumber);ETnumero.setError(error);}
        else if (numero.contains(",")) {error = getActivity().getResources().getString(R.string.InvalidValue); ETnumero.setError(error);}
        else if (spec==null || spec.compareTo("")==0) {error = getActivity().getResources().getString(R.string.conn_timeout);}
        else{
            editTask = new EditProfileTask(UserSessionInfo.getInstance().getEmail(),
                    nome,
                    cognome,
                    lista_province.getSelectedItem().toString(),
                    anno,
                    numero,
                    spec,
                    ""
                    );
            editTask.execute();
        }
    }

    @Override
    public void onDetach() {
        super.onStop();
        if(editTask!=null)editTask.cancel(true);
    }

    public class EditProfileTask extends AsyncTask<String, Void, String> {

        private String email;
        private String nome;
        private String cognome;
        private String provincia;
        private String anno;
        private String numero;
        private String primaEx;
        private String altreEx;

        EditProfileTask(String email,
                 String nome,
                 String cognome,
                 String provincia,
                 String anno,
                 String numero,
                 String primaEx,
                 String altreEx) {

            this.email= email;
            this.nome=nome;
            this.cognome=cognome;
            this.provincia=provincia;
            this.anno=anno;
            this.numero=numero;
            this.primaEx=primaEx;
            this.altreEx=altreEx;
        }

        @Override
        protected String doInBackground(String... params) {

            conn.setParametri(TIPO_ELEMENTO, ACCESSO, email, nome, cognome, provincia, anno, numero, primaEx, altreEx);

            return conn.newConnect();

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);
            if (!result.equals(ToastMsgs.CONN_TIMEOUT)) {

                UserSessionInfo u = UserSessionInfo.getInstance();
                u.setNome(nome);
                u.setCognome(cognome);
                u.setProvincia(provincia);
                u.setAnnoIscr(anno);
                u.setNumIscr(numero);
                u.setExp(primaEx);

                salvaInShared(primaEx);

                creaMessaggio(getActivity().getApplicationContext().getResources().getString(R.string.profileEdited));
            }

            editTask = null;

        }

        @Override
        protected void onCancelled() {
            editTask = null;
        }
    }

    public void salvaInShared(String exp){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("exp", exp);
        editor.commit();
    }

    public void creaMessaggio(CharSequence message){
        Context context = getActivity().getApplicationContext();
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }
}
