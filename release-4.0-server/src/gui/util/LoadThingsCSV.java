package gui.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import exportedAPI.OpcatConstants;
import exportedAPI.RelativeConnectionPoint;

import gui.Opcat2;
import gui.dataProject.CSVFileLoader;
import gui.opdProject.Opd;
import gui.opmEntities.OpmThing;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.util.opcatGrid.GridPanel;

/**
 * This class will load a CSV in the form - ThingName,Thing Type(Process,
 * Object),Phys?,Env? into the current OPD.
 * 
 * @author raanan
 * 
 */
public class LoadThingsCSV {

	File file;

	public LoadThingsCSV(File file) {
		this.file = file;
	}

	public void load() {

		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("Line");
		colNames.add("Name");
		colNames.add("Type");
		colNames.add("Description");
		colNames.add("Phys ?");
		colNames.add("Env ?");
		colNames.add("Data Type");
		colNames.add("Data Length");
		colNames.add("parentname");
		colNames.add("abstracttype");
		// parent has to be before the children in order for it to work
		colNames.add("Error String");

		GridPanel gridPanel = new GridPanel(colNames);
		gridPanel.setTabName("Loading Errors");
		gridPanel.RemoveFromExtensionToolsPanel();

		CSVFileLoader loader = new CSVFileLoader(file.getPath());

		Iterator iter = loader.getRowsIterator();

		int i = 50;
		int line = 0;
		Opd startingOpd = Opcat2.getCurrentProject().getCurrentOpd();

		while (iter.hasNext()) {
			Vector row = (Vector) iter.next();
			line++;
			try {
				String name = (String) row.elementAt(0);
				String type = (String) row.elementAt(1);
				String description = (String) row.elementAt(2);
				String phys = (String) row.elementAt(3);
				String env = (String) row.elementAt(4);
				String dataType = (String) row.elementAt(5);
				Object obj = row.elementAt(6);
				String parentName = (String) row.elementAt(7);
				String abstractType = (String) row.elementAt(8);

				String dataTypeLength;
				if (obj != null) {
					dataTypeLength = (String) row.elementAt(6);
				} else {
					dataTypeLength = "0";
				}

				ThingInstance thing = null;
				ThingInstance parentInstance = null;
				RelativeConnectionPoint sourcePoint = null;
				RelativeConnectionPoint targetPoint = null;
				ThingEntry parent = null;
				Opd zoom = startingOpd;

				if ((parentName != null)
						&& !(parentName.equalsIgnoreCase("none"))) {

					parent = (ThingEntry) Opcat2.getCurrentProject()
							.getEntryByName(parentName);

					if (parent != null) {

						// connect as attribute
						sourcePoint = new RelativeConnectionPoint(
								OpcatConstants.S_BORDER, 0.5);
						targetPoint = new RelativeConnectionPoint(
								OpcatConstants.N_BORDER, 0.5);
						parentInstance = (ThingInstance) parent
								.getInstanceInOPD(startingOpd);

						zoom = parent.getUnfoldingOpd();
						if (zoom == null) {
							Opcat2.getCurrentProject().showOPD(
									startingOpd.getOpdId());
							zoom = Opcat2.getCurrentProject().unfolding(
									parentInstance, true, true, true, false);

							zoom.setView(true);

						}
					} else {
						zoom = startingOpd;
					}

				}

				Opcat2.getCurrentProject().showOPD(zoom.getOpdId());
				zoom.refit();

				if (type.equalsIgnoreCase("object")) {

					parentInstance = zoom.getMainInstance();

					thing = Opcat2.getCurrentProject().addObject(i, i,
							zoom.getDrawingArea(), -1, -1, false);

				}

				if (type.equalsIgnoreCase("process")) {
					thing = Opcat2.getCurrentProject().addProcess(
							i,
							i,
							Opcat2.getCurrentProject().getCurrentOpd()
									.getDrawingArea(), -1, -1);
				}

				if (parent != null) {

					if ((parent instanceof ObjectEntry)
							|| (thing instanceof ObjectInstance)) {
						FundamentalRelationInstance fr = Opcat2
								.getCurrentProject().addRelation(
										parentInstance.getConnectionEdge(),
										sourcePoint, thing.getConnectionEdge(),
										targetPoint,
										OpcatConstants.EXHIBITION_RELATION,
										zoom.getDrawingArea(), -1, -1);
					} else {
						FundamentalRelationInstance fr = Opcat2
								.getCurrentProject().addRelation(
										parentInstance.getConnectionEdge(),
										sourcePoint, thing.getConnectionEdge(),
										targetPoint,
										OpcatConstants.AGGREGATION_RELATION,
										zoom.getDrawingArea(), -1, -1);
					}
				}

				if (thing != null) {

					thing.getEntry().setName(name);

					if (env.equalsIgnoreCase("yes")) {
						thing.getEntry().setEnvironmental(true);
					}

					if (phys.equalsIgnoreCase("yes")) {
						((OpmThing) thing.getEntry().getLogicalEntity())
								.setPhysical(true);
					}

					thing.getEntry().setDescription(description);
					if (thing instanceof ObjectInstance) {
						if ((getType(dataType, dataTypeLength) != null)
								&& (!getType(dataType, dataTypeLength)
										.equalsIgnoreCase(""))) {
							((ObjectEntry) thing.getEntry()).setType(getType(
									dataType, dataTypeLength));
							((ObjectEntry) thing.getEntry())
									.setTypeOriginId(getTypeOriginId(dataType,
											dataTypeLength));
						}
					}

					thing.getConnectionEdge().fitToContent();
					i += 5;
				} else {
					Object[] errorRow = new Object[colNames.size()];
					errorRow[0] = new Long(line + 2);

					for (int count = 0; count < colNames.size() - 2; count++) {
						errorRow[count + 1] = row.elementAt(count);
					}
					if (row.elementAt(1) != null) {
						errorRow[8] = "can not create " + row.elementAt(1)
								+ " Thing";
					} else {
						errorRow[8] = "can not create Thing";
					}
					gridPanel.getGrid().addRow(errorRow, null);
				}
			} catch (Exception ex) {
				// report fail on object.
				Object[] errorRow = new Object[colNames.size()];
				errorRow[0] = new Long(line + 2);

				for (int count = 0; count < colNames.size() - 2; count++) {
					try {
						errorRow[count + 1] = row.elementAt(count);
					} catch (Exception errorEx) {
						errorRow[count + 1] = "Problem !!!";
						if (errorRow[colNames.size() - 1] == null) {
							errorRow[colNames.size() - 1] = "Index "
									+ (count + 1) + ", ";
						} else {
							errorRow[colNames.size() - 1] = errorRow[colNames
									.size() - 1]
									+ "Index " + (count + 1) + ", ";
						}
					}
				}
				gridPanel.getGrid().addRow(errorRow, null);
			}
		}
		if (gridPanel.getGrid().getRowCount() > 0)
			gridPanel.AddToExtensionToolsPanel();
		Opcat2.getCurrentProject().setCanClose(false);
		Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
		Opcat2.getCurrentProject().getCurrentOpd().refit();
		Opcat2.getCurrentProject().getCurrentOpd().getDrawingArea().repaint();
	}

