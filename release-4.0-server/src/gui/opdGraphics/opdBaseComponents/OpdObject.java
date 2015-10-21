package gui.opdGraphics.opdBaseComponents;

import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import exportedAPI.opcatAPIx.IXCheckResult;
import gui.Opcat2;
import gui.checkModule.CheckModule;
import gui.checkModule.CheckResult;
import gui.opdGraphics.GraphicsUtils;
import gui.opdGraphics.dialogs.ObjectPropertiesDialog;
import gui.opdGraphics.dialogs.StatePropertiesDialog;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmState;
import gui.opmEntities.OpmThing;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;
import gui.projectStructure.ThingEntry;
import gui.undo.UndoableAddState;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;

import util.Configuration;

/**
 * This class is a graphic representation of the Object as defined in OPM. It
 * can contain other OPD components as explained in <a href = "OpdThing.html">
 * <code>OpdThing</code></a>. In addition it can contain <a href =
 * "OpdState.html"><code>OpdState</code></a>s in two modes: autoarranged and
 * free placed.
 */
public class OpdObject extends OpdThing {
	// ---------------------------------------------------------------------------
	// The private attributes/members are located here

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private int statesDrawingStyle; // 0 - none 1 - implisit(default) 2 -

	// explisit

	private boolean statesAutoarrange;

	private ObjectEntry myObjectEntry;

	// private Animator animator = new Animator(1000);
	// private ScreenTransition transition = new ScreenTransition(
	// (JComponent) this, this, animator);
	OpmState sampleOpmState = null;

	/**
	 * <p>
	 * Constructs <code>OpdObject</code> object and initialize all defaults.
	 * </p>
	 * <strong>No default constructor for this class.</strong><br>
	 * 
	 * @param <code>pOpdId long</code>, uniqe identifier of the OPD this
	 *        component is added to.
	 * @param <code>pEntityInOpdId long</code>, uniqe identifier of the
	 *        component within OPD it is added to.
	 * @param <code>pProject OpdProject</code>, project this component is added
	 *        to.
	 * @param <code>pEntry ThingEntry</code>, the entry of the thing, this
	 *        <code>OpdThing</code> represents graphicaly.
	 * @param <code>fNode DefaultMutableTreeNode</code>, the reference to the
	 *        parent node in <a href = "../opdProject/RepositoryManager.html">
	 *        <code>RepositoryManager</code></a> tree.
	 */
	public OpdObject(ThingEntry pEntry, OpdProject pProject, long pOpdId,
			long pEntityInOpdId) {
		super(pOpdId, pEntityInOpdId, pProject, pEntry);
		this.myObjectEntry = (ObjectEntry) pEntry;

		this.borderColor = (Color) pProject.getConfiguration().getProperty(
				"ObjectColor");

		this.statesDrawingStyle = 1;

		this.setStatesAutoarrange(true);

		// animator.setAcceleration(.2f);
		// animator.setDeceleration(.2f);
	}

	/**
	 * <p>
	 * Paints the <code>OpdObject</code>
	 * </p>
	 * <p>
	 * If it has other OPD components, calls <code>paintComponents()</code>
	 * method and paints them.
	 * </p>
	 */
	public void paintComponent(Graphics g) {
		int shadowOffset = 0;

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;
		int selOffset = Math.round((float) (SELECTION_OFFSET * factor));
		int dSelOffset = 2 * selOffset;

		Graphics2D g2 = (Graphics2D) g;
		Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AntiAlias);

		if (this.isShadow()) {
			shadowOffset = Math.round((float) (SHADOW_OFFSET * factor));
			// shadowOffset = Math.round(SHADOW_OFFSET);
			g2.setColor(this.backgroundColor.darker());
			g2.fillRect(shadowOffset, shadowOffset, this.getWidth()
					- shadowOffset - 1 - dSelOffset, this.getHeight()
					- shadowOffset - 1 - dSelOffset);
		}

