package it.uniba.di.sss1415.app_consulenze.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Giuseppe on 14/07/2015.
 */
public class ServerResponseDataSorter {

    public static void sort(ArrayList<HashMap<String,String>> res){
        Collections.sort(res, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {

                if (lhs.get("data").equals(rhs.get("data"))) {
                    return lhs.get("oraInizio").compareTo(rhs.get("oraInizio"));
                } else {
                    return lhs.get("data").compareTo(rhs.get("data"));
                }

            }
        });
    }


}
