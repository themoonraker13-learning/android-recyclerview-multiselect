package com.codesters.recyclerviewsample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements Adapter.ViewHolder.ClickListener {

    Toolbar toolbar;
    Toast toast;
    Boolean LIST_VIEW_MODE = false, GRID_VIEW_MODE = false;
    private Adapter adapter;
    private RecyclerView recyclerView;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new Adapter(this);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LIST_VIEW_MODE = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            finish();
        }

        if (id == R.id.action_view) {
            if (LIST_VIEW_MODE) {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                LIST_VIEW_MODE = false;
                GRID_VIEW_MODE = true;
            } else if (GRID_VIEW_MODE) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                GRID_VIEW_MODE = false;
                LIST_VIEW_MODE = true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem view = menu.findItem(R.id.action_view);

        if (GRID_VIEW_MODE) {
            view.setTitle("List View");
        }
        if (LIST_VIEW_MODE) {
            view.setTitle("Grid View");
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        } else {
            Item item = Adapter.getItem(position);
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(this, item.getTitle() + " was clicked", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
            getSupportActionBar().hide();
        }
        toggleSelection(position);

        return true;
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_cab, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    adapter.removeItems(adapter.getSelectedItems());
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;
            getSupportActionBar().show();
        }
    }
}
