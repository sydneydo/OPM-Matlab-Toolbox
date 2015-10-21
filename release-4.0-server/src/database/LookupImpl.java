package database;

public class LookupImpl implements Lookupable {
	private int id;
	private String name;
	private String description;
	private int color;

	public LookupImpl(int id, String name, String description, int color) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.color = color;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}

}
