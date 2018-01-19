package rv.jorge.quizzz.screens.support;

import android.support.v7.widget.RecyclerView;

/**
 *
 * EndlessScrollListener will notify the class extending it when we reach the end of a
 * Recycler View.
 *
 * To use it, extend EndlessScrollListener and write the onScrolledToEnd listener.
 *
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!recyclerView.canScrollVertically(1)) {
            onScrolledToEnd();
        }
    }

    public abstract void onScrolledToEnd();
}
