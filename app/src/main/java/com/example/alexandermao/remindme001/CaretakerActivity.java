package com.example.alexandermao.remindme001;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

/* This activity is the page caretakers see after they login

   1. Caretaker profile button, allows caretaker to edit own information, see CaretakerProfileActivity
   2. Logout button, allows switching of users, goes back to LoginActivity
   3. Tab Layout for Caretaker's tasks, and patients
   4. Task tab has CaretakerTasksActivity
   5. Patients tab has CaretakerPatientsActivity

*/
public class CaretakerActivity extends AppCompatActivity{

    private Caretaker caretaker;
    private HashMap<String, Patient> patients;
    private HashMap<String, Caretaker> caretakers;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private GlobalVars v;
    private Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker);
        v = GlobalVars.getSingleInstance();
        this.patients = v.getPatients();
        this.caretakers = v.getCaretakers();

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new CaretakerTasksActivity(), "Tasks");
        adapter.AddFragment(new CaretakerPatientsActivity(), "Patients");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();

            }
        });

    }
    public void logout() {
        NavUtils.navigateUpFromSameTask(this);
    }


}
