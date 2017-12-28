package com.example.android.newsappstage2project.data;
/**
 * Created by Emanuele on 27/12/2017.
 */
public class Section {
    private final String urlParameter;
    private final String tabTitle;
    private final int loader_ID;
    /**
     * Construct a new section object
     *
     * @param urlParameter section url parameter
     * @param tabTitle     title of the tab
     * @param loader_ID    unique id of the loader associated to the section
     */
    public Section(String urlParameter, String tabTitle, @DataUtils.LoaderID int loader_ID) {
        this.urlParameter = urlParameter;
        this.tabTitle = tabTitle;
        this.loader_ID = loader_ID;
    }
    /**
     * Get the url section parameter
     *
     * @return the url section parameter
     */
    public String getUrlParameter() {
        return urlParameter;
    }
    /**
     * Get the section title
     *
     * @return the section title
     */
    public String getTabTitle() {
        return tabTitle;
    }
    /**
     * Get the loader ID for the section
     *
     * @return the loader ID for the section
     */
    public int getLoader_ID() {
        return loader_ID;
    }
}
