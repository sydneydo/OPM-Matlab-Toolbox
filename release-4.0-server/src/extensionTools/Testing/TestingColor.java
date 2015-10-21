package extensionTools.Testing;

import java.awt.Color;

public class TestingColor implements Comparable<TestingColor>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String value = " ";

	Color color = null;

	public TestingColor(Color color, String value ) {

	    this.value = value ; 
	    this.color = color ; 
	}

	public String toString() {

		return value;
	}

	public Color getColor() {
		return color;
	}

	public int compareTo(TestingColor o) {
	    return o.toString().compareToIgnoreCase(this.toString()) ; 
	}

}
