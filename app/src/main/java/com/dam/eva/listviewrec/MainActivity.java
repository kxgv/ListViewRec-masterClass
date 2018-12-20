package com.dam.eva.listviewrec;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity  {

    Button boto, botoDesc;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter,tAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView textView;
    private boolean sdAvailable=false;
    private boolean sdWriteAccess=false;
    final static String API_KEY="cbd7c30e4df4db78909fa86ab147b342";

    //TODO: 17/12/18  TREURE API

    private String city;
    private EditText editTextCity;
    private int DATASET_COUNT = 60;
    private Button butJ, butX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView= (TextView) findViewById(R.id.textView);
        butJ =(Button) findViewById(R.id.buttonJ);
        butX =(Button) findViewById(R.id.buttonX);
        butJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openWeather(true);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        butX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openWeather(false);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        String estat=Environment.getExternalStorageState();
        // comprova si hi ha SD i si puc escriure en ella
        if (estat.equals(Environment.MEDIA_MOUNTED))
        {
            sdAvailable=true;
            sdWriteAccess=true;
        }
        else if (estat.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            sdAvailable=true;
            sdWriteAccess=false;
        }
        else {
            sdAvailable=false;
            sdWriteAccess=false;
            //manifest --> uses-permission
        }
        editTextCity=(EditText) findViewById(R.id.editTextCity);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> input = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            input.add("Test" + i);
        }
        mAdapter = new MyAdapter(input);
        recyclerView.setAdapter(mAdapter);
    }

    //es crida quan clickes botó principal, únic que hi haurà  la pràctica.
    // el paràmetre format no cal per la PT16, només un
    public void logica(Boolean format, String nomCiutat) throws ParseException, JSONException, XmlPullParserException {
        //previ a descarregar cap fitxer xml o json, veure si hi és a la bbdd amb el criteri d' actualització de dades que ens convé...
        //mitja hora ha passat ja desde la primera línia de temperatures, ha passat,
        //o potser volem veure que la ultima fila donada de dades, la seva hora no sigui anterior al dia actual.o sigui, max 5 dies.

        TemperaturesHelper2 temperaturesHelper = new TemperaturesHelper2(this);

        if (!(temperaturesHelper.estaCiutatDescarregada(nomCiutat) && temperaturesHelper.estaActualitzada(nomCiutat))) {
            //eliminar dades obsoletes
            temperaturesHelper.eliminaDades(nomCiutat);
            //descàrrega xml /json
            openWeather(format);
            //temperaturesHelper.guarda(nomCiutat, temps<Bloc>);

        }
        // si està ja descarregada i actualitzada,...
        // inflar fragments dinàmics (segons radio buttons de formats, o spinners (innovació demanada a la PT16)
        // ... o sino, com fins ara, cridar a mostra l'adapter

        temperaturesHelper.close();
    }

    public void openWeather(Boolean JSonFormat) throws XmlPullParserException, JSONException {
        String nomCiutat;
        city= editTextCity.getText().toString();
        if (!city.isEmpty()) nomCiutat=city;
        else nomCiutat="Petropavlovsk";

        //nomCiutat="Irkutsk";

        String result;
        //Boolean JSonFormat=true;
        String myUrl;

        if (!JSonFormat)
         myUrl = "http://api.openweathermap.org/data/2.5/forecast?q=" +
                nomCiutat + "&units=metric&mode=xml&appid=" + API_KEY;
        else
         myUrl = "http://api.openweathermap.org/data/2.5/forecast?q=" +
                nomCiutat + "&units=metric&appid=" + API_KEY;


        Descarregador descarregador = new Descarregador(this);
        try {

            result = descarregador.execute(myUrl).get();

            //Toast.makeText(this, "Result és:" + result, Toast.LENGTH_SHORT).show();
            List<Temp> temps;

            if (!(result == null)) {
                Parser pars = new Parser();
                if (!JSonFormat) {
                    temps = pars.parsejaXml(result);
                    //List<Temp> model = new ArrayList<>();
                    Temp temp = new Temp("dataProvaThoraprova", "22", "hot","100");
                    //model.add(temp);
                    temps.add(temp);
                } else {
                    //Log.d("test", "openWeather,entra parsejaJSon ");

                    temps = pars.parsejaJSon(result);
                    Temp temp = new Temp("dataProva horaprova", "22", "hot","100");
                    temps.add(temp);
                }

                if (temps != null) {
                    tAdapter = new CustomAdapter(temps);
                    recyclerView.setAdapter(tAdapter);
                }

                //guardem temps a BBDD
                //TemperaturesHelper temperaturesHelper = new TemperaturesHelper(this);
                TemperaturesHelper2 temperaturesHelper2 = new TemperaturesHelper2(this);
                temperaturesHelper2.guarda(nomCiutat,temps);

                //List<Temp> tempsLlegidesBBDD=temperaturesHelper.llegeix("Barcelona");
                //mostra en bucle, al adapter...
            }
    } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("test", "openWeather: "+e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("test", "openWeather: "+e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("test", "openWeather: "+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("test", "openWeather: "+e.getMessage());
        }
    }

    class Descarregador extends AsyncTask<String, Void, String> {

        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        public Descarregador(Context context) {
        }
        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];

            //String stringUrl1 = params[1];
            //String stringUrl2 = params[2];

            String result = "";
            String inputLine;
            try {

                URL myUrl = new URL(stringUrl);

                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

                Log.d("teste",  stringUrl);

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();

                    InputStreamReader streamReader
                            = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                        //Log.d("teste", inputLine);

                    }
                    reader.close();
                    streamReader.close();
                    connection.disconnect();
                    result = stringBuilder.toString();

                Log.d("testparsed", " " + result);



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.d("teste", e.getMessage());

            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            int id = item.getItemId();
            if (id == R.id.readMemInt) {

                FileInputStream fis= openFileInput("memint2.txt");
                InputStreamReader inputStreamReader=new InputStreamReader(fis);
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer=new StringBuffer();
                String lines;
                while ((lines=bufferedReader.readLine())!=null) {
                    stringBuffer.append(lines+ "\n");

                }
                textView.setText(stringBuffer.toString());


                try {
                    BufferedReader fin= new BufferedReader(new InputStreamReader(
                            openFileInput("meminterna.txt")));
                    String line= fin.readLine();  //o bucle, .append
                    //textView.setText(line);
                    fin.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("test", "Error: " + e.getMessage());

                }
                return true;

            }
            else if (id == R.id.readProgram) {
                //ex, descripcions de jugadors... dins el apk
                //no es pot escriure dins el apk en runtime, va signat.
                //data/data/my_apk/files/meminterna.txt
                // Device file explorer, abans android device monitor
                try {
                    InputStream fraw= getResources().openRawResource(R.raw.fraw);
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fraw));
                String line= null;
                    line = bufferedReader.readLine();
                textView.setText(line);
                fraw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            }

            else if (id == R.id.writeMemInt) {

                String myMess= editTextCity.getText().toString();
                FileOutputStream fout2=openFileOutput("memint2.txt",Context.MODE_PRIVATE);
                fout2.write("el que jo li passi per editText".getBytes());
                //fout2.write(myMess.getBytes());
                fout2.close();


                try {
                    OutputStreamWriter fout= new OutputStreamWriter(
                            openFileOutput("meminterna.txt",Context.MODE_PRIVATE));
                    fout.write("Contingut del fitxer de mem. interna");
                    fout.close();

                     } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("test", e.getMessage());

                }
                return true;


            } else if (id == R.id.exListView) {
                Intent intent=new Intent(this, ListViewRec.class);
                startActivity(intent);
                return true;
            }

            else if (id == R.id.readSD) {
                if (sdAvailable){
                    try {
                        File ruta_sd=Environment.getExternalStorageDirectory();
                        File f=new File(ruta_sd.getAbsolutePath(),"filesd.txt");
                        BufferedReader fin=new BufferedReader(new InputStreamReader(
                                new FileInputStream(f)));
                        String line=fin.readLine();
                        textView.setText(line);
                        fin.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                else Toast.makeText(this, "no és sdAvailable", Toast.LENGTH_SHORT).show();
            }

            else if (id == R.id.writeSD) {
                if (sdAvailable && sdWriteAccess) {
                    try {
                        File ruta_sd = Environment.getExternalStorageDirectory();
                        File f = new File(ruta_sd.getAbsolutePath(), "filesd.txt");
                        OutputStreamWriter fout = null;
                        try {
                            fout = new OutputStreamWriter(
                                    new FileOutputStream(f));
                            Log.d("testtotok", ruta_sd.getAbsolutePath());

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.d("testSDD", e.getMessage()+ e.getCause());

                        }
                        fout.write("Contingut del fitxer de la SD");
                        fout.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("testSD", e.getMessage()+ e.getCause());
                        //Toast.makeText(this, String.valueOf(sdAvailable)+"," + String.valueOf(sdWriteAccess), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }

            else if (id == R.id.openWeather) {
                //exemple HttpURLConnection
                //ha de ser crida AsyncTask desde versió 4 Android
                openWeather(false);
                return true;

            }else if (id == R.id.bbDD) {
                    Intent intent=new Intent(this,ExempleSQLLite.class);
                    startActivity(intent);
            }

                return super.onOptionsItemSelected(item);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("onOptionsItemSelected: ",e.getMessage());
        }
        return true;
    }
}