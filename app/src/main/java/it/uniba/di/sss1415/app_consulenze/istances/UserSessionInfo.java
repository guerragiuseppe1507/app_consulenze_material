package it.uniba.di.sss1415.app_consulenze.istances;

/**
 * Created by Valerio on 13/07/2015.
 */
public class UserSessionInfo {

    //Dichiarazione e instanziazione di un oggetto della classe Context
    private final static UserSessionInfo instance = new UserSessionInfo();

    //Metodo che restituisce l'oggetto statico instance di tipo Context
    public static UserSessionInfo getInstance() {
        return instance;
    }

    //Instanziazione degli oggetti Email
    private String email = "";

    //Di seguito i metodi che permettono di accedere in lettura e scrittura alla mail
    public void setEmail(String input) {

        email = input;
    }

    public String getEmail(){

        return email;
    }

}