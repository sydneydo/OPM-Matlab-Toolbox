package gui.opdGraphics;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;

import javax.swing.SwingUtilities;

import exportedAPI.OpcatConstants;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.opdBaseComponents.OpdProcess;

public class GraphicsUtils {
    private static long timeValues[] = { 1, 1000, 1000 * 60, 1000 * 60 * 60,
	    1000 * 60 * 60 * 24, 1000 * 60 * 60 * 24 * 30,
	    1000 * 60 * 60 * 24 * 30 * 365 };

    private static MouseEvent lastMouseEvent;

    public static double calcutateRotationAngle(Point head, Point tail) {
	double tan = (head.getY() - tail.getY()) / (head.getX() - tail.getX());
	double angle = Math.atan(tan);

	if ((tan > 0) && (head.getX() >= tail.getX())) {
	    angle = 1.5 * Math.PI + angle;
	    return angle;
	}
	if ((tan < 0) && (head.getX() >= tail.getX())) {
	    angle = angle - Math.PI / 2;
	    return angle;
	}
	if ((tan > 0) && (head.getX() < tail.getX())) {
	    angle = angle + Math.PI / 2;
	    return angle;
	}
	if ((tan < 0) && (head.getX() < tail.getX())) {
	    angle = Math.PI / 2 - Math.abs(angle);
	    return angle;
	}
	if ((tan == 0) && (head.getX() < tail.getX())) {
	    angle = (Math.PI / 2);
	}
	if ((tan == 0) && (head.getX() > tail.getX())) {
	    angle = -(Math.PI / 2);
	}
	return angle;
    }

    // public static RelativeConnectionPoint
        // calculateConnectionPoint(OpdConnectionEdge mainItem,
        // OpdConnectionEdge secondItem)
    public static RelativeConnectionPoint calculateConnectionPoint(
	    Connectable mainItem, Connectable secondItem) {

	Point sItemCenter;

	Point tmpPoint = new Point(secondItem.getX(), secondItem.getY());
	tmpPoint = SwingUtilities.convertPoint(secondItem.getParent(),
		tmpPoint, mainItem.getParent());
	sItemCenter = new Point(
		(int) ((tmpPoint.getX() + (double) secondItem.getActualWidth() / 2) - (mainItem
			.getX() + (double) mainItem.getActualWidth() / 2)),
		(int) ((mainItem.getY() + (double) mainItem.getActualHeight() / 2) - (tmpPoint
			.getY() + (double) secondItem.getActualHeight() / 2)));

	double k;

	if (sItemCenter.getX() == 0) {
	    k = sItemCenter.getY() / 0.0001;
	} else {
	    k = sItemCenter.getY() / sItemCenter.getX();
	}

	if (k == 0.0) {
	    k = 0.0001;
	}

	double a = mainItem.getActualWidth() / 2.0;
	double b = mainItem.getActualHeight() / 2.0;

	if (mainItem instanceof OpdProcess) {

	    double x = Math.sqrt((a * a * b * b) / (b * b + (a * a * k * k)));

	    if ((sItemCenter.getY() >= 0) && (sItemCenter.getX() >= 0)) {
		return new RelativeConnectionPoint(OpcatConstants.N_BORDER,
			(x + a) / mainItem.getActualWidth());
	    }

	    if ((sItemCenter.getY() >= 0) && (sItemCenter.getX() <= 0)) {
		return new RelativeConnectionPoint(OpcatConstants.N_BORDER,
			(a - x) / mainItem.getActualWidth());
	    }

	    if ((sItemCenter.getY() <= 0) && (sItemCenter.getX() >= 0)) {
		return new RelativeConnectionPoint(OpcatConstants.S_BORDER,
			(x + a) / mainItem.getActualWidth());
	    }

	    if ((sItemCenter.getY() <= 0) && (sItemCenter.getX() <= 0)) {
		return new RelativeConnectionPoint(OpcatConstants.S_BORDER,
			(a - x) / mainItem.getActualWidth());
	    }

	}

	double x1 = Math.abs(b / k);
	double y1 = Math.abs(a * k);

	if ((sItemCenter.getY() >= 0) && (sItemCenter.getX() >= 0)) {
	    if (x1 <= a) {
		return new RelativeConnectionPoint(OpcatConstants.N_BORDER,
			(x1 + a) / (2 * a));
	    } else {
		return new RelativeConnectionPoint(OpcatConstants.E_BORDER,
			(b - y1) / (2 * b));
	    }
	}

	if ((sItemCenter.getY() >= 0) && (sItemCenter.getX() <= 0)) {
	    if (x1 <= a) {
		return new RelativeConnectionPoint(OpcatConstants.N_BORDER,
			(a - x1) / (2 * a));
	    } else {
		return new RelativeConnectionPoint(OpcatConstants.W_BORDER,
			(b - y1) / (2 * b));
	    }
	}

	if ((sItemCenter.getY() <= 0) && (sItemCenter.getX() >= 0)) {
	    if (x1 <= a) {
		return new RelativeConnectionPoint(OpcatConstants.S_BORDER,
			(x1 + a) / (2 * a));
	    } else {
		return new RelativeConnectionPoint(OpcatConstants.E_BORDER,
			(b + y1) / (2 * b));
	    }
	}

	if ((sItemCenter.getY() <= 0) && (sItemCenter.getX() <= 0)) {
	    if (x1 <= a) {
		return new RelativeConnectionPoint(OpcatConstants.S_BORDER,
			(a - x1) / (2 * a));
	    } else {
		return new RelativeConnectionPoint(OpcatConstants.W_BORDER,
			(b + y1) / (2 * b));
	    }
	}

	return new RelativeConnectionPoint(OpcatConstants.S_BORDER, 0.5);

    }

