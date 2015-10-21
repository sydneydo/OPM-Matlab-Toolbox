package gui.opdGraphics.opdBaseComponents;

import exportedAPI.OpcatConstants;
import exportedAPI.RelativeConnectionPoint;
import gui.opdGraphics.dialogs.ProcessPropertiesDialog;
import gui.opdProject.GenericTable;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmThing;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ThingEntry;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.text.AttributedString;
import java.util.StringTokenizer;

import util.Configuration;

/**
 * This class is a graphic representation of the Process as defined in OPM. It
 * can contain other OPD components as explained in <a href = "OpdThing.html"><code>OpdThing</code></a>.
 */
public class OpdProcess extends OpdThing {

	/**
	 * 
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Constructs <code>OpdProcess</code> object and initialize all defaults.
	 * </p>
	 * <strong>No default constructor for this class.</strong><br>
	 * 
	 * @param <code>pOpdId long</code>, uniqe identifier of the OPD this
	 *            component is added to.
	 * @param <code>pEntityInOpdId long</code>, uniqe identifier of the
	 *            component within OPD it is added to.
	 * @param <code>pProject OpdProject</code>, project this component is added
	 *            to.
	 * @param <code>pEntry ThingEntry</code>, the entry of the thing, this
	 *            <code>OpdThing</code> represents graphicaly.
	 * @param <code>fNode DefaultMutableTreeNode</code>, the reference to the
	 *            parent node in <a href =
	 *            "../opdProject/RepositoryManager.html"><code>RepositoryManager</code></a>
	 *            tree.
	 */
	public OpdProcess(ThingEntry pEntry, OpdProject pProject, long pOpdId,
			long pEntityInOpdId) {
		super(pOpdId, pEntityInOpdId, pProject, pEntry);
		this.borderColor = (Color) pProject.getConfiguration().getProperty(
				"ProcessColor");
	}

