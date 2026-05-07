/**
 *
 */
package com.clubsport.util;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *JsonUtil est une classe qui sert à convertir tout type 
 *d'objets en reponses JSON
 *
 *@author bpenw
 *@version 1.0
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
    public static void main (String args[]) {
    	String mess = "Message" + " : " + "Parametres manquants";
    	String json = JsonUtil.toJson(mess);
    	System.out.print(json);
    }
}
