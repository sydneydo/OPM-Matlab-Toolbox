package gui.opdGraphics.opdBaseComponents;

//-----------------------------------------------------------------
/**
 ** The purpose of this class is to
 **
 **
 **	@see
 **	@version	2.0
 **	@author		Stanislav Obydionnov
 **	@author		Yevgeny   Yaroker
 **/
//-----------------------------------------------------------------
import exportedAPI.OpcatConstants;
import exportedAPI.OpdKey;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.dialogs.StatePropertiesDialog;
import gui.opdGraphics.popups.StatePopup;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opdProject.StateMachine;
import gui.opmEntities.OpmState;
import gui.projectStructure.StateInstance;

import java.awt.BasicStroke;
import java.awt.Color;
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
import java.util.StringTokenizer;

import javax.swing.JPopupMenu;

/**
 * <p>
 * This class graphicaly represents OPM State as defined in OPM documentation.
 * State can be drawn inside <a href = "OpdObject.html"><code>OpdObject</code>
 * </a> or outside it (this option is not implemented). State also can be drawn
 * as final or initial state according to OPD documentation.
 * </p>
 */

public class OpdState extends OpdConnectionEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public final double MIN_EXTERNAL_ROUND = 12.0;

	public final double MIN_INTERNAL_ROUND = 7.0;

	/**
	 * <p>
	 * Constructs <code>OpdState</code> object and initialize all defaults.
	 * </p>
	 * <strong>No default constructor for this class.</strong><br>
	 * 
	 * @param <code>pOpdId long</code>, uniqe identifier of the OPD this
	 *        component is added to.
	 * @param <code>pEntityInOpdId long</code>, uniqe identifier of the
	 *        component within OPD it is added to.
	 * @param <code>pProject OpdProject</code>, project this component is added
	 *        to.
	 * @param <code>mState OpmState</code>, the logic representation of the
	 *        state <a href = "../opmEntities/OpmState.html">
	 *        <code>OpmState</code></a>.
	 * @param <code>fNode DefaultMutableTreeNode</code>, the reference to the
	 *        parent node in <a href = "../opdProject/RepositoryManager.html">
	 *        <code>RepositoryManager</code></a> tree.
	 */
	public OpdState(OpmState mState, OpdProject pProject, long pOpdId,
			long pEntityInOpdId) {
		super(pOpdId, pEntityInOpdId, pProject);
		this.myEntity = mState;

		GenericTable config = pProject.getConfiguration();
		this.borderColor = (Color) config.getProperty("StateColor");
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double factor = currentSize / normalSize;
		double width = ((Integer) config.getProperty("StateWidth")).intValue()
				* factor;
		double height = ((Integer) config.getProperty("StateHeight"))
				.intValue()
				* factor;

		this.setSize((int) width, (int) height);
		this.setMinimumSize(new Dimension(this.getWidth() / 2,
				this.getHeight() / 2));

		this.myEntry = this.myProject.getComponentsStructure().getEntry(
				mState.getId());
	}

	/**
	 * @return the string repersentation of State, actually returns Entity name.
	 */
	public String toString() {
		return this.myEntity.getName();
	}

	/**
	 * Paints <code>OpdState</code>.
	 * <p>
	 * If state is initial paints it with bolder border. If state is final
	 * paints with double line border.
	 * </p>
	 */
	public void paintComponent(Graphics g) {
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;
		int selOffset = Math.round((float) (SELECTION_OFFSET * factor));
		int dSelOffset = 2 * selOffset;

		int stringX;
		int stringY;
		int extRound = Math.max((int) (this.getHeight() / 4.0),
				(int) (this.MIN_EXTERNAL_ROUND * factor));
		int intRound = Math.max(
				(int) ((this.getHeight() - 10.0 * factor) / 4.0),
				(int) (this.MIN_INTERNAL_ROUND * factor));

		Graphics2D g2 = (Graphics2D) g;
		Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AntiAlias);

		OpmState opmState = (OpmState) this.myEntity;
		Font currentFont = (Font) this.myProject.getConfiguration()
				.getProperty("StateFont");
		currentFont = currentFont
				.deriveFont((float) (currentFont.getSize2D() * factor));
		g2.setFont(currentFont);
		this.currentMetrics = this.getFontMetrics(currentFont);

		g2.setColor(this.backgroundColor);
		g2.fillRoundRect(selOffset, selOffset,
				this.getWidth() - 1 - dSelOffset, this.getHeight() - 1
						- dSelOffset, extRound, extRound);
		g2.setColor(this.borderColor);
		if (opmState.isInitial()) {
			g2.setStroke(new BasicStroke((float) factor * 3.0f));
			g2.drawRoundRect(selOffset, selOffset, this.getWidth() - 1
					- dSelOffset, this.getHeight() - 1 - dSelOffset, extRound,
					extRound);
		}

		g2.setStroke(new BasicStroke((float) factor * 1.3f));

		if (opmState.isFinal()) {
			g2.drawRoundRect(selOffset, selOffset, this.getWidth() - 1
					- dSelOffset, this.getHeight() - 1 - dSelOffset, extRound,
					extRound);

			int dist = Math.round((float) ((dSelOffset + 1) * factor));
			dist = Math.max(dist, 5);

			g2.drawRoundRect(dist, dist, this.getWidth() - 1 - dist * 2, this
					.getHeight()
					- 1 - dist * 2, intRound, intRound);

		}

		if (!opmState.isInitial() && !opmState.isFinal()) {
			g2.drawRoundRect(selOffset, selOffset, this.getWidth() - 1
					- dSelOffset, this.getHeight() - 1 - dSelOffset, extRound,
					extRound);
		}

		g2.setColor(this.textColor);

		StringTokenizer st = new StringTokenizer(this.myEntity.getName(), "\n");

		int numOfRows = 0;

		String tString;
		while (st.hasMoreTokens()) {
			tString = st.nextToken();
			numOfRows++;
		}

		st = new StringTokenizer(this.myEntity.getName(), "\n");

		stringY = (this.getHeight() - this.currentMetrics.getAscent()
				* numOfRows - selOffset) / 2;
		int dY = 0;

		while (st.hasMoreTokens()) {
			tString = st.nextToken();
			AttributedString ats = new AttributedString(tString);
			ats.addAttribute(TextAttribute.FONT, currentFont);
			stringX = (this.getWidth()
					- this.currentMetrics.stringWidth(tString) - selOffset) / 2;
			dY = dY + this.currentMetrics.getAscent();
			g2.drawString(ats.getIterator(), stringX, stringY + dY);
		}

		if (this.isSelected()) {
			this.drawSelection(g2);
		}

		_drawIcon(g2, g2.getClip());
	}

	/**
	 * This method constructs and shows <a href = "StatePropertiesDialog.html">
	 * <code>StatePropertiesDialog</code></a>
	 * <p>
	 * Updates changed State properties not only in this OpdState, but in <a
	 * href = "../opmEntities/OpmState.html"><code>OpmState</code></a> this
	 * <code>OpdState</code> represents.
	 */
	public void callPropertiesDialog(int showTabs, int showButtons) {
		StateInstance si = (StateInstance) this.getEntry().getInstance(
				new OpdKey(this.getOpdId(), this.getEntityInOpdId()));
		StatePropertiesDialog sd = new StatePropertiesDialog(si,
				this.myProject, true, showButtons);
		sd.showDialog();
	}

	/**
	 * @return <code>true</code> if the point (pX, pY) that given in coordinates
	 *         of this component is in area, that clicking and dragging mouse
	 *         will move the componenet, otherwise <code>false</code>.
	 */
	public boolean inMove(int pX, int pY) {
		OpdObject parentObject = (OpdObject) this.getParent();
		if (parentObject.isStatesAutoarrange()) {
			parentObject.setStatesAutoarrange(false);
			// return false;
		}

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		int borderSensitivity = (int) Math.round((currentSize / normalSize)
				* BORDER_SENSITIVITY);

		if ((pX > borderSensitivity) && (pY > borderSensitivity)
				&& (pX < this.getWidth() - borderSensitivity)
				&& (pY < this.getHeight() - borderSensitivity)) {
			return true;
		} else {
			return false;
		}
	}

	// public boolean contains(int pX, int pY)
	// {
	// OpdObject parentObject = (OpdObject)getParent();
	// if (parentObject.isStatesAutoarrange()) return false;
	//
	// return super.contains(pX, pY);
	// }

	/**
	 * @return <code>true</code> if the point (pX, pY) that given in coordinates
	 *         of this component is in area, that clicking and dragging mouse
	 *         will resize the componenet, otherwise <code>false</code>.
	 */
	public boolean inResize(int pX, int pY) {
		OpdObject parentObject = (OpdObject) this.getParent();
		if (parentObject.isStatesAutoarrange()) {
			return false;
		}

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		int borderSensitivity = (int) Math.round((currentSize / normalSize)
				* BORDER_SENSITIVITY);

		if (!((pX > borderSensitivity) && (pY > borderSensitivity)
				&& (pX < this.getWidth() - borderSensitivity) && (pY < this
				.getHeight()
				- borderSensitivity))) {
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
	 * @return the border (as defined in <a href = "OpdBaseComponent.html">
	 *         <code>OpdBaseComponent</code></a> where the point (pX, pY) given
	 *         in this component coordinates is, or <code>NOT_IN_BORDER</code>.
	 */
	public int whichBorder(int pX, int pY) {
		int tempWidth = this.getWidth();
		int tempHeight = this.getHeight();
		// OpdObject parentObject = (OpdObject)getParent();
		// if (parentObject.isStatesAutoarrange()) return NOT_IN_BORDER;

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
				return new Point(this.getWidth() - 2, 2);
			}

			retPoint.setLocation(this.getWidth() * pPoint.getParam(), 2);
			break;
		case OpcatConstants.S_BORDER:

			if (pPoint.getParam() < 0) {
				return new Point(2, this.getHeight() - 2);
			}
			if (pPoint.getParam() > 1) {
				return new Point(this.getWidth() - 2, this.getHeight() - 2);
			}

			retPoint.setLocation(this.getWidth() * pPoint.getParam(), this
					.getHeight() - 2);
			break;
		case OpcatConstants.E_BORDER:

			if (pPoint.getParam() < 0) {
				return new Point(this.getWidth() - 2, 2);
			}
			if (pPoint.getParam() > 1) {
				return new Point(this.getWidth() - 2, this.getHeight() - 2);
			}

			retPoint.setLocation(this.getWidth() - 2, this.getHeight()
					* pPoint.getParam());
			break;
		case OpcatConstants.W_BORDER:

			if (pPoint.getParam() < 0) {
				return new Point(2, 2);
			}
			if (pPoint.getParam() > 1) {
				return new Point(2, this.getHeight() - 2);
			}

			retPoint.setLocation(2, this.getHeight() * pPoint.getParam());
			break;
		}

		return retPoint;
	}

	public void showPopupMenu(int pX, int pY) {
		JPopupMenu jpm = new StatePopup(this.myProject, this.getInstance());
		// Point showPoint = GraphicsUtils.findPlace4Menu(this, new Point(pX,
		// pY),
		// jpm.getPreferredSize());
		// jpm.show(this, (int) showPoint.getX(), (int) showPoint.getY());
		jpm.show(this, pX, pY);
	}

	public Dimension getActualSize() {

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		Font currentFont = (Font) config.getProperty("StateFont");
		currentFont = currentFont
				.deriveFont((float) (currentFont.getSize2D() * factor));
		Integer stWidth = (Integer) config.getProperty("StateWidth");
		Integer stHeight = (Integer) config.getProperty("StateHeight");
		this.currentMetrics = this.getFontMetrics(currentFont);

		double sw = 0;
		int numOfRows = 0;

		StringTokenizer st = new StringTokenizer(this.myEntity.getName(), "\n");

		while (st.hasMoreTokens()) {
			String tString = st.nextToken();
			sw = Math.max(sw, this.currentMetrics.stringWidth(tString));
			numOfRows++;
		}

		double sh = this.currentMetrics.getAscent() * numOfRows;

		double extension = 10 * factor;
		int width = Math.max((int) (stWidth.doubleValue() * factor), (int) Math
				.round(sw * 1.2 + extension));
		int height = Math.max((int) (stHeight.doubleValue() * factor),
				(int) Math.round(sh + extension));

		return new Dimension((int) width, (int) height);

	}

	public void fitToContent() {

		Dimension dim = this.getActualSize();

		this.setSize((int) dim.getWidth(), (int) dim.getHeight());

	}

	protected void addObjects2Move() {
	}

	/**
	 * The method implicitly define the cursor type when hovering over a state.
	 * It's a walk around the draw problem.
	 * 
	 * @author Eran Toch
	 */
	public void mouseEntered(MouseEvent e) {
		switch (StateMachine.getCurrentState()) {
//		case StateMachine.USUAL_DRAW:
//			/** *********HIOTeam*************** */
//			Cursor drawCursor = CursorFactory.getDrawCursor();
//			this.setCursor(drawCursor);
//			break;
//		/** *********HIOTeam*************** */
		case StateMachine.USUAL_SELECT:
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			break;
		case StateMachine.EDIT_CUT:
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			break;
		case StateMachine.USUAL_MOVE:
			this.setCursorForLocation(e);
			break;
		default:
			super.mouseEntered(e);
		}
	}

	public void callShowUrl() {

	}
}
