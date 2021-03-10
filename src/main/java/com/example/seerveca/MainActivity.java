package com.example.seerveca;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.SearchAdapter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //ListView lvprovider;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque =new ArrayDeque<>(5);
    boolean flag = true;
    boolean back=true;




    Toolbar toolbar;

    ProviderAdapter adapter;
    String [] name = {"AC Repair","Automobile Mechanic","Plumbing","Electronic Repair"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_Nevigation);

        bottomNavigationView.setSelectedItemId(R.id.home);
        //lvprovider=findViewById(R.id.lvprovider);
        mLoginFormView=findViewById(R.id.login_form);
        mProgressView=findViewById(R.id.login_progressbar);
        tvLoad=findViewById(R.id.tvload);


       integerDeque.push(R.id.home);
       loadFragment(new HomeFrag());
       bottomNavigationView.setSelectedItemId(R.id.home);
       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {


               int id = item.getItemId();
               if(integerDeque.contains(id))
               {
                   if(id==R.id.home)
                   {

                       if(integerDeque.size()!=1)
                       {
                           if(flag)
                           {
                               integerDeque.addFirst(R.id.home);
                               flag=false;

                           }
                       }
                   }
                   integerDeque.remove(id);
               }
               integerDeque.push(id);
               loadFragment(getFragment(item.getItemId()));
               return true;

           }


       });









        /**/


    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,fragment,fragment.getClass().getSimpleName()).commit();
    }


    private Fragment getFragment(int itemId) {
        switch (itemId)
        {
            case R.id.home:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                back=!back;
                return new HomeFrag();
            case R.id.search:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                back=false;
                return new searchFrag();
            case R.id.message:
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                back=false;
                return new meassageFrag();
            case R.id.profile:
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                back=false;
                return new profileFrag();

        }
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        back=true;
        return new HomeFrag();
    }

    @Override
    public void onBackPressed() {

        try {
            integerDeque.pop();
            if(!integerDeque.isEmpty())
            {
                loadFragment(getFragment(integerDeque.peek()));
            }
            else
            {

                bottomNavigationView.getMenu().getItem(0).setChecked(true);


                if (back==true)
                {
                    Toast.makeText(MainActivity.this, "Press back again to exit Serveca app.", Toast.LENGTH_LONG).show();


                }






            }

        }
        catch (Exception e)
        {

            {
                MainActivity.this.finish();

            }



        }

    }

    /*@Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.my_menu,menu);
            MenuItem searchItem = menu.findItem(R.id.action);
             SearchView searchView = (SearchView)searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });

            return true;
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==1)
            {
                adapter.notifyDataSetChanged();
            }

        }*/
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}