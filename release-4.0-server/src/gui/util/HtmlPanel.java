package gui.util;

import gui.images.browser.BrowserImages;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

public class HtmlPanel extends JPanel {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	 

	private JEditorPane html;

	private Stack forward, backward;

	private JComboBox urlBar;

	private Cursor waitCursor, defaultCursor;

	private JToolBarButton forwardButton, backwardButton;

	private boolean goMutex;

	public HtmlPanel(String defaultURL) throws OpcatException {
		this.setLayout(new BorderLayout());
		this.forward = new Stack();
		this.backward = new Stack();
		this.waitCursor = new Cursor(Cursor.WAIT_CURSOR);
		this.defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		this.goMutex = true;
		this.urlBar = new JComboBox();
		this.urlBar.setEditable(false);
		this.urlBar.addItem(defaultURL.toString());
		this.urlBar.addActionListener(this.goAction);
		this.add(this.ConstructToolBar(), BorderLayout.NORTH);
		try {
			URL url = null;
			try {
				url = new URL(defaultURL);
			} catch (java.net.MalformedURLException e) {
				url = null;
				throw new OpcatException("Cannot open " + defaultURL);
				// JOptionPane.showMessageDialog(null, "Cannot open " +
				// defaultURL, "URL Error" ,JOptionPane.ERROR_MESSAGE);
			}
			if (url != null) {
				this.html = new JEditorPane(url);
				this.html.setEditable(false);
				this.html.addHyperlinkListener(this.createHyperLinkListener());
				JScrollPane scroller = new JScrollPane();
				JViewport vp = scroller.getViewport();
				vp.add(this.html);
				this.add(scroller, BorderLayout.CENTER);
			}
		} catch (MalformedURLException e) {
			throw new OpcatException("URL is not valid: " + defaultURL);
		} catch (IOException e) {
			throw new OpcatException("URL is not valid: " + defaultURL);
		}
	}

	// construct navigation toolbar
	private JToolBar ConstructToolBar() {
		JToolBar tb = new JToolBar();
		this.backAction.setEnabled(false);
		this.forwardAction.setEnabled(false);
		this.backwardButton = new JToolBarButton(this.backAction,
				"Return to Previous Page");
		this.backwardButton.setEnabled(false);
		tb.add(this.backwardButton);
		this.forwardButton = new JToolBarButton(this.forwardAction, "Go to NextPage");
		this.forwardButton.setEnabled(false);
		tb.add(this.forwardButton);
		// tb.add(new JToolBarButton(stopAction, "Stop Loading"));
		// tb.add(new JToolBarButton(searchAction, "Search Hep for..."));
		tb.add(new JToolBar.Separator());
		tb.add(this.urlBar);
		this.urlBar.setSize(
				(int) (tb.getSize().getWidth() - 100 - tb.getMargin().left
						- tb.getMargin().right - (new JToolBar.Separator())
						.getSize().getWidth()), (int) (tb.getSize().getHeight()
						- tb.getMargin().top - tb.getMargin().bottom));
		// tb.add(new JToolBarButton(goAction, "Jump to this URL"));
		return tb;
	}

	// navigation toolbar actions
	Action backAction = new AbstractAction("Back", BrowserImages.BACK) {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		 

		public void actionPerformed(ActionEvent e) {
			try {

				HtmlPanel.this.forward.push(HtmlPanel.this.html.getPage());

				URL currUrl = (URL) HtmlPanel.this.backward.pop();

				HtmlPanel.this.html.setPage(currUrl);
				HtmlPanel.this.goMutex = false;
				HtmlPanel.this.urlBar.setSelectedItem(currUrl.toString());
				HtmlPanel.this.goMutex = true;

				HtmlPanel.this.forwardButton.setEnabled(true);
				HtmlPanel.this.forwardAction.setEnabled(true);
				if (HtmlPanel.this.backward.isEmpty()) {
					HtmlPanel.this.backAction.setEnabled(false);
					HtmlPanel.this.backwardButton.setEnabled(false);
				} else {
					HtmlPanel.this.backAction.setEnabled(true);
					HtmlPanel.this.backwardButton.setEnabled(true);
				}
			} catch (IOException ioe) {
				System.out.println("IOE: " + ioe);
			}
		}
	};

