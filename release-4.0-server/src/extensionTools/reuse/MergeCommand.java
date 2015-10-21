package extensionTools.reuse;

import exportedAPI.util.OpcatApiException;
import gui.Opcat2;
import gui.checkModule.CheckPrecedence;
import gui.opdProject.Opd;
import gui.opdProject.OpdProject;
import gui.opmEntities.OpmUniDirectionalRelation;
import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.FundamentalRelationInstance;
import gui.projectStructure.GeneralRelationInstance;
import gui.projectStructure.Instance;
import gui.projectStructure.LinkEntry;
import gui.projectStructure.LinkInstance;
import gui.projectStructure.ObjectEntry;
import gui.projectStructure.ObjectInstance;
import gui.projectStructure.ProcessInstance;
import gui.projectStructure.RelationEntry;
import gui.projectStructure.StateEntry;
import gui.projectStructure.StateInstance;
import gui.projectStructure.ThingEntry;
import gui.projectStructure.ThingInstance;
import gui.util.OpcatException;
import gui.util.OpcatLogger;

import java.util.Enumeration;
import java.util.LinkedList;

/**
 * Manages the merge operation - taking two elements and merging them together.
 * 
 * 
 * TODO: When dragging things with an inzoom into targets without, then things
 * that were within the container are placed outside the container 
 * 
 * TODO: When dragging things into unfolded things, then the name of the main entity in the
 * target does not change
 */
public class MergeCommand {
    OpdProject currentProject;

    OpdProject reusedProject = null;

    ReuseCommand reuseCommand;

    public LinkedList mergedOpds;

    public final static int MERGE_THINGS = 1;

    public final static int MERGE_OPD = 2;

    public final static int MERGE_SYSTEM = 3;

    /**
         * Constructor for the merge operation. Sets the current project
         * parameter. Does not carry any action.
         */
    public MergeCommand(OpdProject _currentProject) {
	this.currentProject = _currentProject;
	// creating a reuseCommand instance so we can use its reuse functions
	this.reuseCommand = new ReuseCommand(this.currentProject,
		this.currentProject, ReuseCommand.NO_REUSE);
    }

    /**
         * Performs the merge action between two things.
         * 
         * @param draggedInstance
         *                The thing which is going to be absorbed into the
         *                target, and is going to be deleted
         * @param targetInstance
         *                The thing which is going to remain after the merge
         * @throws OpcatApiException
         *                 Thrown when the dragged thing has inzooming or
         *                 unfolding levels
         */
    public boolean commitMerge(ThingInstance draggedInstance,
	    ThingInstance targetInstance, boolean addStates)
	    throws OpcatApiException {
	if (!this.isInstancesTupleOfObjectsOrProcesses(draggedInstance,
		targetInstance)) {
	    return false;
	}
	// Handling Zooming in
	ThingEntry draggedEntry = (ThingEntry) draggedInstance.getEntry();
	if (draggedEntry.getZoomedInOpd() != null) {
	    throw new OpcatApiException(
		    "Things with inzooming levels cannot be merged");
	    // mergeZoominLevels(draggedInstance, targetInstance);
	}
	// Handling unfolding
	if (draggedInstance.getUnfoldingOpd() != null) {
	    throw new OpcatApiException(
		    "Things with unfolding levels cannot be merged");
	    // mergeUnfoldedLevels(draggedInstance, targetInstance);
	}
	// in case the drag and drop is within objects copy the states
	if (addStates && (draggedInstance instanceof ObjectInstance)) {
	    this.reuseCommand.addReusedStates((ObjectInstance) targetInstance,
		    (ObjectInstance) draggedInstance,
		    ((ObjectInstance) targetInstance).getOpd());
	}
	// link and relations copying
	this.mergeRelations(draggedInstance, targetInstance);

	// deleting source and everything that is connected to it
	this.cleanUpMerge(draggedInstance);

	// Updating the model, and refreshing the OPL
	this.currentProject.updateChanges();
	this.currentProject.refresh();
	Opcat2.getUndoManager().discardAllEdits();
	return true;
    }

