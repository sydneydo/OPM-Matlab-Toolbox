package expose.gui;

import expose.OpcatExposeConstants.OPCAT_EXOPSE_ADVISOR_TYPE;
import expose.logic.OpcatExposeAdvice;
import expose.logic.OpcatExposeAdvisorImpl;
import expose.logic.OpcatExposeInterfaceItem;
import extensionTools.validator.gui.PictureRenderer;
import gui.Opcat2;
import gui.images.opm.OPMImages;
import gui.images.standard.StandardImages;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmAgent;
import gui.opmEntities.OpmAggregation;
import gui.opmEntities.OpmBiDirectionalRelation;
import gui.opmEntities.OpmConditionLink;
import gui.opmEntities.OpmConsumptionEventLink;
import gui.opmEntities.OpmConsumptionLink;
import gui.opmEntities.OpmEffectLink;
import gui.opmEntities.OpmEntity;
import gui.opmEntities.OpmExceptionLink;
import gui.opmEntities.OpmExhibition;
import gui.opmEntities.OpmInstantination;
import gui.opmEntities.OpmInstrument;
import gui.opmEntities.OpmInstrumentEventLink;
import gui.opmEntities.OpmInvocationLink;
import gui.opmEntities.OpmObject;
import gui.opmEntities.OpmProcess;
import gui.opmEntities.OpmResultLink;
import gui.opmEntities.OpmSpecialization;
import gui.opmEntities.OpmState;
import gui.opmEntities.OpmUniDirectionalRelation;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Entry;
import gui.projectStructure.FundamentalRelationEntry;
import gui.projectStructure.GeneralRelationEntry;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.StateInstance;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import modelControl.OpcatMCManager;

import com.sciapp.renderers.BooleanRenderer;

public class OpcatExposeAdvisorGUI {

	private ConnectionEdgeInstance baseInstance;
	JMenuItem menu;
	private final String connectedString = "Add Connected ";
	private GridPanel panel;
	private OPCAT_EXOPSE_ADVISOR_TYPE advisorType;
	private String name = "";

	public OpcatExposeAdvisorGUI(ConnectionEdgeInstance instance,
			OPCAT_EXOPSE_ADVISOR_TYPE advisorType) {
		super();
		this.baseInstance = instance;
		this.advisorType = advisorType;

		if (advisorType == OPCAT_EXOPSE_ADVISOR_TYPE.INTERFACE_LOCAL) {
			name = " Interface ";
		} else if (advisorType == OPCAT_EXOPSE_ADVISOR_TYPE.PROERTIES_TO) {
			name = " Proprties (Son of) ";
		} else if (advisorType == OPCAT_EXOPSE_ADVISOR_TYPE.PROPERTIES_FROM) {
			name = " Proprties ";
		} else if (advisorType == OPCAT_EXOPSE_ADVISOR_TYPE.INTERFACE_ROOT) {
			name = " Interface (root parent) ";
		} else {
			name = "Unknown ";
		}
		menu = new JMenuItem(name + " Advisor");
		menu.addActionListener(new advisor());

	}

	public JMenuItem init() {
		return menu;
	}

