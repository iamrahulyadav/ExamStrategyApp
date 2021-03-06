package com.talentsprint.android.esa.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anudeep Reddy on 7/10/2017.
 */

public class CurrentAffairsListObject implements Serializable {
    private String topicName;
    private List<CurrentAffairArticles> currentAffairArticles;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<CurrentAffairArticles> getCurrentAffairArticles() {
        return currentAffairArticles;
    }

    public void setCurrentAffairArticles(List<CurrentAffairArticles> currentAffairArticles) {
        this.currentAffairArticles = currentAffairArticles;
    }

    public class CurrentAffairArticles implements Serializable {
        public String date;
        public ArrayList<CurrentAffairsObject> articles;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ArrayList<CurrentAffairsObject> getArticles() {
            return articles;
        }

        public void setArticles(ArrayList<CurrentAffairsObject> articles) {
            this.articles = articles;
        }
    }
}
