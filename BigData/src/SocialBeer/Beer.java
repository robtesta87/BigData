package SocialBeer;

public class Beer {
	
	private String beerName;
	private String beerId;
	private String brewerId;
	private String ABV;
	private String style;
	
	

	public Beer(String beerName, String beerId, String brewerId, String aBV, String style) {
		super();
		this.beerName = beerName;
		this.beerId = beerId;
		this.brewerId = brewerId;
		ABV = aBV;
		this.style = style;
	}
	
	public Beer() {
		super();
	}
	
	public String getBeerName() {
		return beerName;
	}
	public void setBeerName(String beerName) {
		this.beerName = beerName;
	}
	public String getBeerId() {
		return beerId;
	}
	public void setBeerId(String beerId) {
		this.beerId = beerId;
	}
	public String getBrewerId() {
		return brewerId;
	}
	public void setBrewerId(String brewerId) {
		this.brewerId = brewerId;
	}
	public String getABV() {
		return ABV;
	}
	public void setABV(String aBV) {
		ABV = aBV;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
}
