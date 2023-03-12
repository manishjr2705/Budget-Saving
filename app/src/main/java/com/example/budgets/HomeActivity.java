package com.example.budgets;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    //Fragment
    private dashFragment dashFragment;
    private incomeFragment incomeFragment;
    private expenseFragment expenseFragment;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("expense manager");
        setSupportActionBar(toolbar);

        mAuth=FirebaseAuth.getInstance();

        bottomNavigationView=findViewById(R.id.bottomNavigationbar);
        frameLayout=findViewById(R.id.main_frame);
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
          this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_open
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.naView);
        navigationView.setNavigationItemSelectedListener(this);

        dashFragment=new dashFragment();
        incomeFragment=new incomeFragment();
        expenseFragment=new expenseFragment();
        setFragment(dashFragment);

        bottomNavigationView.setOnItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.dash:
                        setFragment(dashFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.dash_colour);
                        return;

                    case R.id.income:
                        setFragment(incomeFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.income_colour);
                        return;

                    case R.id.expense:
                        setFragment(expenseFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.expense_colour);
                        return;

                    default:
                         return;
                }
            }
        });

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else{
            super.onBackPressed();
    }

    }

        public void displaySelectedListener(int itemId){
            Fragment fragment=null;

            switch (itemId){
                case R.id.dash:
                    fragment=new dashFragment();
                    break;

                case R.id.income:
                    fragment=new incomeFragment();
                    break;

                case R.id.expense:
                    fragment=new expenseFragment();
                    break;

                case R.id.Logout:
                    mAuth.signOut();
                    startActivities(new Intent[]{new Intent(getApplicationContext(), MainActivity.class)});
                    break;
            }

              if (fragment!=null){

                  FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                  fragmentTransaction.replace(R.id.main_frame,fragment);
                  fragmentTransaction.commit();
              }

              DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
              drawerLayout.closeDrawer(GravityCompat.START);

        }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener(item.getItemId());
        return true;
    }
}