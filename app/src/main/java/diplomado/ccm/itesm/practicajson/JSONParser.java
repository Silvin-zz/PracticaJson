package diplomado.ccm.itesm.practicajson;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Laboratorios on 7/25/15.
 */
public class JSONParser {


    InputStream inputStream         = null;
    JSONArray   jarray              = null;
    String      json                = null;


    public void JSONParse(){

    }


    public JSONArray getJSONFromUrl(String url){


        jarray                      = null;
        StringBuilder   builder     = new StringBuilder();
        HttpClient      client      = new DefaultHttpClient();
        HttpGet         httpGet     = new HttpGet(url);

        try{

            HttpResponse    response    = client.execute(httpGet);
            StatusLine      status      = response.getStatusLine();
            int statusCode              = status.getStatusCode();

            if(statusCode == HttpStatus.SC_OK){  //Si respondio con 200 la url

                HttpEntity      entity     = response.getEntity();
                InputStream     content    = entity.getContent();
                BufferedReader  reader     = new BufferedReader( new InputStreamReader(content));
                String          line;

                while ((line = reader.readLine()) != null){

                    builder.append(line);
                }

            } // Si el status code fue diferente de 200
            else{
                Log.e("JSONParser", "error al descargar el archivo, " + statusCode);
            }

        }
        catch (IOException e){  //Cachamos el error general por si se genera al momento de intentar consumir la url.
            e.printStackTrace();
        }


        /** Convertimos el resultado en JSON **/

        try{  //Intentamos convertir en json el builder.
            jarray = new JSONArray(builder.toString());
        }
        catch (JSONException e){  //Excepcion si no se pudo convertir el json el builder.
            e.printStackTrace();  //Manda la excepcion al log.
        }
        Log.e("Salufos", url);
        Log.e("saludos", "=============================================");
        Log.e("saludos", Integer.toString(jarray.length()));
        try {
            Log.e("Saludoss", jarray.get(0).toString());
            Log.e("Saludoss", jarray.get(1).toString());
            Log.e("Saludoss", jarray.get(2).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jarray;


    }

}
