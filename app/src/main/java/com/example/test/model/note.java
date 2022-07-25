package com.example.test.model;

import java.io.Serializable;

public class note implements Serializable {

    private final String id;
    private final String title;
    private final String content;
    private final String timeline;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTimeline() {
        return timeline;
    }


    public note(String id, String title, String content, String timeline) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timeline = timeline;
    }
}
