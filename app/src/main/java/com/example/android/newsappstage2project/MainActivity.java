package com.example.android.newsappstage2project;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.newsappstage2project.data.DataUtils;
import com.example.android.newsappstage2project.data.Section;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate layout
        setContentView(R.layout.activity_main);
        //reading from preferences which sections to show, how many news per section
        //and how many news per row
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //save news per page and news per row in static variables
        DataUtils.pageSize = preferences.getString(getString(R.string.settings_news_per_page_key),
                getString(R.string.settings_news_per_page_default));
        DataUtils.rowSize = preferences.getString(getString(R.string.settings_news_per_row_key),
                getString(R.string.settings_news_per_row_default));
        //list that contains sections to show
        ArrayList<Section> sections = new ArrayList<>();
        //front page - always visible, doesn't require url parameter
        sections.add(new Section(
                null,
                getString(R.string.front_page_tab),
                DataUtils.LoaderID.FRONT_PAGE_LOADER_ID));
        //business
        if (preferences.getBoolean(getString(R.string.settings_section_business_key),
                DataUtils.SHOW_SECTION_DEFAULT)) {
            sections.add(new Section(
                    getString(R.string.business_url_parameter),
                    getString(R.string.business_tab_title),
                    DataUtils.LoaderID.BUSINESS_LOADER_ID));
        }
        //world
        if (preferences.getBoolean(getString(R.string.settings_section_world_key),
                DataUtils.SHOW_SECTION_DEFAULT)) {
            sections.add(new Section(
                    getString(R.string.world_url_parameter),
                    getString(R.string.world_tab_title),
                    DataUtils.LoaderID.WORLD_LOADER_ID));
        }
        //lifestyle
        if (preferences.getBoolean(getString(R.string.settings_section_lifestyle_key),
                DataUtils.SHOW_SECTION_DEFAULT)) {
            sections.add(new Section(
                    getString(R.string.lifestyle_url_parameter),
                    getString(R.string.lifestyle_tab_title),
                    DataUtils.LoaderID.LIFESTYLE_LOADER_ID));
        }
        //tech
        if (preferences.getBoolean(getString(R.string.settings_section_tech_key),
                DataUtils.SHOW_SECTION_DEFAULT)) {
            sections.add(new Section(
                    getString(R.string.tech_url_parameter),
                    getString(R.string.tech_tab_title),
                    DataUtils.LoaderID.TECH_LOADER_ID));
        }
        //culture
        if (preferences.getBoolean(getString(R.string.settings_section_culture_key),
                DataUtils.SHOW_SECTION_DEFAULT)) {
            sections.add(new Section(
                    getString(R.string.culture_url_parameter),
                    getString(R.string.culture_tab_title),
                    DataUtils.LoaderID.CULTURE_LOADER_ID));
        }
        //travel
        if (preferences.getBoolean(getString(R.string.settings_section_travel_key),
                DataUtils.SHOW_SECTION_DEFAULT)) {
            sections.add(new Section(
                    getString(R.string.travel_url_parameter),
                    getString(R.string.travel_tab_title),
                    DataUtils.LoaderID.TRAVEL_LOADER_ID));
        }
        //search tab - always visible, doesn't require url parameter
        sections.add(new Section(
                null,
                getString(R.string.search_tab),
                DataUtils.LoaderID.SEARCH_LOADER_ID));
        //get view pager e tab layout references
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        //create adapter and set it to the view pager
        SectionAdapter sectionAdapter = new SectionAdapter(getSupportFragmentManager(), sections);
        viewPager.setAdapter(sectionAdapter);
        //set view pager to keep in memory all the tabs in order to avoid continuous api queries when the fragments are destroyed
        int pageToRetain = sections.size() - 1;
        viewPager.setOffscreenPageLimit(pageToRetain);
        //let view pager populate adapter
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu for the activity
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings_menu) {
            //open settings activity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
