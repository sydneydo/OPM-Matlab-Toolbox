package extensionTools.validator.recommender;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IConnectionEdgeInstance;
import exportedAPI.opcatAPI.ILinkInstance;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IRelationInstance;
import exportedAPI.opcatAPIx.IXConnectionEdgeInstance;
import exportedAPI.opcatAPIx.IXInstance;
import exportedAPI.opcatAPIx.IXSystem;
import expose.OpcatExposeKey;
import expose.OpcatExposeManager;
import extensionTools.validator.algorithm.Neighbor;
import extensionTools.validator.algorithm.Offence;
import extensionTools.validator.finder.Finder;
import gui.Opcat2;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.projectStructure.ConnectionEdgeEntry;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessInstance;
import gui.projectStructure.StateInstance;
import gui.undo.CompoundUndoAction;
import gui.undo.UndoableAddFundamentalRelation;
import gui.undo.UndoableAddGeneralRelation;
import gui.undo.UndoableAddLink;
import gui.undo.UndoableAddObject;
import gui.undo.UndoableAddProcess;

/**
 * Performs an advice, giving by an advisor. The advice results in a creation of
 * a new thing (process or object), and connecting it to the current thing.
 * 
 * @author Eran Toch Created: 05/06/2004
 */
public class PerformAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private Neighbor neighbor;

	private ConnectionEdgeInstance instance;

	private IXSystem system;

	private MetaLibrary metaLib;

	private Offence offence;

	public PerformAction(Neighbor _neighbor, IConnectionEdgeInstance _instance,
			IXSystem _system, MetaLibrary _metaLib) {
		this.neighbor = _neighbor;
		this.instance = (ConnectionEdgeInstance) _instance;
		this.system = _system;
		this.metaLib = _metaLib;
	}

	public PerformAction(Offence _offence, IConnectionEdgeInstance _instance,
			IXSystem _system, MetaLibrary _metaLib) {

		this.offence = _offence;
		this.instance = (ConnectionEdgeInstance) _instance;
		this.system = _system;
		this.metaLib = _metaLib;
	}

	/**
	 * Performs the action specified by the advisor. The method perfomrs the
	 * following actions:<br>
	 * 1. Adds the new logical thing and its instance.<br>
	 * 2. Adds the role to the thing.<br>
	 * 3. Adds a connection (link, relation) to the thing.<br>
	 * 4. Opens up the thing properties window, allowing the user to configure
	 * the new thing.
	 */
	public void actionPerformed(ActionEvent arg0) {
		try {

			ConnectionEdgeEntry roleThing;
			if (offence == null) {
				roleThing = (ConnectionEdgeEntry) this.neighbor
						.getConnectedThing();

			} else {
				roleThing = (ConnectionEdgeEntry) ((OpdProject) this.metaLib
						.getProjectHolder()).getSystemStructure().getEntry(
						offence.getLaw().getApplicant().getRole().getThingID());
			}

			int neighborType;
			int direction;
			if (offence == null) {
				neighborType = this.neighbor.getConnectionType();
				direction = this.neighbor.getDirection();
			} else {
				direction = offence.getLaw().getDirection();
				neighborType = offence.getLaw().getEdgeType();
			}

			Role role = new Role(roleThing.getId(), this.metaLib);

			ConnectionEdgeInstance newInstance = null;

			/**
			 * search for an entity with the inserted role
			 */
			boolean connectToExisting = false;

			IXConnectionEdgeInstance existingInstance = null;
			Opd opd = Opcat2.getCurrentProject().getOpdByID(
					instance.getKey().getOpdId());
			String type = Opd.getEntryType(roleThing);
			if (neighborType != OpcatConstants.STATE) {
				ArrayList<Instance> instances = opd
						.getInstancesInOPDOfType(type);
				if (instances.size() > 0) {
					for (Instance ins : instances) {
						if (ins.getKey().getEntityInOpdId() != instance
								.getKey().getEntityInOpdId()) {
							opd.getSelection().deselectAll();
							opd.getSelection().addSelection(ins, true);
							int ret = JOptionPane.showOptionDialog(Opcat2
									.getFrame(), "Connect to existing Entity ("
									+ ins.getEntry().getName() + ") ?",
									"Opcat II",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.INFORMATION_MESSAGE, null,
									null, null);
							if (ret == JOptionPane.YES_OPTION) {
								existingInstance = (IXConnectionEdgeInstance) ins;
								connectToExisting = true;
								break;
							}
							if (ret == JOptionPane.CANCEL_OPTION) {
								connectToExisting = false;
								break;
							}
						}
					}
				}
			}

			boolean addRole = false;

			if (!connectToExisting) {
				int ret = JOptionPane.showOptionDialog(Opcat2.getFrame(),
						"Add new Connected Entity ?", "Opcat II",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, null, null);
				if (ret == JOptionPane.YES_OPTION) {
					addRole = true;
				} else {
					ret = JOptionPane.showOptionDialog(Opcat2.getFrame(),
							"Add new local Entity ?", "Opcat II",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, null, null);
					if (ret == JOptionPane.NO_OPTION) {
						return;
					}
				}
			}

			addRole = ((!connectToExisting) && addRole);

			if (!connectToExisting) {
				if (!(instance instanceof StateInstance)
						&& (instance.getIOPD().getMainIInstance() != null)
						&& (((ConnectionEdgeInstance) instance
								.getParentIThingInstance() != null) || (instance
								.getKey().equals(instance.getIOPD()
								.getMainIInstance().getKey())))) {
					int ret = JOptionPane.showOptionDialog(Opcat2.getFrame(),
							"Insert outside the main entity ?", "Opcat II",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, null, null);
					if (ret == JOptionPane.NO_OPTION) {
						ConnectionEdgeInstance parent = (ConnectionEdgeInstance) instance
								.getParentIThingInstance();
						if (parent == null) {
							parent = (ConnectionEdgeInstance) instance
									.getIOPD().getMainIInstance();
						}
						newInstance = MetaLibrary.insertConnectionEdge(role,
								Opcat2.getCurrentProject().getCurrentOpd(),
								addRole, (ConnectionEdgeInstance) instance,
								parent);
					} else {
						newInstance = MetaLibrary.insertConnectionEdge(role,
								Opcat2.getCurrentProject().getCurrentOpd(),
								addRole, (ConnectionEdgeInstance) instance,
								null);
					}

				} else {
					newInstance = MetaLibrary.insertConnectionEdge(role, Opcat2
							.getCurrentProject().getCurrentOpd(), addRole,
							(ConnectionEdgeInstance) instance, null);
				}
			} else {
				newInstance = (ConnectionEdgeInstance) existingInstance;
			}

			if (newInstance == null) {
				return;
			}

			boolean template = !instance.isPointerInstance();
			if (!template && addRole) {
				OpcatExposeKey key = OpcatExposeManager.getExposeKey(role
						.getOntology().getPath(), role.getThingId());

				if (key == null) {
					// off line or private exposed
					key = new OpcatExposeKey(role.getOntology().getPath(), role
							.getThingId(), true);
				}
				newInstance.setPointer(key);
			}

			IXInstance connection = this.createConnection(newInstance);

			CompoundUndoAction undoAction = new CompoundUndoAction();
			if (connection instanceof ILinkInstance) {
				undoAction.addAction(new UndoableAddLink(
						(OpdProject) this.system, (LinkInstance) connection));
			} else if (connection instanceof IRelationInstance) {
				if (connection instanceof GeneralRelationInstance) {
					undoAction.addAction(new UndoableAddGeneralRelation(
							(OpdProject) this.system,
							(GeneralRelationInstance) connection));
				} else if (connection instanceof FundamentalRelationInstance) {
					undoAction.addAction(new UndoableAddFundamentalRelation(
							(OpdProject) this.system,
							(FundamentalRelationInstance) connection));
				}
			}

			if (!connectToExisting) {

				if (roleThing instanceof IObjectEntry) {
					undoAction.addAction(new UndoableAddObject(
							(OpdProject) this.system,
							(ObjectInstance) newInstance));
				} else if (roleThing instanceof IProcessEntry) {
					undoAction.addAction(new UndoableAddProcess(
							(OpdProject) this.system,
							(ProcessInstance) newInstance));
				}

			}

			Opcat2.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(this.system, undoAction));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// /**
	// * Creates the new Thing.
	// *
	// * @param roleThing
	// * @return
	// */
	// protected IXConnectionEdgeInstance createThing(IThingEntry roleThing)
	// {
	// long opdID = this.instance.getKey().getOpdId();
	// int x = this.instance.getX() + this.instance.getWidth() + 30;
	// int y = this.instance.getY() + this.instance.getHeight() + 30;
	// IXConnectionEdgeInstance newInstance = null;
	// IXThingInstance parent = (IXThingInstance) this.instance
	// .getParentIThingInstance();
	// if (roleThing instanceof IObjectEntry) {
	// if (parent != null) {
	// newInstance = this.system.addObject(this.renderName(roleThing),
	// x, y, parent);
	// } else {
	// newInstance = this.system.addObject(this.renderName(roleThing),
	// x, y, opdID);
	// }
	// } else if (roleThing instanceof IProcessEntry) {
	// if (parent != null) {
	// newInstance = this.system.addProcess(
	// this.renderName(roleThing), x, y, parent);
	// } else {
	// newInstance = this.system.addProcess(
	// this.renderName(roleThing), x, y, opdID);
	// }
	// }
	// return newInstance;
	//
	// }

	// /**
	// * Creates the thing name.
	// *
	// * @param roleThing
	// * @return
	// */
	// protected String renderName(IThingEntry roleThing) {
	// // return "New "+ roleThing.getName();
	// return "";
	// }

	// /**
	// * Creates the role of the new thing.
	// *
	// * @param newInstance
	// * @param roleThing
	// * @throws Exception
	// */
	// protected void createRole(IXConnectionEdgeInstance newInstance,
	// IThingEntry roleThing) throws Exception {
	// IXThingEntry logicalEntry = (IXThingEntry) newInstance.getIXEntry();
	// Role role = null;
	// try {
	// role = new Role(roleThing.getId(), this.metaLib);
	// } catch (Exception ex) {
	// throw ex;
	// }
	// logicalEntry.addRole(role);
	// }

	/**
	 * Creates the connection to the new thing.
	 * 
	 * @param newInstance
	 */
	protected IXInstance createConnection(IXConnectionEdgeInstance newInstance) {
		int type;
		int direction;
		if (offence == null) {
			type = this.neighbor.getConnectionType();
			direction = this.neighbor.getDirection();
		} else {
			direction = offence.getLaw().getDirection();
			type = offence.getLaw().getEdgeType();
		}
		IXInstance connection = null;
		// Handling relations
		if ((type < 300) && (type >= 200)) {
			if (direction == Finder.DESTINATION_DIRECTION) {
				connection = this.system.addRelation(
						(IXConnectionEdgeInstance) this.instance,
						(IXConnectionEdgeInstance) newInstance, type);
			} else if (direction == Finder.SOURCE_DIRECTION) {
				connection = this.system.addRelation(
						(IXConnectionEdgeInstance) newInstance,
						(IXConnectionEdgeInstance) this.instance, type);
			}
		}
		// Handling links
		if ((type >= 300) && (type < 400)) {
			if (direction == Finder.DESTINATION_DIRECTION) {
				connection = this.system.addLink(
						(IXConnectionEdgeInstance) this.instance,
						(IXConnectionEdgeInstance) newInstance, type);
			} else if (direction == Finder.SOURCE_DIRECTION) {
				connection = this.system.addLink(
						(IXConnectionEdgeInstance) newInstance,
						(IXConnectionEdgeInstance) this.instance, type);
			}
		}
		return connection;
	}

}