    public static boolean checkCustomCardinalityLegality(String str) {
	int i = 0;
	int from, to, iTmp = 0;
	String strTmp;
	StringTokenizer strTok = new StringTokenizer(str, ",");

	for (; strTok.hasMoreTokens();) {
	    i = 0;
	    strTmp = strTok.nextToken();

	    try {
		if (Character.isDigit(strTmp.charAt(i))) {
		    i++;
		} else {
		    return false;
		}

		try {
		    while (Character.isDigit(strTmp.charAt(i))) {
			i++;
		    }
		} catch (IndexOutOfBoundsException ioobe) {
		    return true;
		}

		from = Integer.parseInt(strTmp.substring(0, i));

		if ((strTmp.charAt(i) != '.') && (strTmp.charAt(i + 1) != '.')) {
		    return false;
		}

		i = i + 2;

		if (Character.isLetter(strTmp.charAt(i))) {
		    if (strTmp.length() == i + 1) {
			return true;
		    }
		}

		if (Character.isDigit(strTmp.charAt(i))) {
		    iTmp = i;
		    i++;
		}

		try {
		    while (Character.isDigit(strTmp.charAt(i))) {
			i++;
		    }
		    if (Character.isLetter(strTmp.charAt(i))) {
			return false;
		    }
		} catch (IndexOutOfBoundsException ioobe) {
		    to = Integer.parseInt(strTmp.substring(iTmp, i));
		    if (from >= to) {
			return false;
		    } else {
			return true;
		    }
		}
	    } catch (IndexOutOfBoundsException ioobe) {
		return false;
	    }
	}
	return false;
    }

    public static String capitalizeFirstLetters(String str) {
	String result = "";
	StringTokenizer st = new StringTokenizer(str, "\n ", true);

	while (st.hasMoreTokens()) {
	    String tString = st.nextToken();
	    result += tString.substring(0, 1).toUpperCase()
		    + tString.substring(1);
	}

	return result;

    }

    public static long getMsec4TimeString(String time) {
	if (time.equals("infinity")) {
	    return Long.MAX_VALUE;
	}

	StringTokenizer st = new StringTokenizer(time, ";");

	int i = 0;
	long result = 0;

	while (st.hasMoreTokens()) {
	    String tmp = st.nextToken();
	    result += timeValues[i] * Long.parseLong(tmp);
	    i++;
	}

	return result;
    }