	private boolean _checkCharVectorType(String length) {
		int val;
		try {
			val = Integer.parseInt(length);
		} catch (NumberFormatException nfe) {
			return false;
		}
		if (val < 0) {
			return false;
		}
		return true;
	}

	private int getTypeOriginId(String type, String length) {
		// / private final String compound[] = { "char", "date", "time",
		// "integer" };

		// private final String basic[] = { "Boolean", "char", "short",
		// "integer",
		// "unsigned integer", "long", "float", "double" };

		if (type.equalsIgnoreCase("char")) {
			return 0;
		}

		if (type.equalsIgnoreCase("integer")) {
			return 3;
		}

		if (type.equalsIgnoreCase("time")) {
			return 2;
		}

		if (type.equalsIgnoreCase("date")) {
			return 1;
		}

		if (type.equalsIgnoreCase("Boolean")) {
			return 0;
		}
		if (type.equalsIgnoreCase("short")) {
			return 2;
		}
		if (type.equalsIgnoreCase("unsigned integer")) {
			return 4;
		}
		if (type.equalsIgnoreCase("long")) {
			return 5;
		}
		if (type.equalsIgnoreCase("float")) {
			return 6;
		}
		if (type.equalsIgnoreCase("double")) {
			return 7;
		}

		return -1;
	}

	private String getType(String type, String length) {

		if (type.equalsIgnoreCase("char")) {
			if (!this._checkCharVectorType(length)) {
				return null;
			}
			return "char[" + length + "]";
		}

		if (type.equalsIgnoreCase("integer")) {
			if (!this._checkCharVectorType(length)) {
				return null;
			}
			return "INT" + length;
		}

		if (type.equalsIgnoreCase("date")) {
			return "date";
		}

		if (type.equalsIgnoreCase("time")) {
			return "time";
		}

		if (type.equalsIgnoreCase("Boolean")) {
			return "Boolean";
		}
		if (type.equalsIgnoreCase("short")) {
			return "short";
		}
		if (type.equalsIgnoreCase("unsigned integer")) {
			return "unsigned integer";
		}
		if (type.equalsIgnoreCase("long")) {
			return "long";
		}
		if (type.equalsIgnoreCase("float")) {
			return "float";
		}
		if (type.equalsIgnoreCase("double")) {
			return "double";
		}

		return null;
	}
}
