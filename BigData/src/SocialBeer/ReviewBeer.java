package SocialBeer;

public class ReviewBeer {

	private double appearance;
	private double aroma;
	private double palate;
	private double taste;
	private double overall;
	private String time;
	private String text;
	private int lengthText;
	
	public ReviewBeer(double appearance, double aroma, double palate,
			double taste, double overall, String time, String text) {
		super();
		this.appearance = appearance;
		this.aroma = aroma;
		this.palate = palate;
		this.taste = taste;
		this.overall = overall;
		this.time = time;
		this.text = text;
	}

	public ReviewBeer() {
		super();
	}

	public double getAppearance() {
		return appearance;
	}

	public void setAppearance(double appearance) {
		this.appearance = appearance;
	}

	public double getAroma() {
		return aroma;
	}

	public void setAroma(double aroma) {
		this.aroma = aroma;
	}

	public double getPalate() {
		return palate;
	}

	public void setPalate(double palate) {
		this.palate = palate;
	}

	public double getTaste() {
		return taste;
	}

	public void setTaste(double taste) {
		this.taste = taste;
	}

	public double getOverall() {
		return overall;
	}

	public void setOverall(double overall) {
		this.overall = overall;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLengthText() {
		return lengthText;
	}

	public void setLengthText(int lengthText) {
		this.lengthText = lengthText;
	}
	
	
}
