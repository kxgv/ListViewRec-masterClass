package com.dam.eva.listviewrec;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class Parser extends AppCompatActivity {

    public List<Temp> parsejaJSon(String json) throws JSONException {

        String time = null;
        String temperature = null;
        String calorFred = null;
        Temp temp;
        String humid=null;

        List<Temp> llista = new ArrayList<Temp>();
        String direccion="";

        //https://www.jetbrains.com/help/idea/2017.3/set-up-a-git-repository.html#clone-repo
        //Creem un objecte JSONObject para poder acceder als atributs o camps
        //Creo un JSONObject a partir del StringBuilder passat

        JSONObject respuestaJSON = null;
        try {
            respuestaJSON = new JSONObject(json);

        //accedim al vector de resultats
            JSONArray resultJSON = respuestaJSON.getJSONArray("list");

            if (resultJSON.length() > 0) {
                Integer i = 0;

                while (i < resultJSON.length()) {
                    temperature = resultJSON.getJSONObject(i).getJSONObject("main").getString("temp");

                    time = resultJSON.getJSONObject(i).getString("dt_txt");
                    if (temperature != null) {
                        if (Double.parseDouble(temperature) >= 20) {
                            calorFred = "hot";
                        } else {
                            calorFred = "cold";
                        }
                    }

                    temp = new Temp(time, temperature, calorFred, humid);
                    Log.d("test", "bloc " + String.valueOf(i) + " " + time + " " + temperature + " " + humid + " " + calorFred);
                    llista.add(temp);
                    time = null;
                    temperature = null;
                    humid = null;
                    i += 1;

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("test", "parsejaJSon: " + e.getMessage());
        } catch (Exception e){
            Log.d("test", "exc: " + e.getMessage());

        }
        return llista;
    }

    public List<Temp> parsejaXml(String xml) throws XmlPullParserException, IOException, JSONException {

        String time = null;
        String temperature = null;
        String calorFred = null, humid=null;
        Temp temp;

        List<Temp> llista = new ArrayList<Temp>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        //https://www.google.com/maps/place/Institut+Escola+del+Treball/@41.3890464,2.1454964,17z/data=!3m1!4b1!4m5!3m4!1s0x12a4a2847eeed3b5:0xfcbfd60966182d80!8m2!3d41.3890464!4d2.1476851
        //http://maps.googleapis.com/maps/api/geocode/json?latlng=41.3890464,2.1454964


        xpp.setInput(new StringReader(xml));
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("time")) {
                    time = xpp.getAttributeValue(null, "from");
                }
                if (xpp.getName().equals("temperature")) {
                    temperature = xpp.getAttributeValue(null, "value");
                }
                if (xpp.getName().equals("humidity")) {
                    humid = xpp.getAttributeValue(null, "value");
                }

                if (temperature != null) {
                    if (Double.parseDouble(temperature) >= 20) {
                        calorFred = "hot";
                    } else {
                        calorFred = "cold";
                    }
                }

                if (time != null && temperature != null) {
                    try {
                        time = time.replace   ("T"," ");
                        temp = new Temp(time, temperature, calorFred,humid);
                        Log.d("test", "parsejant "+time+temperature+calorFred+humid);
                        llista.add(temp);
                        time=null;
                        temperature=null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("test", "parsejaXml: "+e.getMessage());
                    }
                }
            }

            eventType = xpp.next();

        }
        if (llista==null) Log.d("test", "llista nula");
        return llista;
    }

}
