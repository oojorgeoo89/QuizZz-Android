package rv.jorge.quizzz.screens;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;

import rv.jorge.quizzz.QuizApplication;
import rv.jorge.quizzz.R;
import rv.jorge.quizzz.model.User;
import rv.jorge.quizzz.screens.editquiz.EditQuizFragment;
import rv.jorge.quizzz.screens.login.LoginFragment;
import rv.jorge.quizzz.screens.quizlists.home.HomeFragment;
import rv.jorge.quizzz.screens.quizlists.myquizzes.MyQuizzesFragment;
import rv.jorge.quizzz.screens.support.FragmentUmbrella;

/**
 *
 * MainActivity hosts the Application Drawer and orchestrates the Fragment changes
 * when the user selects an item from the Drawer.
 *
 * MainActivity is responsible for updating the Drawer when the user logs in/out.
 *
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , FragmentUmbrella {

    private static final String HOME_FRAGMENT_TAG = "HOME_FRAGMENT_TAG";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    MainActivityViewModel mainViewModel;
    LogoutViewModel logoutViewModel;

    private FragmentManager fragmentManager;
    private NavigationView navigationView;

    private boolean shouldBackGoToHome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Injecting dependencies in Fragment
        ((QuizApplication) getApplication())
                .getComponent()
                .inject(this);

        // Binding View Models
        logoutViewModel = ViewModelProviders.of(this, viewModelFactory).get(LogoutViewModel.class);
        mainViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mainViewModel.getIsUserLoggedInObservable().observe(this, user -> {
            updateNavigationDrawer(user);
            if (user != null && shouldLoginChangeFragment()) {
                navigateBackToHome();
            }
        });

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
        mainViewModel.checkIsUserLoggedIn();

        // Start up the initial fragment
        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.main_fragment) == null) {
            navigateBackToHome();
        }

    }

    private boolean shouldLoginChangeFragment() {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.main_fragment);

        if (currentFragment.getClass().equals(HomeFragment.class)) {
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (shouldBackGoToHome) {
                shouldBackGoToHome = false;
                navigateBackToHome();
            } else {
                if (fragmentManager.getBackStackEntryCount() == 1)
                    finish();
                else
                    super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            swapFragment(new HomeFragment());
        } else if (id == R.id.nav_add_quiz) {
            swapFragment(new EditQuizFragment());
        } else if (id == R.id.nav_my_quizzes) {
            swapFragment(new MyQuizzesFragment());
        } else if (id == R.id.nav_login) {
            swapFragment(new LoginFragment());
        } else if (id == R.id.nav_logout) {
            logoutViewModel.logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void swapFragment(Fragment fragment) {
        clearBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.commit();
    }

    private void updateNavigationDrawer(User user) {
        if (user != null) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_logged_in);
            ((TextView) navigationView.getHeaderView(0)
                    .findViewById(R.id.navigation_drawer_username))
                    .setText(user.getUsername());
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
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment, newFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateBackToHomeOnNextBackKeyPress() {
        shouldBackGoToHome = true;
    }

    @Override
    public void navigateBackToHome() {
        if (!fragmentManager.popBackStackImmediate(HOME_FRAGMENT_TAG, 0)) {

            clearBackStack();

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_fragment, new HomeFragment())
                    .addToBackStack(HOME_FRAGMENT_TAG)
                    .commit();
        }
    }

    private void clearBackStack() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


}
