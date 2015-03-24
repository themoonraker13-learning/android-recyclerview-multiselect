package com.codesters.recyclerviewsample;

public class Item {
	private String title;
	private String subtitle;

	Item(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
	}

	public String getTitle() {
		return title;
	}

	public String getSubtitle() {
		return subtitle;
	}
}
