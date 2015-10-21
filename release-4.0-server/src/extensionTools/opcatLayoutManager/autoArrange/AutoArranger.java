
package extensionTools.opcatLayoutManager.autoArrange;

import java.util.*;

import javax.swing.RepaintManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;

import org.apache.commons.collections.*;

import util.Configuration;

import gui.Opcat2;
import gui.opdGraphics.DrawingArea;
import gui.opdGraphics.opdBaseComponents.BaseGraphicComponent;


/**
 * The class is the entry point into the package, and is 
 * responsible for the "automatic layout" feature implementation. 
 */
public class AutoArranger
{
    /**
     * A "standard" issues handler
     */
    private static final IssueHandler overpappingNodes = new IssueHandlerOverpappingNodes();
    
    /**
     * A "standard" issues handler
     */
    private static final IssueHandler lineCrossesNode = new IssueHandlerLineCrossesNode();
    
    /**
     * A "standard" issues handler
     */
    private static final IssueHandler lineCrossesLine = new IssueHandlerLineCrossesLine();
    
    /**
     * A collection of "issue detector" objects, each of which is an
     * instance of a class implementing "IssueHandler" interface.
     * 
     * They are sequentially applied to all pairs of nodes in the
     * system to check for issues and identify the best solutions.
     * 
     * Note that a "SortedBag" implementation of "Collection" has been 
     * chosen to store issues handlers. This collection essentially works 
     * as a "Map", but allows storing of multiple instances of the same
     * key value. Thus, issue handlers can be stored by their weight as
     * the key value and choosing of the "lightest" or "heaviest" issue
     * handler can be performed in a constant time.
     */
    private static SortedBag setOfIssueHandlers = null;

    /**
     * A list of operations "suggested" at each stage of the algorithm
     */
    private static LinkedList suggestedOperationList = null;
    
    /**
     * Initialize the class - mainly the issue handlers
     */
    static
    {
        //initialize the storage for issue handllers
        setOfIssueHandlers = new TreeBag(new Comparator() {
            //define a REVERSE comparator - we need to get the heaviest issues first!
            public int compare(Object o1, Object o2)
            {
                return - ((IssueHandler) o1).compareTo(o2);
            }
        });
        
        //add the standard issue handlers
        addIssueHandler(overpappingNodes);
        addIssueHandler(lineCrossesNode);
        addIssueHandler(lineCrossesLine);
    }
    
    /**
     * This class is responsible for "undoability" of the autoarrangement action.
     * 
     * It simple saves off the original and final states and restores the appropriate
     * one upon a request for an undo/redo editing operation.
     */
    private static class AutoArrangeUndoableEdit extends AbstractUndoableEdit
    {
        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SystemStateKeeper orgState = null;
        
        private SystemStateKeeper endState = null;
        
        public AutoArrangeUndoableEdit(SystemStateKeeper org, SystemStateKeeper end)
        {
            orgState = org;
            endState = end;
        }
        
        public void undo()
        {
            super.undo();
            orgState.restoreState();
        }
        
        public void redo()
        {
            super.redo();
            endState.restoreState();
        }
    }
    