		g2.setColor(this.backgroundColor);
		g2.fillRect(selOffset, selOffset, this.getWidth() - shadowOffset - 1
				- dSelOffset, this.getHeight() - shadowOffset - 1 - dSelOffset);
		g2.setColor(this.textColor);
		this.drawText(g2);

		/*
		 * switch (statesDrawingStyle) { case 0: break; case 1:
		 * paintComponents(g2); break; case 2: paintComponents(g2); break; }
		 */
		g2.setColor(this.borderColor);

		ObjectEntry myEntry = (ObjectEntry) this.getEntry();
		boolean zoomedIn = (myEntry.getZoomedInOpd() != null);
		boolean unfolded = (myEntry.getUnfoldingOpd() != null);

		if (zoomedIn && !unfolded) {
			g2.setStroke(new BasicStroke((float) factor * 3.5f));
			g2.setColor(Color.GRAY);
		} else if (!zoomedIn && unfolded) {
			g2.setStroke(new BasicStroke((float) factor * 3.5f));
			g2.setColor(borderColor);
		} else if (zoomedIn && unfolded) {
			g2.setStroke(new BasicStroke((float) factor * 3.5f));
			g2.setColor(Color.RED);
		} else {
			g2.setStroke(new BasicStroke((float) factor * 2.0f));
		}

		// different look styles
		// if (this.isBoldBorder()) {
		// g2.setStroke(new BasicStroke((float) factor * 3.5f));
		// } else {
		// g2.setStroke(new BasicStroke((float) factor * 2.0f));
		// }

		if (this.isDashed()) {
			if (zoomedIn && !unfolded) {
				g2.setStroke(new BasicStroke((float) factor * 3.5f,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f,
						new float[] { 6 }, 0.0f));
				g2.setColor(Color.GRAY);
			} else if (!zoomedIn && unfolded) {
				g2.setStroke(new BasicStroke((float) factor * 3.5f,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f,
						new float[] { 6 }, 0.0f));
				g2.setColor(borderColor);

			} else if (zoomedIn && unfolded) {
				g2.setStroke(new BasicStroke((float) factor * 3.5f,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f,
						new float[] { 6 }, 0.0f));
				g2.setColor(Color.RED);
			} else {
				g2.setStroke(new BasicStroke((float) factor * 2.0f,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f,
						new float[] { 6 }, 0.0f));
			}
		}

		g2.drawRect(selOffset, selOffset, this.getWidth() - shadowOffset - 1
				- dSelOffset, this.getHeight() - shadowOffset - 1 - dSelOffset);
		g2.setStroke(new BasicStroke());
		if (this.isSelected()) {
			this.drawSelection(g2);
		}
		/**
		 * removed the roles from the Object
		 */
		if (Configuration.getInstance().getProperty("show_roles_on_process") != null) {
			String str = Configuration.getInstance().getProperty(
					"show_roles_on_objects");
			if (str.equalsIgnoreCase("yes"))
				this._drawRole(g2);
		}
		/**
		 * End
		 */
		this._drawNoOfInstances(g2);

