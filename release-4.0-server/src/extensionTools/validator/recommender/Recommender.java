package extensionTools.validator.recommender;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPI.IThingEntry;
import exportedAPI.opcatAPI.IThingInstance;
import exportedAPI.opcatAPIx.IXSystem;
import extensionTools.validator.algorithm.Neighbor;
import extensionTools.validator.finder.FindSpecificNeighbors;
import extensionTools.validator.finder.Finder;
import extensionTools.validator.finder.GeneralizedFinder;
import gui.images.opm.OPMImages;
import gui.images.standard.StandardImages;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.Opd;
import gui.opmEntities.Constants;
import gui.projectStructure.ThingInstance;

/**
 * Generates a list of recommendations for additional connections to a given
 * thing, based on its role.
 * 
 * @author Eran Toch Created: 04/06/2004
 */
public class Recommender {
	private ThingInstance instance;

	private IThingEntry thing;

	private IXSystem system;

	private JMenu menu;

	private final static String name = "Template Advisor";

	public Recommender(IThingInstance _instance, IXSystem _system) {
		this.thing = (IThingEntry) _instance.getIEntry();
		this.instance = (ThingInstance) _instance;
		this.system = _system;
	}

	public JMenu init() {
		menu = new JMenu(name);
		menu.addMenuListener(new myListner());
		menu.setIcon(StandardImages.EMPTY);
		return menu;
	}

	public JMenu getAdvices() {
		menu.removeAll();
		Enumeration libs = this.system
				.getMetaLibraries(MetaLibrary.TYPE_POLICY);
		Enumeration roles = this.thing.getRoles(MetaLibrary.TYPE_POLICY);

		// The menu is diables if no meta-libraries exist or if the thing has no
		// roles

		// System.out.print(libs.hasMoreElements() + "  ,  "
		// + roles.hasMoreElements() + "\n");

		if (!libs.hasMoreElements() || !roles.hasMoreElements()) {
			menu.setEnabled(false);
			return menu;
		}
		int numOfAdvices = 0;
		while (roles.hasMoreElements()) {
			Role role = (Role) roles.nextElement();
			if (role.getState() == Role.LOADED) {
				try {
					ISystemStructure structure = role.getOntology()
							.getStructure();
					if (structure.getIEntry(role.getThingId()) instanceof IThingEntry) {
						IThingEntry roleEntry = (IThingEntry) structure
								.getIEntry(role.getThingId());
						Finder vThing = new Finder();
						vThing.addFinder(new GeneralizedFinder());
						vThing.addFinder(new FindSpecificNeighbors());
						Vector advises = vThing.findNeighbors(roleEntry,
								structure);
						Neighbor neighbor = null;
						HashMap<String, Integer> sizes = getTypesNumInArray(advises);

						for (int i = 0; i < advises.size(); i++) {
							neighbor = (Neighbor) advises.get(i);
							int size = gotLocalNeighbor(neighbor, system, thing);
							String type = Opd.getEntryType(neighbor
									.getConnectedThing());
							Integer numNeig = sizes.get(type);

							if (size < numNeig) {
								JMenuItem item = this
										.createItem(neighbor, role);
								menu.add(item);
								numOfAdvices++;

								numNeig--;
								sizes.remove(type);
								sizes.put(type, numNeig);
							}

						}
					}
				} catch (Exception E) {
					E.printStackTrace();
				}
			}
		}
		if (numOfAdvices == 0) {
			menu.setEnabled(false);
		}
		return menu;
	}

	private HashMap<String, Integer> getTypesNumInArray(Vector advises) {
		HashMap<String, Integer> ret = new HashMap<String, Integer>();

		for (int i = 0; i < advises.size(); i++) {
			Neighbor neighbor = (Neighbor) advises.get(i);
			String type = Opd.getEntryType(neighbor.getConnectedThing());
			if (ret.containsKey(type)) {
				Integer val = ret.get(type);
				val++;
				ret.remove(type);
				ret.put(type, val);
			} else {
				ret.put(type, 1);
			}
		}

		return ret;
	}

