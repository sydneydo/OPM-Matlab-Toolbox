package expose;

import database.Lookup;
import database.OpcatDatabaseConnection;
import database.OpcatDatabaseConstants;
import database.OpcatDatabaseLookupDAO;
import exportedAPI.opcatAPI.IInstance;
import exportedAPI.opcatAPI.IRelationInstance;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_CHANGE_TYPE;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_ENTITY_TYPES;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_STATUS;
import gui.Opcat2;
import gui.actions.expose.OpcatExposeChange;
import gui.controls.FileControl;
import gui.dataProject.DataCreatorType;
import gui.dataProject.DataProject;
import gui.metaLibraries.logic.MetaLibrary;
import gui.metaLibraries.logic.Role;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmEntity;
import gui.opmEntities.OpmObject;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessInstance;
import gui.projectStructure.StateInstance;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatLogger;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.text.StyledEditorKit.BoldAction;

import messages.OpcatMessage;
import messages.OpcatMessagesManager;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_OP;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SEVIRITY;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SUBSYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_TYPE;
import modelControl.OpcatMCDirEntry;
import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.util.SVNPathUtil;
import org.tmatesoft.svn.core.wc.SVNRevision;

import com.jhlabs.image.OpacityFilter;

import user.OpcatUser;
import util.Configuration;

public class OpcatExposeManager {

	private OpdProject myProject = null;

	private ArrayList<OpcatExposeChange> latestExposedChangeMap = new ArrayList<OpcatExposeChange>();

	private boolean duringCompundChange = false;

	public void startCompundChange() {
		this.duringCompundChange = true;
	}

	public void endCompundChange() {
		this.duringCompundChange = false;
	}

	public ArrayList<OpcatExposeChange> getLatestExposedChangeMap() {
		return latestExposedChangeMap;
	}

	public OpcatExposeManager(OpdProject project) {
		this.myProject = project;

	}

	public void setLatestExposedChangeMap(
			ArrayList<OpcatExposeChange> latestExposedChangeMap) {
		this.latestExposedChangeMap = latestExposedChangeMap;
	}

