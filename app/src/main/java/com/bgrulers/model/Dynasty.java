package com.bgrulers.model;

import java.util.Collection;

/**
 * Created by sslavkov on 5/30/2016.
 */

public class Dynasty extends JpaEntityModel {
	
	private String name;
	private String description;

    private Collection<Ruler> rulers;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Ruler> getRulers() {
        return rulers;
    }

    public void setRulers(Collection<Ruler> rulers) {
        this.rulers = rulers;
    }
}
