package com.example.lab6_currencyapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Currency;

public class MainActivity extends AppCompatActivity {

    private EditText etEnterCurrency;
    private String baseCurrency;
    private ArrayList<CurrencyItem> currencyList;
    private CurrencyAdapter adapter;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvCurrencies=findViewById(R.id.lvCurrencies);
        etEnterCurrency= findViewById(R.id.etEnterCurrency);
        Button btnSearch=findViewById(R.id.btnSearch);
        currencyList=new ArrayList<>();

        adapter=new CurrencyAdapter(this,R.layout.list_item,currencyList);
        lvCurrencies.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the base for currency conversion
                baseCurrency=etEnterCurrency.getText().toString();

                runnable=new Runnable() {
                    @Override
                    public void run() {
                        getCurrencies();
                    }
                };

                //retrieve data on separate thread
                Thread thread=new Thread(null, runnable,"background");
                thread.start();

                //close the soft keyboard
                InputMethodManager inputManager= (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public void getCurrencies(){
        //Base URL, ready to attach the currency
        final String baseURL="https://api.exchangeratesapi.io/latest?base=";

        //Attach user input, or "USD if there is none.
        String urlWithBase=baseURL.concat(TextUtils.isEmpty(baseCurrency) ? "USD": baseCurrency);

        //Build the request
        JsonObjectRequest request=new JsonObjectRequest(
                Request.Method.GET,
                urlWithBase,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Sucess!", Toast.LENGTH_SHORT).show();
                        try {
                            response = response.getJSONObject("rates");
                            currencyList.clear();

                            //get and set data entry
                            for (int i = 0; i < response.names().length(); i++) {
                                String key = response.names().getString(i);
                                double value = Double.parseDouble(response.get(response.names().getString(i)).toString());
                                currencyList.add(new CurrencyItem(key, value));
                            }
                            //notify that source data has changed
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(
                        getApplicationContext(), "Error retrieving data", Toast.LENGTH_SHORT
                ).show();

            }
        }
        );
        //Add the request to queue
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
