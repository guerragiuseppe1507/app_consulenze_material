package it.uniba.di.sss1415.app_consulenze.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Giuseppe on 17/06/2015.
 */
public class JsonHandler {

    public static JSONObject generaJson(String tipoElem, String[] params) throws JSONException {
        JSONObject json = new JSONObject();
        String[] strutturaJson = StrutturaJson.getStruct(tipoElem);
        for (int j = 0; j < strutturaJson.length; j++){
            json.put(strutturaJson[j],params[j]);
        }

        return json;
    }

    public static ArrayList<HashMap<String,String>> fromJsonToMapList(String tipoElem, String result) throws JSONException {

        //creo un nuovo oggetto Json dal risultato ottenuto dal server
        JSONObject oggettoJson = new JSONObject(result);
        String[] strutturaJson = StrutturaJson.getReturnStruct(StrutturaJson.getReturnStructName(tipoElem));

        JSONArray resultArray = oggettoJson.getJSONArray(StrutturaJson.getReturnStructName(tipoElem));

        ArrayList<HashMap<String,String>> listaElem = new ArrayList<HashMap<String,String>>();

        for (int i = 0; i<resultArray.length(); i++){
            HashMap<String,String> map = new HashMap<String, String>();
            JSONObject singoloResult = resultArray.getJSONObject(i);

            for (int j = 0; j < strutturaJson.length; j++){
                map.put(strutturaJson[j], singoloResult.getString(strutturaJson[j]));
            }

            listaElem.add(map);
        }

        return listaElem;
    }

}
