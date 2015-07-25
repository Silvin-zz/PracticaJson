package diplomado.ccm.itesm.practicajson;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ConsultaJsonActivity extends Activity {


    private final String URL_BASE               = "http://10.48.214.98:8080/WebDemoJson/";
    private final String URL_EPOCAS             = URL_BASE + "Despachador?servicio=getAllEpocas";
    private final String URL_MONEDAS            = URL_BASE + "Despachador?servicio=getNombresByEpoca&epoca=";
    private final String URL_DETALLE_MONEDA     = URL_BASE + "Despachador?servicio=getMonedaByEpocaAndNombre&epoca=";
    private final String URL_IMAGE              = URL_BASE + "/images/";


    private DetalleFichaTecnica dft;
    private Spinner             spEpoca;
    private Spinner             spMoneda;
    private WebView             wv;
    private List<String>        listEpocas;
    private List<String>        listMonedas;



    //Listener de moneda
    private AdapterView.OnItemSelectedListener itemMonedaListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String epoca    = (String)spEpoca.getSelectedItem();
            String moneda   = (String)spMoneda.getSelectedItem();
            Thread thread   = new Thread(new runnableDetalleFichaTecnica(epoca,moneda));
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            wv.loadUrl(URL_IMAGE + dft.getImgAnverso());

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    //Listener de Epoca.
    private AdapterView.OnItemSelectedListener itemEpocaListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String epoca    = (String) spEpoca.getSelectedItem();
            Thread thread   = new Thread(new runnableMonedas(epoca));
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArrayAdapter<String> aa = new ArrayAdapter<String>(ConsultaJsonActivity.this, android.R.layout.simple_spinner_item, listMonedas);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMoneda.setAdapter(aa);


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };




    /**
     * OBtenemos las epocas y las cargamos a un array (String) de epocas.
     */
    private Runnable runnableEpocas  =  new Runnable() {
        @Override
        public void run() {
            listEpocas = new ArrayList<>();
            Log.e("Runnable Epocas", URL_EPOCAS);
            JSONParser jParser      = new JSONParser();
            JSONArray json          = jParser.getJSONFromUrl(URL_EPOCAS);
            for(int i = 0; i < json.length(); i++){

                try{
                    listEpocas.add(json.getString(i));
                }
                catch (JSONException je){
                    Log.e("Runable", je.getMessage());
                }

            } //Fin de recorrer todas las epocas que devuelve el servicio

        }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_json);

        spEpoca         = (Spinner) findViewById(R.id.spinnerEpoca);
        spMoneda        = (Spinner) findViewById(R.id.spinnerMoneda);
        wv              = (WebView) findViewById(R.id.webViewImgMoneda);

        Thread thread   = new Thread(this.runnableEpocas);

        try {

            thread.start();
            thread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> aa = new ArrayAdapter<String>(ConsultaJsonActivity.this, android.R.layout.simple_spinner_item, listEpocas);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEpoca.setAdapter(aa);

        spEpoca.setOnItemSelectedListener(itemEpocaListener);
        spMoneda.setOnItemSelectedListener(itemMonedaListener);






    }


    private class runnableMonedas implements Runnable{

        String nombreEpoca;

        public runnableMonedas(String nombreEpoca) {
            this.nombreEpoca = nombreEpoca.replace(" ", "%20");
        }

        public void run(){
            listMonedas = new ArrayList<>();
            Log.e("Runnable Monedas", URL_MONEDAS + this.nombreEpoca);
            JSONParser jParser      = new JSONParser();
            JSONArray json          = jParser.getJSONFromUrl(URL_MONEDAS + this.nombreEpoca);
            for(int i = 0; i < json.length(); i++){

                try{
                    listMonedas.add(json.get(i).toString());
                }
                catch (JSONException je){
                    Log.e("Runable", je.getMessage());
                }

            } //Fin de recorrer todas las epocas que devuelve el servicio


        } //Fin metodo run

    } //Fin de clase privada runnableMonedas.


    private class runnableDetalleFichaTecnica implements Runnable{

        private String epoca;
        private String moneda;


        public runnableDetalleFichaTecnica(String epoca, String moneda) {

            this.epoca              = epoca.replace(" ", "%20");
            this.moneda             = moneda.replace(" ", "%20");
        }

        public void run(){

            Log.e("Runnable Detalle FT", URL_DETALLE_MONEDA + this.epoca + "&nombre=" + this.moneda);
            JSONParser  jParser     = new JSONParser();
            JSONArray   json        = jParser.getJSONFromUrl(URL_DETALLE_MONEDA + this.epoca + "&nombre=" + this.moneda);
            Gson        gson        = new Gson();
            JSONObject  c           = null;

            try {
                c                   = json.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            dft                     = gson.fromJson(c.toString(), DetalleFichaTecnica.class);






        } //Fin del metodo run
    }





}