	public static void OFRI_CLEANUP(File rpgConvertedDirectory) {

		try {

			handleOnline();

			OpcatMCManager.getInstance().forceConnection();

			Statement stat = OpcatDatabaseConnection.getInstance()
					.getConnection().createStatement(
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String clean = "delete from  expose_usage where expose_id in (select ID from expose where locate('/"
					+ rpgConvertedDirectory.getName() + "/' ,model_uri) > 0)";
			stat.executeUpdate(clean);

			clean = "delete from  expose_data where expose_id in (select ID from expose where locate('/"
					+ rpgConvertedDirectory.getName() + "/' ,model_uri) > 0) ";
			stat.executeUpdate(clean);

			clean = "delete from  expose where locate('/"
					+ rpgConvertedDirectory.getName() + "/' ,model_uri) > 0 ";
			stat.executeUpdate(clean);

			SVNURL root = OpcatMCManager.getInstance().getFileURL(
					rpgConvertedDirectory);
			LinkedList<OpcatMCDirEntry> files = OpcatMCManager.getInstance()
					.getFileListFromRepo(root);

			for (OpcatMCDirEntry entry : files) {
				OpcatMCManager.getInstance().doRemoteDelete(entry,
						"Auto delete ");
			}
			OpcatMCManager.deleteDirectory(rpgConvertedDirectory);

			rpgConvertedDirectory.mkdirs();

		} catch (SQLException e) {
			OpcatLogger.logError(e);
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}

	}

	private static String getIDSQL(String modelURI, long entityID) {
		return "select distinct " + OpcatDatabaseConstants.FIELD_NAME_ID
				+ "  from " + OpcatExposeConstants.TABLE_EXPOSE + " where "
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_ID + " = "
				+ entityID + " AND "
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI + " = '"
				+ modelURI + "' ORDER BY "
				+ OpcatDatabaseConstants.FIELD_NAME_ID + " DESC";
	}

	public static long getExposeServerID(String modelURI, long entityID) {

		try {

			handleOnline();

			Statement stat = OpcatDatabaseConnection.getInstance()
					.getConnection().createStatement(
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = getIDSQL(modelURI, entityID);

			ResultSet rs;
			rs = stat.executeQuery(query);

			if (rs.next()) {
				return rs.getLong(1);
			}

		} catch (SQLException e) {
			OpcatLogger.logError(e);
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}
		return OpcatExposeConstants.EXPOSE_UNDEFINED_ID;
	}

	public static long getExposeServerID(OpcatExposeKey key) {

		try {

			handleOnline();

			Statement stat = OpcatDatabaseConnection.getInstance()
					.getConnection().createStatement(
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = getIDSQL(key.getModelEncodedURI(), key
					.getOpmEntityID());

			ResultSet rs;
			rs = stat.executeQuery(query);

			if (rs.next()) {
				return rs.getLong(1);
			}

		} catch (SQLException e) {
			OpcatLogger.logError(e);
		} catch (Exception e) {
			OpcatLogger.logError(e);

		}
		return OpcatExposeConstants.EXPOSE_UNDEFINED_ID;
	}

	public static boolean isExposedInServer(OpcatExposeKey key) {
		try {
			handleOnline();

			if (getExposeServerID(key) != OpcatExposeConstants.EXPOSE_UNDEFINED_ID) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	private static void handleOnline() throws Exception {
		if (!OpcatMCManager.isOnline()) {
			throw new Exception("off-line mode");
		}
	}

	@SuppressWarnings("unchecked")
	private OpcatExposeChangeResult allowExposeThingChange(Instance thing,
			OPCAT_EXPOSE_CHANGE_TYPE type) {

		OpcatExposeChangeResult ret = new OpcatExposeChangeResult();

		if (type == OPCAT_EXPOSE_CHANGE_TYPE.DELETE_THING) {
			if (thing.getEntry().getLogicalEntity().isExposed()
					&& thing.isExposeOriginal()) {
				ret
						.fail("Expose Change : deleting a source instance of an exposed entry "
								+ thing.getEntry().getName());
				return ret;
			}

		} else if (type == OPCAT_EXPOSE_CHANGE_TYPE.CUT_THING) {
			if (thing.getEntry().getLogicalEntity().isExposed()
					&& thing.isExposeOriginal()) {
				ret
						.fail("Expose Change : cutting a source instance of an exposed entry "
								+ thing.getEntry().getName());
				return ret;
			}

		}

		if ((type == OPCAT_EXPOSE_CHANGE_TYPE.DELETE_THING)
				|| (type == OPCAT_EXPOSE_CHANGE_TYPE.CUT_THING)) {

			ThingInstance myThing = (ThingInstance) thing;
			Enumeration<LinkInstance> sources = myThing.getRelatedSourceLinks();
			while (sources.hasMoreElements()) {
				LinkInstance li = sources.nextElement();
				if ((li.getDestinationInstance() instanceof ProcessInstance)
						&& li.getDestinationInstance().getEntry()
								.getLogicalEntity().isExposed()) {
					ret = allowExposeChange(li.getSourceInstance(), li
							.getDestinationInstance(),
							OPCAT_EXPOSE_CHANGE_TYPE.DELETE_LINK);
					if (ret.isDenied() || ret.isWarning()) {
						return ret;
					}
				}
			}

			sources = myThing.getRelatedDestinationLinks();
			while (sources.hasMoreElements()) {
				LinkInstance li = sources.nextElement();
				if ((li.getSourceInstance() instanceof ProcessInstance)
						&& li.getSourceInstance().getEntry().getLogicalEntity()
								.isExposed()) {
					ret = allowExposeChange(li.getSourceInstance(), li
							.getDestinationInstance(),
							OPCAT_EXPOSE_CHANGE_TYPE.DELETE_LINK);
					if (ret.isDenied() || ret.isWarning()) {
						return ret;
					}
				}
			}

			// Enumeration<FundamentalRelationInstance> rels = myThing
			// .getRelatedSourceFundamentalRelation();
			// while (rels.hasMoreElements()) {
			// FundamentalRelationInstance li = rels.nextElement();
			// if ((li.getSourceInstance() instanceof ProcessInstance)
			// && li.getSourceInstance().getEntry().getLogicalEntity()
			// .isExposed()) {
			// ret = allowExposeChange(li.getSourceInstance(), li
			// .getDestinationInstance(),
			// OPCAT_EXPOSE_CHANGE_TYPE.DELETE_REALTION);
			// if (ret.isDenied()) {
			// return ret;
			// }
			// }
			// }

			Enumeration<FundamentalRelationInstance> rels = myThing
					.getRelatedDestinationFundamentalRelation();
			while (rels.hasMoreElements()) {
				FundamentalRelationInstance li = rels.nextElement();
				if ((li.getSourceInstance() instanceof ObjectInstance)
						&& li.getSourceInstance().getEntry().getLogicalEntity()
								.isExposed()) {
					ret = allowExposeChange(li.getSourceInstance(), li
							.getDestinationInstance(),
							OPCAT_EXPOSE_CHANGE_TYPE.DELETE_REALTION);
					if (ret.isDenied() || ret.isWarning()) {
						return ret;
					}
				}
			}
		}
		ret.approve();
		return ret;
	}

	private OpcatExposeChangeResult allowExposeStateChange(Instance source,
			OPCAT_EXPOSE_CHANGE_TYPE type) {

		OpcatExposeChangeResult ret = new OpcatExposeChangeResult();

		if (type == OPCAT_EXPOSE_CHANGE_TYPE.DELETE_STATE) {
			StateInstance state = (StateInstance) source;
			if (state.getParent().getEntry().getLogicalEntity().isExposed()
					&& state.getParentThingInstance().isExposeOriginal()) {
				ret
						.fail("Expose Change : deleting a state from a source instance of an exposed entry ");
				return ret;
			}

		} else if (type == OPCAT_EXPOSE_CHANGE_TYPE.CUT_STATE) {
			StateInstance state = (StateInstance) source;
			if (state.getParent().getEntry().getLogicalEntity().isExposed()
					&& state.getParentThingInstance().isExposeOriginal()) {
				ret
						.fail("Expose Change : cutting a state from a source instance of an exposed entry "
								+ state.getParentThingInstance().getEntry()
										.getName());
				return ret;
			}

		} else if (type == OPCAT_EXPOSE_CHANGE_TYPE.ADD_STATE) {
			if (source.getEntry().getLogicalEntity().isExposed()
					&& source.isExposeOriginal()) {
				ret
						.fail("Expose Change : adding a state to a source instance of an exposed entry "
								+ source.getEntry().getName());
				return ret;
			}
		}

		ret.approve();
		return ret;
	}

	public OpcatExposeChangeResult allowExposeChange(Opd deletedOpd,
			OPCAT_EXPOSE_CHANGE_TYPE op) {

		OpcatExposeChangeResult ret = new OpcatExposeChangeResult();
		ret.addAtStartOfMessage("Opd deletion :");
		boolean fail = false;

		if (op == OPCAT_EXPOSE_CHANGE_TYPE.DELETE_OPD) {
			Enumeration<Instance> deleted = myProject.getSystemStructure()
					.getInstancesInOpd(deletedOpd.getOpdId());

			while (deleted.hasMoreElements()) {
				Instance delete = deleted.nextElement();
				OPCAT_EXPOSE_CHANGE_TYPE type = null;
				if (delete instanceof ThingInstance) {
					type = OPCAT_EXPOSE_CHANGE_TYPE.DELETE_THING;
				} else if (delete instanceof LinkInstance) {
					type = OPCAT_EXPOSE_CHANGE_TYPE.DELETE_LINK;
				} else if (delete instanceof FundamentalRelationInstance) {
					type = OPCAT_EXPOSE_CHANGE_TYPE.DELETE_REALTION;
				} else if (delete instanceof StateInstance) {
					type = OPCAT_EXPOSE_CHANGE_TYPE.DELETE_STATE;
				}

				if (type != null) {
					if (delete instanceof ThingInstance) {
						OpcatExposeChangeResult localRet = allowExposeChange(
								delete, type);
						if (localRet.isDenied()) {
							fail = true;
							ret.addAtEndOfMessage(localRet.getReaseon());
							// return ret;
						}
					}
				}
			}
		}
		if (fail)
			ret.fail(ret.getReaseon());
		return ret;
	}

	public OpcatExposeChangeResult allowExposeCompundChange(Instance instance,
			OPCAT_EXPOSE_CHANGE_TYPE changeType) {

		OpcatExposeChangeResult ret = new OpcatExposeChangeResult();

		if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.ZOOM_IN) {
			ret.approve();
			return ret;
		} else if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.UNFOLD) {
			ret.approve();
			return ret;
		}

		ret.approve();
		return ret;
	}

	public OpcatExposeChangeResult allowExposeChange(Instance instance,
			OPCAT_EXPOSE_CHANGE_TYPE changeType) {

		OpcatExposeChangeResult ret = new OpcatExposeChangeResult();
		ret.approve();

		if (duringCompundChange)
			return ret;

		OPCAT_EXPOSE_CHANGE_TYPE myType = changeType;

		if (instance instanceof ThingInstance) {
			if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.DELETE) {
				myType = OPCAT_EXPOSE_CHANGE_TYPE.DELETE_THING;
			} else if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.ADD) {
				myType = OPCAT_EXPOSE_CHANGE_TYPE.ADD_THING;
			}
			return allowExposeChange(instance, instance, myType);

		} else if (instance instanceof LinkInstance) {
			if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.DELETE) {
				myType = OPCAT_EXPOSE_CHANGE_TYPE.DELETE_LINK;
			} else if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.ADD) {
				myType = OPCAT_EXPOSE_CHANGE_TYPE.ADD_LINK;
			}
			return allowExposeChange(((LinkInstance) instance)
					.getSourceInstance(), ((LinkInstance) instance)
					.getDestinationInstance(), myType);

		} else if (instance instanceof FundamentalRelationInstance) {

			if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.DELETE) {
				myType = OPCAT_EXPOSE_CHANGE_TYPE.DELETE_REALTION;
			} else if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.ADD) {
				myType = OPCAT_EXPOSE_CHANGE_TYPE.ADD_RELATION;
			}
			return allowExposeChange(((FundamentalRelationInstance) instance)
					.getSourceInstance(),
					((FundamentalRelationInstance) instance)
							.getDestinationInstance(), myType);
		} else if (instance instanceof StateInstance) {
			if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.DELETE) {
				myType = OPCAT_EXPOSE_CHANGE_TYPE.DELETE_STATE;
			} else if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.ADD) {
				myType = OPCAT_EXPOSE_CHANGE_TYPE.ADD_STATE;
			}
			return allowExposeChange(instance, instance, myType);
		}

		return ret;
	}

	public OpcatExposeChangeResult allowExposeChange(Instance source,
			Instance destination, OPCAT_EXPOSE_CHANGE_TYPE changeType) {

		OpcatExposeChangeResult ret = new OpcatExposeChangeResult();
		ret.approve();

		if (duringCompundChange)
			return ret;

		/**
		 * we allow any change but only warn the user, we can extend this to
		 * include any test we like.
		 * 
		 */
		if (changeType == OPCAT_EXPOSE_CHANGE_TYPE.COPY) {
			return ret;
		}

		if ((changeType == OPCAT_EXPOSE_CHANGE_TYPE.ADD_THING)
				|| (changeType == OPCAT_EXPOSE_CHANGE_TYPE.DELETE_THING)
				|| (changeType == OPCAT_EXPOSE_CHANGE_TYPE.CUT_THING)) {
			return allowExposeThingChange(source, changeType);
		}

		if ((changeType == OPCAT_EXPOSE_CHANGE_TYPE.ADD_STATE)
				|| (changeType == OPCAT_EXPOSE_CHANGE_TYPE.DELETE_STATE)
				|| (changeType == OPCAT_EXPOSE_CHANGE_TYPE.CUT_STATE)) {
			return allowExposeStateChange(source, changeType);
		}

		if ((changeType == OPCAT_EXPOSE_CHANGE_TYPE.ADD_LINK)
				|| (changeType == OPCAT_EXPOSE_CHANGE_TYPE.DELETE_LINK)
				|| (changeType == OPCAT_EXPOSE_CHANGE_TYPE.CUT_LINK)) {
			return allowExposeLinkChange(source, destination, changeType);
		}
		if ((changeType == OPCAT_EXPOSE_CHANGE_TYPE.ADD_RELATION)
				|| (changeType == OPCAT_EXPOSE_CHANGE_TYPE.DELETE_REALTION)
				|| (changeType == OPCAT_EXPOSE_CHANGE_TYPE.CUT_RELATION)) {

			return allowExposeRelationChange(source, destination, changeType);

		}
		return ret;
	}

	private OpcatExposeChangeResult allowExposeRelationChange(Instance source,
			Instance destination, OPCAT_EXPOSE_CHANGE_TYPE type) {

		OpcatExposeChangeResult ret = new OpcatExposeChangeResult();
		ret.approve();

		boolean sourceExposed = source.getEntry().getLogicalEntity()
				.isExposed();
		boolean destinationExposed = destination.getEntry().getLogicalEntity()
				.isExposed();
		boolean destinationProcess = (destination instanceof ProcessInstance);
		boolean sourceProcess = (source instanceof ProcessInstance);
		boolean bothExposed = sourceExposed && destinationProcess;

		if (sourceProcess) {
			return ret;
		}

		if (((source instanceof ObjectInstance) && source.getEntry()
				.getLogicalEntity().isExposed())) {
			ret
					.warn("Expose Change : change relation connected to an exposed entry ");
			return ret;
		}

		return ret;
	}

	private OpcatExposeChangeResult allowExposeLinkChange(Instance source,
			Instance destination, OPCAT_EXPOSE_CHANGE_TYPE type) {

		OpcatExposeChangeResult ret = new OpcatExposeChangeResult();
		ret.approve();

		boolean sourceExposed = source.getEntry().getLogicalEntity()
				.isExposed();
		boolean destinationExposed = destination.getEntry().getLogicalEntity()
				.isExposed();
		boolean destinationProcess = (destination instanceof ProcessInstance);
		boolean sourceProcess = (source instanceof ProcessInstance);
		boolean bothExposed = sourceExposed && destinationProcess;

		if (!bothExposed) {
			/**
			 * allow changes of links to objects.
			 */
			if (sourceExposed && !sourceProcess) {
				return ret;
			}
			if (destinationExposed && !destinationProcess) {
				return ret;
			}

		}

		if (source.getEntry().getLogicalEntity().isExposed()
				|| destination.getEntry().getLogicalEntity().isExposed()) {
			if (type == OPCAT_EXPOSE_CHANGE_TYPE.DELETE_LINK) {
				ret
						.warn("Expose Change : deleting link connected to an exposed entry ");
			} else if (type == OPCAT_EXPOSE_CHANGE_TYPE.ADD_LINK) {
				ret
						.warn("Expose Change : adding link connected to an exposed entry ");
			} else {
				ret
						.warn("Expose Change : deleting link connected to an exposed entry ");
			}
			return ret;
		}

		return ret;
	}

	public void addPrivateExposeChange(Instance instance, OPCAT_EXPOSE_OP op) {
		addExposeChange(instance, op, true);
	}

	private void commonUnExpose(Entry entry, boolean publicUnExpose,
			boolean privateUnExpose) {
		OpmEntity myEntity = entry.getLogicalEntity();

		if (privateUnExpose)
			myEntity.setPrivateExposed(false);

		if (publicUnExpose)
			myEntity.setPublicExposed(false);

		Enumeration<Instance> instances = entry.getInstances();
		while (instances.hasMoreElements()) {
			Instance instance = instances.nextElement();
			instance.setExposeOriginal(false);
		}
	}

	private void addExposeChange(Entry entry, OPCAT_EXPOSE_OP op,
			boolean isPrivate) {
		if (op == OPCAT_EXPOSE_OP.NONE)
			return;

		OpcatExposeChange change = new OpcatExposeChange(entry.getId(),
				isPrivate, op);

		if (isPrivate) {
			if (op == OPCAT_EXPOSE_OP.DELETE) {

			} else {
				entry.getLogicalEntity().setPrivateExposed(true);
			}
		} else {
			if (op != OPCAT_EXPOSE_OP.DELETE) {
				entry.getLogicalEntity().setPublicExposed(true);
			} else if (op == OPCAT_EXPOSE_OP.DELETE) {

			}
		}

		OpcatExposeChange last = getExposeLastChangeOp(entry.getId(), isPrivate);

		if ((last != null) && (last.getOp() == OPCAT_EXPOSE_OP.ADD)
				&& (op != OPCAT_EXPOSE_OP.DELETE)) {
			return;
		}

		if ((op == OPCAT_EXPOSE_OP.UPDATE) && (last != null)) {
			return;
		}

		if (last != null) {
			latestExposedChangeMap.remove(change);
		}
		latestExposedChangeMap.add(change);

		entry.getLogicalEntity().setExposedChanged(true);

	}

	private void addExposeChange(Instance instance, OPCAT_EXPOSE_OP op,
			boolean isPrivate) {

		Entry entry = instance.getEntry();
		addExposeChange(entry, op, isPrivate);

		if (op == OPCAT_EXPOSE_OP.ADD) {
			instance.setExposeOriginal(true);
		} else if (op == OPCAT_EXPOSE_OP.DELETE) {
			// instance.setExposeOriginal(false);
		}

	}

	public void addPublicExposeChange(Instance instance, OPCAT_EXPOSE_OP op) {
		addExposeChange(instance, op, false);

	}

	public void addPublicExposeChange(Entry entry, OPCAT_EXPOSE_OP op) {
		addExposeChange(entry, op, false);
	}

	public void addPrivateExposeChange(Entry entry, OPCAT_EXPOSE_OP op) {
		addExposeChange(entry, op, true);
	}

	public OpcatExposeChange getExposeLastChangeOp(long entityID,
			boolean privateOP) {

		for (OpcatExposeChange i : latestExposedChangeMap) {
			if ((i.getEntryID() == entityID)
					&& (i.isPrivateAction() == privateOP))
				return i;
		}

		return null;
	}

	public ArrayList<OpcatExposeUser> getExposeUsage(OpcatExposeKey key,
			boolean getPrivate, boolean getPublic) {

		ArrayList<OpcatExposeUser> users = new ArrayList<OpcatExposeUser>();

		try {

			if ((OpcatMCManager.isOnline()) && getPublic) {
				Statement stat = OpcatDatabaseConnection.getInstance()
						.getConnection().createStatement(
								ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);

				String query = " select distinct " + "a."
						+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_MODEL_URI
						+ ","
						+ "a."
						+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_MODEL_NAME
						+ "," + "a."
						+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_EXPOSE_ID
						+ ", b."
						+ OpcatExposeConstants.FIELD_EXPOSE_DATA_OP_USER
						+ " from " + OpcatExposeConstants.TABLE_EXPOSE_USAGE
						+ " a " + " inner join "
						+ OpcatExposeConstants.TABLE_EXPOSE_DATA + " b "
						+ " on a."
						+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_EXPOSE_ID
						+ " = " + "b."
						+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID
						+ " where " + "a."
						+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_EXPOSE_ID
						+ " = " + key.getId();

				ResultSet rs = stat.executeQuery(query);

				while (rs.next()) {
					OpcatExposeUser user = new OpcatExposeUser(key, rs
							.getString(2), rs.getString(1), rs.getString(4));
					users.add(user);
				}
			}
			if (getPrivate)
				users.addAll(getPrivateExposeUsage(key));

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
		return users;
	}

	private ArrayList<OpcatExposeUser> getPrivateExposeUsage(OpcatExposeKey key) {
		ArrayList<OpcatExposeUser> users = new ArrayList<OpcatExposeUser>();
		ArrayList<Instance> instances = myProject.getSystemStructure()
				.getPointerInstances(true);
		for (Instance instance : instances) {
			if (instance.getPointer().isPrivate()) {
				OpcatExposeKey insKey = instance.getPointer();
				if (insKey.equals(key)) {
					OpcatExposeUser user = new OpcatExposeUser(instance,
							"Current");
					users.add(user);
				}
			}
		}
		return users;
	}

	/**
	 * A list of users of the given model
	 * 
	 * @param encodedModelPath
	 * @return
	 */
	public static ArrayList<OpcatExposeUser> getExposeUsage(
			String encodedModelPath) {

		ArrayList<OpcatExposeUser> users = new ArrayList<OpcatExposeUser>();

		try {

			handleOnline();

			Statement stat = OpcatDatabaseConnection.getInstance()
					.getConnection().createStatement(
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = " select distinct " + "a."
					+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_MODEL_URI
					+ "," + "a."
					+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_MODEL_NAME
					+ "," + "a."
					+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_EXPOSE_ID
					+ ", b." + OpcatExposeConstants.FIELD_EXPOSE_DATA_OP_USER
					+ ",b." + OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID
					+ " from " + OpcatExposeConstants.TABLE_EXPOSE_USAGE
					+ " a " + " inner join "
					+ OpcatExposeConstants.TABLE_EXPOSE_DATA + " b " + " on a."
					+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_EXPOSE_ID + " = "
					+ "b." + OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID
					+ " inner join " + OpcatExposeConstants.TABLE_EXPOSE
					+ " c " + " on b."
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID + " = "
					+ "c." + OpcatDatabaseConstants.FIELD_NAME_ID + " where "
					+ "c." + OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI
					+ " = '" + encodedModelPath + "'";

			ResultSet rs = stat.executeQuery(query);

			while (rs.next()) {
				OpcatExposeKey key = getExposeKey(rs.getLong(5));
				OpcatExposeUser user = new OpcatExposeUser(key,
						rs.getString(2), rs.getString(1), rs.getString(4));
				users.add(user);
			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
		return users;
	}

	/**
	 * <code>true</code> if the expose is used
	 * 
	 * @param key
	 * @return
	 */
	public boolean isUsed(OpcatExposeKey key, boolean getPrivate,
			boolean getPublic) {
		ArrayList<OpcatExposeUser> users = getExposeUsage(key, getPrivate,
				getPublic);
		return (users.size() > 0);
	}

	public static File getModelIntoOpcatTemp(String encodedPath)
			throws Exception {

		File ret = null;

		String decoded = SVNURL.parseURIEncoded(
				OpcatMCManager.getSvnURL() + encodedPath).toDecodedString();

		String fileName = SVNPathUtil.tail(decoded);

		String relativePath = SVNPathUtil.removeTail(encodedPath);

		File destination;
		destination = new File(OpcatMCManager.getOpcatMCTemporeryDirectory()
				.getPath()
				+ FileControl.fileSeparator + fileName);
		destination.delete();
		destination.mkdirs();

		ret = new File(destination.getPath() + FileControl.fileSeparator
				+ fileName);

		OpcatMCManager.getInstance().doExport(relativePath, SVNRevision.HEAD,
				destination);

		if (!ret.exists()) {
			// throw new
			// Exception("Could not get source library model from MC");
		}

		ret.setWritable(false);

		return ret;
	}

	/**
	 * insert expose entity into {@link OpdProject}.
	 * 
	 * @param useParentInstance
	 *            - if not <code>null</code> then used as the parent of the
	 *            inserted entity
	 * @param useInOpd
	 *            - the {@link Opd} in which the entity is inserted to
	 * @param entity
	 *            - {@link OpcatExposeEntity} the entity to be inserted
	 * @param x
	 *            X cordinate of the inserted instance
	 * @param y
	 *            y cordinate of the inserted instance.
	 */
	public Instance useExpose(ThingInstance useParentInstance, Opd useInOpd,
			OpcatExposeEntity entity, int x, int y, boolean doExistingChecks) {
		// ThingInstance ins = null;
		Instance instance = null;

		try {

			if (myProject == null) {
				return null;
			}

			MetaLibrary meta;
			if (entity.getKey().isPrivate()) {

				meta = myProject
						.getMetaManager()
						.createNewMetaLibraryReference(
								MetaLibrary.TYPE_POLICY,
								new Object[] { myProject.getPath(), myProject },
								DataCreatorType.DATA_TYPE_OPCAT_LIBRARAY,
								DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE);

			} else {

				boolean connected = OpcatMCManager.getInstance()
						.forceConnection();
				if (connected) {
					meta = myProject
							.getMetaManager()
							.createNewMetaLibraryReference(
									MetaLibrary.TYPE_POLICY,
									entity.getKey().getModelEncodedURI(),
									DataCreatorType.DATA_TYPE_OPCAT_LIBRARAY,
									DataCreatorType.REFERENCE_TYPE_REPOSITORY_FILE);
				} else {
					OpcatLogger
							.logError(new Exception(
									"Error inserting expose, not connected to repository"));
					return null;
				}
			}
			meta.setHidden(true);
			myProject.getMetaManager().addMetaLibrary(meta);
			meta.load();

			if (meta.getProjectHolder() == null) {
				throw new Exception("Error loading expose model");
			}

			Role role = new Role(entity.getKey().getOpmEntityID(), meta);

			if ((useParentInstance != null) && useParentInstance.isZoomedIn()) {
				instance = MetaLibrary.insertConnectionEdge(role, myProject,
						useInOpd, true, null, useParentInstance,
						doExistingChecks, x, y);

			} else {
				instance = MetaLibrary.insertConnectionEdge(role, myProject,
						useInOpd, true, null, null, doExistingChecks, x, y);
			}

			if (instance == null) {
				return instance;
			}
			/**
			 * make sure there is only one role here. kind of a patch as there
			 * could not be more then one role here. it's a new instance
			 */
			instance.getEntry().getLogicalEntity().getRolesManager().clear();
			instance.getEntry().getLogicalEntity().getRolesManager().addRole(
					role);
			instance.setPointer(entity.getKey());

			return instance;
		} catch (Exception ex) {
			if (instance != null) {
				if (OpcatMCManager.isOnline()) {
					removeExposeUsage(entity.getKey(), instance);
				}
				myProject.delete(instance);
			}
			OpcatLogger.logError(ex);
			// ex.printStackTrace();
			return null;
		}
	}

	/**
	 * insert expose entity into {@link OpdProject}.
	 * 
	 * @param useParentInstance
	 *            - if not <code>null</code> then used as the parent of the
	 *            inserted entity
	 * @param useInOpd
	 *            - the {@link Opd} in which the entity is inserted to
	 * @param entity
	 *            - {@link OpcatExposeEntity} the entity to be inserted
	 * @param x
	 *            X cordinate of the inserted instance
	 * @param y
	 *            y cordinate of the inserted instance.
	 */
	public void useExpose(ThingInstance useParentInstance, Opd useInOpd,
			OpcatExposeEntity entity, int x, int y) {

		useExpose(useParentInstance, useInOpd, entity, x, y, true);

	}

	private void removeExposeUsage(OpcatExposeKey key, Instance usingInstance) {

		String sql = " delete from  "
				+ OpcatExposeConstants.TABLE_EXPOSE_USAGE
				+ " where "
				+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_EXPOSE_ID
				+ " = "
				+ key.getId()
				+ " AND "
				+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_MODEL_URI
				+ " = '"
				+ key.getModelEncodedURI()
				+ "'"
				+ " AND "
				+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_ENTITY_IN_OPD_ID
				+ " = " + usingInstance.getKey().getEntityInOpdId() + " AND "
				+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_ENTITY_OPD_ID
				+ " = " + usingInstance.getKey().getOpdId();

		Statement stat;
		try {
			handleOnline();

			stat = OpcatDatabaseConnection.getInstance().getConnection()
					.createStatement();
			stat.executeUpdate(sql);
			usingInstance.setPointer(null);

		} catch (SQLException e) {
			OpcatLogger.logError(e);
		} catch (Exception e) {
			OpcatLogger.logError(e);

		}

	}

	private void sendUnUseMessage(OpcatExposeEntity entity, OpdProject project) {
		try {

			String message = project.getName() + " removed usage of "
					+ entity.getOpmEntityName();

			if (message != null) {
				OPCAT_MESSAGES_OP op = OPCAT_MESSAGES_OP.EXPOSE_CHANGE_DONE;
				OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.ENTITY;
				OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.EXPOSE;
				OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.WARNING;
				OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.MODELS;

				String subject = entity.getOpmEntityName() + "@"
						+ entity.getKey().getModelDecodedURI();

				OpcatMessage opcatMessage = new OpcatMessage(OpcatUser
						.getCurrentUser().getName(), entity.getOpmEntityName(),
						type, type, subsystem, subsystem, system, sevi, op,
						subject, message);

				OpcatMessagesManager.getInstance().sendMessage(opcatMessage);
			}
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}
	}

	public static void removeModelUsage(String modelPath) throws SQLException {

		Statement stat = OpcatDatabaseConnection.getInstance().getConnection()
				.createStatement();

		stat.executeUpdate("delete from "
				+ OpcatExposeConstants.TABLE_EXPOSE_USAGE + " where "
				+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_MODEL_URI
				+ " = '" + modelPath + "'");
	}

	public boolean commitUsage() throws Exception {

		handleOnline();

		Statement stat = OpcatDatabaseConnection.getInstance().getConnection()
				.createStatement();

		SVNURL url = myProject.getMcURL();

		try {

			Statement select = OpcatDatabaseConnection.getInstance()
					.getConnection().createStatement(
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = select.executeQuery("select distinct "
					+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_EXPOSE_ID
					+ " from " + OpcatExposeConstants.TABLE_EXPOSE_USAGE
					+ " where "
					+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_MODEL_URI
					+ " = '" + url.getPath() + "'");

			HashMap<Long, Long> current = new HashMap<Long, Long>();
			while (rs.next()) {
				Long id = rs.getLong(1);
				current.put(id, id);
			}

			select.close();

			removeModelUsage(url.getPath());

			ArrayList<Instance> instances = myProject.getSystemStructure()
					.getPointerInstances(false);

			for (Instance instance : instances) {
				long id = getExposeServerID(instance.getPointer());
				OpcatExposeKey key = new OpcatExposeKey(id, instance
						.getPointer().getModelEncodedURI(), instance
						.getPointer().getOpmEntityID(), false);

				ArrayList<OpcatExposeEntity> entities = getExposedList(key,
						false);

				if ((entities == null) || (entities.size() == 0)) {
					/**
					 * usage of non exposed entities entered with advisor
					 * 
					 */
				} else if (entities.size() == 1) {
					OpcatExposeEntity entity = entities.get(0);
					updateUsage(stat, entity, instance, url, myProject
							.getName());
					current.remove(entity.getKey().getId());
				} else if (entities.size() > 1) {
					OpcatLogger
							.logError("Error : More then one key for public Expose");
					return false;
				}

			}

			/**
			 * send the unuse messages.
			 */
			for (Long id : current.keySet()) {
				OpcatExposeKey key = getExposeKey(id);
				ArrayList<OpcatExposeEntity> entities = getExposedList(key,
						false);
				if ((entities != null) && (entities.size() == 1)) {
					OpcatExposeEntity entity = entities.get(0);
					sendUnUseMessage(entity, myProject);
				}
			}

			return true;
		} catch (Exception ex) {
			OpcatLogger.logError(ex, false);
			// ex.printStackTrace();
			return false;
		}

	}

	private void updateUsage(Statement stat, OpcatExposeEntity entity,
			Instance instance, SVNURL projectURL, String projectName)
			throws SQLException {

		String fields = OpcatExposeConstants.FIELD_EXPOSE_USAGE_EXPOSE_ID
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_MODEL_NAME
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_MODEL_URI
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_ENTITY_IN_OPD_ID
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_ENTITY_OPD_ID
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_USAGE_USING_ENTITY_NAME;

		String values = entity.getKey().getId() + ",'" + projectName + "','"
				+ projectURL.getPath() + "',"
				+ instance.getKey().getEntityInOpdId() + ","
				+ instance.getKey().getOpdId() + ",'"
				+ instance.getEntry().getName() + "'";

		String sql = " insert into  " + OpcatExposeConstants.TABLE_EXPOSE_USAGE
				+ "(" + fields + ") values (" + values + ")";

		stat.executeUpdate(sql);
	}

	public void clearExposeLocalChanges() {
		try {
			for (OpcatExposeChange i : latestExposedChangeMap) {
				Entry entry = myProject.getSystemStructure().getEntry(
						i.getEntryID());
				if (entry != null) {
					entry.getLogicalEntity().setExposedChanged(false);
				}
			}
		} catch (Exception ex) {

		}
		latestExposedChangeMap.clear();
	}

	public void clearExposeLocalChanges(OpdProject project) {
		clearExposeLocalChanges();

		if ((project == null)
				|| !project.getGlobalID().equalsIgnoreCase(
						myProject.getGlobalID())) {
			return;
		}
		Enumeration<Entry> remoteEntries = project.getSystemStructure()
				.getElements();
		while (remoteEntries.hasMoreElements()) {
			Entry remote = remoteEntries.nextElement();
			Entry local = myProject.getSystemStructure().getEntry(
					remote.getId());
			if (local != null) {
				local.getLogicalEntity().setPrivateExposed(
						remote.getLogicalEntity().isPrivateExposed());
				local.getLogicalEntity().setPublicExposed(
						remote.getLogicalEntity().isPublicExposed());
			}
		}
	}

	public HashMap<Long, OPCAT_EXPOSE_OP> loadLatestExposedChangeMap(File file) {

		return null;
	}

	/**
	 * commits changes to repository. this will commit only changes to the given
	 * {@link OpdProject opdProject}. if <code>opdProject</code> is
	 * <code>null</code> the {@link FileControl#getCurrentProject()} will be
	 * used.
	 * 
	 * @throws SQLException
	 */
	public ArrayList<OpcatExposeError> commitExpose() throws SQLException {
		ArrayList<OpcatExposeError> commit = new ArrayList<OpcatExposeError>();
		try {

			if (myProject == null) {
				return commit;
			}

			ArrayList<OpcatExposeChange> processed = new ArrayList<OpcatExposeChange>();

			for (OpcatExposeChange change : latestExposedChangeMap) {

				Entry entry = myProject.getSystemStructure().getEntry(
						change.getEntryID());

				OpcatExposeEntity i = new OpcatExposeEntity(entry, OpcatUser
						.getCurrentUser().getName());

				i.getKey().refreshID();

				i.setOp(change.getOp());

				// if (!i.getKey().isPrivate()) {

				if (i.getOp() == OPCAT_EXPOSE_OP.ADD) {
					ArrayList<OpcatExposeUser> users = new ArrayList<OpcatExposeUser>();
					if (change.isPrivateAction()) {
						// users = getExposeUsage(i.getKey(), true, false);
					} else {
						users = getExposeUsage(i.getKey(), false, true);
					}
					if (users.size() == 0) {
						processed.add(change);
					} else {
						commit.add(new OpcatExposeError("There are "
								+ users.size() + " active users", entry
								.getName()));
					}

				}

				if (i.getOp() == OPCAT_EXPOSE_OP.DELETE) {
					ArrayList<OpcatExposeUser> users = new ArrayList<OpcatExposeUser>();
					if (change.isPrivateAction()) {
						users = getExposeUsage(i.getKey(), true, false);
					} else {
						users = getExposeUsage(i.getKey(), false, true);
					}
					if (users.size() == 0) {
						processed.add(change);
					} else {
						commit.add(new OpcatExposeError("There are "
								+ users.size() + " active users", entry
								.getName()));
					}
				}

				if (i.getOp() == OPCAT_EXPOSE_OP.CHANGE_INTERFACE) {
					ArrayList<OpcatExposeUser> users = new ArrayList<OpcatExposeUser>();
					if (change.isPrivateAction()) {
						//users = getExposeUsage(i.getKey(), true, false);
					} else {
						users = getExposeUsage(i.getKey(), false, true);
					}
					if (users.size() == 0) {
						processed.add(change);
					} else {
						commit.add(new OpcatExposeError("There are "
								+ users.size() + " active users", entry
								.getName()));
					}
				}

				if (i.getOp() == OPCAT_EXPOSE_OP.UPDATE) {
					processed.add(change);
				}

			}

			if (commit.size() == 0) {

				for (OpcatExposeChange change : processed) {

					OpcatExposeKey key = new OpcatExposeKey(new File(myProject
							.getPath()), change.getEntryID(), change
							.isPrivateAction());

					OpcatExposeEntity i = new OpcatExposeEntity(myProject
							.getSystemStructure()
							.getEntry(key.getOpmEntityID()), OpcatUser
							.getCurrentUser().getName());

					i.getKey().refreshID();

					i.setOp(change.getOp());

					if (!change.isPrivateAction()) {
						if (i.getOp() == OPCAT_EXPOSE_OP.ADD) {
							if (i.getKey().getId() == OpcatExposeConstants.EXPOSE_UNDEFINED_ID) {
								addExposeToDB(i);
								sendAddedMessage(i);
							}
							latestExposedChangeMap.remove(change);
						}
					}

					if (i.getOp() == OPCAT_EXPOSE_OP.DELETE) {
						Entry entry = myProject.getSystemStructure().getEntry(
								i.getKey().getOpmEntityID());
						if (!change.isPrivateAction()) {
							if (i.getKey().getId() != OpcatExposeConstants.EXPOSE_UNDEFINED_ID) {
								removeExposeFromDB(i);
							} else {
								// can happen only if we add then delete without
								// commiting first
								// any other case it's an error
							}
							commonUnExpose(entry, true, false);
							sendDeletedMessage(i);
							latestExposedChangeMap.remove(change);
						}
						if (change.isPrivateAction()) {
							commonUnExpose(entry, false, true);
							latestExposedChangeMap.remove(change);
						}
					}

					if (!change.isPrivateAction()) {
						if (i.getOp() == OPCAT_EXPOSE_OP.UPDATE) {
							updateExposeData(i);
							sendChangedeMessage(i);
							latestExposedChangeMap.remove(change);
						}

						if (i.getOp() == OPCAT_EXPOSE_OP.CHANGE_INTERFACE) {
							updateExposeData(i);
							sendInterfaceChangedMEssage(i);
							latestExposedChangeMap.remove(change);
						}
					}

					myProject.getSystemStructure()
							.getEntry(change.getEntryID()).getLogicalEntity()
							.setExposedChanged(false);

				}

			}

			// OpcatExposeList.getInstance().refresh(getExposedList(null,
			// true));

		} catch (Exception e) {
			commit.add(new OpcatExposeError(e.getLocalizedMessage(), e
					.getCause().toString()));
			OpcatLogger.logError(e, false);
		}

		return commit;
	}

	public void removeExposeFromDB(OpcatExposeEntity entity) throws Exception {
		removeExposeFromDB(entity.getKey());

	}

	public static void removeExposeFromDB(OpcatExposeKey key) throws Exception {

		handleOnline();

		String insertKey;
		Statement stat;

		OpcatExposeEntity entity = getServerExpose(key);

		insertExposeData(entity);

		if (entity.getKey().getId() != OpcatExposeConstants.EXPOSE_UNDEFINED_ID) {

			String update = "UPDATE " + OpcatExposeConstants.TABLE_EXPOSE_DATA
					+ " SET "
					+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI
					+ " =  ( select distinct "
					+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI
					+ " from " + OpcatExposeConstants.TABLE_EXPOSE + " where "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + " = "
					+ entity.getKey().getId() + ") WHERE "
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID
					+ " =  ( select distinct "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + " from "
					+ OpcatExposeConstants.TABLE_EXPOSE + " where "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + " = "
					+ entity.getKey().getId() + ")";
			stat = OpcatDatabaseConnection.getInstance().getConnection()
					.createStatement();
			stat.executeUpdate(update);

			update = "UPDATE " + OpcatExposeConstants.TABLE_EXPOSE_DATA
					+ " SET " + OpcatExposeConstants.FIELD_EXPOSE_ENTITY_ID
					+ " =  ( select distinct "
					+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_ID + " from "
					+ OpcatExposeConstants.TABLE_EXPOSE + " where "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + " = "
					+ entity.getKey().getId() + ") WHERE "
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID
					+ " =  ( select distinct "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + " from "
					+ OpcatExposeConstants.TABLE_EXPOSE + " where "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + " = "
					+ entity.getKey().getId() + ")";
			stat = OpcatDatabaseConnection.getInstance().getConnection()
					.createStatement();
			stat.executeUpdate(update);
		}

		insertKey = "DELETE FROM " + OpcatExposeConstants.TABLE_EXPOSE
				+ " WHERE " + OpcatDatabaseConstants.FIELD_NAME_ID + " = "
				+ entity.getKey().getId();
		stat = OpcatDatabaseConnection.getInstance().getConnection()
				.createStatement();
		stat.executeUpdate(insertKey);

	}

	public void updateExposeDescription(OpcatExposeKey key, String description) {
		if (OpcatMCManager.isOnline()) {

			try {

				long exposeID = getExposeServerID(key);
				updateExposeDataField(String.valueOf(exposeID),
						OpcatExposeConstants.FIELD_EXPOSE_DATA_DESCRIPTION,
						description);
			} catch (SQLException e) {
				OpcatLogger.logError(e);
			} catch (Exception e) {
				OpcatLogger.logError(e);

			}
		}
	}

	private void updateExposeDataField(String exposeID, String fieldName,
			String FieldData) throws Exception {

		handleOnline();

		String dataValues = "'" + FieldData + "'";

		String whereClause = OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID
				+ " = " + exposeID;

		/**
		 * we always add to the data table keeping history there.
		 */
		String insertData = " UPDATE " + OpcatExposeConstants.TABLE_EXPOSE_DATA
				+ " SET " + fieldName + " = " + dataValues + " WHERE "
				+ whereClause;

		Statement stat = OpcatDatabaseConnection.getInstance().getConnection()
				.createStatement();
		stat.executeUpdate(insertData);

	}

	private void updateExposeData(OpcatExposeEntity entity) throws Exception {

		handleOnline();

		Lookup typelookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatExposeConstants.TABLE_EXPOSE_OPM_ENTITY_TYPE),
				OPCAT_EXPOSE_ENTITY_TYPES.class);

		Lookup oplookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatExposeConstants.TABLE_EXPOSE_OP), OPCAT_EXPOSE_OP.class);

		Lookup statuslookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatExposeConstants.TABLE_EXPOSE_STATUS),
				OPCAT_EXPOSE_STATUS.class);

		String dataFieldsNames = OpcatExposeConstants.FIELD_EXPOSE_DATA_DESCRIPTION
				+ " = '"
				+ entity.getDescription()
				+ "'"
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_ENTITY_DESCRIPTION
				+ " = '"
				+ entity.getOpmEntityDescription()
				+ "'"
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_ENTITY_NAME
				+ " = '"
				+ entity.getOpmEntityName()
				+ "'"
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_ENTITY_TYPE
				+ " = "
				+ typelookup.getId(entity.getOpmEntityType())
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID
				+ " = "
				+ getExposeServerID(entity.getKey().getModelEncodedURI(),
						entity.getKey().getOpmEntityID())
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_OP
				+ " = "
				+ oplookup.getId(entity.getOp())
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_STATUS
				+ " = "
				+ statuslookup.getId(entity.getStatus())
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_MODEL_ID
				+ " = '"
				+ entity.getModelID()
				+ "'"
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_MODEL_NAME
				+ " = '"
				+ entity.getModelName()
				+ "'"
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_OP_USER
				+ " = '"
				+ entity.getUserID() + "'";

		/**
		 * we always add to the data table keeping history there.
		 */
		String insertData = "UPDATE " + OpcatExposeConstants.TABLE_EXPOSE_DATA
				+ " SET " + dataFieldsNames + " where "
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID + " = "
				+ entity.getKey().getId();

		Statement stat = OpcatDatabaseConnection.getInstance().getConnection()
				.createStatement();
		stat.executeUpdate(insertData);

	}

	private static void insertExposeData(OpcatExposeEntity entity)
			throws Exception {

		handleOnline();

		String dataFieldsNames = OpcatExposeConstants.FIELD_EXPOSE_DATA_DESCRIPTION
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_ENTITY_DESCRIPTION
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_ENTITY_NAME
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_ENTITY_TYPE
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_OP
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_STATUS
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_MODEL_ID
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_MODEL_NAME
				+ ","
				+ OpcatExposeConstants.FIELD_EXPOSE_DATA_OP_USER;

		Lookup typelookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatExposeConstants.TABLE_EXPOSE_OPM_ENTITY_TYPE),
				OPCAT_EXPOSE_ENTITY_TYPES.class);

		Lookup oplookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatExposeConstants.TABLE_EXPOSE_OP), OPCAT_EXPOSE_OP.class);

		Lookup statuslookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatExposeConstants.TABLE_EXPOSE_STATUS),
				OPCAT_EXPOSE_STATUS.class);

		String dataValues = "'"
				+ entity.getDescription()
				+ "','"
				+ entity.getOpmEntityDescription()
				+ "','"
				+ entity.getOpmEntityName()
				+ "',"
				+ typelookup.getId(entity.getOpmEntityType())
				+ ","
				+ getExposeServerID(entity.getKey().getModelEncodedURI(),
						entity.getKey().getOpmEntityID()) + ","
				+ oplookup.getId(entity.getOp()) + ","
				+ statuslookup.getId(entity.getStatus()) + ",'"
				+ entity.getModelID() + "','" + entity.getModelName() + "','"
				+ entity.getUserID() + "'";

		/**
		 * we always add to the data table keeping history there.
		 */
		String insertData = "INSERT INTO "
				+ OpcatExposeConstants.TABLE_EXPOSE_DATA + " ("
				+ dataFieldsNames + ") values (" + dataValues + ")";

		Statement stat = OpcatDatabaseConnection.getInstance().getConnection()
				.createStatement();
		stat.executeUpdate(insertData);

	}

	public void addExposeToDB(OpcatExposeEntity entity) throws Exception {

		handleOnline();

		boolean newEntity = (entity.getKey().getId() == OpcatExposeConstants.EXPOSE_UNDEFINED_ID);

		String keyFieldsNames = OpcatExposeConstants.FIELD_EXPOSE_ENTITY_ID
				+ "," + OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI;

		String insertKey;

		String keyValues = entity.getKey().getOpmEntityID() + "," + "'"
				+ entity.getKey().getModelEncodedURI() + "'";

		if (newEntity) {

			insertKey = "INSERT INTO " + OpcatExposeConstants.TABLE_EXPOSE
					+ " (" + keyFieldsNames + ") values (" + keyValues + ")";
			Statement stat = OpcatDatabaseConnection.getInstance()
					.getConnection().createStatement();
			stat.executeUpdate(insertKey);

		}

		insertExposeData(entity);

	}

	public boolean updateExposeInDB(OpcatExposeEntity entity)
			throws SQLException {

		boolean ret = false;

		try {

			handleOnline();

			insertExposeData(entity);

			ret = true;

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

		return ret;

	}

	private static boolean isPrivateIncluded(OpcatExposeEntity entity,
			String modelURI) {
		try {

			handleOnline();

			String query = "select distinct * from "
					+ OpcatExposeConstants.TABLE_EXPOSE_ALLOWED;

			Statement stat = OpcatDatabaseConnection.getInstance()
					.getConnection().createStatement(
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stat.executeQuery(query);

			return rs.next();

		} catch (SQLException ex) {
			OpcatLogger.logError(ex);
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}

		return false;

	}

	public static OpcatExposeKey getExposeKey(long exposeID) throws Exception {

		Statement stat = OpcatDatabaseConnection.getInstance().getConnection()
				.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

		ResultSet rs = stat.executeQuery(" select distinct "
				+ OpcatDatabaseConstants.FIELD_NAME_ID + ","
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_ID + ","
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI + " from "
				+ OpcatExposeConstants.TABLE_EXPOSE + " where "
				+ OpcatDatabaseConstants.FIELD_NAME_ID + " = " + exposeID);
		if (rs.next()) {
			OpcatExposeKey key = new OpcatExposeKey(rs.getLong(1), rs
					.getString(3), rs.getLong(2), false);
			return key;
		} else {
			return null;
		}

	}

	public static OpcatExposeKey getExposeKey(String modelEncodedRepoPath,
			long opmEntityID) throws Exception {

		if (!OpcatMCManager.isOnline()) {
			return null;
		}

		Statement stat = OpcatDatabaseConnection.getInstance().getConnection()
				.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

		ResultSet rs = stat.executeQuery(" select distinct "
				+ OpcatDatabaseConstants.FIELD_NAME_ID + ","
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_ID + ","
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI + " from "
				+ OpcatExposeConstants.TABLE_EXPOSE + " where "
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_ID + " = "
				+ opmEntityID + " AND "
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI + " = '"
				+ modelEncodedRepoPath + "'");
		if (rs.next()) {
			OpcatExposeKey key = new OpcatExposeKey(rs.getLong(1), rs
					.getString(3), rs.getLong(2), false);
			return key;
		} else {
			return null;
		}

	}

	/**
	 * get all exposed keys originated from exposed entities from the given
	 * file.
	 * 
	 * @param modelEncodedRepoPath
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<OpcatExposeKey> getExposeKey(
			String modelEncodedRepoPath) throws Exception {

		ArrayList<OpcatExposeKey> keys = new ArrayList<OpcatExposeKey>();

		Statement stat = OpcatDatabaseConnection.getInstance().getConnection()
				.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

		ResultSet rs = stat.executeQuery(" select distinct "
				+ OpcatDatabaseConstants.FIELD_NAME_ID + ","
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_ID + ","
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI + " from "
				+ OpcatExposeConstants.TABLE_EXPOSE + " where "
				+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI + " = '"
				+ modelEncodedRepoPath + "'");
		while (rs.next()) {
			OpcatExposeKey key = new OpcatExposeKey(rs.getLong(1), rs
					.getString(3), rs.getLong(2), false);
			keys.add(key);
		}
		return keys;

	}

	/**
	 * getting an expose entity using null as the asking project = no security
	 * on the expose.
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static OpcatExposeEntity getServerExpose(OpcatExposeKey key)
			throws Exception {

		if (key == null) {
			return null;
		}

		ArrayList<OpcatExposeEntity> entities = getServerExposedList(null, key);
		if (entities.size() == 1) {
			return entities.get(0);
		} else if (entities.size() == 0) {
			return null;
		} else {
			throw (new Exception("More the one entity with expose key : "
					+ key.getKey()));
		}
	}

	/**
	 * get expose list using myProject as the asking project ang getting all
	 * exposed. (null) as key
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<OpcatExposeEntity> getExposedList(
			OpcatExposeKey lookedFor, boolean getAlsoPrivate) throws Exception {
		ArrayList<OpcatExposeEntity> ret = new ArrayList<OpcatExposeEntity>();
		if (lookedFor != null) {
			lookedFor.refreshID();
		}
		ret.addAll(getServerExposedList(myProject, lookedFor));
		if (getAlsoPrivate)
			ret = addAndMarkPrivates(lookedFor, ret);
		return ret;
	}

	private ArrayList<OpcatExposeEntity> addAndMarkPrivates(
			OpcatExposeKey lookedFor, ArrayList<OpcatExposeEntity> publicExposed)
			throws Exception {
		ArrayList<OpcatExposeEntity> ret = new ArrayList<OpcatExposeEntity>();

		Enumeration<Entry> entries = myProject.getSystemStructure()
				.getElements();
		while (entries.hasMoreElements()) {
			Entry entry = entries.nextElement();
			if (entry.getLogicalEntity().isPrivateExposed()) {
				String description = entry.getDescription();
				String modelname = myProject.getName();
				String modelid = myProject.getGlobalID();
				String opmentitydescription = entry.getDescription();
				String opmentityname = entry.getName();

				OPCAT_EXPOSE_ENTITY_TYPES opmentitytype = (entry
						.getLogicalEntity() instanceof OpmObject ? OPCAT_EXPOSE_ENTITY_TYPES.OBJECT
						: OPCAT_EXPOSE_ENTITY_TYPES.PROCESS);

				OPCAT_EXPOSE_STATUS status = OPCAT_EXPOSE_STATUS.NORMAL;

				String modeluri = myProject.getMcURL().getURIEncodedPath();

				long exposeid = OpcatExposeConstants.EXPOSE_UNDEFINED_ID;

				long opmentityid = entry.getLogicalEntity().getId();

				OpcatExposeKey key = new OpcatExposeKey(exposeid, modeluri,
						opmentityid, true);

				OPCAT_EXPOSE_OP op = OPCAT_EXPOSE_OP.ADD;

				OpcatExposeEntity expose = new OpcatExposeEntity(key,
						opmentitytype, opmentitydescription, modelid,
						modelname, null, description, opmentityname, status, op);

				boolean marked = false;
				for (OpcatExposeEntity publicEntity : publicExposed) {
					OpcatExposeKey publicKey = publicEntity.getKey();
					if (key.equals(publicKey)) {
						marked = true;
						publicEntity.getKey().setPrivate(true);
						if (lookedFor != null) {
							if (lookedFor.equals(publicEntity.getKey())) {
								ret.add(publicEntity);
							}
						} else {
							ret.add(publicEntity);
						}
						break;
					}
				}
				if (!marked) {
					if (lookedFor != null) {
						if (lookedFor.equals(expose.getKey())) {
							ret.add(expose);
						}
					} else {
						ret.add(expose);
					}
				}
			}
		}

		boolean found = false;

		for (OpcatExposeEntity publicEntity : publicExposed) {
			found = false;
			for (OpcatExposeEntity privateEntity : ret) {
				if (privateEntity.getKey().equals(publicEntity.getKey())) {
					found = true;
					break;
				}
			}
			if (!found) {
				if (lookedFor != null) {
					if (lookedFor.equals(publicEntity.getKey())) {
						ret.add(publicEntity);
					}
				} else {
					ret.add(publicEntity);
				}
			}
		}
		return ret;

	}

	/**
	 * ig key is null ALL expose entities will be returned
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static ArrayList<OpcatExposeEntity> getServerExposedList(
			OpdProject askingProject, OpcatExposeKey lookedFor)
			throws Exception {

		ArrayList<OpcatExposeEntity> ret = new ArrayList<OpcatExposeEntity>();

		if (!OpcatMCManager.isOnline()) {
			return ret;
		}

		try {
			handleOnline();

			Statement stat = OpcatDatabaseConnection.getInstance()
					.getConnection().createStatement(
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = "SELECT DISTINCT " + "t1."
					+ OpcatDatabaseConstants.FIELD_NAME_ID + "," + "t2."
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_MODEL_ID + ","
					+ "t2." + OpcatExposeConstants.FIELD_EXPOSE_DATA_MODEL_NAME
					+ "," + "t1."
					+ OpcatExposeConstants.FIELD_EXPOSE_ENTITY_MODEL_URI + ","
					+ "t1." + OpcatExposeConstants.FIELD_EXPOSE_ENTITY_ID + ","
					+ "t2."
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_ENTITY_NAME + ","
					+ "t3." + OpcatDatabaseConstants.FIELD_NAME_NAME + ","
					+ "t2."
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_ENTITY_DESCRIPTION
					+ "," + "t2."
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_DESCRIPTION + ","
					+ "t2." + OpcatExposeConstants.FIELD_EXPOSE_DATA_OP_USER
					+ "," + "t4." + OpcatDatabaseConstants.FIELD_NAME_NAME
					+ "," + "t5." + OpcatDatabaseConstants.FIELD_NAME_NAME
					+ "  FROM " + OpcatExposeConstants.TABLE_EXPOSE + " t1";

			query = query + " LEFT JOIN (select distinct * " + " FROM "
					+ OpcatExposeConstants.TABLE_EXPOSE_DATA + " ) "
					+ " t2 on t1." + OpcatDatabaseConstants.FIELD_NAME_ID
					+ " = t2."
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_ID;

			query = query + " LEFT JOIN (select distinct "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + ","
					+ OpcatDatabaseConstants.FIELD_NAME_NAME + " FROM "
					+ OpcatExposeConstants.TABLE_EXPOSE_OPM_ENTITY_TYPE
					+ ") t3 on t2."
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_ENTITY_TYPE
					+ "= t3." + OpcatDatabaseConstants.FIELD_NAME_ID;

			query = query + " LEFT JOIN (select distinct "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + ","
					+ OpcatDatabaseConstants.FIELD_NAME_NAME + " FROM "
					+ OpcatExposeConstants.TABLE_EXPOSE_OP + ") t4 on t2."
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_OP
					+ " = t4." + OpcatDatabaseConstants.FIELD_NAME_ID;

			query = query + " LEFT JOIN (select distinct "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + ","
					+ OpcatDatabaseConstants.FIELD_NAME_NAME + " FROM "
					+ OpcatExposeConstants.TABLE_EXPOSE_STATUS + ") t5 on t2."
					+ OpcatExposeConstants.FIELD_EXPOSE_DATA_EXPOSE_STATUS
					+ " = t5." + OpcatDatabaseConstants.FIELD_NAME_ID;

			String where = (lookedFor == null ? "" : " where t1."
					+ OpcatDatabaseConstants.FIELD_NAME_ID + " = "
					+ lookedFor.getId());

			query = query + where;

			ResultSet rs = stat.executeQuery(query);

			while (rs.next()) {
				try {
					long id = rs.getLong(1);
					String modelID = rs.getString(2);
					String modelName = rs.getString(3);
					String modelURI = rs.getString(4);
					long opmEntityID = rs.getLong(5);
					String opmEntityName = rs.getString(6);

					OPCAT_EXPOSE_ENTITY_TYPES type = Enum.valueOf(
							OPCAT_EXPOSE_ENTITY_TYPES.class, rs.getString(7)
									.toUpperCase());

					String opmEntityDescription = rs.getString(8);
					String description = rs.getString(9);
					String user = rs.getString(10);

					OPCAT_EXPOSE_OP op = Enum.valueOf(OPCAT_EXPOSE_OP.class, rs
							.getString(11).toUpperCase());

					OPCAT_EXPOSE_STATUS status = Enum.valueOf(
							OPCAT_EXPOSE_STATUS.class, rs.getString(12)
									.toUpperCase());

					OpcatExposeKey key = new OpcatExposeKey(id, modelURI,
							opmEntityID, false);
					OpcatExposeEntity entity = new OpcatExposeEntity(key, type,
							opmEntityDescription, modelID, modelName, user,
							description, opmEntityName, status, op);

					boolean add = false;

					if ((askingProject != null)
							&& ((status == OPCAT_EXPOSE_STATUS.PRIVATE) || (status == OPCAT_EXPOSE_STATUS.PRIVATE_CHANGE_REQUEST))) {

						File file = new File(askingProject.getPath());
						SVNURL url = askingProject.getMcURL();

						if (url != null) {
							if (isPrivateIncluded(entity, url
									.getURIEncodedPath())) {
								add = true;
							}
						}

					} else {
						add = true;
					}

					if (add)
						ret.add(entity);
				} catch (Exception ex) {
					OpcatLogger.logError(ex);
				}
			}

		} catch (SQLException ex) {
			OpcatLogger.logError(ex);
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

		return ret;
	}

	public void sendInterfaceChangedMEssage(OpcatExposeEntity changed) {
		try {

			OpcatExposeEntity entity = changed;

			String message = entity.getOpmEntityName() + " interface changed";
			OPCAT_MESSAGES_OP op = OPCAT_MESSAGES_OP.EXPOSE_CHANGE_DONE;
			OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.ENTITY;
			OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.EXPOSE;
			OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.WARNING;
			OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.MODELS;

			String subject = entity.getOpmEntityName() + "@"
					+ entity.getKey().getModelDecodedURI();
			String username = null;
			try {
				username = OpcatUser.getCurrentUser().getName();
				if (username == null) {
					username = entity.getOpmEntityName() + "@"
							+ entity.getKey().getModelDecodedURI();
				}
			} catch (Exception ex) {

			}
			OpcatMessage opcatMessage = new OpcatMessage(username, OpcatUser
					.getCurrentUser().getName(), type, type, subsystem,
					subsystem, system, sevi, op, subject, message);
			OpcatMessagesManager.getInstance().sendMessage(opcatMessage);
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}
	}

	public void sendChangedeMessage(OpcatExposeEntity changed) {

		try {
			OpcatExposeEntity entity = changed;

			String message = entity.getOpmEntityName() + " changed";
			OPCAT_MESSAGES_OP op = OPCAT_MESSAGES_OP.EXPOSE_CHANGE_REQUEST;
			OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.ENTITY;
			OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.EXPOSE;
			OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.WARNING;
			OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.MODELS;

			String subject = entity.getOpmEntityName() + "@"
					+ entity.getKey().getModelDecodedURI();
			String username = null;
			try {
				username = OpcatUser.getCurrentUser().getName();
				if (username == null) {
					username = entity.getOpmEntityName() + "@"
							+ entity.getKey().getModelDecodedURI();
				}
			} catch (Exception ex) {

			}
			OpcatMessage opcatMessage = new OpcatMessage(username, OpcatUser
					.getCurrentUser().getName(), type, type, subsystem,
					subsystem, system, sevi, op, subject, message);
			OpcatMessagesManager.getInstance().sendMessage(opcatMessage);
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}
	}

	public void sendAddedMessage(OpcatExposeEntity changed) {
		try {
			OpcatExposeEntity entity = changed;

			String message = entity.getOpmEntityName() + " added";
			OPCAT_MESSAGES_OP op = OPCAT_MESSAGES_OP.EXPOSE_ADDED;
			OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.ENTITY;
			OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.EXPOSE;
			OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.WARNING;
			OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.MODELS;

			String subject = entity.getOpmEntityName() + "@"
					+ entity.getKey().getModelDecodedURI();
			String username = null;
			try {
				username = OpcatUser.getCurrentUser().getName();
				if (username == null) {
					username = entity.getOpmEntityName() + "@"
							+ entity.getKey().getModelDecodedURI();
				}
			} catch (Exception ex) {

			}

			OpcatMessage opcatMessage = new OpcatMessage(username, OpcatUser
					.getCurrentUser().getName(), type, type, subsystem,
					subsystem, system, sevi, op, subject, message);
			OpcatMessagesManager.getInstance().sendMessage(opcatMessage);
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}
	}

	public void sendDeletedMessage(OpcatExposeEntity changed) {
		try {

			OpcatExposeEntity entity = changed;

			String message = entity.getOpmEntityName() + " deleted";
			OPCAT_MESSAGES_OP op = OPCAT_MESSAGES_OP.EXPOSE_ADDED;
			String subject = entity.getOpmEntityName() + "@"
					+ entity.getKey().getModelDecodedURI();
			OPCAT_MESSAGES_TYPE type = OPCAT_MESSAGES_TYPE.ENTITY;
			OPCAT_MESSAGES_SUBSYSTEMS subsystem = OPCAT_MESSAGES_SUBSYSTEMS.EXPOSE;
			OPCAT_MESSAGES_SEVIRITY sevi = OPCAT_MESSAGES_SEVIRITY.WARNING;
			OPCAT_MESSAGES_SYSTEMS system = OPCAT_MESSAGES_SYSTEMS.MODELS;

			String username = null;
			try {
				username = OpcatUser.getCurrentUser().getName();
				if (username == null) {
					username = entity.getOpmEntityName() + "@"
							+ entity.getKey().getModelDecodedURI();
				}
			} catch (Exception ex) {

			}
			OpcatMessage opcatMessage = new OpcatMessage(username, OpcatUser
					.getCurrentUser().getName(), type, type, subsystem,
					subsystem, system, sevi, op, subject, message);
			OpcatMessagesManager.getInstance().sendMessage(opcatMessage);
		} catch (Exception e1) {
			OpcatLogger.logError(e1);
		}
	}

	public void treatExpsoeChange(Instance instance) {

		if (instance instanceof LinkInstance) {
			addLinkChange((LinkInstance) instance);
			return;
		}

		if (instance instanceof FundamentalRelationInstance) {
			addRelationChange((FundamentalRelationInstance) instance);
			return;
		}

		if (instance instanceof ObjectInstance) {
			addObjectTypeChange((ObjectInstance) instance);
			return;
		}

		return;
	}

	private void addLinkChange(LinkInstance li) {

		boolean check = (Configuration.getInstance().getProperty(
				"expose_treat_links_delete_as_change").equalsIgnoreCase("true"));

		if (!check)
			return;

		if (li.getSourceInstance() instanceof ProcessInstance) {
			if (li.getSourceInstance().getEntry().getLogicalEntity()
					.isExposed()) {
				if (li.getSourceInstance().getEntry().getLogicalEntity()
						.isPrivateExposed()) {

					addPrivateExposeChange(li.getSourceInstance(),
							OPCAT_EXPOSE_OP.CHANGE_INTERFACE);
				}
				if (li.getSourceInstance().getEntry().getLogicalEntity()
						.isPublicExposed()) {
					addPublicExposeChange(li.getSourceInstance(),
							OPCAT_EXPOSE_OP.CHANGE_INTERFACE);
				}
			}
		}

		if (li.getDestinationInstance() instanceof ProcessInstance) {
			if (li.getDestinationInstance().getEntry().getLogicalEntity()
					.isExposed()) {
				if (li.getDestinationInstance().getEntry().getLogicalEntity()
						.isPrivateExposed()) {

					addPrivateExposeChange(li.getDestinationInstance(),
							OPCAT_EXPOSE_OP.CHANGE_INTERFACE);
				}
				if (li.getDestinationInstance().getEntry().getLogicalEntity()
						.isPublicExposed()) {
					addPublicExposeChange(li.getDestinationInstance(),
							OPCAT_EXPOSE_OP.CHANGE_INTERFACE);
				}
			}
		}
	}

	private void addObjectTypeChange(ObjectInstance myInstance) {

		boolean check = (Configuration.getInstance().getProperty(
				"expose_treat_object_type_change_as_change")
				.equalsIgnoreCase("true"));

		if (!check)
			return;

		if (myInstance.getEntry().getLogicalEntity().isExposed()) {
			if (myInstance.getEntry().getLogicalEntity().isPublicExposed()) {
				myProject.getExposeManager().addPublicExposeChange(myInstance,
						OPCAT_EXPOSE_OP.CHANGE_INTERFACE);
			}

			if (myInstance.getEntry().getLogicalEntity().isPrivateExposed()) {
				myProject.getExposeManager().addPrivateExposeChange(myInstance,
						OPCAT_EXPOSE_OP.CHANGE_INTERFACE);
			}
		}
	}

	private void addRelationChange(FundamentalRelationInstance myInstance) {

		boolean check = (Configuration.getInstance().getProperty(
				"expose_treat_relation_delete_as_change")
				.equalsIgnoreCase("true"));

		if (!check)
			return;

		if (myInstance.getSourceInstance() instanceof ObjectInstance) {
			if (myInstance.getSourceInstance().getEntry().getLogicalEntity()
					.isExposed()) {
				if (myInstance.getSourceInstance().getEntry()
						.getLogicalEntity().isPrivateExposed()) {

					addPrivateExposeChange(myInstance.getSourceInstance(),
							OPCAT_EXPOSE_OP.CHANGE_INTERFACE);
				}

				if (myInstance.getSourceInstance().getEntry()
						.getLogicalEntity().isPublicExposed()) {
					addPublicExposeChange(myInstance.getSourceInstance(),
							OPCAT_EXPOSE_OP.CHANGE_INTERFACE);
				}
			}
		}
	}
}