    /**
         * Handles copying of zoomin levels between the draffed thing and the
         * target thing.
         * 
         * @param draggedInstance
         *                source (deleted) thing
         * @param targetInstance
         *                target (survived) thing
         */
    protected void mergeZoominLevels(ThingInstance draggedInstance,
	    ThingInstance targetInstance) {
	ThingEntry sourceEntry = (ThingEntry) draggedInstance.getEntry();
	ThingEntry targetEntry = (ThingEntry) targetInstance.getEntry();
	Opd opdSource = sourceEntry.getZoomedInOpd(); // Used for inner level
	// copying - as the OPD
	// of the dragged thing
	Opd opdTarget = targetEntry.getZoomedInOpd();
	; // Used for inner level copying - as the OPD of the target thing

	ThingEntry thEntry = (ThingEntry) opdSource.getMainInstance()
		.getIXEntry();
	thEntry.setZoomedInOpd(null);
	if (opdTarget == null) {
	    this.currentProject.zoomIn(targetInstance);
	    opdTarget = targetEntry.getZoomedInOpd();
	    // add the main thing
	}
	this.reusedProject = this.currentProject;
	try {
	    this.reuseCommand.addReusedOpd(opdSource, opdTarget, null, false);
	} catch (OpcatException e) {
	    OpcatLogger.logError(e);
	}
	this.currentProject.deleteRootOpd(opdSource);
    }

    /**
         * Handles copying of unfolded levels between the draffed thing and the
         * target thing.
         * 
         * @param draggedInstance
         *                source (deleted) thing
         * @param targetInstance
         *                target (survived) thing
         */
    protected void mergeUnfoldedLevels(ThingInstance draggedInstance,
	    ThingInstance targetInstance) {
	Opd opdSource = draggedInstance.getUnfoldingOpd();

	targetInstance.setUnfoldingOpd(opdSource);
	draggedInstance.removeUnfoldingOpd();
	ThingInstance oldMainInstance = opdSource.getMainInstance();
	ThingInstance newMainInstance = this.createMainInstance(
		draggedInstance, targetInstance, opdSource);
	try {
	    this.commitMerge(oldMainInstance, newMainInstance, true);
	} catch (OpcatApiException ex) {
	}
	this.currentProject.setCurrentOpd(opdSource.getParentOpd());
	// if (opdTarget == null) {
	// opdTarget = currentProject
	// .createNewOpd(false, (Instance) targetInstance, currentProject
	// .getCurrentOpd());
	// }
	// reuseCommand.addReusedOpd(opdSource, opdTarget, null, false);
	// currentProject.deleteRootOpd(opdSource);
    }

    private ThingInstance createMainInstance(ThingInstance draggedInstance,
	    ThingInstance targetInstance, Opd opdSource) {
	ThingInstance added = null;
	this.currentProject.setCurrentOpd(opdSource);
	if (draggedInstance instanceof ObjectInstance) {
	    added = this.currentProject.addObjectInstance(draggedInstance
		    .getEntry().getName(), targetInstance.getX(),
		    targetInstance.getY(), (ThingEntry) draggedInstance
			    .getEntry());
	} else {
	    added = this.currentProject.addProcessInstance(draggedInstance
		    .getEntry().getName(), targetInstance.getX(),
		    targetInstance.getY(), (ThingEntry) draggedInstance
			    .getEntry());
	}
	return added;
    }