	/**
	 * <p>
	 * Paints the <code>OpdObject</code>
	 * </p>
	 * <p>
	 * If it has other OPD components inside, calls
	 * <code>paintComponents()</code> method and paints them.
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
			g2.setColor(this.backgroundColor.darker());
			g2.fillOval(shadowOffset, shadowOffset, this.getWidth()
					- shadowOffset - 1 - dSelOffset, this.getHeight()
					- shadowOffset - 1 - dSelOffset);
		}

		g2.setColor(this.backgroundColor);
		g2.fillOval(selOffset, selOffset, this.getActualWidth() - 1
				- dSelOffset, this.getActualHeight() - 1 - dSelOffset);

		g2.setColor(this.textColor);
		this.drawText(g2);

		g2.setColor(this.borderColor);

		// lines styles
		ProcessEntry myEntry = (ProcessEntry) this.getEntry();
		boolean zoomedIn = (myEntry.getZoomedInOpd() != null);
		boolean unfolded = (myEntry.getUnfoldingOpd() != null);

		if (zoomedIn && !unfolded) {
			g2.setStroke(new BasicStroke((float) factor * 3.5f));
			g2.setColor(borderColor);
		} else if (unfolded && !zoomedIn) {
			g2.setStroke(new BasicStroke((float) factor * 3.5f));
			g2.setColor(Color.GRAY);
		} else if (zoomedIn && unfolded) {
			g2.setStroke(new BasicStroke((float) factor * 3.5f));
			g2.setColor(Color.RED);
		} else {
			g2.setStroke(new BasicStroke((float) factor * 2.0f));
		}
		// if (this.isBoldBorder()) {
		// g2.setStroke(new BasicStroke((float) factor * 3.5f));
		// } else {
		// g2.setStroke(new BasicStroke((float) factor * 2.0f));
		// }

		if (this.isDashed()) {
			if (zoomedIn && !unfolded) {
				g2.setStroke(new BasicStroke((float) factor * 3.5f,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
						new float[] { 6 }, 0.0f));
				g2.setColor(borderColor);
			} else if (unfolded && !zoomedIn) {
				g2.setStroke(new BasicStroke((float) factor * 3.5f,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
						new float[] { 6 }, 0.0f));
				g2.setColor(Color.GRAY);
			} else if (zoomedIn && unfolded) {
				g2.setStroke(new BasicStroke((float) factor * 3.5f,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
						new float[] { 6 }, 0.0f));
				g2.setColor(Color.RED);
			} else {
				g2.setStroke(new BasicStroke((float) factor * 2.0f,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
						new float[] { 6 }, 0.0f));
			}
		}
		// if (this.isDashed()) {
		// if (this.isBoldBorder()) {
		// g2.setStroke(new BasicStroke((float) factor * 3.5f,
		// BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
		// new float[] { 6 }, 0.0f));
		// } else // !isBoldBorder()
		// {
		// g2.setStroke(new BasicStroke((float) factor * 2.0f,
		// BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
		// new float[] { 6 }, 0.0f));
		// }
		// }
		g2.drawOval(selOffset, selOffset, this.getActualWidth() - 1
				- dSelOffset, this.getActualHeight() - 1 - dSelOffset);
		g2.setStroke(new BasicStroke());
		if (this.isSelected()) {
			this.drawSelection(g2);
		}
		/**
		 * Removed from the Process graphics
		 */
		if (Configuration.getInstance().getProperty("show_roles_on_process") != null) {
			String str = Configuration.getInstance().getProperty(
					"show_roles_on_process");
			if (str.equalsIgnoreCase("yes"))
				this._drawRole(g2);
		}
		/**
		 * 
		 */
		this._drawNoOfInstances(g2);

		
		_drawIcon(g2, new Ellipse2D.Double(0, 0, this.getActualWidth() - 1
				- dSelOffset, this.getActualHeight() - 1 - dSelOffset));

	}

	/**
	 * This method constructs and shows <a href =
	 * "ProcessPropertiesDialog.html"><code>ProcessPropertiesDialog</code></a>
	 * <p>
	 * Updates changed Process properties not only in this OpdProcess, but in <a
	 * href = "../opmEntities/OpmProcess.html"><code>OpmProcess</code></a>
	 * this <code>OpdProcess</code> represents.
	 */
	public void callPropertiesDialog(int showTabs, int showButtons) {
		ProcessPropertiesDialog pd = new ProcessPropertiesDialog(this,
				(ProcessEntry) this.myEntry, this.myProject, false);
		pd.showDialog();
	}

	/**
	 * This method constructs and shows <a href =
	 * "ProcessPropertiesDialog.html"><code>ProcessPropertiesDialog</code></a>
	 * <p>
	 * Updates changed Process properties not only in this OpdProcess, but in <a
	 * href = "../opmEntities/OpmProcess.html"><code>OpmProcess</code></a>
	 * this <code>OpdProcess</code> represents.
	 * 
	 * @return Returns <code>true</code> if the dialog was "ok" pressed.
	 *         <code>false</code> if "cancel" was pressed.
	 */
	public boolean callPropertiesDialogWithFeedback(int showTabs,
			int showButtons) {
		ProcessPropertiesDialog pd = new ProcessPropertiesDialog(this,
				(ProcessEntry) this.myEntry, this.myProject, false);
		return pd.showDialog();
	}

	/**
	 * @return <code>true</code> if the point (pX, pY) that given in
	 *         coordinates of this component is in area, that clicking and
	 *         dragging mouse will move the componenet, otherwise
	 *         <code>false</code>.
	 */
	public boolean inMove(int pX, int pY) {
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		int borderSensitivity = (int) Math.round((currentSize / normalSize)
				* BORDER_SENSITIVITY);

		if (this._lengthOK(pX, pY) - this._lengthOM(pX, pY) >= borderSensitivity) {
			return true;
		}
		return false;
	}

	/**
	 * @return <code>true</code> if the point (pX, pY) that given in
	 *         coordinates of this component is in area, that clicking and
	 *         dragging mouse will resize the componenet, otherwise
	 *         <code>false</code>.
	 */
	public boolean inResize(int pX, int pY) {
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		int borderSensitivity = (int) Math.round((currentSize / normalSize)
				* BORDER_SENSITIVITY);

		if (this._lengthOK(pX, pY) - this._lengthOM(pX, pY) < borderSensitivity) {
			return true;
		}
		return false;
	}

	public boolean isInAdjacentArea(int x, int y) {
		GenericTable config = this.myProject.getConfiguration();
		Integer nSize = (Integer) config.getProperty("NormalSize");
		Integer cSize = (Integer) config.getProperty("CurrentSize");

		double proximity = 12.0 * cSize.doubleValue() / nSize.doubleValue();

		if (Math.abs(this._lengthOK(x, y) - this._lengthOM(x, y)) <= (int) proximity) {
			return true;
		}
		return false;
	}

	/**
	 * @return the border (as defined in <a href = "OpdBaseComponent.html"><code>OpdBaseComponent</code></a>
	 *         where the point (pX, pY) given in this component coordinates is,
	 *         or <code>NOT_IN_BORDER</code>.
	 */
	public int whichBorder(int pX, int pY) {
		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		int borderSensitivity = (int) Math.round((currentSize / normalSize)
				* BORDER_SENSITIVITY);

		int tempWidth = this.getActualWidth();
		int tempHeight = this.getActualHeight();

		if (this._lengthOK(pX, pY) - this._lengthOM(pX, pY) > borderSensitivity) {
			return OpcatConstants.NOT_IN_BORDER;
		}

		if ((pX >= tempWidth * 3 / 4) && (pY <= tempHeight / 4)) {
			return OpcatConstants.NE_BORDER;
		}

		if ((pX <= tempWidth / 4) && (pY <= tempHeight / 4)) {
			return OpcatConstants.NW_BORDER;
		}

		if ((pX >= tempWidth * 3 / 4) && (pY >= tempHeight * 3 / 4)) {
			return OpcatConstants.SE_BORDER;
		}

		if ((pX <= tempWidth / 4) && (pY >= tempHeight * 3 / 4)) {
			return OpcatConstants.SW_BORDER;
		}

		if ((pX >= tempWidth / 4) && (pX <= tempWidth * 3 / 4)
				&& (pY >= tempHeight * 3 / 4)) {
			return OpcatConstants.S_BORDER;
		}

		if ((pY >= tempHeight / 4) && (pY <= tempHeight * 3 / 4)
				&& (pX <= tempWidth / 4)) {
			return OpcatConstants.W_BORDER;
		}

		if ((pY >= tempHeight / 4) && (pY <= tempHeight * 3 / 4)
				&& (pX >= tempWidth * 3 / 4)) {
			return OpcatConstants.E_BORDER;
		}

		if ((pX >= tempWidth / 4) && (pX <= tempWidth * 3 / 4)
				&& (pY <= tempHeight / 4)) {
			return OpcatConstants.N_BORDER;
		}

		return OpcatConstants.NOT_IN_BORDER;

	}

	private int _lengthOK(int pX, int pY) {
		double u, v;

		double u1, v1;
		double a, b;

		if (pX - this.getActualWidth() / 2 == 0) {
			return this.getActualHeight() / 2;
		}
		if (pY - this.getActualHeight() / 2 == 0) {
			return this.getActualWidth() / 2;
		}

		a = this.getActualWidth();
		b = this.getActualHeight();
		u1 = pX - a / 2;
		v1 = pY - b / 2;

		u = (a * a) * (b * b) * (u1 * u1);
		u = u / (4 * (b * b * u1 * u1 + a * a * v1 * v1));
		u = Math.sqrt(u);
		v = u * v1 / u1;
		return (int) Math.sqrt(u * u + v * v);
	}

	private int _lengthOM(int pX, int pY) {
		double u1, v1;

		u1 = (pX - (this.getActualWidth() / 2));
		v1 = (pY - (this.getActualHeight() / 2));
		return (int) Math.sqrt(u1 * u1 + v1 * v1);
	}

	/**
	 * <p>
	 * Checks whether this component "contains" the specified point, where x and
	 * y are defined to be relative to the coordinate system of this component.
	 * </p>
	 * <p>
	 * The function will return <code>true</code> if given point is inside
	 * Processes elipse.
	 * </p>
	 * 
	 * @param <code>x</code> the x coordinate of the point.
	 * @param <code>y</code> the y coordinate of the point.
	 */
	public boolean contains(int pX, int pY) {
		if ((pX < 0) || (pX > this.getActualWidth()) || (pY < 0)
				|| (pY > this.getActualHeight())) {
			return false;
		}

		if (this._lengthOK(pX, pY) - this._lengthOM(pX, pY) >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * @return A point of connection according to <code>pSide</code> and
	 *         <code>parem</code> arguments
	 * @param <code>pSide</code> -- the side of OPD graphic component
	 * @param <code>param</code> -- The relation of connection point to length of
	 *            the given side
	 */
	public Point getAbsoluteConnectionPoint(RelativeConnectionPoint pPoint) {
		if (pPoint.getParam() < 0) {
			return new Point(2, this.getActualHeight() / 2);
		}
		if (pPoint.getParam() > 1) {
			return new Point(this.getActualWidth() - 2,
					this.getActualHeight() / 2);
		}

		Point retPoint = new Point(0, 0);
		double x, y, a, b, p; // We compute the x,y,a,b relatively to center
		// of ellipse

		p = pPoint.getParam() - 0.5;
		b = this.getActualHeight() / 2;
		a = this.getActualWidth() / 2;

		x = a * 2 * p;
		y = Math.sqrt((1 - (x * x) / (a * a)) * (b * b));

		switch (pPoint.getSide()) {
		case OpcatConstants.N_BORDER: {
			retPoint.setLocation(a * 2 * pPoint.getParam(), b - y + 2);
			break;
		}
		case OpcatConstants.S_BORDER: {
			retPoint.setLocation(a * 2 * pPoint.getParam(), b + y - 2);
			break;
		}
		case OpcatConstants.E_BORDER:
			// System.err.println("E border error");
			retPoint.setLocation(this.getActualWidth(), this.getActualHeight()
					* pPoint.getParam());
			break;
		case OpcatConstants.W_BORDER:
			// System.err.println("W border error");
			retPoint.setLocation(0, this.getActualHeight() * pPoint.getParam());
			break;
		}

		return retPoint;
	}

	private void drawText(Graphics2D g2) {
		int stringX = 0;
		int stringY = 0;
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

		double aw = this.getActualWidth();
		double ah = this.getActualHeight();
		Point tempPoint;

		String head = "";

		if (((OpmThing) this.myEntity).getScope()
				.equals(OpcatConstants.PRIVATE)) {
			head = "- ";
		}

		if (((OpmThing) this.myEntity).getScope().equals(
				OpcatConstants.PROTECTED)) {
			head = "# ";
		}

		StringTokenizer st = new StringTokenizer(
				head + this.myEntity.getName(), "\n");

		int numOfRows = 0;
		double sw = 0;

		while (st.hasMoreTokens()) {
			String tString = st.nextToken();
			sw = Math.max(sw, this.currentMetrics.stringWidth(tString));
			numOfRows++;
		}

		st = new StringTokenizer(head + this.myEntity.getName(), "\n");
		int dY = 0;

		if (this.getTextPosition().equals("NW")) {
			tempPoint = this
					.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
							OpcatConstants.N_BORDER, 0.25));
			stringX = (int) tempPoint.getX();
			stringY = (int) (tempPoint.getY() + 3);

			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);
				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(), stringX, stringY + dY);
			}

			return;

		}

		if (this.getTextPosition().equals("N")) {
			tempPoint = this
					.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
							OpcatConstants.N_BORDER, (aw - sw) / (2 * aw)));
			stringX = (int) tempPoint.getX();
			stringY = (int) (tempPoint.getY() + 3);

			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);
				stringX = ((int) aw - this.currentMetrics.stringWidth(tString) - 3) / 2;
				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(), stringX, stringY + dY);
			}

			return;

		}

		if (this.getTextPosition().equals("NE")) {
			tempPoint = this
					.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
							OpcatConstants.N_BORDER, 0.75));
			stringX = (int) (tempPoint.getX() - sw);
			stringY = (int) (tempPoint.getY() + 3);

			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);
				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(),
						stringX
								+ (int) (sw - this.currentMetrics
										.stringWidth(tString)), stringY + dY);
			}

			return;
		}

		if (this.getTextPosition().equals("W")) {
			stringX = 8;

			stringY = ((int) ah - this.currentMetrics.getAscent() * numOfRows - 3) / 2;

			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);

				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(), stringX, stringY + dY);
			}

			return;

		}

		if (this.getTextPosition().equals("C")) {

			stringY = ((int) ah - this.currentMetrics.getAscent() * numOfRows - 3) / 2;

			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);

				stringX = ((int) aw - this.currentMetrics.stringWidth(tString) - 3) / 2;
				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(), stringX, stringY + dY);
			}

			return;

		}

		if (this.getTextPosition().equals("E")) {
			stringX = (int) (aw - sw - 10);
			stringY = ((int) ah - this.currentMetrics.getAscent() * numOfRows - 3) / 2;

			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);

				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(),
						stringX
								+ (int) (sw - this.currentMetrics
										.stringWidth(tString)), stringY + dY);
			}

			return;

		}

		if (this.getTextPosition().equals("SW")) {
			tempPoint = this
					.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
							OpcatConstants.S_BORDER, 0.25));
			stringX = (int) tempPoint.getX();
			stringY = (int) (tempPoint.getY() - 6 - this.currentMetrics
					.getAscent()
					* numOfRows);

			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);
				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(), stringX, stringY + dY);
			}

			return;

		}

		if (this.getTextPosition().equals("S")) {
			tempPoint = this
					.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
							OpcatConstants.S_BORDER, (aw - sw) / (2 * aw)));

			stringY = (int) (tempPoint.getY() - 6 - this.currentMetrics
					.getAscent()
					* numOfRows);

			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);
				stringX = ((int) aw - this.currentMetrics.stringWidth(tString) - 3) / 2;
				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(), stringX, stringY + dY);
			}

			return;

		}

		if (this.getTextPosition().equals("SE")) {
			tempPoint = this
					.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
							OpcatConstants.S_BORDER, 0.75));
			stringX = (int) (tempPoint.getX() - sw);
			stringY = (int) (tempPoint.getY() - 6 - this.currentMetrics
					.getAscent()
					* numOfRows);

			while (st.hasMoreTokens()) {
				String tString = st.nextToken();
				AttributedString ats = new AttributedString(tString);
				ats.addAttribute(TextAttribute.FONT, currentFont);
				dY = dY + this.currentMetrics.getAscent();
				g2.drawString(ats.getIterator(),
						stringX
								+ (int) (sw - this.currentMetrics
										.stringWidth(tString)), stringY + dY);
			}

			return;

		}

	}

	private void _drawRole(Graphics2D g2) {
		String role = ((OpmThing) this.myEntity).getPoliciesRole();
		if (role == null) {
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

		Point tempPoint = this
				.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
						OpcatConstants.N_BORDER, 0.2));
		stringY = (int) (this.currentMetrics.getHeight() + tempPoint.getY() - 3 * factor);
		stringX = (int) (tempPoint.getX());
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

		Point tempPoint = this
				.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
						OpcatConstants.S_BORDER, 0.8));
		stringY = (int) (tempPoint.getY() - 3 * factor);
		stringX = (int) (tempPoint.getX() - this.currentMetrics
				.stringWidth(noi));
		g2.drawString(noi, stringX, stringY);
		return;
	}

	public Dimension getActualSize() {

		if (this.isZoomedIn()) {
			return new Dimension(this.getWidth(), this.getHeight());
		}

		GenericTable config = this.myProject.getConfiguration();
		Integer oWidth = (Integer) config.getProperty("ThingWidth");
		Integer oHeight = (Integer) config.getProperty("ThingHeight");

		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		Font currentFont = (Font) this.myProject.getConfiguration()
				.getProperty("ThingFont");
		currentFont = currentFont
				.deriveFont((float) (currentFont.getSize2D() * factor));
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

		sh = sh / 2;
		sw = sw / 2;
		double k = 12.0 / 7.0;

		double b = Math.sqrt((sw * sw) / (k * k) + sh * sh);
		double a = b * k;
		double extension = 15 * factor;

		double width = Math.max(oWidth.doubleValue() * factor * 3 / 4, 2 * a
				* 1.2 + extension);
		double height = Math.max(oHeight.doubleValue() * factor * 3 / 4, 2 * b
				* 1.2 + extension);

		return new Dimension((int) width, (int) height);

	}

	public void fitToContent() {
		if (this.isZoomedIn()) {
			return;
		}

		Dimension dim = this.getActualSize();

		this.setActualSize((int) dim.getWidth(), (int) dim.getHeight());
	}

	public void drawSelection(Graphics2D g) {
		Stroke oldStroke = g.getStroke();

		g.setStroke(new BasicStroke());

		Point p;

		g.setColor(Color.white);

		GenericTable config = this.myProject.getConfiguration();
		double currentSize = ((Integer) config.getProperty("CurrentSize"))
				.doubleValue();
		double normalSize = ((Integer) config.getProperty("NormalSize"))
				.doubleValue();
		double factor = currentSize / normalSize;

		int selOffset = Math.round((float) (SELECTION_OFFSET * factor));
		int dSelOffset = 2 * selOffset;

		p = this.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
				OpcatConstants.N_BORDER, 0.2));
		g.fillRect((int) p.getX() + 1 - selOffset, (int) p.getY() - selOffset,
				dSelOffset, dSelOffset);
		g.fillRect(this.getActualWidth() / 2 - selOffset, 0, dSelOffset,
				dSelOffset);
		p = this.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
				OpcatConstants.N_BORDER, 0.8));
		g.fillRect((int) p.getX() + 1 - selOffset, (int) p.getY() + 1
				- selOffset, dSelOffset, dSelOffset);
		g.fillRect(0, this.getActualHeight() / 2 - selOffset, dSelOffset,
				dSelOffset);
		p = this.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
				OpcatConstants.S_BORDER, 0.2));
		g.fillRect((int) p.getX() + 1 - selOffset, (int) p.getY() - 1
				- selOffset, dSelOffset, dSelOffset);
		g.fillRect(this.getActualWidth() / 2 - selOffset, this
				.getActualHeight()
				- 1 - dSelOffset, dSelOffset, dSelOffset);
		p = this.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
				OpcatConstants.S_BORDER, 0.8));
		g.fillRect((int) p.getX() - selOffset, (int) p.getY() - 1 - selOffset,
				dSelOffset, dSelOffset);
		g.fillRect(this.getActualWidth() - 1 - dSelOffset, this
				.getActualHeight()
				/ 2 - selOffset, dSelOffset, dSelOffset);

		g.setColor(Color.black);

		p = this.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
				OpcatConstants.N_BORDER, 0.2));
		g.drawRect((int) p.getX() + 1 - selOffset, (int) p.getY() - selOffset,
				dSelOffset, dSelOffset);
		g.drawRect(this.getActualWidth() / 2 - selOffset, 0, dSelOffset,
				dSelOffset);
		p = this.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
				OpcatConstants.N_BORDER, 0.8));
		g.drawRect((int) p.getX() + 1 - selOffset, (int) p.getY() + 1
				- selOffset, dSelOffset, dSelOffset);
		g.drawRect(0, this.getActualHeight() / 2 - selOffset, dSelOffset,
				dSelOffset);
		p = this.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
				OpcatConstants.S_BORDER, 0.2));
		g.drawRect((int) p.getX() + 1 - selOffset, (int) p.getY() - 1
				- selOffset, dSelOffset, dSelOffset);
		g.drawRect(this.getActualWidth() / 2 - selOffset, this
				.getActualHeight()
				- 1 - dSelOffset, dSelOffset, dSelOffset);
		p = this.getAbsoluteConnectionPoint(new RelativeConnectionPoint(
				OpcatConstants.S_BORDER, 0.8));
		g.drawRect((int) p.getX() - selOffset, (int) p.getY() - 1 - selOffset,
				dSelOffset, dSelOffset);
		g.drawRect(this.getActualWidth() - 1 - dSelOffset, this
				.getActualHeight()
				/ 2 - selOffset, dSelOffset, dSelOffset);

		g.setStroke(oldStroke);
	}

	// public void callShowUrl()
	// {
	// try {
	// BrowserLauncher.openURL(this.myEntity.getUrl());
	// } catch (IOException e ) {
	//			
	// }
	// }

}