    public static String convertCardinality2GrRepr(String s) {
	String str = s.trim();

	if (str.equals("1") || str.equals("1..1")) {
	    return " ";
	}

	if (str.equals("0..1")) {
	    return "?";
	}

	if ((str.length() == 4) && str.startsWith("0..")
		&& Character.isLetter(str.charAt(3))) {
	    return "*";
	}

	if ((str.length() == 4) && str.startsWith("1..")
		&& Character.isLetter(str.charAt(3))) {
	    return "+";
	}

	return str;

	// int i = 0;
	// int from, to, iTmp = 0;
	// String strTmp;
	// StringTokenizer strTok = new StringTokenizer(str, ",");
	//
	// for(;strTok.hasMoreTokens();)
	// {
	// i = 0;
	// strTmp = strTok.nextToken();
	//
	// try{
	// if(Character.isDigit(strTmp.charAt(i)))
	// i++;
	//
	// else return false;
	//
	// try{
	// while(Character.isDigit(strTmp.charAt(i)))
	// i++;
	// }
	// catch(IndexOutOfBoundsException ioobe)
	// {
	// return true;
	// }
	//
	// from = Integer.parseInt(strTmp.substring(0,i));
	//
	// if( (strTmp.charAt(i) != '.') && (strTmp.charAt(i+1) != '.') )
	// return false;
	//
	// i = i+2;
	//
	// if(Character.isLetter(strTmp.charAt(i)))
	// if(strTmp.length() == i+1)
	// return true;
	//
	// if(Character.isDigit(strTmp.charAt(i)))
	// {
	// iTmp = i;
	// i++;
	// }
	//
	// try{
	// while(Character.isDigit(strTmp.charAt(i)))
	// i++;
	// if(Character.isLetter(strTmp.charAt(i)))
	// return false;
	// }
	// catch(IndexOutOfBoundsException ioobe)
	// {
	// to = Integer.parseInt(strTmp.substring(iTmp,i));
	// if(from >= to)
	// return false;
	// else
	// return true;
	// }
	// }
	// catch(IndexOutOfBoundsException ioobe)
	// {
	// return false;
	// }
	// }
	// return false;
    }

    public static OpdConnectionEdge findAdjacentComponent(Container cn, int x,
	    int y) {
	Component components[] = cn.getComponents();
	OpdConnectionEdge retEntity = null;

	for (int i = 0; i < components.length; i++) {
	    if (components[i] instanceof OpdConnectionEdge) {
		OpdConnectionEdge temp = (OpdConnectionEdge) components[i];
		Point convPoint;
		convPoint = SwingUtilities.convertPoint(cn, x, y, temp);
		retEntity = findAdjacentComponent(temp, (int) convPoint.getX(),
			(int) convPoint.getY());

		if (retEntity != null) {
		    return retEntity;
		}

		// System.err.println(temp + " "+(int)convPoint.getX()+" "+
                // (int)convPoint.getY());

		if (temp.isInAdjacentArea((int) convPoint.getX(),
			(int) convPoint.getY())) {
		    return temp;
		}
	    }
	}

	return null;
    }

    public static Point findPlace4Menu(Component cm, Point clickPlace,
	    Dimension menuSize) {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Point onScreenPoint = new Point(clickPlace);
	SwingUtilities.convertPointToScreen(onScreenPoint, cm);
	int x, y;
	if (onScreenPoint.getX() + menuSize.getWidth() > screenSize.getWidth()) {
	    x = (int) (clickPlace.getX() - menuSize.getWidth());
	} else {
	    x = (int) clickPlace.getX();
	}

	if (onScreenPoint.getY() + menuSize.getHeight() > screenSize
		.getHeight()) {
	    y = (int) (clickPlace.getY() - menuSize.getHeight());
	} else {
	    y = (int) (clickPlace.getY());
	}

	return new Point(x, y);

    }

    /**
         * @return Returns the lastMouseEvent.
         */
    public static MouseEvent getLastMouseEvent() {
	return lastMouseEvent;
    }

    /**
         * @param lastMouseEvent
         *                The lastMouseEvent to set.
         */
    public static void setLastMouseEvent(MouseEvent lastMouseEvent) {
	GraphicsUtils.lastMouseEvent = lastMouseEvent;
    }

}
