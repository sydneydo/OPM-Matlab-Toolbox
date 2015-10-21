package gui.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

// Small toolbar button without text, only icon
public class JToolBarCombo extends JComboBox implements MouseListener {
	 

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

	public JToolBarCombo(Object items[]) {
		super(items);
		this.setBorder(m_inactive);
		this.addMouseListener(this);
		this.setRequestFocusEnabled(false);
	}

	public float getAlignmentY() {
		return 0.5f;
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		this.setBorder(m_inactive);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		if (this.isEnabled()) {
			this.setBorder(m_lowered);
		}
	}

	public void mouseExited(MouseEvent e) {
		this.setBorder(m_inactive);
	}
}
