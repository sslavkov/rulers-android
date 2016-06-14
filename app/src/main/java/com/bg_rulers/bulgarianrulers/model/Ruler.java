package com.bg_rulers.bulgarianrulers.model;

import java.util.Date;

/**
 * Created by sslavkov on 5/30/2016.
 */

public class Ruler {

	private String name;
    private Title title;
    private String extraTitle;
    private Date reignStart;
    private Date reignEnd;
    private String information;
    private Dynasty dynasty;

    public String getName() {
        return name;
    }
	
    public void setName(String name) {
        this.name = name;
    }
	
    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Date getReignStart() {
        return reignStart;
    }

    public void setReignStart(Date reignStart) {
        this.reignStart = reignStart;
    }

    public Date getReignEnd() {
        return reignEnd;
    }

    public void setReignEnd(Date reignEnd) {
        this.reignEnd = reignEnd;
    }

    public Dynasty getDynasty() {
        return dynasty;
    }

    public void setDynasty(Dynasty dynasty) {
        this.dynasty = dynasty;
    }

    public String getExtraTitle() {
        return extraTitle;
    }

    public void setExtraTitle(String extraTitle) {
        this.extraTitle = extraTitle;
    }
}
