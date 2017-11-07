package com.example.mks.gadgethunter.Activities;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mks.gadgethunter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by Muntakim on 04-Oct-17.
 */

public class MainTabs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    DrawerLayout menu;
    MenuItem menuItem;
    NavigationView navigationView;
    Toolbar toolbar1;
    ActionBarDrawerToggle hamburgerIcon;
    ImageView profilePic;
    int shopOwnerFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);
        initialize();
    }

    private void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        menu = (DrawerLayout) findViewById(R.id.hamburgerMenu);
        hamburgerIcon = new ActionBarDrawerToggle(this, menu, R.string.open, R.string.close);
        menu.addDrawerListener(hamburgerIcon);
        hamburgerIcon.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        menuItem = (MenuItem) findViewById(R.id.messages);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.txtName);
        TextView email = (TextView) header.findViewById(R.id.txtEmail);
        profilePic = (ImageView) header.findViewById(R.id.imageView);
        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        loadProfilePic();
        checkIfOwner();
    }

    private void checkIfOwner() {
        FirebaseDatabase.getInstance().getReference().child("ShopName").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String shopOwner = dataSnapshot.getKey();
                if (shopOwner.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    shopOwnerFlag = 1;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProfilePic() {
        //loads Google Account Profile Picture in Navigation Menu

        Picasso.with(getApplicationContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .resize(150, 150)
                .into(profilePic, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        profilePic.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        //Error placeholder image already loaded into the view, do further handling of this situation here
                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (hamburgerIcon.onOptionsItemSelected(item)) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainTabs.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.messages) {
            Intent intent = new Intent(MainTabs.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.messages) {
            intent = new Intent(MainTabs.this, MainTabs.class);
            startActivity(intent);
        } else if (id == R.id.settings) {
            intent = new Intent(MainTabs.this, Settings.class);
            startActivity(intent);
        } else if (id == R.id.threads) {
            intent = new Intent(MainTabs.this, Threads.class);
            startActivity(intent);
        } else if (id == R.id.add_new_product) {
            if (shopOwnerFlag == 0) {
                intent = new Intent(MainTabs.this, PromptShop.class);
                startActivity(intent);
            } else {
                intent = new Intent(MainTabs.this, AddProductActivity.class);
                startActivity(intent);
            }


        }
        else if (id==R.id.notifications){
            intent = new Intent(MainTabs.this, NotificationActivity.class);
            startActivity(intent);
        }
            else if (id == R.id.view_shop) {
            if (shopOwnerFlag == 0) {
                intent = new Intent(MainTabs.this, PromptShop.class);
                startActivity(intent);
            } else {
                intent = new Intent(MainTabs.this, ShopOwnersTabs.class);
                intent.putExtra("ShopOwner","true");
                intent.putExtra("shopId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
            }


        }
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a Fragment depending on the Placeholder.
            switch (position) {
                case 0:
                    Featured tab1 = new Featured();
                    return tab1;

                case 1:

                    Forum tab2 = new Forum();
                    return tab2;
                case 2:

                    Shop tab3 = new Shop();
                    return tab3;

                default:
                    return new Featured();
            }
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //Returns the header of the fragments
            switch (position) {
                case 0:
                    return "Featured";
                case 1:
                    return "Forum";
                case 2:
                    return "Shop";
            }
            return null;
        }
    }
}
