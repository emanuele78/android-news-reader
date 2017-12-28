package com.example.android.newsappstage2project.data;
import android.graphics.Bitmap;

import java.util.Date;
/**
 * Created by Emanuele on 18/12/2017.
 * This class represents a news object
 */
public class News {
    private final String title;
    private final String section;
    private final String author;
    private final Date date;
    private final Bitmap image;
    private final String webUrl;
    /**
     * Construct a news object
     *
     * @param title the title of the news
     * @param section the section of the news
     * @param author the author of the news
     * @param date the publish date of the news
     * @param image the cover image of the news
     * @param webUrl the web url of the news
     */
    public News(String title, String section, String author, Date date, Bitmap image, String webUrl) {
        this.title = title;
        this.section = section;
        this.author = author;
        this.date = date;
        this.image = image;
        this.webUrl = webUrl;
    }
    /**
     * Returns the title of the news
     *
     * @return a string object that contains the title of the news
     */
    public String getTitle() {
        return title;
    }
    /**
     * Returns the section of the news
     *
     * @return a string object that contains the section of the news
     */
    public String getSection() {
        return section;
    }
    /**
     * Returns the author of the news
     *
     * @return a string object that contains the author of the news
     */
    public String getAuthor() {
        return author;
    }
    /**
     * Returns the publish date of the news
     *
     * @return a date object that represents the publish date of the news
     */
    public Date getDate() {
        return date;
    }
    /**
     * Returns the cover image of the news
     *
     * @return a bitmap that represents the cover image of the news
     */
    public Bitmap getImage() {
        return image;
    }
    /**
     * Returns the web url of the news
     *
     * @return a string object that contains the web url of the news
     */
    public String getWebUrl() {
        return webUrl;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        News news = (News) o;
        if (!title.equals(news.title))
            return false;
        if (!section.equals(news.section))
            return false;
        if (author != null ? !author.equals(news.author) : news.author != null)
            return false;
        if (date != null ? !date.equals(news.date) : news.date != null)
            return false;
        if (image != null ? !image.equals(news.image) : news.image != null)
            return false;
        return webUrl != null ? webUrl.equals(news.webUrl) : news.webUrl == null;
    }
    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + section.hashCode();
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (webUrl != null ? webUrl.hashCode() : 0);
        return result;
    }
}
