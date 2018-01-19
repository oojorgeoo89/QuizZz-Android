package rv.jorge.quizzz.screens.support;

import android.support.v4.app.Fragment;

/**
 *
 * This interface indicates than an Activity or Fragment has the ability to add Fragments
 * to the Call Stack.
 *
 */

public interface FragmentUmbrella {
    void addFragmentToStack(Fragment newFragment);
}