    protected void mergeRelations(ThingInstance draggedInstance,
	    ThingInstance targetInstance) {
	// link and relations copying
	ThingEntry draggedEntry = (ThingEntry) draggedInstance.getEntry();
	ThingEntry targetEntry = (ThingEntry) targetInstance.getEntry();
	LinkInstance linkInstance;
	long linkInstanceSourceEntryID = 0;
	long linkInstanceDestinationEntryID = 0;
	LinkEntry linkEntry;
	Instance tmpImstance;
	GeneralRelationInstance g_relationInstance = null;
	FundamentalRelationInstance f_relationInstance = null;
	RelationEntry rEntry;
	CheckPrecedence check = new CheckPrecedence();

	Enumeration relatedInstances = draggedInstance.getRelatedInstances();
	while (relatedInstances.hasMoreElements()) {
	    tmpImstance = (Instance) relatedInstances.nextElement();
	    // link section
	    // /////////////
	    if (tmpImstance instanceof LinkInstance) {
		linkInstance = (LinkInstance) tmpImstance;
		linkEntry = (LinkEntry) linkInstance.getIXEntry();
		if (linkInstance.getSourceInstance() instanceof ThingInstance) {
		    linkInstanceSourceEntryID = linkInstance
			    .getSourceInstance().getEntry().getId();
		} else if (linkInstance.getSourceInstance() instanceof StateInstance) {
		    linkInstanceSourceEntryID = ((StateEntry) (linkInstance
			    .getSourceInstance().getEntry()))
			    .getParentObjectId();
		}

		if (linkInstance.getDestinationInstance() instanceof ThingInstance) {
		    linkInstanceDestinationEntryID = linkInstance
			    .getDestinationInstance().getEntry().getId();
		} else if (linkInstance.getDestinationInstance() instanceof StateInstance) {
		    linkInstanceDestinationEntryID = ((StateEntry) (linkInstance
			    .getDestinationInstance().getEntry()))
			    .getParentObjectId();
		}
		// cases - if the sourc and target of this link are the dragged
		// and dropped
		// elements remove it !!!!
		if (((linkInstanceSourceEntryID == draggedEntry.getId()) && (linkInstanceDestinationEntryID == targetEntry
			.getId()))
			|| ((linkInstanceSourceEntryID == targetEntry.getId()) && (linkInstanceDestinationEntryID == draggedEntry
				.getId()))) {
		    this.currentProject.deleteLink(linkInstance);
		    continue;
		}
		// ThingInstance otherSideInstance = null;
		ConnectionEdgeInstance otherSideInstance = null;
		if (linkInstanceSourceEntryID == draggedEntry.getId()) {
		    otherSideInstance = (ConnectionEdgeInstance) linkInstance
			    .getDestinationInstance();
		} else if (linkInstance.getSourceIXInstance() instanceof ConnectionEdgeInstance) {
		    otherSideInstance = (ConnectionEdgeInstance) linkInstance
			    .getSourceIXInstance();
		}
		if (otherSideInstance != null) {
		    Enumeration e = otherSideInstance.getRelatedInstances();
		    while (e.hasMoreElements()) {
			Instance ele = (Instance) e.nextElement();
			if (ele instanceof LinkInstance) {
			    LinkInstance lIns = (LinkInstance) ele;
			    if ((lIns.getSource().getEntry().getId() == targetEntry
				    .getId())
				    || (lIns.getDestination().getEntry()
					    .getId() == targetEntry.getId())) {
				if (check.checkPredenceBetweenInstances(lIns,
					linkInstance) == lIns) {
				    this.currentProject
					    .deleteLink(linkInstance);
				    continue;
				} else {
				    this.currentProject.deleteLink(lIns);
				}
			    }

			}
			if (ele instanceof GeneralRelationInstance) {
			    GeneralRelationInstance gIns = (GeneralRelationInstance) ele;
			    if ((gIns.getSource().getEntry().getId() == targetEntry
				    .getId())
				    || (gIns.getDestination().getEntry()
					    .getId() == targetEntry.getId())) {
				if (check.checkPredenceBetweenInstances(gIns,
					linkInstance).getKey() == gIns.getKey()) {
				    this.currentProject
					    .deleteLink(linkInstance);
				    continue;
				} else {
				    this.currentProject
					    .deleteGeneralRelation(gIns);
				}
			    }

			}

			if (ele instanceof FundamentalRelationInstance) {
			    FundamentalRelationInstance fIns = (FundamentalRelationInstance) ele;
			    if ((fIns.getSourceInstance().getEntry().getId() == targetEntry
				    .getId())
				    || (fIns.getDestination().getEntry()
					    .getId() == targetEntry.getId())) {
				if (check.checkPredenceBetweenInstances(fIns,
					linkInstance).getKey() == fIns.getKey()) {
				    this.currentProject
					    .deleteLink(linkInstance);
				    continue;
				} else {
				    this.currentProject
					    .deleteFundamentalRelation(fIns);
				}
			    }
			}
		    }
		}
		if (linkInstanceSourceEntryID == draggedEntry.getId()) {
		    ConnectionEdgeInstance myInstance = targetInstance;
		    if (linkInstance.getSourceInstance() instanceof StateInstance) {
			/**
                         * a patch but i don't see a way to get the state other
                         * then compare names.
                         * 
                         */
			StateEntry state = ((ObjectEntry) targetEntry)
				.getState(linkInstance.getSourceInstance()
					.getEntry().getName());
			Enumeration<Instance> stateEnum = state.getInstances();
			while (stateEnum.hasMoreElements()) {
			    StateInstance stateInstance = (StateInstance) stateEnum
				    .nextElement();
			    if ((stateInstance.getKey().getOpdId() == linkInstance
				    .getKey().getOpdId())
				    && (stateInstance.getEntry().getName()
					    .equalsIgnoreCase(linkInstance
						    .getSourceInstance()
						    .getEntry().getName()))) {
				myInstance = stateInstance;
				break;
			    }
			}
		    }
		    this.currentProject.addLink(myInstance, linkInstance
			    .getDestinationInstance(), linkEntry.getLinkType());
		} else {
		    this.currentProject.addLink(
			    (ConnectionEdgeInstance) linkInstance
				    .getSourceIInstance(),
			    (ConnectionEdgeInstance) targetInstance, linkEntry
				    .getLinkType());
		}
		this.currentProject.deleteLink(linkInstance);

		// end if link section
	    }

	    // general relation section
	    // /////////////////////////
	    if (tmpImstance instanceof GeneralRelationInstance) {
		g_relationInstance = (GeneralRelationInstance) tmpImstance;
		rEntry = (RelationEntry) g_relationInstance.getIXEntry();
		// cases - if the sourc and target of this relation are the
		// dragged and dropped
		// elements remove it !!!!
		if (((g_relationInstance.getSource().getEntry().getId() == draggedEntry
			.getId()) && (g_relationInstance.getDestination()
			.getEntry().getId() == targetEntry.getId()))
			|| ((g_relationInstance.getSource().getEntry().getId() == targetEntry
				.getId()) && (g_relationInstance
				.getDestination().getEntry().getId() == draggedEntry
				.getId()))) {
		    this.currentProject
			    .deleteGeneralRelation(g_relationInstance);
		    continue;
		}
		ThingInstance otherSideInstance;
		if (g_relationInstance.getSource().getEntry().getId() == draggedEntry
			.getId()) {
		    otherSideInstance = (ThingInstance) g_relationInstance
			    .getDestinationInstance();
		} else {
		    otherSideInstance = (ThingInstance) g_relationInstance
			    .getSourceIXInstance();
		}

		Enumeration e = otherSideInstance.getRelatedInstances();
		while (e.hasMoreElements()) {
		    Instance ele = (Instance) e.nextElement();
		    if (ele instanceof LinkInstance) {
			LinkInstance lIns = (LinkInstance) ele;
			if ((lIns.getSource().getEntry().getId() == targetEntry
				.getId())
				|| (lIns.getDestination().getEntry().getId() == targetEntry
					.getId())) {
			    if (check.checkPredenceBetweenInstances(lIns,
				    g_relationInstance) == lIns) {
				this.currentProject
					.deleteGeneralRelation(g_relationInstance);
				continue;
			    } else {
				this.currentProject.deleteLink(lIns);
			    }
			}
		    }
		    if (ele instanceof GeneralRelationInstance) {
			GeneralRelationInstance gIns = (GeneralRelationInstance) ele;
			if ((gIns.getSource().getEntry().getId() == targetEntry
				.getId())
				|| (gIns.getDestination().getEntry().getId() == targetEntry
					.getId())) {
			    if (check.checkPredenceBetweenInstances(gIns,
				    g_relationInstance).getKey() == gIns
				    .getKey()) {
				this.currentProject
					.deleteGeneralRelation(g_relationInstance);
				continue;
			    } else {
				this.currentProject.deleteGeneralRelation(gIns);
			    }
			}
		    }
		    if (ele instanceof FundamentalRelationInstance) {
			FundamentalRelationInstance fIns = (FundamentalRelationInstance) ele;
			if ((fIns.getSourceInstance().getEntry().getId() == targetEntry
				.getId())
				|| (fIns.getDestination().getEntry().getId() == targetEntry
					.getId())) {
			    if (check.checkPredenceBetweenInstances(fIns,
				    g_relationInstance).getKey() == fIns
				    .getKey()) {
				this.currentProject
					.deleteGeneralRelation(g_relationInstance);
				continue;
			    } else {
				this.currentProject
					.deleteFundamentalRelation(fIns);
			    }
			}
		    }
		}
		if (g_relationInstance.getSource().getEntry().getId() == draggedEntry
			.getId()) {
		    this.currentProject.addRelation(
			    (ConnectionEdgeInstance) targetInstance,
			    (ConnectionEdgeInstance) g_relationInstance
				    .getDestinationInstance(), rEntry
				    .getRelationType());
		} else {
		    // this.currentProject.addRelation(
		    // (ConnectionEdgeInstance) targetInstance,
		    // (ConnectionEdgeInstance) g_relationInstance
		    // .getDestinationInstance(), rEntry
		    // .getRelationType());
		    Instance newins = (Instance) this.currentProject
			    .addRelation(
				    (ConnectionEdgeInstance) g_relationInstance
					    .getSourceInstance(),
				    (ConnectionEdgeInstance) targetInstance,
				    rEntry.getRelationType());
		    if (newins.getEntry().getLogicalEntity() instanceof OpmUniDirectionalRelation) {
			String meaning = ((OpmUniDirectionalRelation) g_relationInstance
				.getEntry().getLogicalEntity())
				.getForwardRelationMeaning();
			((OpmUniDirectionalRelation) newins.getEntry()
				.getLogicalEntity())
				.setForwardRelationMeaning(meaning);
		    }

		    if (newins.getEntry().getLogicalEntity() instanceof OpmUniDirectionalRelation) {
			String meaning = ((OpmUniDirectionalRelation) g_relationInstance
				.getEntry().getLogicalEntity())
				.getForwardRelationMeaning();
			((OpmUniDirectionalRelation) newins.getEntry()
				.getLogicalEntity())
				.setForwardRelationMeaning(meaning);

			meaning = ((OpmUniDirectionalRelation) g_relationInstance
				.getEntry().getLogicalEntity())
				.getBackwardRelationMeaning();
			((OpmUniDirectionalRelation) newins.getEntry()
				.getLogicalEntity())
				.setBackwardRelationMeaning(meaning);
		    }
		    newins.getEntry().updateInstances();
		}
		this.currentProject
			.deleteGeneralRelation((GeneralRelationInstance) tmpImstance);

		// end of general relation section
	    }

	    if (tmpImstance instanceof FundamentalRelationInstance) {

		f_relationInstance = (FundamentalRelationInstance) tmpImstance;
		rEntry = (RelationEntry) f_relationInstance.getIXEntry();
		// cases - if the sourc and target of this link are the dragged
		// and dropped
		// elements remove it !!!!
		if (((f_relationInstance.getSourceInstance().getEntry().getId() == draggedEntry
			.getId()) && (f_relationInstance.getDestination()
			.getEntry().getId() == targetEntry.getId()))
			|| ((f_relationInstance.getSourceInstance().getEntry()
				.getId() == targetEntry.getId()) && (f_relationInstance
				.getDestination().getEntry().getId() == draggedEntry
				.getId()))) {
		    this.currentProject
			    .deleteFundamentalRelation(f_relationInstance);
		    continue;
		}
		ThingInstance otherSideInstance;
		if (f_relationInstance.getSourceInstance().getEntry().getId() == draggedEntry
			.getId()) {
		    otherSideInstance = (ThingInstance) f_relationInstance
			    .getDestinationInstance();
		} else {
		    otherSideInstance = (ThingInstance) f_relationInstance
			    .getSourceIXInstance();
		}

		Enumeration e = otherSideInstance.getRelatedInstances();
		while (e.hasMoreElements()) {
		    Instance ele = (Instance) e.nextElement();
		    if (ele instanceof LinkInstance) {
			LinkInstance lIns = (LinkInstance) ele;
			if ((lIns.getSource().getEntry().getId() == targetEntry
				.getId())
				|| (lIns.getDestination().getEntry().getId() == targetEntry
					.getId())) {
			    if (check.checkPredenceBetweenInstances(lIns,
				    f_relationInstance) == lIns) {
				this.currentProject
					.deleteFundamentalRelation(f_relationInstance);
				continue;
			    } else {
				this.currentProject.deleteLink(lIns);
			    }
			}

		    }
		    if (ele instanceof GeneralRelationInstance) {
			GeneralRelationInstance gIns = (GeneralRelationInstance) ele;
			if ((gIns.getSource().getEntry().getId() == targetEntry
				.getId())
				|| (gIns.getDestination().getEntry().getId() == targetEntry
					.getId())) {
			    if (check.checkPredenceBetweenInstances(gIns,
				    f_relationInstance).getKey() == gIns
				    .getKey()) {
				this.currentProject
					.deleteFundamentalRelation(f_relationInstance);
				continue;
			    } else {
				this.currentProject.deleteGeneralRelation(gIns);
			    }
			}

		    }

		    if (ele instanceof FundamentalRelationInstance) {
			FundamentalRelationInstance fIns = (FundamentalRelationInstance) ele;
			if ((fIns.getSourceInstance().getEntry().getId() == targetEntry
				.getId())
				|| (fIns.getDestination().getEntry().getId() == targetEntry
					.getId())) {
			    if (check.checkPredenceBetweenInstances(fIns,
				    f_relationInstance).getKey() == fIns
				    .getKey()) {
				this.currentProject
					.deleteFundamentalRelation(f_relationInstance);
				continue;
			    } else {
				this.currentProject
					.deleteFundamentalRelation((FundamentalRelationInstance) tmpImstance);
			    }
			}
		    }
		}
		if (f_relationInstance.getSourceInstance().getEntry().getId() == draggedEntry
			.getId()) {
		    this.currentProject.addRelation(
			    (ConnectionEdgeInstance) targetInstance,
			    (ConnectionEdgeInstance) f_relationInstance
				    .getDestinationInstance(), rEntry
				    .getRelationType());
		} else {
		    this.currentProject.addRelation(
			    (ConnectionEdgeInstance) f_relationInstance
				    .getSourceInstance(),
			    (ConnectionEdgeInstance) targetInstance, rEntry
				    .getRelationType());
		}
		this.currentProject
			.deleteFundamentalRelation(f_relationInstance);
		// end if fundamental relation section
	    }
	}

	Opcat2.updateStructureChange(Opcat2.LOGICAL_CHANGE);
    }