	Action forwardAction = new AbstractAction("Forward", BrowserImages.FORWARD) {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = -7077454383563428101L;

		public void actionPerformed(ActionEvent e) {
			try {
				HtmlPanel.this.backward.push(HtmlPanel.this.html.getPage());
				HtmlPanel.this.backwardButton.setEnabled(true);
				HtmlPanel.this.backAction.setEnabled(true);

				HtmlPanel.this.html.setPage((URL) HtmlPanel.this.forward.peek());

				HtmlPanel.this.goMutex = false;
				HtmlPanel.this.urlBar.setSelectedItem(((URL) HtmlPanel.this.forward.pop()).toString());
				HtmlPanel.this.goMutex = true;
				if (HtmlPanel.this.forward.isEmpty()) {
					HtmlPanel.this.forwardAction.setEnabled(false);
					HtmlPanel.this.forwardButton.setEnabled(false);
				} else {
					HtmlPanel.this.forwardAction.setEnabled(true);
					HtmlPanel.this.forwardButton.setEnabled(true);
				}
			} catch (IOException ioe) {
				System.out.println("IOE: " + ioe);
			}

		}
	};

	Action stopAction = new AbstractAction("Stop", BrowserImages.STOP) {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = -9113526041136987084L;

		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null,
					"This option is not implemented yet", "Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
	};

	Action searchAction = new AbstractAction("Search", BrowserImages.SEARCH) {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = 3635195510079078514L;

		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null,
					"This option is not implemented yet", "Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
	};

	Action goAction = new AbstractAction("Go", BrowserImages.GO) {
		 

		/**
		 * 
		 */
		private static final long serialVersionUID = -4251189557839837221L;

		public void actionPerformed(ActionEvent e) {
			if (!HtmlPanel.this.goMutex) {
				return;
			}

			try {
				if (!HtmlPanel.this.html.getPage().equals(
						new URL((String) HtmlPanel.this.urlBar.getSelectedItem()))) {
					HtmlPanel.this.backward.push(HtmlPanel.this.html.getPage());

					HtmlPanel.this.setCursor(HtmlPanel.this.waitCursor);
					/** *************************************************************** */
					HtmlPanel.this.html.setPage(new URL((String) HtmlPanel.this.urlBar.getSelectedItem()));
					/** *************************************************************** */
					HtmlPanel.this.setCursor(HtmlPanel.this.defaultCursor);
					HtmlPanel.this.forward.clear();
					HtmlPanel.this.backwardButton.setEnabled(true);
					HtmlPanel.this.backAction.setEnabled(true);
					HtmlPanel.this.forwardButton.setEnabled(false);
					HtmlPanel.this.forwardAction.setEnabled(false);
				}
			} catch (IOException ioe) {
				HtmlPanel.this.backward.pop();
				System.out.println("IOE: " + ioe);
			}
		}
	};

	public HyperlinkListener createHyperLinkListener() {
		return new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				final HyperlinkEvent tempE = e;

				if (tempE.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if (tempE instanceof HTMLFrameHyperlinkEvent) {
						((HTMLDocument) HtmlPanel.this.html.getDocument())
								.processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent) tempE);
					} else {
						HtmlPanel.this.setCursor(HtmlPanel.this.waitCursor);

						if (!HtmlPanel.this.html.getPage().equals(tempE.getURL())) {
							HtmlPanel.this.backward.push(HtmlPanel.this.html.getPage());

							try {
								HtmlPanel.this.html.setPage(tempE.getURL());

								HtmlPanel.this.urlBar.addItem(tempE.getURL().toString());

								if (HtmlPanel.this.urlBar.getItemCount() > 10) {
									HtmlPanel.this.urlBar
											.removeItemAt(HtmlPanel.this.urlBar.getItemCount() - 1);
								}

								HtmlPanel.this.goMutex = false;
								HtmlPanel.this.urlBar.setSelectedItem(tempE.getURL()
										.toString());
								HtmlPanel.this.goMutex = true;
								HtmlPanel.this.setCursor(HtmlPanel.this.defaultCursor);
								HtmlPanel.this.forward.clear();
								HtmlPanel.this.backwardButton.setEnabled(true);
								HtmlPanel.this.backAction.setEnabled(true);
								HtmlPanel.this.forwardButton.setEnabled(false);
								HtmlPanel.this.forwardAction.setEnabled(false);
							} catch (IOException ioe) {
								HtmlPanel.this.backward.pop();
								HtmlPanel.this.setCursor(HtmlPanel.this.defaultCursor);
								System.out.println("IOE: " + ioe);
							}
						}
					}
				}
			}
		};
	}

}
