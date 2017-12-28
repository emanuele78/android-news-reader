package com.example.android.newsappstage2project;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.newsappstage2project.data.Section;

import java.util.List;
/**
 * Created by Emanuele on 27/12/2017.
 */
public class SectionAdapter extends FragmentPagerAdapter {
    private final List<Section> sections;
    private final int SECTIONS_COUNT;
    private final int SEARCH_TAB;
    /**
     * Construct new section adapter
     *
     * @param fm       fragment manager reference
     * @param sections List of section objects
     */
    public SectionAdapter(FragmentManager fm, List<Section> sections) {
        super(fm);
        this.sections = sections;
        SECTIONS_COUNT = sections.size();
        //zero based - last tab position used by search tab
        SEARCH_TAB = SECTIONS_COUNT - 1;
    }
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        Section section = sections.get(position);
        //put loader id in bundle
        bundle.putInt("ID", section.getLoader_ID());
        if (section.getUrlParameter() != null) {
            bundle.putString("section", section.getUrlParameter());
        }
        //check if current fragment is search fragment
        if (position == SEARCH_TAB) {
            //last position used by search fragment
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.setArguments(bundle);
            return searchFragment;
        } else {
            //other section
            NewsFragment newsFragment = new NewsFragment();
            newsFragment.setArguments(bundle);
            return newsFragment;
        }
    }
    @Override
    public int getCount() {
        return SECTIONS_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return sections.get(position).getTabTitle();
    }
}
