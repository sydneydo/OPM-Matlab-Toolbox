package gui.actions.edit;

import exportedAPI.OpdKey;
import gui.controls.EditControl;
import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.Role;
import gui.metaLibraries.logic.RolesManager;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmThing;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;
import gui.projectStructure.MainStructure;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ProcessEntry;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatLogger;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.w3c.dom.events.EventException;

public class MetaInsertAction extends EditAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Role myRole;

	public MetaInsertAction(String name, Icon icon, Role role) {
		super(name, icon);
		myRole = role;
	}

	public MetaInsertAction(Role role, Icon icon) {
		super(role.getThing().getName().replaceAll("\n", " "), icon);
		myRole = role;
	}

	public void actionPerformed(ActionEvent arg0) throws EventException {
		try {
			super.actionPerformed(arg0);
		} catch (EventException e) {
			JOptionPane.showMessageDialog(this.gui.getFrame(), e.getMessage()
					.toString(), "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}

		MetaHideAction hide = new MetaHideAction("XXX", null, false,
				StandardImages.System_Icon);
		hide.actionPerformed(null);

		MetaColoringAction color = new MetaColoringAction("xxx", null,
				StandardImages.META_COLOR_DEF);
		color.actionPerformed(null);

		Opd currentOPD = edit.getCurrentProject().getCurrentOpd();

		currentOPD.startBusyAnimation() ; 

		// get a list of things connected to the role. and paste in the current
		// location
		HashMap<OpdKey, Instance> instances = new HashMap<OpdKey, Instance>();
		Iterator<Entry> iter = Collections.list(
				edit.getCurrentProject().getSystemStructure().getElements())
				.iterator();

		MainStructure system = edit.getCurrentProject().getSystemStructure();

		while (iter.hasNext()) {
			Entry ent = (Entry) iter.next();
			if ((ent instanceof ProcessEntry) || (ent instanceof ObjectEntry)) {
				OpmThing opmThing = (OpmThing) ent.getLogicalEntity();
				RolesManager rolesManger = opmThing.getRolesManager();

				ArrayList<Entry> doInsert = new ArrayList<Entry>();
				if (rolesManger.contains(myRole)) {
					doInsert.add(ent);
				}
				if (myRole.getThing() instanceof OpmThing) {
					OpmThing opm = (OpmThing) myRole.getThing();

					ArrayList<Entry> sons = new ArrayList<Entry>();

					OpdProject project = (OpdProject) myRole.getOntology()
							.getProjectHolder();
					Entry remoteEntry = project.getSystemStructure().getEntry(
							opm.getId());
					if (remoteEntry != null) {
						sons = ((ThingEntry) remoteEntry).getAllSons();
					}

					for (int i = 0; i < sons.size(); i++) {
						Entry entry = (Entry) sons.get(i);
						try {
							Role newRole = new Role(entry.getLogicalEntity()
									.getId(), myRole.getOntology());
							if (rolesManger.contains(newRole)) {
								doInsert.add(ent);
							}
						} catch (Exception ex) {
							OpcatLogger.logError(ex);
						}
					}
				}

				if (doInsert.size() > 0) {
					for (int i = 0; i < doInsert.size(); i++) {
						Entry locEntry = (Entry) doInsert.get(i);
						Instance ins = (Instance) locEntry.getInstances()
								.nextElement();
						if (!instances.containsKey(ins.getKey())) {
							if (!currentOPD.isContainingInstanceOfEntry(ins
									.getEntry())) {
								instances.put(ins.getKey(), ins);
							}
						}
					}
				}
			}
		}

		edit.getCurrentProject().deselectAll();
		edit.getCurrentProject().clearClipBoard();
		Iterator<OpdKey> keys = instances.keySet().iterator();
		int i = 100;

		int maxHeight = 0;
		int maxWidth = 0;

		int accumulatedY = 0;

		Instance myInstance = myRole.getRoleInstanceinCurrentOPD();
		while (keys.hasNext()) {
			OpdKey key = (OpdKey) keys.next();
			Instance inst = (Instance) instances.get(key);
			inst.getOpd().getSelection().addSelection(inst, true);

			int x, y;
			if (myInstance != null) {
				x = 10; // myInstance.getGraphicalRepresentation().getX();
				y = 20 + accumulatedY; // myInstance.getGraphicalRepresentation().getY()
				// + accumulatedY;

			} else {
				x = i;
				y = i;
			}
			if (myInstance != null) {
				edit.getCurrentProject()._copy(inst.getOpd(), currentOPD, x, y,
						((ThingInstance) myInstance).getThing(), true);
			} else {
				edit.getCurrentProject()._copy(inst.getOpd(), currentOPD, x, y,
						currentOPD.getDrawingArea(), true);
			}
			Instance insertedInstance = inst;
			Enumeration<Instance> insertedInstances = system.getInstanceInOpd(
					currentOPD, inst.getEntry().getId());
			while (insertedInstances.hasMoreElements()) {
				insertedInstance = insertedInstances.nextElement();
				Point location = insertedInstance.getGraphicalRepresentation()
						.getLocation();
				((ConnectionEdgeInstance) insertedInstance).getConnectionEdge()
						.fitToContent();
				insertedInstance.getGraphicalRepresentation().setLocation(
						location);
			}
			accumulatedY = accumulatedY
					+ insertedInstance.getGraphicalRepresentation().getHeight();

			if (insertedInstance.getGraphicalRepresentation().getWidth() + 2 > maxWidth) {
				maxWidth = insertedInstance.getGraphicalRepresentation()
						.getWidth();
			}

			maxHeight = accumulatedY;

			if (myInstance != null) {
				((ThingInstance) myInstance).setTextPosition("N"); // .getGraphicalRepresentation().setSentToBAck(true);
				if ((maxHeight > myInstance.getGraphicalRepresentation()
						.getHeight())
						|| (maxWidth > myInstance.getGraphicalRepresentation()
								.getWidth())) {
					if (myInstance != null) {
						myInstance.getGraphicalRepresentation().setSize(
								new Dimension(maxWidth + 10, maxHeight + 40));
					}
				}
			}

			i += 50;
		}

		EditControl.getInstance().getCurrentProject().getCurrentOpd().refit();
		currentOPD.stopBusyAnimation() ; 
	}
}