	private int gotLocalNeighbor(Neighbor remoteNeighbor, IXSystem localSystem,
			IThingEntry localThing) {
		int num = 0;
		ISystemStructure structure = (ISystemStructure) localSystem
				.getIXSystemStructure();
		IThingEntry roleEntry = (IThingEntry) structure.getIEntry(localThing
				.getId());
		Finder vThing = new Finder();
		vThing.addFinder(new GeneralizedFinder());
		vThing.addFinder(new FindSpecificNeighbors());
		Vector advises = vThing.findNeighbors(roleEntry, structure);
		for (int i = 0; i < advises.size(); i++) {
			Neighbor neighbor = (Neighbor) advises.get(i);
			String remoteType = Opd.getEntryType(remoteNeighbor
					.getConnectedThing());
			String localType = Opd.getEntryType(neighbor.getConnectedThing());

			if (remoteType.equalsIgnoreCase(localType)) {
				if (remoteNeighbor.getConnectionType() == remoteNeighbor
						.getConnectionType()) {
					num++;
				}

			}
		}
		return num;
	}

	/**
	 * Creates a menu item that creates a new connected thing, according to the
	 * a given role.
	 * 
	 * @param neighbor
	 *            The connected thing object
	 * @param role
	 *            The original role that the advice was created according to.
	 * @return
	 */
	protected JMenuItem createItem(Neighbor neighbor, Role role) {
		String name = Opd.getEntryType(neighbor.getConnectedThing()) + " ("
				+ neighbor.getConnectedThing().getName() + ")";
		String cName = Constants.type2String(neighbor.getConnectionType());
		Icon icon = this.iconToType(neighbor.getConnectionType());
		String message = "New " + cName + " to " + name;
		Action doAdvice = new PerformAction(neighbor, this.instance,
				this.system, role.getOntology());
		JMenuItem item = new JMenuItem(doAdvice);
		item.setText(message);
		item.setIcon(icon);
		return item;
	}

	protected Icon iconToType(int relType) {
		switch (relType) {
		case OpcatConstants.SPECIALIZATION_RELATION:
			return OPMImages.GENERALIZATION;
		case OpcatConstants.EXHIBITION_RELATION:
			return OPMImages.EXHIBITION;
		case OpcatConstants.INSTANTINATION_RELATION:
			return OPMImages.INSTANTIATION;
		case OpcatConstants.AGGREGATION_RELATION:
			return OPMImages.AGGREGATION;
		case OpcatConstants.UNI_DIRECTIONAL_RELATION:
			return OPMImages.UNI_DIRECTIONAL;
		case OpcatConstants.BI_DIRECTIONAL_RELATION:
			return OPMImages.BI_DIRECTIONAL;
		case OpcatConstants.CONSUMPTION_LINK:
			return OPMImages.RESULT_LINK;
		case OpcatConstants.EFFECT_LINK:
			return OPMImages.EFFECT_LINK;
		case OpcatConstants.INSTRUMENT_LINK:
			return OPMImages.INSTRUMENT_LINK;
		case OpcatConstants.CONDITION_LINK:
			return OPMImages.CONDITION_LINK;
		case OpcatConstants.AGENT_LINK:
			return OPMImages.AGENT_LINK;
		case OpcatConstants.RESULT_LINK:
			return OPMImages.RESULT_LINK;
		case OpcatConstants.INVOCATION_LINK:
			return OPMImages.INVOCATION_LINK;
		case OpcatConstants.INSTRUMENT_EVENT_LINK:
			return OPMImages.INSTRUMENT_EVENT_LINK;
		case OpcatConstants.EXCEPTION_LINK:
			return OPMImages.EXCEPTION_LINK;
		case OpcatConstants.CONSUMPTION_EVENT_LINK:
			return OPMImages.CONSUMPTION_EVENT_LINK;
		case OpcatConstants.OBJECT:
			return OPMImages.OBJECT;
		case OpcatConstants.PROCESS:
			return OPMImages.PROCESS;
		case OpcatConstants.STATE:
			return OPMImages.STATE;
		}
		return StandardImages.EMPTY;
	}

	class myListner implements MenuListener {

		@Override
		public void menuCanceled(MenuEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void menuDeselected(MenuEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void menuSelected(MenuEvent e) {
			getAdvices();
		}

	}
}
