package com.example.revalocator;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
 DrawerLayout drawerLayout;
 NavigationView navigationView;
 Toolbar toolbar;
 LocationStorage SavedLocation=new LocationStorage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //storing locations lat & long
        SavedLocation.addLocation("Library",13.114605,77.635293);
        SavedLocation.addLocation("Rangasthala",13.114673,77.634885);
        SavedLocation.addLocation("Admin Block",13.113907,77.634604);
        SavedLocation.addLocation("Applied Science",13.113950,77.635636);
        SavedLocation.addLocation("Saugandhika",13.115670,77.636016);
        SavedLocation.addLocation("Ground",13.116570,77.636176);
        SavedLocation.addLocation("Food Court",13.115664,77.635998);
        SavedLocation.addLocation("Coffee",13.114886,77.635889);
        SavedLocation.addLocation("Maggie Point",13.116095,77.634932);
        SavedLocation.addLocation("Nandhini",13.116201,77.635376);


        drawerLayout = findViewById(R.id.drawable);
        navigationView =findViewById(R.id.navigationn);
        toolbar  = findViewById(R.id.tool);
        //step 1 set up the toolbar
        setSupportActionBar(toolbar);// to set the toolbar

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer,R.string.ClosedDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Load MapsFragment initially
       fragmentload(new MapsFragment(),0);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                int id = item.getItemId();
                if(id == R.id.contact)
                {
                    fragmentload(new Timeline(),1);
                } else if (id == R.id.home) {
                    fragmentload(new My_profile(),1);
                } else if (id == R.id.bluehtoot) {
                    fragmentload(new MapsFragment(),1);
                }
                else {
                    fragmentload(new MapsFragment(),0);
                }
                //on click of any button close the drawer

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    }


    @Override
    public void onBackPressed() {
        //if drawer is opne then closed the drawer first on backpressed else close the activity

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void fragmentload(Fragment fragment, int flag)
    {
        FragmentManager fragmentManager   = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(flag == 0)
        {
            fragmentTransaction.add(R.id.container,fragment);

        }
        else
        {
            fragmentTransaction.replace(R.id.container,fragment);
        }
        fragmentTransaction.commit();

    }
}