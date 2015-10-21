package extensionTools.search;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

import exportedAPI.OpdKey;
import exportedAPI.opcatAPIx.IXEntry;
import exportedAPI.opcatAPIx.IXInstance;
import exportedAPI.opcatAPIx.IXObjectEntry;
import exportedAPI.opcatAPIx.IXProcessEntry;
import exportedAPI.opcatAPIx.IXStateEntry;
import exportedAPI.opcatAPIx.IXSystem;
import gui.Opcat2;
import gui.controls.FileControl;
import gui.controls.GuiControl;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.projectStructure.Entry;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

/**
 * This class is the Action of the search Button in the {@link SearchDialog } It
 * gets an OptionBase from the dialog and creates a Search algorithem by using
 * the AlgorithemFactory. this search is then used to search the project.
 * 
 * The search output is then added into the grid. a row for each instance. The
 * Instance Key is added as a tag to the grid. this tag is latter used in
 * {@link SearchAction} to move the focus to the selected line.
 * 
 * singleton
 * 
 * @author raanan
 * 
 */
public class SearchAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private String tabname = "None";

	GridPanel searchPanel = null;

	IXSystem opcat = null;

	Entry parents[];

	OptionsBase[] myOptions;

	public SearchAction(String tabname) {
		opcat = (IXSystem) FileControl.getInstance().getCurrentISystem();
		this.myOptions = new OptionsBase[1];
		this.tabname = tabname;
		this.searchPanel = this.InitSearchGridPanel();

	}

	public SearchAction(Entry parent, OptionsBase options, String tabname,
			OpdProject project) {
		this.opcat = project;
		this.myOptions = new OptionsBase[1];
		this.myOptions[0] = options;
		this.tabname = tabname;

		this.parents = new Entry[1];
		this.parents[0] = parent;
		this.searchPanel = this.InitSearchGridPanel();

	}

	public SearchAction(Entry[] parents, OptionsBase[] options, String tabname) {
		opcat = (IXSystem) FileControl.getInstance().getCurrentISystem();
		this.myOptions = options;
		this.tabname = tabname;
		this.parents = parents;
		this.searchPanel = this.InitSearchGridPanel();

	}

	public void show(boolean clearOldData) {
		if (clearOldData) {
			searchPanel.ClearData();
			searchPanel.RemoveFromExtensionToolsPanel();
		}
		actionPerformed(null);
	}

	public void actionPerformed(ActionEvent e) {

		try {
			GuiControl.getInstance().getGlassPane().setVisible(true);
			GuiControl.getInstance().getGlassPane().start();

			Vector output = new Vector();
			for (int i = 0; i < this.myOptions.length; i++) {
				// Search here
				/**
				 * TODO: Add a options factory so i could use the algo factory
				 * without the dialog, it's input should be OptionBase class
				 */
				AlgoFactory factory;
				if (this.myOptions[i] != null) {
					factory = new AlgoFactory(this.myOptions[i]);
				} else {
					factory = new AlgoFactory(SearchDialog.OptionsFactory());
				}

				AlgoInterface search = (AlgoInterface) factory.create();
				if (parents == null) {
					output.add(search.PreformSearch(null, this.opcat));
				} else {
					output.add(search.PreformSearch(parents[i], this.opcat));
				}
			}

			searchPanel.RemoveFromExtensionToolsPanel();
			searchPanel = null;
			searchPanel = InitSearchGridPanel();
			searchPanel.AddToExtensionToolsPanel();

			Iterator outIter = output.iterator();
			while (outIter.hasNext()) {
				Vector entries = (Vector) outIter.next();
				Iterator entIter = entries.iterator();
				while (entIter.hasNext()) {
					IXEntry ent = (IXEntry) entIter.next();
					this.SearchGridAddRow(ent);
				}
			}

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
		GuiControl.getInstance().getGlassPane().stop();

	}

	private GridPanel InitSearchGridPanel() {
		ArrayList<String> cols = new ArrayList<String>();
		cols.add("Thing Type");
		cols.add("OPD");
		cols.add("Thing Name");
		// cols.add("Roles") ;
		GridPanel locPanel;
		locPanel = new GridPanel(cols);
		if (opcat.hashCode() == ((IXSystem) FileControl.getInstance()
				.getCurrentISystem()).hashCode()) {
			locPanel.setInstanceTag(opcat);
		}
		locPanel.getGrid().setDuplicateRows(false);
		locPanel.getButtonPane().add(new JLabel(""));
		locPanel.getButtonPane().add(new JLabel(""));
		locPanel.getButtonPane().add(new JLabel(""));
		locPanel.setTabName(tabname);
		locPanel.doLayout();
		locPanel.getGrid().setShowRowHeader(true);
		return locPanel;
	}

	private void SearchGridAddRow(IXEntry entry) {

		// Entry entry = (Entry) ent ;

		Enumeration insEnum = entry.getInstances();
		for (; insEnum.hasMoreElements();) {
			try {
				IXInstance ins = null;
				OpdKey key = null;
				Object[] tag = new Object[2];
				Object[] row = new Object[this.searchPanel.getGrid()
						.getColumnCount()];

				ins = (IXInstance) insEnum.nextElement();
				if (ins.getKey().getOpdId() != OpdProject.CLIPBOARD_ID) {
					key = ins.getKey();
					String type = "None";
					if (entry instanceof IXProcessEntry) {
						type = "Process";
					} else if (entry instanceof IXObjectEntry) {
						type = "Object";
					} else if (entry instanceof IXStateEntry) {
						type = "State";
					} else {
						type = "Link";
					}
					row[0] = type;
					row[1] = this.opcat.getIXSystemStructure().getIXOpd(
							key.getOpdId()).getName().replaceAll("\n", " ");
					row[2] = entry.getName().replaceAll("\n", " ");

					tag[0] = key;
					tag[1] = new Long(entry.getId());

					searchPanel.getGrid().addRow(row, tag);
					searchPanel.getGrid().getTableRowHeader().addMouseListener(
							new MouseAdapter() {

								public void mouseEntered(MouseEvent e) {
									int dh = searchPanel.getGrid()
											.getRowHeight();
									dh += searchPanel.getGrid()
											.getIntercellSpacing().height;
									int h = dh;
									int rc = searchPanel.getGrid()
											.getRowCount();
									int i = 0;
									for (i = 0; i < rc; i++) {
										if (e.getY() < h)
											break;
										h += dh;
									}

									Opd opd = Opcat2.getCurrentProject()
											.getCurrentOpd();

									if (opcat instanceof OpdProject) {

										try {
											OpdProject project = (OpdProject) opcat;

											OpdKey key = (OpdKey) searchPanel
													.getGrid().GetTag(i)[0];

											Opd remoteOpd = project
													.getSystemStructure()
													.getOpd(key.getOpdId());

											String projectName = project
													.getPath();
											if (projectName.endsWith(".opz")) {
												projectName = projectName
														.substring(
																0,
																projectName
																		.length() - 4);
											}

											String imagePath = projectName
													+ FileControl.fileSeparator
													+ remoteOpd.getName() + "_"
													+ key.getOpdId() + "_"
													+ "OPDfile" + ".jpeg";

											BufferedImage bi = null;

											FileInputStream fis = new FileInputStream(
													imagePath);

											JPEGImageDecoder decoder = JPEGCodec
													.createJPEGDecoder(fis);
											bi = decoder
													.decodeAsBufferedImage();

											BufferedImage img = bi;
											if (opd.getOpdContainer()
													.getHeight() < bi
													.getHeight()) {

												img = scaleToSize(opd
														.getOpdContainer()
														.getWidth(), opd
														.getOpdContainer()
														.getHeight(), bi);
											}

											JPanel panel = new JPanel();
											ImageIcon icon = new ImageIcon(img);

											JLabel label = new JLabel(icon,
													JLabel.CENTER);
											panel.add(label);
											opd.getOpdContainer().setGlassPane(
													panel);
											opd.getOpdContainer()
													.getGlassPane().setVisible(
															true);
										} catch (Exception ex) {
											// OpcatLogger.logError(ex);
										}
									}

								}

								public void mouseExited(MouseEvent e) {
									Opd opd = Opcat2.getCurrentProject()
											.getCurrentOpd();

									opd.getOpdContainer().getGlassPane()
											.setVisible(false);
								}
							});
				}
			} catch (Exception e) {
				OpcatLogger.logError(e);
			}
		}
	}

	public GridPanel getSearchPanel() {
		return searchPanel;
	}

	private BufferedImage scaleToSize(int nMaxWidth, int nMaxHeight,
			BufferedImage imgSrc) {
		int nHeight = imgSrc.getHeight();
		int nWidth = imgSrc.getWidth();
		double scaleX = (double) nMaxWidth / (double) nWidth;
		double scaleY = (double) nMaxHeight / (double) nHeight;
		double fScale = Math.min(scaleX, scaleY);
		return scale(fScale, imgSrc);
	}

	private BufferedImage scale(double scale, BufferedImage srcImg) {
		if (scale == 1) {
			return srcImg;
		}
		AffineTransformOp op = new AffineTransformOp(AffineTransform
				.getScaleInstance(scale, scale), null);
		return op.filter(srcImg, null);
	}

}
