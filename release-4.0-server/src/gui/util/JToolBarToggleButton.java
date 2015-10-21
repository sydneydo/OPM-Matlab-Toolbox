package gui.util;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

// Small toolbar button without text, only icon
public class JToolBarToggleButton extends JToggleButton implements
		MouseListener {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	protected static final BevelBorder m_raised = new BevelBorder(
			BevelBorder.RAISED);

	protected static final BevelBorder m_lowered = new BevelBorder(
			BevelBorder.LOWERED);

	protected static final EtchedBorder m_inactive = new EtchedBorder();

	protected static final Insets myInsets = new Insets(2, 2, 2, 2);

	protected int state;

	protected String pressedTip;

	protected String notPressedTip;

	public JToolBarToggleButton(Action act, String pressedTip,
			String notPressedTip) {
		super((Icon) act.getValue(Action.SMALL_ICON));
		this.setSelected(false);
		this.addActionListener(act);
		this.addMouseListener(this);
		this.setRequestFocusEnabled(false);
		this.pressedTip = pressedTip;
		this.notPressedTip = notPressedTip;
		this.setContentAreaFilled(false);
	}

	public float getAlignmentY() {
		return 0.5f;
	}

	public void mousePressed(MouseEvent e) {
	}

	public void setSelected(boolean b) {
		super.setSelected(b);
		if (b) {
			this.setToolTipText(this.pressedTip);
			this.setBorder(m_lowered);
		} else {
			this.setToolTipText(this.notPressedTip);
			this.setBorder(m_inactive);
		}
	}

	public void mouseReleased(MouseEvent e) {
		this.setSelected(this.isSelected());
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		if (this.isEnabled()) {
			if (!this.isSelected()) {
				this.setBorder(m_raised);
			}
		}
	}

	public void mouseExited(MouseEvent e) {
		if (!this.isSelected()) {
			this.setBorder(m_inactive);
		}
	}
}