		_drawIcon(g2, g2.getClip());

	}

	// public Component add(Component comp)
	// {
	// Component temp = super.add(comp);
	// if (isStatesAutoarrange())
	// {
	// drawStates();
	// }
	// return temp;
	// }
	/**
	 * This method constructs and shows <a href = "ObjectPropertiesDialog.html">
	 * <code>ObjectPropertiesDialog</code></a>
	 * <p>
	 * Updates changed Object properties not only in this OpdObject, but in <a
	 * href = "../opmEntities/OpmObject.html"><code>OpmObject</code></a> this
	 * <code>OpdObject</code> represents.
	 */
	public void callPropertiesDialog(int showTabs, int showButtons) {
		ObjectPropertiesDialog od = new ObjectPropertiesDialog(this,
				(ObjectEntry) this.myEntry, this.myProject, false);
		od.showDialog();
	}

	/**
	 * This method constructs and shows <a href =
	 * "ProcessPropertiesDialog.html"><code>ProcessPropertiesDialog</code></a>
	 * <p>
	 * Updates changed Process properties not only in this OpdProcess, but in <a
	 * href = "../opmEntities/OpmProcess.html"><code>OpmProcess</code></a> this
	 * <code>OpdProcess</code> represents.
	 * 
	 * @return Returns <code>true</code> if the dialog was "ok" pressed.
	 *         <code>false</code> if "cancel" was pressed.
	 */
	public boolean callPropertiesDialogWithFeedback(int showTabs,
			int showButtons) {
		ObjectPropertiesDialog od = new ObjectPropertiesDialog(this,
				(ObjectEntry) this.myEntry, this.myProject, false);
		return od.showDialog();
	}

	// public void setSize(int w, int h){
	// super.setSize(w, h);
	// if (statesAutoarrange)
	// {
	// drawStates();
	// }
	//
	// }

	/**
	 * @return <code>true</code> if the point (pX, pY) that given in coordinates
	 *         of this component is in area, that clicking and dragging mouse
	 *         will move the componenet, otherwise <code>false</code>.
	 */
	public boolean inMove(int pX, int pY) {
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		int borderSensitivity = (int) Math.round((currentSize / normalSize)
				* BORDER_SENSITIVITY);

		if ((pX > borderSensitivity) && (pY > borderSensitivity)
				&& (pX < this.getActualWidth() - borderSensitivity)
				&& (pY < this.getActualHeight() - borderSensitivity)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isInAdjacentArea(int x, int y) {
		GenericTable config = this.myProject.getConfiguration();
		Integer nSize = (Integer) config.getProperty("NormalSize");
		Integer cSize = (Integer) config.getProperty("CurrentSize");

		double proximity = 12.0 * cSize.doubleValue() / nSize.doubleValue();

		if (!((x > (int) -proximity) && (y > (int) -proximity)
				&& (x < this.getActualWidth() + (int) proximity) && (y < this
				.getActualHeight()
				+ (int) proximity))) {
			return false;
		}

		if ((x > (int) proximity) && (y > (int) proximity)
				&& (x < this.getActualWidth() - (int) proximity)
				&& (y < this.getActualHeight() - (int) proximity)) {
			return false;
		}

		return true;
	}

	/**
	 * @return <code>true</code> if the point (pX, pY) that given in coordinates
	 *         of this component is in area, that clicking and dragging mouse
	 *         will resize the componenet, otherwise <code>false</code>.
	 */
	public boolean inResize(int pX, int pY) {
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		int borderSensitivity = (int) Math.round((currentSize / normalSize)
				* BORDER_SENSITIVITY);

		if (!((pX > borderSensitivity) && (pY > borderSensitivity)
				&& (pX < this.getActualWidth() - borderSensitivity) && (pY < this
				.getActualHeight()
				- borderSensitivity))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return the border (as defined in <a href = "OpdBaseComponent.html">
	 *         <code>OpdBaseComponent</code></a> where the point (pX, pY) given
	 *         in this component coordinates is, or <code>NOT_IN_BORDER</code>.
	 */
	public int whichBorder(int pX, int pY) {
		int tempWidth = this.getActualWidth();
		int tempHeight = this.getActualHeight();

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		int borderSensitivity = (int) Math.round((currentSize / normalSize)
				* BORDER_SENSITIVITY);

		if ((pX >= tempWidth - borderSensitivity) && (pY <= borderSensitivity)) {
			return OpcatConstants.NE_BORDER;
		}

		if ((pX <= borderSensitivity) && (pY <= borderSensitivity)) {
			return OpcatConstants.NW_BORDER;
		}

		if ((pX >= tempWidth - borderSensitivity)
				&& (pY >= tempHeight - borderSensitivity)) {
			return OpcatConstants.SE_BORDER;
		}

		if ((pX <= borderSensitivity) && (pY >= tempHeight - borderSensitivity)) {
			return OpcatConstants.SW_BORDER;
		}

		if ((pX >= borderSensitivity) && (pX <= tempWidth - borderSensitivity)
				&& (pY >= tempHeight - borderSensitivity)) {
			return OpcatConstants.S_BORDER;
		}

		if ((pY >= borderSensitivity) && (pY <= tempHeight - borderSensitivity)
				&& (pX <= borderSensitivity)) {
			return OpcatConstants.W_BORDER;
		}

		if ((pY >= borderSensitivity) && (pY <= tempHeight - borderSensitivity)
				&& (pX >= tempWidth - borderSensitivity)) {
			return OpcatConstants.E_BORDER;
		}

		if ((pX >= borderSensitivity) && (pX <= tempWidth - borderSensitivity)
				&& (pY <= borderSensitivity)) {
			return OpcatConstants.N_BORDER;
		}

		return OpcatConstants.NOT_IN_BORDER;
	}

	/**
	 * Returns whether the states are autoarranged.
	 * 
	 * @retun <code>true</code> if line is arranged, otherwise
	 *        <code>false</code>
	 */
	public boolean isStatesAutoarrange() {
		return this.statesAutoarrange;
	}

	/**
	 * <p>
	 * Sets and unsets autoarrange for states.
	 * </p>
	 * <p>
	 * If states are autoarranged their sizes and positions will be calculated
	 * automaticly. If not autoarranged user can move and resize states as he
	 * wants.
	 * </p>
	 * 
	 * @param <code>yn boolean</code>, if <code>true</code> autoarrang is on, if
	 *        <code>false</code> autoarrange off
	 */
	public void setStatesAutoarrange(boolean yn) {
		this.statesAutoarrange = yn;
		if (this.statesAutoarrange) {
			this.drawStates();
		}

	}

	/**
	 * Return how to draw states: inside OpdObject, not draw at all or outside
	 * the OpdObject (not implemented);
	 */
	public int getStatesDrawingStyle() {
		return this.statesDrawingStyle;
	}

	/**
	 * <p>
	 * Sets states drawing style.
	 * </p>
	 * Inside OpdObject, not draw at all or outside the OpdObject (not
	 * implemented);
	 */
	public void setStatesDrawingStyle(int style) {
		this.statesDrawingStyle = style;
	}

	/**
	 * @return A point of connection according to <code>pSide</code> and
	 *         <code>parem</code> arguments
	 * @param <code>pSide</code> -- the side of OPD graphic component
	 * @param <code>param</code> -- The relation of connection point to length
	 *        of the given side
	 */
	public Point getAbsoluteConnectionPoint(RelativeConnectionPoint pPoint) {
		Point retPoint = new Point(0, 0);

		switch (pPoint.getSide()) {
		case OpcatConstants.N_BORDER:

			if (pPoint.getParam() < 0) {
				return new Point(2, 2);
			}
			if (pPoint.getParam() > 1) {
				return new Point(this.getActualWidth() - 2, 2);
			}

			retPoint.setLocation(this.getActualWidth() * pPoint.getParam(), 2);
			break;
		case OpcatConstants.S_BORDER:

			if (pPoint.getParam() < 0) {
				return new Point(2, this.getActualHeight() - 2);
			}
			if (pPoint.getParam() > 1) {
				return new Point(this.getActualWidth() - 2, this
						.getActualHeight() - 2);
			}

			retPoint.setLocation(this.getActualWidth() * pPoint.getParam(),
					this.getActualHeight() - 2);
			break;
		case OpcatConstants.E_BORDER:

			if (pPoint.getParam() < 0) {
				return new Point(this.getActualWidth() - 2, 2);
			}
			if (pPoint.getParam() > 1) {
				return new Point(this.getActualWidth() - 2, this
						.getActualHeight() - 2);
			}

			retPoint.setLocation(this.getActualWidth() - 2, this
					.getActualHeight()
					* pPoint.getParam());
			break;
		case OpcatConstants.W_BORDER:

			if (pPoint.getParam() < 0) {
				return new Point(2, 2);
			}
			if (pPoint.getParam() > 1) {
				return new Point(2, this.getActualHeight() - 2);
			}

			retPoint.setLocation(2, this.getActualHeight() * pPoint.getParam());
			break;
		}

		return retPoint;
	}

	public boolean isThereVisibleState() {
		Component components[] = this.getComponents();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof OpdState) {
				OpdState tempState = (OpdState) components[i];
				if (tempState.isVisible()) {
					return true;
				}
			}
		}

		return false;

	}

	public void drawStates() {
		OpdState tempState;
		int numStates = 0;
		double maxHeight = 0;
		double sumWidth = 0;
		TreeMap statesMap = new TreeMap();
		double room;

		Component components[] = this.getComponents();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof OpdState) {
				tempState = (OpdState) components[i];
				if (tempState.isVisible()) {
					// tempState.fitToContent();
					// TODO: when states width overflows the object width or
					// notepadwidth
					sumWidth += tempState.getWidth();
					maxHeight = Math.max(tempState.getHeight(), maxHeight);

					numStates++;
					statesMap.put(new Long(tempState.getEntity().getId()),
							tempState);
				}
			}
		}

		room = (this.getActualWidth() - sumWidth) / (numStates + 1);

		// Configuration config = myProject.getConfiguration();
		// double normalSize =
		// ((Integer)config.getProperty("NormalSize")).doubleValue();
		// double currentSize =
		// ((Integer)config.getProperty("CurrentSize")).doubleValue();
		// double factor = currentSize/normalSize;
		// double width =
		// ((Integer)config.getProperty("StateWidth")).intValue()*factor;
		// double height =
		// ((Integer)config.getProperty("StateHeight")).intValue()*factor;

		double currY = room;

		for (Iterator it = statesMap.values().iterator(); it.hasNext();) {
			tempState = (OpdState) it.next();
			tempState.setLocation((int) currY, this.getActualHeight() - 8
					- (int) maxHeight);
			tempState.setSize(tempState.getWidth(), (int) maxHeight);
			currY += room + tempState.getWidth();

			// int tmp = (getActualWidth() - (int)(numStates*width))/(numStates
			// + 3);
			// if (tmp > 3)
			// {
			// space = tmp;
			// }

			// tempState.setLocation(space+(cnt*(space+(getActualWidth()-space*numStates-space)/numStates)),
			// getActualHeight()-5-(int)height);
			// tempState.setSize((getActualWidth()-space*numStates-space)/numStates,
			// (int)height);
		}
	}

	private void drawText(Graphics2D g2) {

		int stringX = 0;
		int stringY = 0;
		OpmObject myObj = (OpmObject) this.myEntity;

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		Font currentFont = (Font) config.getProperty("ThingFont");
		currentFont = currentFont
				.deriveFont((float) (currentFont.getSize2D() * factor));

		g2.setFont(currentFont);
		this.currentMetrics = this.getFontMetrics(currentFont);

		String text;
		int sh;

		int numOfRows = 0;
		double sw = 0;

		int textShift;

		if ((this.myObjectEntry.getStateHash().size() == 0)
				|| (this.statesDrawingStyle == 0)) {
			textShift = 0;
		} else {
			textShift = 0;
			Component components[] = this.getComponents();

			for (int i = 0; i < components.length; i++) {
				if ((components[i] instanceof OpdState)
						&& components[i].isVisible()) {
					textShift = Math.max(textShift, components[i].getHeight());
				}
			}

		}

		if ((myObj.getType() == null) || myObj.getType().trim().equals("")) {
			text = this.myEntity.getName();
		} else {
			text = myObj.getName() + " :\n" + myObj.getType();
		}

		String head = "";

		if (((OpmThing) this.myEntity).getScope()
				.equals(OpcatConstants.PRIVATE)) {
			head = "- ";
		}

		if (((OpmThing) this.myEntity).getScope().equals(
				OpcatConstants.PROTECTED)) {
			head = "# ";
		}

		StringTokenizer st = new StringTokenizer(head + text, "\n");

		while (st.hasMoreTokens()) {
			String tString = st.nextToken();
			sw = Math.max(sw, this.currentMetrics.stringWidth(tString));
			numOfRows++;
		}

		sh = this.currentMetrics.getAscent() * numOfRows;

		st = new StringTokenizer(head + text, "\n");
		int dY = 0;

		if (this.getTextPosition().equals("C")
				|| this.getTextPosition().equals("E")
				|| this.getTextPosition().equals("W"))

		{
			stringY = (this.getActualHeight() - 3 - textShift - sh) / 2;
		}

		if (this.getTextPosition().equals("N")
				|| this.getTextPosition().equals("NE")
				|| this.getTextPosition().equals("NW"))

		{
			stringY = 6;
		}

		if (this.getTextPosition().equals("S")
				|| this.getTextPosition().equals("SE")
				|| this.getTextPosition().equals("SW"))

		{
			stringY = (this.getActualHeight() - 9 - textShift - sh);
		}

		//
		if (this.getTextPosition().equals("C")
				|| this.getTextPosition().equals("S")
				|| this.getTextPosition().equals("N")) {
			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				stringX = (this.getActualWidth()
						- this.currentMetrics.stringWidth(tString) - 3) / 2;
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);
				if (myObj.isKey()) {
					ats.addAttribute(TextAttribute.UNDERLINE,
							TextAttribute.UNDERLINE_ON);
				}
				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(), stringX, stringY + dY);
			}

			return;
		}

		if (this.getTextPosition().equals("NE")
				|| this.getTextPosition().equals("E")
				|| this.getTextPosition().equals("SE")) {
			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				stringX = (this.getActualWidth() - (int) sw) - 11;
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);
				if (myObj.isKey()) {
					ats.addAttribute(TextAttribute.UNDERLINE,
							TextAttribute.UNDERLINE_ON);
				}
				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(), stringX, stringY + dY);
			}

			return;

		}

		if (this.getTextPosition().equals("NW")
				|| this.getTextPosition().equals("W")
				|| this.getTextPosition().equals("SW")) {
			stringX = 9;

			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);
				if (myObj.isKey()) {
					ats.addAttribute(TextAttribute.UNDERLINE,
							TextAttribute.UNDERLINE_ON);
				}
				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(), stringX, stringY + dY);
			}

			return;

		}

		// end
	}

	private void _drawRole(Graphics2D g2) {
		String role = ((OpmThing) this.myEntity).getPoliciesRole();
		if ((role == null)) {
			return;
		}
		int stringX, stringY;
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		Font currentFont = (Font) config.getProperty("SmallFont");
		currentFont = currentFont
				.deriveFont((float) (currentFont.getSize2D() * factor));
		g2.setColor(Color.black);
		g2.setFont(currentFont);
		this.currentMetrics = this.getFontMetrics(currentFont);

		stringY = (this.currentMetrics.getHeight());
		stringX = 7;
		g2.drawString(role, stringX, stringY);
		return;
	}

	private void _drawNoOfInstances(Graphics2D g2) {
		int val = ((OpmThing) this.myEntity).getNumberOfInstances();
		if (val == 1) {
			return;
		}

		String noi = String.valueOf(val);

		int stringX, stringY;
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		Font currentFont = (Font) config.getProperty("SmallFont");
		currentFont = currentFont
				.deriveFont((float) (currentFont.getSize2D() * factor));
		g2.setColor(Color.black);
		g2.setFont(currentFont);
		this.currentMetrics = this.getFontMetrics(currentFont);

		stringY = (this.getActualHeight() - 7);
		stringX = this.getActualWidth() - 7
				- this.currentMetrics.stringWidth(noi);
		g2.drawString(noi, stringX, stringY);
		return;
	}

	public Dimension getActualSize() {

		if (this.isZoomedIn()) {
			return new Dimension(this.getWidth(), this.getHeight());
		}

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		Integer oWidth = (Integer) config.getProperty("ThingWidth");
		Integer oHeight = (Integer) config.getProperty("ThingHeight");

		Font currentFont = (Font) config.getProperty("ThingFont");
		currentFont = currentFont
				.deriveFont((float) (currentFont.getSize2D() * factor));
		this.currentMetrics = this.getFontMetrics(currentFont);

		double sw = 0;
		int numOfRows = 0;

		OpmObject myObject = (OpmObject) this.myEntity;
		StringTokenizer st = new StringTokenizer(myObject.getName() + "\n"
				+ myObject.getType(), "\n");

		while (st.hasMoreTokens()) {
			String tString = st.nextToken();
			sw = Math.max(sw, this.currentMetrics.stringWidth(tString));
			numOfRows++;
		}

		double sh = this.currentMetrics.getAscent() * numOfRows * 1.3;
		sw = sw * 1.3;

		OpdState tempState;
		int numStates = 0;
		double maxHeight = 0;
		double sumWidth = 0;

		Component components[] = this.getComponents();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof OpdState) {
				tempState = (OpdState) components[i];
				if (tempState.isVisible()) {
					tempState.fitToContent();
					sumWidth += tempState.getWidth();
					maxHeight = Math.max(tempState.getHeight(), maxHeight);

					numStates++;
				}
			}
		}

		sumWidth += (numStates + 1) * 10 * factor;

		double width = Math.max(sumWidth, sw);
		double height = sh + maxHeight;

		width = width + 20 * factor;
		height = height + 20 * factor;

		width = Math.max(width, oWidth.doubleValue() * factor * 3 / 4);
		height = Math.max(height, oHeight.doubleValue() * factor * 3 / 4);

		return new Dimension((int) width, (int) height);

	}

	public void fitToContent() {
		if (this.isZoomedIn()) {
			return;
		}

		Dimension dim = this.getActualSize();
		this.setActualSize((int) dim.getWidth(), (int) dim.getHeight());
		this.drawStates();
	}

	public void setupNextScreen() {
		StateEntry myEntry = this.myProject.addState(this);

		OpmState nState = (OpmState) myEntry.getLogicalEntity();
		nState.copyPropsFrom(sampleOpmState);
		/**
		 * make sure only one is initial or final ;
		 */
		myEntry.setInitial(myEntry.isInitial());
		myEntry.setFinal(myEntry.isFinal());
		myEntry.setDefault(myEntry.isDefault());

		for (Enumeration en = this.getEntry().getInstances(); en
				.hasMoreElements();) {
			ObjectInstance parentInstance = (ObjectInstance) en.nextElement();
			OpdObject pObj = (OpdObject) parentInstance.getThing();

			pObj.repaint();
			if (!pObj.getInstance().getKey().toString().equalsIgnoreCase(
					this.getInstance().getKey().toString())) {
				StateInstance stateInstance = ((ObjectInstance) pObj
						.getInstance()).getState(nState.getName());
				stateInstance.getGraphicalRepresentation().setVisible(false);
				pObj.repaint();
			}

		}

		if (this.isStatesAutoarrange()) {
			this.fitToContent();
		}

		Opcat2.getCurrentProject().getCurrentOpd().refit(
				Opcat2.getCurrentProject().getCurrentOpd().getDrawingArea());

		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		Opcat2.getUndoManager().undoableEditHappened(
				new UndoableEditEvent(this.myProject, new UndoableAddState(
						this.myProject, myEntry)));
		Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
		Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
	}

	private void _addState() {

		OpmObject sampleObj = new OpmObject(0, "");
		ObjectEntry sampleObjEntry = new ObjectEntry(sampleObj, this.myProject);
		OpdObject sampleObjOpd = new OpdObject(sampleObjEntry, this.myProject,
				0, 0);

		sampleOpmState = new OpmState(0, "");
		StateEntry sampleStateEntry = new StateEntry(sampleOpmState, sampleObj,
				this.myProject);
		StateInstance sampleSI = new StateInstance(sampleStateEntry,
				new OpdKey(0, 0), sampleObjOpd, this.myProject);

		StatePropertiesDialog sd = new StatePropertiesDialog(sampleSI,
				this.myProject, false, BaseGraphicComponent.SHOW_OK
						| BaseGraphicComponent.SHOW_CANCEL);

		if (sd.showDialog()) {
			// transition.start();
			StateEntry myEntry = this.myProject.addState(this);

			OpmState nState = (OpmState) myEntry.getLogicalEntity();
			nState.copyPropsFrom(sampleOpmState);
			/**
			 * make sure only one is initial or final ;
			 */
			myEntry.setInitial(myEntry.isInitial());
			myEntry.setFinal(myEntry.isFinal());
			myEntry.setDefault(myEntry.isDefault());

			for (Enumeration en = this.getEntry().getInstances(); en
					.hasMoreElements();) {
				ObjectInstance parentInstance = (ObjectInstance) en
						.nextElement();
				OpdObject pObj = (OpdObject) parentInstance.getThing();

				pObj.repaint();
				if (!pObj.getInstance().getKey().toString().equalsIgnoreCase(
						this.getInstance().getKey().toString())) {
					StateInstance stateInstance = ((ObjectInstance) pObj
							.getInstance()).getState(nState.getName());
					stateInstance.getGraphicalRepresentation()
							.setVisible(false);
					pObj.repaint();
				}

			}

			if (this.isStatesAutoarrange()) {
				this.fitToContent();
			}

			Opcat2.getCurrentProject().getCurrentOpd()
					.refit(
							Opcat2.getCurrentProject().getCurrentOpd()
									.getDrawingArea());

			Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
			Opcat2.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(this.myProject, new UndoableAddState(
							this.myProject, myEntry)));
			Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
			Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
		}

	}

	public void mouseReleased(MouseEvent e) {
		GraphicsUtils.setLastMouseEvent(e);
		if (StateMachine.isWaiting()) {
			return;
		}

		if (StateMachine.getCurrentState() == StateMachine.ADD_STATE) {
			StateMachine.reset();

			CheckResult cr = CheckModule.checkStateAddition(myInstance,
					myProject);
			if (cr.getResult() == IXCheckResult.WRONG) {
				JOptionPane.showMessageDialog(Opcat2.getFrame(), cr
						.getMessage(), "Opcat2 - Error",
						JOptionPane.ERROR_MESSAGE);
				this.setCursorForState(e);
				this.getDrawingArea().repaint();
				return;
			}

			if (cr.getResult() == IXCheckResult.WARNING) {
				int ret = JOptionPane.showOptionDialog(Opcat2.getFrame(), cr
						.getMessage(), "Opcat2 - Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						null, null, null);
				this.setCursorForState(e);
				this.getDrawingArea().repaint();
				if (ret != JOptionPane.YES_OPTION) {
					return;
				}
			}

			this._addState();
		}

		super.mouseReleased(e);
	}

	/**
	 * The method chanegs the cursor to a Hand, when a state is added to the
	 * object.
	 * 
	 * @author Eran Toch
	 */
	public void mouseEntered(MouseEvent e) {
		if (StateMachine.getCurrentState() == StateMachine.ADD_STATE) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			super.mouseEntered(e);
		}
	}

}
