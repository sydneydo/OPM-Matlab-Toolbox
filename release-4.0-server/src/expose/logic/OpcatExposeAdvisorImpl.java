package expose.logic;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import modelControl.OpcatMCManager;

import org.tmatesoft.svn.core.SVNURL;

import expose.OpcatExposeKey;
import expose.OpcatExposeManager;
import expose.OpcatExposeConstants.OPCAT_EXOPSE_ADVISOR_TYPE;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_LINK_DIRECTION;
import gui.Opcat2;
import gui.controls.FileControl;
import gui.dataProject.DataCreatorType;
import gui.metaLibraries.logic.MetaLibrary;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmEntity;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.GeneralRelationEntry;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkEntry;
import gui.util.OpcatLogger;

/**
 * implementation of an expose advisor.
 * 
 * @author raanan
 * 
 */
public class OpcatExposeAdvisorImpl {

	private OpcatExposeAdvisorInterface advisor;
	private ConnectionEdgeInstance sourceInstance;
	private OPCAT_EXOPSE_ADVISOR_TYPE myType;

	/**
	 * sourceEntry is the {@link ConnectionEdgeInstance} on which the advisor
	 * needs to work on
	 * 
	 * @param sourceInstance
	 * @param advisorType
	 * @throws Exception
	 */
	public OpcatExposeAdvisorImpl(ConnectionEdgeInstance sourceInstance,
			OPCAT_EXOPSE_ADVISOR_TYPE advisorType) throws Exception {
		if (advisorType == OPCAT_EXOPSE_ADVISOR_TYPE.INTERFACE_LOCAL) {
			advisor = new OpcatExposeInterfaceAdvisor(sourceInstance);
		} else if (advisorType == OPCAT_EXOPSE_ADVISOR_TYPE.PROPERTIES_FROM) {
			advisor = new OpcatExposeFromPropertiesAdvisor(sourceInstance);
		} else if (advisorType == OPCAT_EXOPSE_ADVISOR_TYPE.PROERTIES_TO) {
			advisor = new OpcatExposeToPropertiesAdvisor(sourceInstance);
		} else if (advisorType == OPCAT_EXOPSE_ADVISOR_TYPE.INTERFACE_ROOT) {
			advisor = new OpcatExposeGlobalInterfaceAdvisor(sourceInstance);
		} else {
			throw new Exception("No supported Advisor");
		}

		this.sourceInstance = sourceInstance;
		this.myType = advisorType;

	}

	public ArrayList<OpcatExposeAdvice> getAdvices() {
		return advisor.getAdvices();
	}

	public void implementAdviceTree(ConnectionEdgeInstance root,
			boolean connected) throws Exception {

		advisor.changeSourceInstance(root);
		ArrayList<OpcatExposeAdvice> advices = advisor.getAdvices();

		for (OpcatExposeAdvice i : advices) {
			for (OpcatExposeInterfaceItem j : i.getProperties()) {
				Instance ret = insert(j, connected, root, false);
				FileControl.getInstance().save(ret.getEntry().getMyProject());
				if (ret != null) {
					implementAdviceTree((ConnectionEdgeInstance) ret, connected);
				}
			}
		}

		advisor.changeSourceInstance(sourceInstance);

	}

	public Instance implementAdviceUsingPropertyName(String propertyName,
			boolean connected, boolean insertInsideMain) throws Exception {

		ArrayList<OpcatExposeAdvice> advices = advisor.getAdvices();
		OpcatExposeInterfaceItem item = null;
		int num = 0;
		for (OpcatExposeAdvice i : advices) {
			for (OpcatExposeInterfaceItem j : i.getProperties()) {
				if (j.getRole().getThingName().equalsIgnoreCase(propertyName)) {
					item = j;
					num++;
				}
			}
		}

		if (item == null)
			return null;

		if (num > 1) {
			throw new Exception(
					"Error : more then one option, returned by finder");
		}

		// try {
		return insert(item, connected, sourceInstance, insertInsideMain);
		// } catch (Exception ex) {
		// return null;
		// }
	}

	public boolean implementAdvice(OpcatExposeInterfaceItem item,
			boolean connected, boolean insertInsideMain) {
		// try {
		return (insert(item, connected, sourceInstance, insertInsideMain) != null);
		// } catch (Exception ex) {
		// return false;
		// }

		// return true;
	}