	private void getAdvices() {

		if (baseInstance.getEntry().getLogicalEntity().getRolesManager()
				.getRolesCollection().size() > 0) {
			if (OpcatMCManager.isOnline()) {
				OpcatMCManager.getInstance().forceConnection();
			}
		}

		String name = baseInstance.getEntry().getName() + " - " + this.name
				+ " Advisor";

		GridPanel.RemovePanel(name);

		ArrayList<String> cols = new ArrayList<String>();
		cols.add("Name");
		cols.add("Type");
		cols.add("Source Name");
		cols.add("Model Name");
		cols.add("Link");
		cols.add("Status");

		panel = new GridPanel(cols);
		panel.setTabName(name);
		panel.GetColumnsModel().getColumn(4).setCellRenderer(
				new PictureRenderer());
		panel.GetColumnsModel().getColumn(1).setCellRenderer(
				new PictureRenderer());
		panel.GetColumnsModel().getColumn(5).setCellRenderer(
				new BooleanRenderer());
		panel.getGrid().addMouseListener(new MouseListner(panel));

		ArrayList<OpcatExposeAdvice> oa = new ArrayList<OpcatExposeAdvice>();

		ArrayList<ConnectionEdgeInstance> instances = new ArrayList<ConnectionEdgeInstance>();

		try {
			OpcatExposeAdvisorImpl advisor = new OpcatExposeAdvisorImpl(
					baseInstance, advisorType);

			oa.addAll(advisor.getAdvices());

			if (baseInstance instanceof ObjectInstance) {
				ObjectInstance objectInstance = (ObjectInstance) baseInstance;
				Enumeration<StateInstance> states = objectInstance
						.getStateInstances();

				while (states.hasMoreElements()) {
					StateInstance state = states.nextElement();
					if (advisorType == OPCAT_EXOPSE_ADVISOR_TYPE.INTERFACE_LOCAL) {
						advisor = new OpcatExposeAdvisorImpl(state, advisorType);
						oa.addAll(advisor.getAdvices());
						instances.add(state);
					}
				}
			}

			for (OpcatExposeAdvice o : oa) {
				for (OpcatExposeInterfaceItem j : o.getProperties()) {

					Object[] row = new Object[cols.size()];
					Object[] rowTag = new Object[2];

					rowTag[0] = o.getSourceInstance();
					rowTag[1] = j;

					row[0] = j.getRole().getThingName();
					row[1] = iconToType((OpmEntity) j.getRole().getThing());
					row[2] = o.getParent().getThingName();
					row[3] = o.getParent().getLibraryName();
					row[4] = iconToType(j.getLink());

					Entry link = ((OpdProject) j.getRole().getOntology()
							.getProjectHolder()).getSystemStructure().getEntry(
							j.getLink().getId());

					if (link instanceof LinkEntry) {
						row[5] = o.getSourceInstance().getOpd()
								.isRoleConnectedToInstanceWithLinK(j.getRole(),
										o.getSourceInstance(),
										((LinkEntry) link).getLinkType(),
										j.getDirection());
					} else if (link instanceof FundamentalRelationEntry) {
						row[5] = o.getSourceInstance().getOpd()
								.isRoleConnectedToInstanceWithRelation(
										j.getRole(),
										o.getSourceInstance(),
										((FundamentalRelationEntry) link)
												.getRelationType(),
										j.getDirection());
					} else if (link instanceof GeneralRelationEntry) {
						row[5] = o.getSourceInstance().getOpd()
								.isRoleConnectedToInstanceWithRelation(
										j.getRole(),
										o.getSourceInstance(),
										((GeneralRelationEntry) link)
												.getRelationType(),
										j.getDirection());
					}

					panel.getGrid().addRow(row, rowTag);
				}
			}
		} catch (Exception e) {
			OpcatLogger.logError(e);
		}

		panel.AddToExtensionToolsPanel();
	}

	protected ImageIcon iconToType(OpmEntity opm) {

		if (opm instanceof OpmConsumptionLink) {
			return OPMImages.RESULT_LINK;
		} else if (opm instanceof OpmConditionLink) {
			return OPMImages.CONDITION_LINK;
		} else if (opm instanceof OpmConsumptionEventLink) {
			return OPMImages.CONSUMPTION_EVENT_LINK;
		} else if (opm instanceof OpmEffectLink) {
			return OPMImages.EFFECT_LINK;
		} else if (opm instanceof OpmExceptionLink) {
			return OPMImages.EXCEPTION_LINK;
		} else if (opm instanceof OpmInstrument) {
			return OPMImages.INSTRUMENT_LINK;
		} else if (opm instanceof OpmInstrumentEventLink) {
			return OPMImages.INSTRUMENT_EVENT_LINK;
		} else if (opm instanceof OpmInvocationLink) {
			return OPMImages.INVOCATION_LINK;
		} else if (opm instanceof OpmAgent) {
			return OPMImages.AGENT_LINK;
		} else if (opm instanceof OpmResultLink) {
			return OPMImages.RESULT_LINK;
		} else if (opm instanceof OpmObject) {
			return OPMImages.OBJECT;
		} else if (opm instanceof OpmState) {
			return OPMImages.STATE;
		} else if (opm instanceof OpmAggregation) {
			return OPMImages.AGGREGATION;
		} else if (opm instanceof OpmExhibition) {
			return OPMImages.EXHIBITION;
		} else if (opm instanceof OpmInstantination) {
			return OPMImages.INSTANTIATION;
		} else if (opm instanceof OpmSpecialization) {
			return OPMImages.GENERALIZATION;
		} else if (opm instanceof OpmBiDirectionalRelation) {
			return OPMImages.BI_DIRECTIONAL;
		} else if (opm instanceof OpmUniDirectionalRelation) {
			return OPMImages.UNI_DIRECTIONAL;
		} else if (opm instanceof OpmProcess) {
			return OPMImages.PROCESS;
		}
		return StandardImages.EMPTY;
	}

