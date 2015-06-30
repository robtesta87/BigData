package SocialBeer;

public class User {
	private String userName;
	private int numberReview;
	private int qualityReview;
	
	
	public User(String userName, int numberReview, int qualityReview) {
		super();
		this.userName = userName;
		this.numberReview = numberReview;
		this.qualityReview = qualityReview;
	}
	
	public User() {
		super();
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getNumberReview() {
		return numberReview;
	}
	public void setNumberReview(int numberReview) {
		this.numberReview = numberReview;
	}
	public int getQualityReview() {
		return qualityReview;
	}
	public void setQualityReview(int qualityReview) {
		this.qualityReview = qualityReview;
	}
}
