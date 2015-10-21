package gui.dataProject;

import java.awt.Color;
import java.util.ArrayList;

public class DataColors {

	private static ArrayList colors = null ; 

	boolean used = false;
	
	boolean lazyInit = true ; 

	public DataColors() {
		used = false;
	}


	public Color getColor(int index) {

	    if(lazyInit) {
		colors = MetaLoadColors.getInstance().load();
		lazyInit = !lazyInit ; 
	    }
		if (index == -1) {
			Color col = new Color(0, 0, 0);
			return col;
		}
		if (index < colors.size()) {
			return (Color) colors.get(index);
		}

		int red = 0;
		int green = 0;
		int blue = 0;

		if (index > 255) {
			red = 255 - ( index % 255 ) ;
			green =  index % 255  ;
			blue = index % 255 ;
		} else {
			red = index ;
			green = 255 -  index;
			blue = 255 % index ;
		}

		Color col = new Color(red, green, blue);
		return col;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}