	private void rightClickEvent(GridPanel panel, MouseEvent e) {

		JPopupMenu popup = panel.getRMenu();

		if ((panel.getGrid().getSelectedRow() >= 0) && (panel != null)
				&& (panel.getSelectedTag() != null)) {

			ConnectionEdgeInstance instance = (ConnectionEdgeInstance) panel
					.getSelectedTag()[0];

			OpcatExposeInterfaceItem rel = (OpcatExposeInterfaceItem) panel
					.getSelectedTag()[1];

			JMenuItem item = new JMenuItem(connectedString);
			item.addActionListener(new addAction(rel, true));
			popup.add(item);
			item = new JMenuItem("Add not Connected");
			item.addActionListener(new addAction(rel, false));
			popup.add(item);
		}
		popup.show(e.getComponent(), e.getX(), e.getY());
	}

	class advisor implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			getAdvices();
		}

	}

	class addAction implements ActionListener {

		boolean connected;
		OpcatExposeInterfaceItem item;

		public addAction(OpcatExposeInterfaceItem item, boolean connected) {

			this.item = item;
			this.connected = connected;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			try {

				OpcatExposeAdvisorImpl impl = new OpcatExposeAdvisorImpl(item
						.getSourceInsatance(), advisorType);

				ConnectionEdgeInstance source = item.getSourceInsatance();

				boolean isInZoomOPD = (source.getIOPD().getMainIInstance() != null);

				boolean hasParent = (source.getParentIThingInstance() != null);

				boolean isSourceTheMainInstance = isInZoomOPD
						&& (source.getKey().equals(source.getIOPD()
								.getMainIInstance().getKey()));

				if (source instanceof StateInstance) {
					hasParent = (((StateInstance) source)
							.getParentObjectInstance()
							.getParentIThingInstance() != null);

					isSourceTheMainInstance = isInZoomOPD
							&& ((((StateInstance) source)
									.getParentObjectInstance().getKey()
									.equals(source.getIOPD().getMainIInstance()
											.getKey())));
				}

				if (isInZoomOPD && (hasParent || isSourceTheMainInstance)) {
					int ret = JOptionPane.showOptionDialog(Opcat2.getFrame(),
							"Insert inside the main entity ? "
									+ source.getIOPD().getMainIInstance()
											.getIEntry().getName(), "Opcat II",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, null, null);

					if (ret == JOptionPane.NO_OPTION) {
						impl.implementAdvice(item, connected, false);
					} else if (ret == JOptionPane.YES_OPTION) {
						impl.implementAdvice(item, connected, true);
					}
				} else {
					impl.implementAdvice(item, connected, false);
				}

				getAdvices();

			} catch (Exception ex) {
				OpcatLogger.logError(ex);
			}
		}
	}

	class MouseListner extends MouseAdapter {
		GridPanel panel;

		public MouseListner(GridPanel panel) {
			super();
			this.panel = panel;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				// showThings();
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				rightClickEvent(panel, e);
			}
		}
	}

}