    /**
     * Returns the "weight" (relative to the heuristic function of 
     * the algorithm) of the current system state. The state weight is the sum of weights of
     * all the issues that take place in this state. 
     * The system state is defined by the list of its instances.
     * @param arrangedEntitiesList the list of instances in the system
     * @return the weight of the system state
     */
    private static long evaluateSystem(List arrangedEntitiesList)
    {
        long weight = 0;
        suggestedOperationList = new LinkedList();
        
        //In "removedItems" we will store the nodes the are already
        //  participating in an issue - we will not be considering
        //  them for any other issues. Remember that according to the
        //  algorithm we apply for issues  starting with the "heaviest" 
        //  and moving towards "lighter" ones.
        IdentityHashMap removedItems = new IdentityHashMap(arrangedEntitiesList.size());
        
        //iterate over the issue handlers, from the heaviest to the lightest
        Iterator iter = setOfIssueHandlers.iterator();
        while (iter.hasNext()) {
            IssueHandler currentIssueHandler = (IssueHandler) iter.next();
            
            //iterator through every pair of nodes and 
            //  check them with the current issue handler
            
            //with the first iterator start with the SECOND node
            Iterator i1 = arrangedEntitiesList.iterator();
            if (! i1.hasNext()) //empty system!!
                return 0;
            i1.next();
            
            while (i1.hasNext()) {
                Object entity1 = i1.next();
                if (removedItems.containsKey(entity1))
                    continue;
                
                Iterator i2 = arrangedEntitiesList.iterator();
                while (i2.hasNext()) {
                    Object entity2 = i2.next();
                    if (removedItems.containsKey(entity2))
                        continue;
                    
                    if (entity2 == entity1)
                        break;
                    long localWeight = currentIssueHandler.evaluateIssue(entity1, entity2);
                    if (localWeight > 0) {
                        removedItems.put(entity1, null);
                        removedItems.put(entity2, null);
                        currentIssueHandler.accumulateOperations(suggestedOperationList);
                        weight += localWeight;
                        //break - no more use to check issues for entity1
                        break;
                    }
                }
            }
        }
        
        //shuffle the list of operations randomly to avoid the uniformity
        //  of operations applied
        Collections.shuffle(suggestedOperationList);
        
        return weight;
    }
    
    /**
     * Adds the given IssueHandler to the collection of issues handlers
     * to be applied for the algorithm.
     * 
     * @param issueHandler the issues handler class to add
     */
    public static void addIssueHandler(IssueHandler issueHandler)
    {
        setOfIssueHandlers.add(issueHandler);
    }
    
