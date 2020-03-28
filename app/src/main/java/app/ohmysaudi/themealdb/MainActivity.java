package app.ohmysaudi.themealdb;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;


import app.ohmysaudi.themealdb.fragments.CategoryFragment;
import app.ohmysaudi.themealdb.fragments.MealDetailFragment;
import app.ohmysaudi.themealdb.fragments.RecipeFragment;
import app.ohmysaudi.themealdb.utils.DrawerLocker;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DrawerLocker {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ActionBar actionBar;
    private ActionBarDrawerToggle toggle;
    private Menu menu;
    public static SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        //Drawer Layout
        toggle = new ActionBarDrawerToggle(this, drawerLayout, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                displayFragment(menuItem.getItemId(), menuItem.getTitle().toString());
                return true;
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0){
                    toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Close the Drawer if Opened
                            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                                drawerLayout.closeDrawers();
                            } else {
                                // Pop previous Fragment from BackStack
                                getSupportFragmentManager().popBackStack();
                                if (!search.isShown()){
                                    search.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                } else {
                    setDrawerEnabled(true);
                }
            }
        });

        displayFragment(R.id.nav_category, "Categories");

    }

    private void displayFragment(int i, String title) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (i) {
            /*case R.id.nav_recipes:
                bundle.putInt("CASE_FRAGMENT", 0);
                fragment = new RecipeFragment();
                break;*/
            case R.id.nav_category:
                fragment = new CategoryFragment();
                break;
            case R.id.nav_random:
                fragment = new MealDetailFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();
                return;
            case R.id.nav_favorites:
                fragment = new RecipeFragment();
                bundle.putInt("CASE_FRAGMENT", 2); // Favorite Recipes
                break;
            case R.id.nav_setting:
                break;
            case R.id.nav_rate:
                break;
            case R.id.nav_about:
                break;
        }
        if (fragment != null) {
            fragment.setArguments(bundle);
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(title);
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.frame_content, fragment);
            beginTransaction.commitAllowingStateLoss();
        }
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawerLayout.setDrawerLockMode(lockMode);
        toggle.setDrawerIndicatorEnabled(enabled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        this.menu = menu;

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) menu.findItem(R.id.search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Fragment fragment = new RecipeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("KEY_SEARCH_TEXT", s);
                bundle.putInt("CASE_FRAGMENT", 3); // Search Recipes
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }


}