    /**
         * Cleans up the model from the dragged thing instances.
         * 
         * @param draggedInstance
         */
    protected void cleanUpMerge(ThingInstance draggedInstance) {
	ThingEntry deletedDraggedEntry = (ThingEntry) draggedInstance
		.getIXEntry();
	if (deletedDraggedEntry.getZoomedInOpd() != null) {
	    this.currentProject.deleteRootOpd(deletedDraggedEntry
		    .getZoomedInOpd());
	}
	// openReusedInstanceList.remove(dragged);
	if (draggedInstance instanceof ObjectInstance) {
	    this.currentProject.deleteObject((ObjectInstance) draggedInstance);
	} else {
	    this.currentProject
		    .deleteProcess((ProcessInstance) draggedInstance);
	}
    }

    /**
         * checking that the drag and drop action is between a tuple of the same
         * type (of objects or processes)
         * 
         * @return <code>true</code> if both the instances are of the same
         *         type, <code>false</code> otherwise
         */
    public boolean isInstancesTupleOfObjectsOrProcesses(Instance aInstance,
	    Instance bInstance) {
	return (((aInstance instanceof ObjectInstance) && (bInstance instanceof ObjectInstance)) || ((aInstance instanceof ProcessInstance) && (bInstance instanceof ProcessInstance)));
    }

}