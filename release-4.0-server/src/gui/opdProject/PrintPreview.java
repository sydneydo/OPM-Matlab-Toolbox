package gui.opdProject;

import gui.images.misc.MiscImages;
import gui.images.standard.StandardImages;
import gui.util.JToolBarButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class PrintPreview extends JFrame {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	protected double zoom_pt;

	protected Pageable my_target;

	protected JComboBox m_cbScale;

	protected PreviewContainer m_preview;

	protected PageFormat pf;

	protected PrinterJob printerJob;

	protected PageSetupData pageSetupData;

	protected final Color BACKGROUND_COLOR = new Color(132, 130, 129);

	protected final Color SHADDOW_COLOR = new Color(0, 0, 0);

	protected final Color FRAME_COLOR = new Color(51, 0, 102);

	protected final int SHADDOW_SIZE = 3;

	protected final int PAGE_OFFSET = 20;

	// protected final int

	public PrintPreview(PrinterJob pj, Pageable target, PageSetupData psd) {
		this(pj, target, "Print Preview", psd);
	}

	public PrintPreview(PrinterJob pj, Pageable target, String title,
			PageSetupData psd) {
		super(title);

		this.setIconImage(MiscImages.LOGO_SMALL_ICON.getImage());

		this.setBackground(this.BACKGROUND_COLOR);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize);
		this.my_target = target;
		this.printerJob = pj;
		this.pageSetupData = psd;
		// this.pf = pf;
		// printProps = new PrintProperties();

		JToolBar tb = new JToolBar();
		JToolBarButton bt = new JToolBarButton(this.printAction, "Print OPDs");
		bt.setText("Print  ");
		bt.setIcon(StandardImages.PRINT);
		tb.add(bt);

		String[] scales = { "50 %", "100 %", "150 %", "200 %", };
		// m_cbScale = new JToolBarCombo(scales);
		this.m_cbScale = new JComboBox(scales);

		this.m_cbScale.addActionListener(this.comboLst);
		this.m_cbScale.setMaximumSize(this.m_cbScale.getPreferredSize());
		this.m_cbScale.setEditable(true);
		tb.addSeparator();
		tb.add(this.m_cbScale);

		bt = new JToolBarButton(this.pageSetup, "Page Setup");
		bt.setText("Page Setup  ");
		bt.setIcon(StandardImages.PAGE_SETUP);
		tb.addSeparator();
		tb.add(bt);

		bt = new JToolBarButton(this.printerSetup, "Printer Setup");
		bt.setText("Printer Setup  ");
		bt.setIcon(StandardImages.PRINTER_SETUP);
		tb.addSeparator();
		tb.add(bt);

		bt = new JToolBarButton(this.closeAction, "Close");
		bt.setText("Close  ");
		bt.setIcon(StandardImages.PREVIEW_CLOSE);
		tb.addSeparator();
		tb.addSeparator();
		tb.add(bt);

		this.getContentPane().add(tb, BorderLayout.NORTH);

		this.m_preview = new PreviewContainer();
		this.m_preview.setDoubleBuffered(true);
		// m_preview.setLayout(new BoxLayout(m_preview, BoxLayout.Y_AXIS));
		this.m_preview.setLayout(null);

		for (int i = 0; i < target.getNumberOfPages(); i++) {
			this.pf = target.getPageFormat(i);
			PagePreview pp = new PagePreview(target.getPrintable(i), this.pf, i);
			pp.setVisible(false);
			// m_preview.add(Box.createVerticalStrut(PAGE_OFFSET));
			this.m_preview.add(pp);
		}

		this.m_preview.add(Box.createVerticalStrut(this.PAGE_OFFSET));
		this.m_cbScale.setSelectedItem("110 %");

		Component[] comps = this.m_preview.getComponents();

		for (int k = 0; k < comps.length; k++) {
			if ((comps[k] instanceof PagePreview)) {
				comps[k].setVisible(true);
			}
		}

		JScrollPane ps = new JScrollPane(this.m_preview);
		this.getContentPane().add(ps, BorderLayout.CENTER);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	class PreviewContainer extends JPanel implements Scrollable {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		 

		public PreviewContainer() {
			this.setBackground(PrintPreview.this.BACKGROUND_COLOR);
		}

		public Dimension getPreferredScrollableViewportSize() {
			return this.getPreferredSize();
		}

		public int getScrollableBlockIncrement(Rectangle r, int orientation,
				int direction) {
			Dimension size;

			size = this.getPreferredSize();
			if (orientation == SwingConstants.VERTICAL) {
				return (int) (size.getHeight() / (PrintPreview.this.my_target.getNumberOfPages()));
			}
			return (int) (size.getWidth() / 10);
		}

		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		public boolean getScrollableTracksViewportWidth() {
			return false;
		}

		public int getScrollableUnitIncrement(Rectangle r, int orientation,
				int direction) {
			Dimension size;

			size = this.getPreferredSize();
			if (orientation == SwingConstants.VERTICAL) {
				return (int) (size.getHeight() / (5 * PrintPreview.this.my_target
						.getNumberOfPages()));
			}
			return (int) (size.getWidth() / 50);

		}

	}

	class PagePreview extends JComponent {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		 

		protected int m_w;

		protected int m_h;

		protected Image m_source;

		protected Image m_img;

		Printable target;

		PageFormat pf;

		int pi;

		public PagePreview(Printable target, PageFormat pf, int pi) {
			this.target = target;
			this.pf = pf;
			this.pi = pi;
			this.setDoubleBuffered(true);
		}

		public PageFormat getFormat() {
			return this.pf;
		}

		public Dimension getMaximumSize() {
			return this.getPreferredSize();
		}

		public Dimension getMinimumSize() {
			return this.getPreferredSize();
		}

		public void paintComponent(Graphics g) {

			Graphics2D g2 = (Graphics2D) g;
			try {
				g2.setColor(Color.white);
				g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
				java.awt.geom.AffineTransform at = g2.getTransform();
				g2.scale(PrintPreview.this.zoom_pt, PrintPreview.this.zoom_pt);
				this.target.print(g2, this.pf, this.pi);

				g2.setTransform(at);

				// Here we draw the frame and the shaddow

				g2.setColor(PrintPreview.this.FRAME_COLOR);
				g2.drawRect(0, 0, (int) (this.pf.getWidth() * PrintPreview.this.zoom_pt - 1),
						(int) (this.pf.getHeight() * PrintPreview.this.zoom_pt - 1));

				g2.setColor(PrintPreview.this.BACKGROUND_COLOR);
				g2.fillRect((int) (this.pf.getWidth() * PrintPreview.this.zoom_pt), 0, PrintPreview.this.SHADDOW_SIZE,
						PrintPreview.this.SHADDOW_SIZE);
				g2.fillRect(0, (int) (this.pf.getHeight() * PrintPreview.this.zoom_pt), PrintPreview.this.SHADDOW_SIZE,
						PrintPreview.this.SHADDOW_SIZE);

				g2.setColor(PrintPreview.this.SHADDOW_COLOR);
				g2.fillRect((int) (this.pf.getWidth() * PrintPreview.this.zoom_pt), PrintPreview.this.SHADDOW_SIZE,
						PrintPreview.this.SHADDOW_SIZE, (int) (this.pf.getHeight() * PrintPreview.this.zoom_pt));
				g2.fillRect(PrintPreview.this.SHADDOW_SIZE, (int) (this.pf.getHeight() * PrintPreview.this.zoom_pt),
						(int) (this.pf.getWidth() * PrintPreview.this.zoom_pt), PrintPreview.this.SHADDOW_SIZE);

			} catch (Exception e) {
				System.err.println(e);
			}

		}
	}

	Action printAction = new AbstractAction("Print") {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		 

		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					PrintPreview.this.printerJob.setPageable(PrintPreview.this.my_target);

					// if (PrintPreview.this.printerJob.printDialog()) {
					try {
						PrintPreview.this.setCursor(Cursor
								.getPredefinedCursor(Cursor.WAIT_CURSOR));
						PrintPreview.this.printerJob.print();
						PrintPreview.this.setCursor(Cursor
								.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						PrintPreview.this.dispose();
					} catch (Exception ex) {
						ex.printStackTrace();
						System.err.println("Printing error: " + ex.toString());
					}
					// }

				}
			});

		}

	};

	Action pageSetup = new AbstractAction("Page Setup") {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 5486696045897025694L;

		public void actionPerformed(ActionEvent e) {
			new PageSetupDialog(PrintPreview.this, "Page Setup",
					PrintPreview.this.pageSetupData);
			if (PrintPreview.this.pageSetupData.isApplyed()) {
				PrintPreview.this.m_preview.repaint();
			}
		}
	};

	Action printerSetup = new AbstractAction("Printer Setup") {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = -6677366107530404452L;

		public void actionPerformed(ActionEvent e) {
			PrintPreview.this.printerJob.printDialog();
		}
	};

	Action closeAction = new AbstractAction("Close") {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = -149822315163376741L;

		public void actionPerformed(ActionEvent e) {
			PrintPreview.this.dispose();
		}
	};

	ActionListener comboLst = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String str = PrintPreview.this.m_cbScale.getSelectedItem().toString();
			if (str.endsWith("%")) {
				str = str.substring(0, str.length() - 1);
			}
			str = str.trim();
			int scale = 0;
			try {
				scale = Integer.parseInt(str);
			} catch (NumberFormatException ex) {
				return;
			}
			PrintPreview.this.zoom_pt = (double) scale / 100;

			int height = PrintPreview.this.PAGE_OFFSET;
			int width = (int) (PrintPreview.this.pf.getWidth() * PrintPreview.this.zoom_pt) + PrintPreview.this.SHADDOW_SIZE;

			int xLocation = 10;

			if (PrintPreview.this.getWidth() > width) {
				xLocation = (PrintPreview.this.getWidth() - width) / 2;
			}

			Component[] comps = PrintPreview.this.m_preview.getComponents();
			for (int k = 0; k < comps.length; k++) {
				if ((comps[k] instanceof PagePreview)) {
					PagePreview pp = (PagePreview) comps[k];
					PrintPreview.this.pf = pp.getFormat();
					// pp.setPreferredSize(new
					// Dimension((int)(pf.getWidth()*zoom_pt)+SHADDOW_SIZE,
					// (int)(pf.getHeight()*zoom_pt)+SHADDOW_SIZE));
					pp.setSize(new Dimension((int) (PrintPreview.this.pf.getWidth() * PrintPreview.this.zoom_pt)
							+ PrintPreview.this.SHADDOW_SIZE, (int) (PrintPreview.this.pf.getHeight() * PrintPreview.this.zoom_pt)
							+ PrintPreview.this.SHADDOW_SIZE));
					pp.setPreferredSize(new Dimension(
							(int) (PrintPreview.this.pf.getWidth() * PrintPreview.this.zoom_pt) + PrintPreview.this.SHADDOW_SIZE,
							(int) (PrintPreview.this.pf.getHeight() * PrintPreview.this.zoom_pt) + PrintPreview.this.SHADDOW_SIZE));
					pp.setLocation(xLocation, height);
					height = height + (int) (PrintPreview.this.pf.getHeight() * PrintPreview.this.zoom_pt)
							+ PrintPreview.this.PAGE_OFFSET + PrintPreview.this.SHADDOW_SIZE;

					// width= Math.max(width,
					// (int)(pf.getWidth()*zoom_pt)+SHADDOW_SIZE);

				}
			}

			width = Math.max(width, PrintPreview.this.getWidth());
			height = Math.max(height, PrintPreview.this.getHeight());

			PrintPreview.this.m_preview.setSize(new Dimension(width, height));
			PrintPreview.this.m_preview.setPreferredSize(new Dimension(width, height));
			PrintPreview.this.getContentPane().validate();
			PrintPreview.this.getContentPane().repaint();
			// m_preview.repaint();
			// doLayout();
			// repaint();

		}
	};

}