	/**
	 * this method will insert {@link ConnectionEdgeInstance} which is defined
	 * by the {@link OpcatExposeInterfaceItem interfaceItem}. It is used to
	 * insert a son from a remote model.
	 * 
	 * @param interfaceItem
	 *            - the son as defined in the remote model represented as an
	 *            {@link OpcatExposeInterfaceItem}
	 * @param connected
	 *            keep the inserted connected to the source model
	 * @param sourceInstance
	 *            source {@link ConnectionEdgeInstance} which is a pointer to a
	 *            remote Instance.
	 * @param insideMainInstance
	 *            if the source instance is inside a main instance then insert
	 *            outside or innside
	 * @return
	 */
	private Instance insert(OpcatExposeInterfaceItem interfaceItem,
			boolean connected, ConnectionEdgeInstance sourceInstance,
			boolean insideMainInstance) {

		try {
			Instance newInstance;

			boolean addRole = false;
			if (connected) {
				addRole = true;
			}

			if (insideMainInstance) {
				ConnectionEdgeInstance parent = (ConnectionEdgeInstance) sourceInstance
						.getParentIThingInstance();
				if (parent == null) {
					parent = (ConnectionEdgeInstance) sourceInstance.getIOPD()
							.getMainIInstance();
				}
				newInstance = MetaLibrary.insertConnectionEdge(interfaceItem
						.getRole(), sourceInstance.getOpd(), addRole,
						(ConnectionEdgeInstance) sourceInstance, parent);
			} else {
				newInstance = MetaLibrary.insertConnectionEdge(interfaceItem
						.getRole(), sourceInstance.getOpd(), addRole,
						(ConnectionEdgeInstance) sourceInstance, null);
			}

			newInstance.getEntry().getLogicalEntity().getRolesManager().clear();
			if (connected) {
				newInstance.getEntry().getLogicalEntity().getRolesManager()
						.addRole(interfaceItem.getRole());
			}
			newInstance.setAdvisor(true);

			if (newInstance == null) {
				return null;
			}

			// boolean template = sourceInstance.isTemplateInstance();
			if (addRole) {
				String encodedURL;
				if (((OpmEntity) interfaceItem.getRole().getThing())
						.isExposed()) {

					if ((interfaceItem.getRole().getOntology()
							.getReferenceType() == DataCreatorType.REFERENCE_TYPE_PRIVATE_FILE)
							|| (interfaceItem.getRole().getOntology()
									.getReferenceType() == DataCreatorType.REFERENCE_TYPE_TEMPLATE_FILE)) {
						// JOptionPane.showMessageDialog(Opcat2.getFrame(),
						// "Looking for URL "
						// + interfaceItem.getRole().getOntology()
						// .getPath());
						SVNURL url = ((OpdProject) interfaceItem.getRole()
								.getOntology().getProjectHolder()).getMcURL(); // .getPath()));
						encodedURL = url.getURIEncodedPath();
					} else {
						encodedURL = interfaceItem.getRole().getOntology()
								.getPath();
					}

					OpcatExposeKey key = OpcatExposeManager.getExposeKey(
							encodedURL, interfaceItem.getRole().getThingId());

					if (key == null) {
						// off line or private key
						key = new OpcatExposeKey(encodedURL, interfaceItem
								.getRole().getThingId(), true);
					}

					newInstance.setPointer(key);
				}
			}

			Entry relationEntry = ((OpdProject) interfaceItem.getRole()
					.getOntology().getProjectHolder()).getSystemStructure()
					.getEntry(interfaceItem.getLink().getId());

			int type = -1;
			if (relationEntry instanceof LinkEntry) {
				type = ((LinkEntry) relationEntry).getLinkType();
			} else if (relationEntry instanceof FundamentalRelationEntry) {
				type = ((FundamentalRelationEntry) relationEntry)
						.getRelationType();
			} else if (relationEntry instanceof GeneralRelationEntry) {
				type = ((GeneralRelationEntry) relationEntry).getRelationType();
			}

			if (interfaceItem.getDirection() == OPCAT_EXPOSE_LINK_DIRECTION.FROM) {

				if (relationEntry instanceof LinkEntry) {

					newInstance.getEntry().getMyProject().addLink(
							(ConnectionEdgeInstance) sourceInstance,
							(ConnectionEdgeInstance) newInstance, type);
				} else {
					newInstance.getEntry().getMyProject().addRelation(
							(ConnectionEdgeInstance) sourceInstance,
							(ConnectionEdgeInstance) newInstance, type);
				}

			} else {
				if (relationEntry instanceof LinkEntry) {
					newInstance.getEntry().getMyProject().addLink(
							(ConnectionEdgeInstance) newInstance,
							(ConnectionEdgeInstance) sourceInstance, type);
				} else {
					newInstance.getEntry().getMyProject().addRelation(
							(ConnectionEdgeInstance) newInstance,
							(ConnectionEdgeInstance) sourceInstance, type);
				}
			}
			newInstance.getEntry().getMyProject().setCanClose(false);

			return newInstance;
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
		return null;
	}
}
