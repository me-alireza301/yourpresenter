package com.google.code.yourpresenter.view;

public enum Role {

	// to enable URL refresh '?faces-redirect=true' is used,
	// see: http://www.coderanch.com/t/522508/JSF/java/commandButton-URL-no-refresh
	PRESENTER("Presenter", "presenter/presenter.jsf?faces-redirect=true"), PROJECTOR(
			"Projector", "projector/projector.jsf?faces-redirect=true"), SPEAKER(
			"Speaker", "TODO"), MUSICIAN("Musician", "TODO"), ADMIN("Admin",
			"admin/admin.jsf?faces-redirect=true");

	private String txt;
	private String url;

	private Role(String txt, String url) {
		this.setTxt(txt);
		this.setUrl(url);
	}

	/**
	 * @return the txt
	 */
	public String getTxt() {
		return txt;
	}

	/**
	 * @param txt
	 *            the txt to set
	 */
	public void setTxt(String txt) {
		this.txt = txt;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