    /**
     * The main entrance into the class/library. Runs the algorithm 
     * that re-organizes the system. Works using the iterative Beam-Width Limited Depth search.
     * The width and the depth of the algorithm may be configured in conf.txt.
     * @param arrangedEntitiesList a List of entities the system consists of
     */
    public static void arrange(List arrangedEntitiesList)
    {
        //set my own repaint manager that does the repainting only at the very end
        DrawingArea drawingArea = null;
        RepaintManager orgRepaintManager = null;
        LocalRepaintManager localRepaintManager = null;
        
        try {
            //retrieve the width and the depth of the beam search
            Configuration config = Configuration.getInstance();
            int searchWidth = config.getSearchWidth();
            int searchDepth = config.getSearchDepth();
            
            //collect all instances in the current OPD 
            //an auxiliary operation - count the number of nodes in advance
            //  (to save time and memory for later on)
            //
            //Besides, we will try to find an instance of BaseGraphicComponent
            //  from which the "DrawingArea" can be retrieved
            int nodeCount = 0;
            Iterator iter = new NodeIterator(arrangedEntitiesList.iterator());
            while (iter.hasNext()) {
                nodeCount++;
                Object o = iter.next();
                if ((drawingArea == null) && (o instanceof BaseGraphicComponent))
                    drawingArea = ((BaseGraphicComponent) o).getDrawingArea();
            }
            SystemStateKeeper.setNumberOfNodes(nodeCount);
            
            //if the "DrawingArea" has been found, initialize the local
            //  repaint manager.
            if (drawingArea != null) {
                orgRepaintManager = RepaintManager.currentManager(null);
                localRepaintManager = new LocalRepaintManager(drawingArea);
                RepaintManager.setCurrentManager(localRepaintManager);
            }
            
            //save off the system state before applying the operation
            SystemStateKeeper originalState = new SystemStateKeeper(
                    new NodeIterator(arrangedEntitiesList.iterator()), 0, null);
            
            //build the "list of existing states" consisting of the initial state only
            long weight = evaluateSystem(arrangedEntitiesList);
            
            Collection currentStates = Collections.singletonList(new SystemStateKeeper(
                    new NodeIterator(arrangedEntitiesList.iterator()), 
                    weight, (List) suggestedOperationList.clone()));
            TreeBag newStates = null;
            
            //start the iterational search with this list
            int curDepth = 0;
            
            SystemStateKeeper stateFound = null;
        ITERATIONAL_SEARCH:
            while (true) {
                //We have a list of possible states. First of all, traverse
                //  the list for the minimally weighted state (especially if
                //  its weight is 0).
                stateFound = null;
                iter = currentStates.iterator();
                while (iter.hasNext()) {
                    SystemStateKeeper oneState = (SystemStateKeeper) iter.next();
                    
                    //if a zero-weighted state is found, we finished the search
                    if (oneState.getWeight() == 0) {
                        stateFound = oneState;
                        break ITERATIONAL_SEARCH;
                    }
                    
                    //this is the first found weight - initialize the "running minimum"
                    if (stateFound == null) {
                        stateFound = oneState;
                        continue;
                    }
                    
                    //this is the next "candidate" for lowest state
                    if (oneState.getWeight() < stateFound.getWeight())
                        stateFound = oneState;
                }
                
                //here oneState points to the state with the minimal weight
                //  (yet, greater than 0).
                
                //check to see if the maximal search depth has been reached
                if (++curDepth >= searchDepth)
                    break ITERATIONAL_SEARCH;
                
                //no - we need to advance to the new iteration
                newStates = new TreeBag();
    
                //try the states one by one
                iter = currentStates.iterator();
                while (iter.hasNext()) {
                    SystemStateKeeper stateKeeper = (SystemStateKeeper) iter.next();
                    
                    //traverse the list of its operations one by one
                    Iterator operationIter = stateKeeper.getOperations().iterator();
                    int currentOperation = 0;
                    IssueOperation[] operationArray = new IssueOperation[0];
                    while (true) {
                        //get the next operation
                        IssueOperation operation = null;
                        if (currentOperation >= operationArray.length) {
                            if (! operationIter.hasNext())
                                break;
                            operationArray = (IssueOperation[]) operationIter.next();
                            currentOperation = 0;
                        }
                            
                        operation = operationArray[currentOperation++];
                        
                        //set this state to the system before applying the next operation
                        stateKeeper.restoreState();
                        
                        //apply the operation
                        while (operation != null) {
                            operation.apply();
                            operation = operation.getNextOperation();
                        }
                        
                        //evaluate the resulting state
                        long newWeight = evaluateSystem(arrangedEntitiesList);
                        
                        //decide whether to add this new state to the existing states
                        boolean shouldAdd = false;
                        //if we don't yet have the maximum number of states, add it anyway
                        if (newStates.size() < searchWidth) {
                            shouldAdd = true;
                        } else {
                            //if the new state's weight is less than the heaviest in the
                            //  collection, remove the heaviest and raise the flag to add
                            //  the new one
                            if (newWeight < ((SystemStateKeeper) newStates.last()).getWeight()) {
                                newStates.remove(newStates.last());
                                shouldAdd = true;
                            }
                        }
                        
                        if (shouldAdd)
                            newStates.add(new SystemStateKeeper(
                                    new NodeIterator(arrangedEntitiesList.iterator()), 
                                    newWeight, (List) suggestedOperationList.clone()));
                    }
                }
                
                currentStates = newStates;
            }
            
            //make sure... no, no, that cannot happen, but just to be 
            //  REALLY, REALLY sure...
            if (stateFound == null)
                throw new RuntimeException("State found turned out to be null!!");
            stateFound.restoreState();
            
            //set the undo manager
            Opcat2.getUndoManager().undoableEditHappened(new UndoableEditEvent(
                    AutoArranger.class, 
                    new AutoArrangeUndoableEdit(originalState, stateFound)));
            Opcat2.setUndoEnabled(Opcat2.getUndoManager().canUndo());
            Opcat2.setRedoEnabled(Opcat2.getUndoManager().canRedo());
        } finally {
            //now we can do the repainting, if necessary
            if (localRepaintManager != null) {
                localRepaintManager.paintAll();
            
                //restore the original RepaintManager
                RepaintManager.setCurrentManager(orgRepaintManager);
            }
        }
    }
}
