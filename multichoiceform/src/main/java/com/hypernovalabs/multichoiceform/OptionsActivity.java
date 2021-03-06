package com.hypernovalabs.multichoiceform;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hypernovalabs.multichoiceform.config.MCFConfig;
import com.hypernovalabs.multichoiceform.config.MCFOptionsConfig;

import java.util.ArrayList;

/**
 * Activity containing a list of a MCFStep's options
 */
public class OptionsActivity extends AppCompatActivity {

    private static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;
    protected static final String EXTRA_CONFIG = EXTRA_PREFIX + ".Config";
    protected static final String EXTRA_SELECTION = EXTRA_PREFIX + ".Selection";

    private OptionsActivity mContext = this;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mOptions;
    private MCFOptionsConfig mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcf_activity_options);

        getIntent().setExtrasClassLoader(MCFOptionsConfig.class.getClassLoader());
        mModel = getIntent().getParcelableExtra(EXTRA_CONFIG);

        setupToolbar();
        setupListView();
    }

    /**
     * Defines and customize the Activity's Toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.mcf_toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.mcf_toolbar_title);

        if (tvTitle != null) {
            tvTitle.setText(mModel.title);
            if (mModel.toolbarTitleColor != 0)
                tvTitle.setTextColor(mModel.toolbarTitleColor);
        }

        if (mModel.toolbarBackgroundColor != 0)
            toolbar.setBackgroundColor(mModel.toolbarBackgroundColor);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Defines the {@link ListView} containing all of the data options.
     * Includes an {@link ListView.OnItemClickListener}, returns to home activity with the selection.
     */
    private void setupListView() {
        mListView = findViewById(R.id.mcf_list_view);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mListView.setEmptyView(findViewById(R.id.mcf_empty_view));
        setEmptyViewTexts(mModel.emptyViewTitle, mModel.emptyViewMsg);

        mOptions = new ArrayList<>(mModel.data);
        mAdapter = new ArrayAdapter<>(mContext, R.layout.mcf_simple_list_item_checked,
                mOptions);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                hideKeyboard();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_SELECTION, String.valueOf(mAdapter.getItem(i)));
                intent.putExtra(MCFConfig.EXTRA_TAG_KEY, mModel.tag);

                setResult(RESULT_OK, intent);
                finish();

                overrideTransition();
            }
        });

        checkSelection();
    }

    private void setEmptyViewTexts(String title, String msg) {
        ((TextView) findViewById(R.id.mcf_tv_empty_title)).setText(title);
        ((TextView) findViewById(R.id.mcf_tv_empty_msg)).setText(msg);
    }

    private void overrideTransition() {
        overridePendingTransition(R.anim.mcf_slide_in_left, R.anim.mcf_slide_out_right);
    }

    /**
     * If there is already a selection for the current {@link com.hypernovalabs.multichoiceform.form.MCFStep}, selects it
     */
    private void checkSelection() {
        if (mModel.selection != null && mModel.selection.length() > 0)
            mListView.setItemChecked(mAdapter.getPosition(mModel.selection), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mModel.isSearchable) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.mcf_menu_search, menu);

            final MenuItem searchItem = menu.findItem(R.id.search);

            Drawable iconDrawable = ContextCompat.getDrawable(mContext, R.drawable.mcf_ic_search);
            iconDrawable.setColorFilter(mModel.searchViewIconTint, PorterDuff.Mode.MULTIPLY);
            searchItem.setIcon(iconDrawable);

            final SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint(mModel.searchViewHint);
            searchView.setIconifiedByDefault(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    hideKeyboard();

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    updateQueriedData(newText);

                    return true;
                }
            });

        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Update the options based on the SearchView query
     */
    private void updateQueriedData(String query) {
        mOptions = new ArrayList<>();

        for (String option : mModel.data) {

            if (option.toLowerCase().contains(query.toLowerCase())) {
                mOptions.add(option);
            }
        }

        mAdapter.clear();
        mAdapter.addAll(mOptions);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overrideTransition();
    }

    /**
     * Tries to hide the Soft Input Keyboard. Should be used in a form activity on button click.
     */
    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
