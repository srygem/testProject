package com.sachin.testproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private RecyclerView mRecycleView;
    private List<infoClass> mItems;
    private infoAdap mAdapter;
    private boolean isLive;
    private infoClass current;
    private Double d1,d2;
    private GoogleMap mMap;
    boolean doubleBackToExitPressedOnce = false;
    private SupportMapFragment mapFragment;
    String j="420";@Override
    public
    void onBackPressed() {
        if(isLive){
            mapFragment.getView ().setVisibility (View.GONE);
            mRecycleView.setVisibility (View.VISIBLE);
            isLive=false;
        }
        else {
            mRecycleView.setVisibility (View.VISIBLE);
            mapFragment.getView ().setVisibility (View.GONE);

        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);




    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        mRecycleView=findViewById(R.id.rv);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setHasFixedSize(true);
        mItems=new ArrayList<> ();
        hitApi ();
        mapFragment = (SupportMapFragment) getSupportFragmentManager ()
                .findFragmentById (R.id.map);
        mapFragment.getView ().setVisibility (View.GONE);
        mapFragment.getMapAsync (this);
        isLive=false;

    }
    Runnable r2=new Runnable() {
        @Override
        public void run() {
            if(isLive){
                hitApi ();
                mMap.clear ();
               Bitmap icon;
                double latitude =Double.parseDouble(mItems.get (Integer.parseInt(j)).getLatitude ());
                d2=latitude;
                if(d1.equals (d2)){
                     icon = BitmapFactory.decodeResource(getResources(),
                            R.drawable.carerd);
                }
                else {
                    icon = BitmapFactory.decodeResource(getResources(),
                            R.drawable.cargreen);
                }
                double longitude =Double.parseDouble(mItems.get (Integer.parseInt(j)).getLongitude ());
                MarkerOptions marker = new MarkerOptions().position(new LatLng (latitude, longitude))
                        .icon (BitmapDescriptorFactory.fromBitmap (icon))
                        .title ("wom");
                int zoom = (int)mMap.getCameraPosition().zoom;
                zoom=20;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom), 100, null);
                mMap.addMarker (marker);
                d1=d2;


              Toast.makeText (getApplicationContext (),"updating live"+String.valueOf (latitude),Toast.LENGTH_LONG).show ();
            }
            else{
            hitApi ();
            Toast.makeText (getApplicationContext (),"updating",Toast.LENGTH_LONG).show ();}
            h2.postDelayed(r2,10000);
        }
    };

    Handler h2=new Handler ();

    @Override
    protected
    void onResume() {
        super.onResume ();
        h2.postDelayed(r2,10000);
    }

    @Override
    protected
    void onStop() {
        super.onStop ();
        h2.removeCallbacks(r2);
    }

    public void hitApi(){
        String str="http://api.vamosys.com/mobile/getGrpDataForTrustedClients?providerName=PANKAJ&fcode=wom";
        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest (Request.Method.GET
                , str, null, new Response.Listener<JSONArray> () {
            @Override
            public
            void onResponse(JSONArray response) {
                mItems.clear ();
                for(int i=0;i<response.length ();i++){
                    try {
                        infoClass infoClassCurrent=new infoClass ();
                        infoClassCurrent.setRegNo (response.getJSONObject (i).getString ("regNo"));
                        infoClassCurrent.setAddress (response.getJSONObject (i).getString ("address"));
                        infoClassCurrent.setDistance (response.getJSONObject (i).getString ("distanceCovered"));
                        infoClassCurrent.setLastSeen (response.getJSONObject (i).getString ("lastSeen"));
                        infoClassCurrent.setLatitude (response.getJSONObject (i).getString ("latitude"));
                        infoClassCurrent.setLongitude (response.getJSONObject (i).getString ("longitude"));
                        infoClassCurrent.setId (response.getJSONObject (i).getString ("rowId"));
                        mItems.add (infoClassCurrent);
                    } catch (JSONException e) {
                        e.printStackTrace ();
                        Log.d ("ERROR>>>>>>", e.toString ());
                    }
                }
                mAdapter=new infoAdap (getApplicationContext (), mItems);
                mRecycleView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener () {
            @Override
            public
            void onErrorResponse(VolleyError error) {
                Log.d ("ERROR>>>>>>>", error.toString ());
                Toast.makeText (getApplicationContext (),error.getMessage ()+"5",Toast.LENGTH_LONG).show ();

            }

        }
        ){

            @Override
            protected
            Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
 //               headers.put("providerName", "");
                return headers;
            }

        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy (
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*48,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonArrayRequest.setShouldCache (false);
       // NetworkController.getInstance().addToRequestQueue(jsonReq);
        Volley.newRequestQueue(MainActivity.this).add (jsonArrayRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(j.equals ("420")){

        }
            else{

        Toast.makeText (this, "map", Toast.LENGTH_SHORT).show ();

        double latitude =Double.parseDouble(mItems.get (Integer.parseInt(j)).getLatitude ());
        double longitude =Double.parseDouble(mItems.get (Integer.parseInt(j)).getLongitude ());

            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.carerd);
        MarkerOptions marker = new MarkerOptions().position(new LatLng (latitude, longitude))
                .icon (BitmapDescriptorFactory.fromBitmap (icon))
                .title ("wom");
        int zoom = (int)mMap.getCameraPosition().zoom;
        zoom=20;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom), 100, null);
        mMap.addMarker (marker);

    }}

    public class infoAdap extends RecyclerView.Adapter<infoAdap.ImageViewHolder> {
        private Context mContext;
        private List<infoClass> mItemsAdapter;

        public infoAdap(Context context, List<infoClass> infoClasseItem) {
            mContext =context;
            mItemsAdapter = infoClasseItem;
        }
        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(mContext).inflate(R.layout.item_card,parent,false);
            return new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull infoAdap.ImageViewHolder holder, int position) {
            final infoClass itemCurrent = mItemsAdapter.get(position);
            holder.t1.setText (itemCurrent.getRegNo ());
            holder.t2.setText (itemCurrent.getDistance ());
            holder.t3.setText (itemCurrent.getAddress ());
            holder.t4.setText (itemCurrent.getLastSeen ());
            holder.t5.setText (itemCurrent.getId ());

        }

        @Override
        public int getItemCount() {
            return mItemsAdapter.size ();
        }
        public class ImageViewHolder extends RecyclerView.ViewHolder{
            TextView t1,t2,t3,t4,t5;
            CardView c1;

            private ImageViewHolder(View itemView) {
                super(itemView);
                c1=itemView.findViewById (R.id.c1);
                t1=itemView.findViewById (R.id.regno);
                t2=itemView.findViewById (R.id.distance);
                t3=itemView.findViewById (R.id.address);
                t4=itemView.findViewById (R.id.last);
                t5=itemView.findViewById (R.id.vehicleid);

                c1.setOnClickListener (new View.OnClickListener () {
                @Override
                public
                void onClick(View view) {
                    j=t5.getText ().toString ();
                    current=mItems.get (Integer.parseInt (j));
                    final String s="https://www.google.co.in/maps/search/"+current.getLatitude ()+","+current.getLongitude ();
                    final PopupMenu popup = new PopupMenu (MainActivity.this, c1);
                    popup.getMenuInflater()
                            .inflate(R.menu.track_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener () {
                        @Override
                        public
                        boolean onMenuItemClick(MenuItem menuItem) {
                            if(menuItem.getItemId ()==R.id.track){
                               mMap.clear ();
                                Toast.makeText (getApplicationContext (),"track",Toast.LENGTH_LONG).show ();
                                isLive=true;
                                mRecycleView.setVisibility (View.GONE);
                                mapFragment.getView ().setVisibility (View.VISIBLE);
                                Toast.makeText (MainActivity.this, "map", Toast.LENGTH_SHORT).show ();

                                double latitude =Double.parseDouble(mItems.get (Integer.parseInt(j)).getLatitude ());
                                double longitude =Double.parseDouble(mItems.get (Integer.parseInt(j)).getLongitude ());
                                d1=latitude;
                                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.carblack);
                                MarkerOptions marker = new MarkerOptions().position(new LatLng (latitude, longitude))
                                        .icon (BitmapDescriptorFactory.fromBitmap (icon))
                                        .title ("wom");
//                                int zoom = (int)mMap.getCameraPosition().zoom;
                               int zoom=50;
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom), 1000, null);
                                mMap.addMarker (marker);

                               // w2.setVisibility (View.VISIBLE);
                               // w2.loadUrl (s);

                                /*Intent intent=new Intent (MainActivity.this,trackActivity.class);
                                intent.putExtra("arg",s);
                                intent.putExtra("id",search.getId ());
                                startActivity (intent);*/
                            }
                            else{
                                isLive=false;
                                mRecycleView.setVisibility (View.GONE);
                                mapFragment.getView ().setVisibility (View.VISIBLE);
                                Toast.makeText (MainActivity.this, "map", Toast.LENGTH_SHORT).show ();

                                double latitude =Double.parseDouble(mItems.get (Integer.parseInt(j)).getLatitude ());
                                double longitude =Double.parseDouble(mItems.get (Integer.parseInt(j)).getLongitude ());


                                MarkerOptions marker = new MarkerOptions().position(new LatLng (latitude, longitude))
                                        .icon (BitmapDescriptorFactory.defaultMarker ())
                                        .title ("wom");
//                                int zoom = (int)mMap.getCameraPosition().zoom;
                                int zoom=50;
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom), 1000, null);
                                mMap.addMarker (marker);
                                //w2.setVisibility (View.VISIBLE);
                                //w2.loadUrl (s);
                            }

                            return true;
                        }
                    });
                    popup.show ();
                }
            });


        }
        }

    }


}
