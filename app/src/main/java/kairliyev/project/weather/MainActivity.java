package kairliyev.project.weather;

import android.app.SearchManager;
import android.app.VoiceInteractor;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CityAdapter.CityAdapterListener {

    private Context context;
    //private SearchView searchView;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private CityAdapter cityAdapter;
    private List<City> cityList;
    private String name;

    private static final String URL = "http://api.openweathermap.org/data/2.5/find?lat=43.2&lon=76.87&cnt=10&appid=8942a6147f5d6552b46d19b424440032";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);


        recyclerView = findViewById(R.id.rView_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cities");

        context = getApplicationContext();

        cityList = new ArrayList<>();

        cityAdapter = new CityAdapter(this, cityList, (CityAdapter.CityAdapterListener) this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(cityAdapter);

        getContacts();

    }

    private void getContacts(){

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET, URL, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray array = response.getJSONArray("list");

                            for(int i = 0 ; i < array.length(); i++ ) {

                                JSONObject jsonObject = array.getJSONObject(i);

                                name = jsonObject.getString("name");

                                cityList.add(new City(name));

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                            Toast.makeText(getApplicationContext(), "Exception here" , Toast.LENGTH_SHORT).show();

                        }




                        cityAdapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        requestQueue.add(request);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                cityAdapter.getFilter().filter(query);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                cityAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCitySelected(City city) {
        Toast.makeText(context, "Name of City" + city, Toast.LENGTH_SHORT).show();
    }
}
