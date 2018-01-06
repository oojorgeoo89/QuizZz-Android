package rv.jorge.quizzz.screens;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.screens.support.FragmentUmbrella;
import rv.jorge.quizzz.service.UserService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , SharedPreferences.OnSharedPreferenceChangeListener
        , FragmentUmbrella {

    private FragmentManager fragmentManager;
    private NavigationView navigationView;

    SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userService = QuizApplication.get(this).getUserService();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        // Setting up the toolbar
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_title);
        setSupportActionBar(toolbar);

        // Setting up the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Setting up the drawer's menu
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateNavigationDrawer(userService.isLoggedIn());

        // Start up the initial fragment
        fragmentManager = getFragmentManager();
        if (fragmentManager.findFragmentById(R.id.main_fragment) == null) {
            swapFragment(new HomeFragment());
        }

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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            swapFragment(new HomeFragment());
        } else if (id == R.id.nav_add_quiz) {
            swapFragment(new EditQuiz());
        } else if (id == R.id.nav_my_quizzes) {
            swapFragment(new MyQuizzesFragment());
        } else if (id == R.id.nav_login) {
            swapFragment(new LoginFragment());
        } else if (id == R.id.nav_logout) {
            userService.logout();
            swapFragment(new HomeFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void swapFragment(Fragment fragment) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.prefs_is_logged_in))) {
            boolean isLoggedIn = sharedPreferences.getBoolean(getString(R.string.prefs_is_logged_in), false);
            updateNavigationDrawer(isLoggedIn);
            if (isLoggedIn) {
                swapFragment(new HomeFragment());
            }
        }
    }

    private void updateNavigationDrawer(boolean isLoggedIn) {
        if (isLoggedIn) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_logged_in);
            ((TextView) navigationView.getHeaderView(0)
                    .findViewById(R.id.navigation_drawer_username))
                    .setText(prefs.getString(getString(R.string.prefs_username),
                            getString(R.string.navigation_drawer_default_username)));
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_not_logged_in);
            ((TextView) navigationView.getHeaderView(0)
                    .findViewById(R.id.navigation_drawer_username))
                    .setText(getString(R.string.navigation_drawer_default_username));
        }
    }

    @Override
    public void addFragmentToStack(Fragment newFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
