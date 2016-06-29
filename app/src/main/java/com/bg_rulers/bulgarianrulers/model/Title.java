package com.bg_rulers.bulgarianrulers.model;

import com.bg_rulers.bulgarianrulers.enums.TitleType;

/**
 * Created by sslavkov on 5/30/2016.
 */

public class Title extends JpaEntityModel {

	private TitleType titleType;
	private String description;
	

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TitleType getTitleType() {
        return titleType;
    }

    public void setTitleType(TitleType titleType) {
        this.titleType = titleType;
    }
}
