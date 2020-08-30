package com.bindi.collegeplanner.itemClasses;

public class EssayItem {

    private String title;
    private String topic;
    private String wordCount;

    public EssayItem(String title, String topic, String wordCount) {
        this.title = title;
        this.topic = topic;
        this.wordCount = wordCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getWordCount() {
        return wordCount;
    }

    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }
}
