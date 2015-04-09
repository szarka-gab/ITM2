package com.example.mac.conve;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    //Declaration
    private EditText editText1;
    private Button button;
    private Spinner spinner_to;
    private TextView textView3;
    private TextView textView4;
    private String base_url = "http://api.fixer.io/latest";
    private AQuery aq;
    private Double resultat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText) findViewById(R.id.editText1); // Menge von E die verwechselt werden sollen
        spinner_to = (Spinner) findViewById(R.id.spinner_to);
        button = (Button) findViewById(R.id.button);  //Button for converting
        textView3 = (TextView) findViewById (R.id.textView3);  // result
        textView4 = (TextView) findViewById (R.id.textView4);  // starting value

        aq = new AQuery(this); //instance

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_to.setAdapter(adapter);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Operation area

                if(editText1.getText().toString().length()<1){
                    Toast.makeText(MainActivity.this, "you have to write a value",Toast.LENGTH_LONG).show(); //if the button is clicked without adding any value, comes an allert
                }else {

                    final Double currency_from = Double.valueOf(editText1.getText().toString()); //Variable with value of Euros which should be converted
                    final String to_currency = String.valueOf(spinner_to.getSelectedItem());  //choosen currency for converting
                    String url = base_url;  // url adress for geeting the JSONobjects

                    aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {   //using AQuery for getting the JSONObject

                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {


                            if (json != null) {
                                try {
                                    JSONObject rates = json.getJSONObject("rates"); // all currency courses are in JSON object saved
                                    Double sss = rates.getDouble(to_currency); // Variable with value of the choosen currency
                                    resultat = currency_from*sss;           //Result value  = given value of euros * choosen currency value
                                    textView4.setText(sss.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                textView4.setText(editText1.getText()+ " " +"EUR"); //given value in euros
                                textView3.setText(resultat.toString()+" " + spinner_to.getSelectedItem().toString()); //result in choosen currency

                            } else {

                                //ajax error, show error code
                                Toast.makeText(aq.getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
