package in.weclub.srmweclubapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindPartner extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rView;
    private RecyclerView.LayoutManager rLM;
    private List<VendorInfo> vendorInfos = new ArrayList<>();
    private VendorAdapter va;
    private SwipeRefreshLayout srp;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_partner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rView= (RecyclerView)findViewById(R.id.rec);
        rLM = new LinearLayoutManager(this);
        rView.setLayoutManager(rLM);

        va = new VendorAdapter(this,vendorInfos);

        srp = (SwipeRefreshLayout)findViewById(R.id.swipeContainerPartner);

        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        final DatabaseReference ref = fd.getReference("Offers");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //vendorInfos.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            String n = ds.child("Vendor Name").getValue(String.class);
                            String l = ds.child("Vendor Location").getValue(String.class);
                            String o = ds.child("Offer").getValue(String.class);
                            String i = ds.child("Vendor Image").getValue(String.class);
                            vendorInfos.add(new VendorInfo(n,l,o,i, ds.getKey()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        va.notifyDataSetChanged();
        rView.setAdapter(va);
        getInfo();
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();

    }

    private void refresh(){
        vendorInfos.clear();
        srp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfo();
            }
        });
        //srp.setRefreshing(false);
    }

    private void getInfo(){
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference ref = fd.getReference("Offers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vendorInfos.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String n = ds.child("Vendor Name").getValue(String.class);
                    String l = ds.child("Vendor Location").getValue(String.class);
                    String o = ds.child("Offer").getValue(String.class);
                    String i = ds.child("Vendor Image").getValue(String.class);
                    vendorInfos.add(new VendorInfo(n,l,o,i, ds.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        va.notifyDataSetChanged();
        rView.setAdapter(va);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.find_partner, menu);
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
            AlertDialog.Builder a = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
            a.setTitle(Html.fromHtml("<font color='#000000'>Logout</font>"));
            a.setMessage(Html.fromHtml("<font color='#0000000'>Are you sure you want to Logout?</font>")).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    auth.signOut();
                    startActivity(new Intent(FindPartner.this, LoginActivity1.class));
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent it;

        switch(id)
        {
            case R.id.virtCard:
               it = new Intent(FindPartner.this, Profile.class);
                startActivity(it);
                //newIntent(Profile.class);
                break;
            case R.id.webLink2:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.weclub.in/"));
                startActivity(browserIntent);
                break;
            case R.id.eventsUp2:
                it = new Intent(FindPartner.this, UpcomingEvents.class);
                startActivity(it);
                //newIntent(UpcomingEvents.class);
                break;
            case R.id.edit2:
                 it = new Intent(FindPartner.this, EditProfile.class);
                startActivity(it);
                //newIntent(EditProfile.class);
                break;
            case R.id.enrolled1:
                it = new Intent(FindPartner.this, EnrolledEvents.class);
                startActivity(it);
               // newIntent(EnrolledEvents.class);
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   /* private void newIntent(Class c){
        final Class d = c;
        new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(FindPartner.this, d.getClass()));
            }
        };
    }*/
}
