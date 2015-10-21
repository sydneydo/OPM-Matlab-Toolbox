package extensionTools.opl2;

import java.util.LinkedList;

import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import extensionTools.opl2.alg.OPLGeneral;
import extensionTools.opl2.generated.AgentSentence;
import extensionTools.opl2.generated.AgentSentenceType;
import extensionTools.opl2.generated.AggregatedObject;
import extensionTools.opl2.generated.ChangingClause;
import extensionTools.opl2.generated.ChangingSentenceType;
import extensionTools.opl2.generated.ConditionClause;
import extensionTools.opl2.generated.ConditionSentenceType;
import extensionTools.opl2.generated.ConsumptionClause;
import extensionTools.opl2.generated.ConsumptionSentenceType;
import extensionTools.opl2.generated.EffectClause;
import extensionTools.opl2.generated.EffectSentenceType;
import extensionTools.opl2.generated.EnablingClause;
import extensionTools.opl2.generated.EnablingSentenceType;
import extensionTools.opl2.generated.ExhibitedObject;
import extensionTools.opl2.generated.GeneralEventSentence;
import extensionTools.opl2.generated.OPLscript;
import extensionTools.opl2.generated.ObjectAggregationSentenceSetType;
import extensionTools.opl2.generated.ObjectAggregationSentenceType;
import extensionTools.opl2.generated.ObjectBiDirectionalRelationSentence;
import extensionTools.opl2.generated.ObjectEnvironmentalPhysicalSentenceType;
import extensionTools.opl2.generated.ObjectExhibitionSentenceSetType;
import extensionTools.opl2.generated.ObjectExhibitionSentenceType;
import extensionTools.opl2.generated.ObjectInZoomingSentenceSetType;
import extensionTools.opl2.generated.ObjectInZoomingSentenceType;
import extensionTools.opl2.generated.ObjectInheritanceSentenceSet;
import extensionTools.opl2.generated.ObjectInstanceSentenceType;
import extensionTools.opl2.generated.ObjectStateSentenceType;
import extensionTools.opl2.generated.ObjectUniDirectionalRelationSentenceType;
import extensionTools.opl2.generated.Operation;
import extensionTools.opl2.generated.ProcessAggregationSentenceSetType;
import extensionTools.opl2.generated.ProcessAggregationSentenceType;
import extensionTools.opl2.generated.ProcessBiDirectionalRelationSentence;
import extensionTools.opl2.generated.ProcessEnvironmentalPhysicalSentenceType;
import extensionTools.opl2.generated.ProcessExhibitionSentenceSetType;
import extensionTools.opl2.generated.ProcessExhibitionSentenceType;
import extensionTools.opl2.generated.ProcessInZoomingSentenceSetType;
import extensionTools.opl2.generated.ProcessInZoomingSentenceType;
import extensionTools.opl2.generated.ProcessInstanceSentenceType;
import extensionTools.opl2.generated.ProcessInvocationSentenceType;
import extensionTools.opl2.generated.ProcessTimeoutSentenceType;
import extensionTools.opl2.generated.ProcessUniDirectionalRelationSentence;
import extensionTools.opl2.generated.ResultClause;
import extensionTools.opl2.generated.ResultSentenceType;
import extensionTools.opl2.generated.Role;
import extensionTools.opl2.generated.StateClause;
import extensionTools.opl2.generated.StateEntranceSentence;
import extensionTools.opl2.generated.StateTimeoutSentence;
import extensionTools.opl2.generated.ThingSentenceSet;
import extensionTools.opl2.generated.ThingSentenceSetType;
import extensionTools.opl2.generated.TypeDeclarationSentenceType;
import gui.opdProject.OpdProject;
import gui.opdProject.OplColorScheme;

//import javax.xml.bind.Marshaller;

public class PrettyPrinter {

    public static final int OR = 0, AND = 1, XOR = 2;

    public static String[] prop = { "or", "and", "either" };

    protected ISystem opcatSystem;

    protected OplColorScheme colorScheme;

    protected StringBuffer bf = new StringBuffer();

    public PrettyPrinter(ISystemStructure elems, ISystem opcatSystem_) {
	this.elements = elems;
	this.opcatSystem = opcatSystem_;
	OpdProject myProject = (OpdProject) this.opcatSystem;
	this.colorScheme = myProject.getOplColorScheme();
	this.startHTML = "<HTML><HEAD></HEAD><BODY><FONT size="
		+ colorScheme.getTextSize() + ">";

	/*
         * + colorScheme.getAttribute(OplColorScheme.DEFAULT_STYLE).
         * openingHTMLFontTag();
         */
	// endHTML = colorScheme.getAttribute(OplColorScheme.DEFAULT_STYLE).
	// closingHTMLFontTag() + "</BODY></HTML>";
	this.endHTML = "</FONT></BODY></HTML>";
	this.setEndLine();
    }

    public StringBuffer getBuffer() {
	return this.bf;
    }

    public void visit(ProcessUniDirectionalRelationSentence e) {
	this.bf.append(this.margin);
	this.printAttr(OplColorScheme.PROCESS_STYLE, e.getSourceName());
	this.bf.append(" ");
	if (e.getRelationName().startsWith("relates")) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, e.getRelationName());
	} else {
	    this.printAttr(OplColorScheme.TAG_STYLE, e.getRelationName());
	}
	this.bf.append(" ");
	this.printAttr(OplColorScheme.PROCESS_STYLE, e.getDestinationName());
	this.bf.append(this.endLine);
    }

    public void visit(ProcessBiDirectionalRelationSentence e, String subjectName) {
	this.bf.append(this.margin);

	if (subjectName.equals(e.getDestinationName())) {
	    this
		    .printAttr(OplColorScheme.PROCESS_STYLE, e
			    .getDestinationName());
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " and ");
	    this.printAttr(OplColorScheme.PROCESS_STYLE, e.getSourceName());
	    if (e.getRelationName().startsWith("equivalent")) {
		this.printAttr(OplColorScheme.DEFAULT_STYLE, " "
			+ e.getRelationName());
	    } else {
		this.printAttr(OplColorScheme.TAG_STYLE, " "
			+ e.getRelationName());
	    }
	} else {
	    this.printAttr(OplColorScheme.PROCESS_STYLE, e.getSourceName());
	    this.printAttr(OplColorScheme.TAG_STYLE, " " + e.getRelationName()
		    + " ");
	    this
		    .printAttr(OplColorScheme.PROCESS_STYLE, e
			    .getDestinationName());
	}
	this.bf.append(this.endLine);
    }

    public void visit(ObjectUniDirectionalRelationSentenceType e) {
	this.bf.append(this.margin);
	boolean has_s = false;
	has_s = this.printCardinality(e.getSourceMinimalCardinality(), e
		.getSourceMaximalCardinality(), true);
	if (has_s) {
	    this.printAttr(OplColorScheme.OBJECT_STYLE,
		    extensionTools.opl2.alg.EnglishRules.pluralOf(e
			    .getSourceName()));
	} else {
	    this.printAttr(OplColorScheme.OBJECT_STYLE, e.getSourceName());
	}
	if (e.getRelationName().startsWith("relates")) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " "
		    + e.getRelationName() + " ");
	} else {
	    this.printAttr(OplColorScheme.TAG_STYLE, " " + e.getRelationName()
		    + " ");
	}
	has_s = this.printCardinality(e.getDestinationMinimalCardinality(), e
		.getDestinationMaximalCardinality(), false);
	if (has_s) {
	    this.printAttr(OplColorScheme.OBJECT_STYLE,
		    extensionTools.opl2.alg.EnglishRules.pluralOf(e
			    .getDestinationName()));
	} else {
	    this.printAttr(OplColorScheme.OBJECT_STYLE, e.getDestinationName());
	}
	this.bf.append(this.endLine);
    }

    public void visit(ObjectBiDirectionalRelationSentence e, String subjectName) {
	boolean has_s = false;
	this.bf.append(this.margin);
	if (subjectName.equals(e.getDestinationName())) {
	    has_s = this.printCardinality(e.getDestinationMinimalCardinality(),
		    e.getDestinationMaximalCardinality(), true);
	    if (has_s) {
		this.printAttr(OplColorScheme.OBJECT_STYLE,
			extensionTools.opl2.alg.EnglishRules.pluralOf(e
				.getDestinationName()));
	    } else {
		this.printAttr(OplColorScheme.OBJECT_STYLE, e
			.getDestinationName());
	    }
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " and ");
	    has_s = this.printCardinality(e.getSourceMinimalCardinality(), e
		    .getSourceMaximalCardinality(), false);
	    if (has_s) {
		this.printAttr(OplColorScheme.OBJECT_STYLE,
			extensionTools.opl2.alg.EnglishRules.pluralOf(e
				.getSourceName()));
	    } else {
		this.printAttr(OplColorScheme.OBJECT_STYLE, e.getSourceName());
	    }
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " are");
	    if (e.getRelationName().startsWith("equivalent")) {
		this.printAttr(OplColorScheme.DEFAULT_STYLE, " "
			+ e.getRelationName());
	    } else {
		this.printAttr(OplColorScheme.TAG_STYLE, " "
			+ e.getRelationName());
	    }
	    this.bf.append(this.endLine);
	} else {
	    has_s = this.printCardinality(e.getSourceMinimalCardinality(), e
		    .getSourceMaximalCardinality(), true);
	    if (has_s) {
		this.printAttr(OplColorScheme.OBJECT_STYLE,
			extensionTools.opl2.alg.EnglishRules.pluralOf(e
				.getSourceName()));
	    } else {
		this.printAttr(OplColorScheme.OBJECT_STYLE, e.getSourceName());
	    }
	    this.printAttr(OplColorScheme.TAG_STYLE, " " + e.getRelationName()
		    + " ");
	    has_s = this.printCardinality(e.getDestinationMinimalCardinality(),
		    e.getDestinationMaximalCardinality(), false);
	    if (has_s) {
		this.printAttr(OplColorScheme.OBJECT_STYLE,
			extensionTools.opl2.alg.EnglishRules.pluralOf(e
				.getDestinationName()));
	    } else {
		this.printAttr(OplColorScheme.OBJECT_STYLE, e
			.getDestinationName());
	    }
	    this.bf.append(this.endLine);
	}
    }

    public void visit(ObjectAggregationSentenceSetType e) {
	this.visit(e.getObjectAggregationSentence());
	this.printParagraph(e.getThingSentenceSet(), false);
    }

    public void visit(ObjectInZoomingSentenceSetType e) {
	this.visit(e.getObjectInZoomingSentence());
	this.printParagraph(e.getThingSentenceSet(), false);
    }

    public void visit(ProcessInZoomingSentenceSetType e) {
	this.visit(e.getProcessInZoomingSentence());
	this.printParagraph(e.getThingSentenceSet(), false);
    }

    public void visit(ObjectInZoomingSentenceType e) {
	int key = OplColorScheme.OBJECT_STYLE;
	if (e.getInZoomedObject().isEmpty() && e.getInZoomedProcess().isEmpty()) {
	    return;
	}
	this.bf.append(this.margin);
	java.util.List lst = e.getInZoomedObject();
	this.printAttr(key, e.getObjectName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " zooms into ");
	int size = lst.size();
	int size1;
	for (int j = 0; j < 2; j++) {
	    if (j == 1) {
		lst = e.getInZoomedProcess();
		size1 = lst.size();
		key = OplColorScheme.PROCESS_STYLE;
		if ((size > 0) && (size1 > 0)) {
		    this.printAttr(OplColorScheme.DEFAULT_STYLE,
			    ", as well as ");
		}
		size = size1;
	    }
	    for (int i = 0; i < size; i++) {
		this.setDelimiter(i, size, AND);
		this.printAttr(key, (String) lst.get(i));
	    }
	}
	this.bf.append(this.endLine);
    }

    public void visit(ProcessInZoomingSentenceType e) {
	int key = OplColorScheme.PROCESS_STYLE;
	if (e.getInZoomedObject().isEmpty() && e.getInZoomedProcess().isEmpty()) {
	    return;
	}
	this.bf.append(this.margin);
	java.util.List lst = e.getInZoomedProcess();

	this.printAttr(key, e.getProcessName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " zooms into ");
	int size = lst.size();
	int size1;
	for (int j = 0; j < 2; j++) {
	    if (j == 1) {
		lst = e.getInZoomedObject();
		size1 = lst.size();
		key = OplColorScheme.OBJECT_STYLE;
		if ((size > 0) && (size1 > 0)) {
		    this.printAttr(OplColorScheme.DEFAULT_STYLE,
			    ", as well as ");
		}
		size = size1;
	    }
	    for (int i = 0; i < size; i++) {
		this.setDelimiter(i, size, AND);
		this.printAttr(key, (String) lst.get(i));
	    }
	}
	this.bf.append(this.endLine);
    }

    public StateClause getDefaultState(java.util.List states) {
	StateClause def = null;
	for (int i = 0; i < states.size(); i++) {
	    if (((StateClause) states.get(i)).isDefault()) {
		def = (StateClause) states.get(i);
	    }
	}
	return def;
    }

    public void visit(ObjectStateSentenceType e) {
	int key = OplColorScheme.OBJECT_STYLE;
	int key2 = OplColorScheme.STATE_STYLE;
	java.util.List states = e.getStateClause();
	StateClause def = null;
	int size = states.size();
	if (size == 0) {
	    return;
	}
	this.bf.append(this.margin);
	this.printAttr(key, e.getObjectName());
	if (size == 1) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " is ");
	} else {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " can be ");
	}
	def = this.getDefaultState(states);
	// printAttr(key2, ( (StateClause) states.get(0)).getStateName());
	if (def != null) {
	    this.printAttr(key2, def.getStateName());
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " by default");
	}
	for (int i = 0; i < size; i++) {
	    if ((StateClause) states.get(i) == def) {
		continue;
	    }
	    this.setDelimiter(i, size, OR);
	    this.printAttr(key2, ((StateClause) states.get(i)).getStateName());
	}
	this.bf.append(this.endLine);
	this.printParagraph(e.getStateClause(), true);
    }

    public void visit(StateClause clause) {
	if (clause.isInitial()) {
	    this.bf.append(this.margin);
	    this.printAttr(OplColorScheme.STATE_STYLE, clause.getStateName());
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " is initial");
	    this.bf.append(this.endLine);
	}
	String max = new String();
	String min = new String();
	if (clause.getMaxTimeValue() != null) {
	    max = OPLGeneral.visit(clause.getMaxTimeValue());
	}
	if (clause.getMinTimeValue() != null) {
	    min = OPLGeneral.visit(clause.getMinTimeValue());
	}
	if (min.length() > 0) {
	    this.bf.append(this.margin);
	    this.printAttr(OplColorScheme.STATE_STYLE, clause.getStateName());
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " lasts ");
	    this.printAttr(OplColorScheme.TAG_STYLE, min);
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " to ");
	    if (max.length() > 0) {
		this.printAttr(OplColorScheme.TAG_STYLE, max);
	    } else {
		this.printAttr(OplColorScheme.TAG_STYLE, "infinity");
	    }
	    this.bf.append(this.endLine);
	}
	if (clause.isFinal()) {
	    this.bf.append(this.margin);
	    this.printAttr(OplColorScheme.STATE_STYLE, clause.getStateName());
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " is final");
	    this.bf.append(this.endLine);
	}
    }

    public void visit(ProcessInvocationSentenceType proc) {
	int key = OplColorScheme.PROCESS_STYLE;
	java.util.List invokedProcs = proc.getTriggeredProcess();
	this.bf.append(this.margin);
	this.printPath(proc.getPathLabel());
	this.printAttr(key, proc.getProcessName());
	this.addActionSentence(key, invokedProcs, " invokes ");
	this.bf.append(this.endLine);
    }

    public void visit(ProcessTimeoutSentenceType proc) {
	int key = OplColorScheme.PROCESS_STYLE;
	java.util.List invokedProcs = proc.getTriggeredProcess();
	this.bf.append(this.margin);
	this.printPath(proc.getPathLabel());
	this.printAttr(key, proc.getProcessName());
	this.addActionSentence(key, invokedProcs, " triggers ");
	this.printAttr(OplColorScheme.DEFAULT_STYLE,
		" when it lasts more than ");
	this.printAttr(OplColorScheme.DEFAULT_STYLE, OPLGeneral.visit(proc
		.getMaxTimeoutValue()));
	this.bf.append(this.endLine);
    }

    public void visit(ProcessAggregationSentenceSetType e) {
	this.visit(e.getProcessAggregationSentence());
	this.printParagraph(e.getThingSentenceSet(), false);
    }

    public void visit(ObjectExhibitionSentenceSetType e) {
	this.visit(e.getObjectExhibitionSentence());
	this.printParagraph(e.getThingSentenceSet(), false);
    }

    public void visit(ProcessExhibitionSentenceSetType e) {
	this.visit(e.getProcessExhibitionSentence());
	this.printParagraph(e.getThingSentenceSet(), false);
    }

    // returns true if entity of the cardinality should come
    // with "s" on the end;
    // for example: many As
    public boolean printCardinality(int min, int max, boolean isFirst) {
	if ((min == max) && (max > 1)) {
	    this.printAttr(OplColorScheme.CARDINALITY_STYLE, Integer
		    .toString(min)
		    + " ");
	    return true;
	}
	if ((min == 1) && (max == 1)) {
	    return false;
	}
	if ((min == 0) && (max == 1)) {
	    if (isFirst) {
		this
			.printAttr(OplColorScheme.CARDINALITY_STYLE,
				"An optional ");
	    } else {
		this
			.printAttr(OplColorScheme.CARDINALITY_STYLE,
				"an optional ");
	    }
	    return false;
	}
	if (max != -1) {
	    this.printAttr(OplColorScheme.CARDINALITY_STYLE, min + " to " + max
		    + " ");
	    return true;
	}
	if ((min == 0) && (max == -1)) {
	    if (isFirst) {
		this.printAttr(OplColorScheme.CARDINALITY_STYLE, "Optional ");
	    } else {
		this.printAttr(OplColorScheme.CARDINALITY_STYLE, "optional ");
	    }
	    return true;
	}
	if ((min == 1) && (max == -1)) {
	    if (isFirst) {
		this.printAttr(OplColorScheme.CARDINALITY_STYLE,
			"At least one ");
	    } else {
		this.printAttr(OplColorScheme.CARDINALITY_STYLE,
			"at least one ");
	    }
	    return false;
	}
	if (isFirst) {
	    this.printAttr(OplColorScheme.CARDINALITY_STYLE, "Many ");
	} else {
	    this.printAttr(OplColorScheme.CARDINALITY_STYLE, "many ");
	}
	return true;
    }

    public void visit(ProcessAggregationSentenceType e) {
	int key = OplColorScheme.PROCESS_STYLE;
	java.util.List exhibits = e.getAggregatedProcess();
	this.bf.append(this.margin);
	this.printAttr(key, e.getProcessName());
	this.addAggregationSentence(key, exhibits);
	this.bf.append(this.endLine);
    }

    public void visit(ObjectAggregationSentenceType e) {
	int key = OplColorScheme.OBJECT_STYLE;
	java.util.List exhibits = e.getAggregatedObject();
	this.bf.append(this.margin);
	this.printAttr(key, e.getObjectName());
	this.addAggregationSentence(key, exhibits);
	this.bf.append(this.endLine);
    }

    public void addExhibitionObjectSentence(int key, java.util.List exhibits,
	    java.util.List operations) {
	ExhibitedObject obj;
	Operation op;
	java.util.List Objects = new LinkedList();
	java.util.List Processes = new LinkedList();
	boolean isObjects = false;
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " exhibits ");
	for (java.util.ListIterator itr = exhibits.listIterator(); itr
		.hasNext();) {
	    obj = (ExhibitedObject) itr.next();
	    Objects.add(obj);
	}
	for (java.util.ListIterator itr = operations.listIterator(); itr
		.hasNext();) {
	    op = (Operation) itr.next();
	    Processes.add(op);
	}
	if (key == OplColorScheme.PROCESS_STYLE) {
	    Objects = Processes;
	    Processes = Objects;
	} else {
	    isObjects = true;
	}
	this.addExhibitedThings(isObjects, Objects);
	if (!Objects.isEmpty() && !Processes.isEmpty()) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, ", as well as ");
	}
	this.addExhibitedThings((!isObjects), Processes);
    }

    public void addExhibitedThings(boolean key_, java.util.List Objects) {
	int listSize = Objects.size();
	ExhibitedObject obj;
	Operation op;
	String objName;
	int key;
	boolean hasS;
	if (key_) {
	    key = OplColorScheme.OBJECT_STYLE;
	} else {
	    key = OplColorScheme.PROCESS_STYLE;
	}
	for (java.util.ListIterator itr = Objects.listIterator(); itr.hasNext();) {
	    if (key_) {
		obj = (ExhibitedObject) itr.next();
		hasS = this.printCardinality(obj.getMinimalCardinality(), obj
			.getMaximalCardinality(), false);
		objName = obj.getAttributeName();
	    } else {
		op = (Operation) itr.next();
		objName = op.getOperationName();
		hasS = false;
	    }
	    if (hasS) {
		objName = extensionTools.opl2.alg.EnglishRules
			.pluralOf(objName);
	    }
	    this.printAttr(key, objName);
	    if (itr.nextIndex() < (listSize - 1)) {
		this.printAttr(OplColorScheme.DEFAULT_STYLE, ", ");
	    } else if (itr.nextIndex() == (listSize - 1)) {
		if (listSize > 2) {
		    this.printAttr(OplColorScheme.DEFAULT_STYLE, ",");
		}
		this.printAttr(OplColorScheme.DEFAULT_STYLE, " and ");
	    }
	}
    }

    public void addAggregationSentence(int key, java.util.List exhibits) {
	int listSize = exhibits.size();
	boolean isObj = false;
	boolean hasS = false;
	AggregatedObject tmp;
	String obj;
	if (key == OplColorScheme.OBJECT_STYLE) {
	    isObj = true;
	}
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " consists of ");
	for (java.util.ListIterator itr = exhibits.listIterator(); itr
		.hasNext();) {
	    if (isObj) {
		tmp = (AggregatedObject) itr.next();
		hasS = this.printCardinality(tmp.getMinimalCardinality(), tmp
			.getMaximalCardinality(), false);
		obj = tmp.getObjectName();
	    } else {
		obj = (String) itr.next();
	    }
	    if (hasS) {
		obj = extensionTools.opl2.alg.EnglishRules.pluralOf(obj);
	    }
	    this.printAttr(key, obj);
	    if (itr.nextIndex() < (listSize - 1)) {
		this.printAttr(OplColorScheme.DEFAULT_STYLE, ", ");
	    } else if (itr.nextIndex() == (listSize - 1)) {
		if (listSize > 2) {
		    this.printAttr(OplColorScheme.DEFAULT_STYLE, ",");
		}
		this.printAttr(OplColorScheme.DEFAULT_STYLE, " and ");
	    }
	}
    }

    public void addActionSentence(int key, java.util.List invokedProcs,
	    String action) {
	this.bf.append(action);
	int size = invokedProcs.size();
	if (size == 1) {
	    this.printAttr(key, (String) invokedProcs.get(0));
	    return;
	}
	if (size == 2) {
	    this.printAttr(key, (String) invokedProcs.get(0));
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " and ");
	    this.printAttr(key, (String) invokedProcs.get(1));
	    return;
	}
	for (int i = 0; i < size; i++) {
	    this.setDelimiter(i, size, AND);
	    this.printAttr(key, (String) invokedProcs.get(i));

	}
    }

    public void visit(ProcessExhibitionSentenceType e) {
	int key = OplColorScheme.PROCESS_STYLE;
	java.util.List exhibits = e.getExhibitedObject();
	this.bf.append(this.margin);
	this.printAttr(key, e.getProcessName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " exhibits ");
	java.util.List ops = e.getOperation();
	int size = ops.size();
	int objSize = exhibits.size();
	for (int i = 0; i < size; i++) {
	    Operation op = (Operation) ops.get(i);
	    this.setDelimiter(i, size, AND);
	    this.printAttr(OplColorScheme.PROCESS_STYLE, op.getOperationName());
	}
	if ((size > 0) && (objSize > 0)) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, ", as well as ");
	}
	for (int i = 0; i < objSize; i++) {
	    ExhibitedObject exhib = (ExhibitedObject) exhibits.get(i);
	    this.setDelimiter(i, objSize, AND);
	    this.printAttr(OplColorScheme.OBJECT_STYLE, exhib
		    .getAttributeName());
	}
	this.bf.append(this.endLine);
    }

    public void visit(ObjectExhibitionSentenceType e) {
	int key = OplColorScheme.OBJECT_STYLE;
	java.util.List exhibits = e.getExhibitedObject();
	java.util.List procs = e.getOperation();
	this.bf.append(this.margin);
	this.printAttr(key, e.getObjectName());
	this.addExhibitionObjectSentence(key, exhibits, procs);
	this.bf.append(this.endLine);
    }

    public void visit1(
	    extensionTools.opl2.generated.ObjectInheritanceSentenceSet e) {
	this.addInhFather(OplColorScheme.OBJECT_STYLE, e.getObjectName(), e
		.getObjectInheritanceSentence());
    }

    public void visit1(
	    extensionTools.opl2.generated.ProcessInheritanceSentenceSet e) {
	this.addInhFather(OplColorScheme.PROCESS_STYLE, e.getObjectName(), e
		.getProcessInheritanceSentence());
    }

    public void visit(ObjectInstanceSentenceType e) {
	this.addInstFather(OplColorScheme.OBJECT_STYLE, e.getObjectName(), e
		.getInstanceFatherName());
    }

    public void visit(ProcessInstanceSentenceType e) {
	this.addInstFather(OplColorScheme.PROCESS_STYLE, e.getProcessName(), e
		.getInstanceFatherName());
    }

    public void visit(ObjectEnvironmentalPhysicalSentenceType sentence) {
	this.addEnvPhysical(OplColorScheme.OBJECT_STYLE, sentence
		.getObjectName(), sentence.isEnvironmental(), sentence
		.isPhysical());
    }

    public void visit(ProcessEnvironmentalPhysicalSentenceType sentence) {
	this.addEnvPhysical(OplColorScheme.PROCESS_STYLE, sentence
		.getProcessName(), sentence.isEnvironmental(), sentence
		.isPhysical());
    }

    protected void addEnvPhysical(int key, String subj, boolean isEnvir,
	    boolean isPhysical) {
	this.bf.append(this.margin);
	this.printAttr(key, subj);
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " is ");
	if (isEnvir && isPhysical) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE,
		    "environmental and physical");
	    this.bf.append(this.endLine);
	    return;
	}
	if (isEnvir) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, "environmental");
	    this.bf.append(this.endLine);
	    return;
	}
	this.printAttr(OplColorScheme.DEFAULT_STYLE, "physical");
	this.bf.append(this.endLine);
    }

    protected void addInhFather(int key, String subj, java.util.List objs) {
	this.bf.append(this.margin);
	this.printAttr(key, subj);
	int objSize = objs.size();
	if (key == OplColorScheme.PROCESS_STYLE) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " is ");
	    for (int i = 0; i < objSize; i++) {
		extensionTools.opl2.generated.ProcessInheritanceSentence exhib = (extensionTools.opl2.generated.ProcessInheritanceSentence) objs
			.get(i);
		this.setDelimiter(i, objSize, AND);
		this.printAttr(key, exhib.getInheritanceFatherName());
	    }
	} else {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " is");
	    for (int i = 0; i < objSize; i++) {
		extensionTools.opl2.generated.ObjectInheritanceSentence exhib = (extensionTools.opl2.generated.ObjectInheritanceSentence) objs
			.get(i);
		this.setDelimiter(i, objSize, AND);
		this.addASubject(key, exhib.getInheritanceFatherName());
	    }
	}
	this.bf.append(this.endLine);
    }

    protected void addInstFather(int key, String subj, String obj) {
	this.bf.append(this.margin);
	this.printAttr(key, subj);

	if (key == OplColorScheme.PROCESS_STYLE) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " is instance of ");
	    this.printAttr(key, obj);
	} else {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " is instance of");
	    this.addASubject(key, obj);
	}
	this.bf.append(this.endLine);
    }

    protected void addASubject(int key, String name) {
	char tmp = name.toLowerCase().charAt(0);
	if ((tmp == 'a') || (tmp == 'o') || (tmp == 'e') || (tmp == 'y')
		|| (tmp == 'u') || (tmp == 'i')) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " an ");
	} else {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " a ");
	}
	this.printAttr(key, name);
    }

    // The root of the OPL-XML tree
    // visit- traversing XML elements
    public void visit(OPLscript opl) {
	java.util.List opls = opl.getThingSentenceSet();
	this.bf.append(this.startHTML);
	for (int i = 0; i < opls.size(); i++) {
	    this.visit((ThingSentenceSetType) opls.get(i));
	}
	int j = this.bf.lastIndexOf("<BR>");
	if (j != -1) {
	    this.bf.delete(j, j + 4);
	}
	this.bf.append(this.endHTML);
    }

    public void visit(ChangingSentenceType sentence) {
	java.util.List clauses = sentence.getChangingClause();
	ChangingClause cl;
	int size = clauses.size();
	this.bf.append(this.margin);
	this.printPath(sentence.getPathLabel());
	this.printAttr(OplColorScheme.PROCESS_STYLE, sentence.getProcessName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " changes ");
	for (int i = 0; i < size; i++) {
	    this.setDelimiter(i, size, sentence.getLogicalRelation());
	    cl = (ChangingClause) clauses.get(i);
	    this.printAttr(OplColorScheme.OBJECT_STYLE, cl.getObjectName());
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " from ");
	    this.printAttr(OplColorScheme.STATE_STYLE, cl.getSourceStateName());
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " to ");
	    this.printAttr(OplColorScheme.STATE_STYLE, cl
		    .getDestinationStateName());
	}
	this.bf.append(this.endLine);
    }

    public void visit(ConditionSentenceType sentence) {
	java.util.List clauses = sentence.getConditionClause();
	ConditionClause cl;
	int size = clauses.size();
	this.bf.append(this.margin);
	this.printPath(sentence.getPathLabel());
	this.printAttr(OplColorScheme.PROCESS_STYLE, sentence.getProcessName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " occurs if ");
	for (int i = 0; i < size; i++) {
	    this.setDelimiter(i, size, sentence.getLogicalRelation());
	    cl = (ConditionClause) clauses.get(i);
	    this.printAttr(OplColorScheme.OBJECT_STYLE, cl.getObjectName());
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " is ");
	    this.printAttr(OplColorScheme.STATE_STYLE, cl.getStateName());
	}
	this.bf.append(this.endLine);
    }

    public void visit(ConsumptionSentenceType sentence) {
	java.util.List clauses = sentence.getConsumptionClause();
	int size = clauses.size();
	ConsumptionClause cl;
	this.bf.append(this.margin);
	this.printPath(sentence.getPathLabel());
	this.printAttr(OplColorScheme.PROCESS_STYLE, sentence.getProcessName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " consumes ");
	for (int i = 0; i < size; i++) {
	    // System.err.println("My relation is: " +
                // sentence.getLogicalRelation());
	    this.setDelimiter(i, size, sentence.getLogicalRelation());
	    cl = (ConsumptionClause) clauses.get(i);
	    if (cl.getStateName() != null) {
		this.printAttr(OplColorScheme.STATE_STYLE, cl.getStateName());
		this.bf.append(" ");
	    }
	    this.printAttr(OplColorScheme.OBJECT_STYLE, cl.getObjectName());
	}
	this.bf.append(this.endLine);

    }

    public void visit(EffectSentenceType sentence) {
	java.util.List clauses = sentence.getEffectClause();
	int size = clauses.size();
	EffectClause cl;
	this.bf.append(this.margin);
	this.printPath(sentence.getPathLabel());
	this.printAttr(OplColorScheme.PROCESS_STYLE, sentence.getProcessName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " affects ");
	for (int i = 0; i < size; i++) {
	    // System.err.println("My relation is1: " +
                // sentence.getLogicalRelation());
	    this.setDelimiter(i, size, sentence.getLogicalRelation());
	    cl = (EffectClause) clauses.get(i);
	    this.printAttr(OplColorScheme.OBJECT_STYLE, cl.getObjectName());
	}
	this.bf.append(this.endLine);
    }

    public void visit(AgentSentenceType sentence) {
	java.util.List procs = sentence.getTriggeredProcessName();
	int size = procs.size();
	this.bf.append(this.margin);
	this.printPath(sentence.getPathLabel());
	this.printAttr(OplColorScheme.OBJECT_STYLE, sentence.getObjectName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " handles ");
	for (int i = 0; i < size; i++) {
	    // System.err.println("My relation is: " +
                // sentence.getLogicalRelation());
	    this.setDelimiter(i, size, sentence.getLogicalRelation());
	    this.printAttr(OplColorScheme.PROCESS_STYLE, (String) procs.get(i));
	}
	this.bf.append(this.endLine);
    }

    // LERA
    public void visit(StateEntranceSentence sentence) {
	java.util.List procs = sentence.getTriggeredProcess();
	int size = procs.size();
	this.bf.append(this.margin);
	this.printPath(sentence.getPathLabel());
	this.printAttr(OplColorScheme.OBJECT_STYLE, sentence.getObjectName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " triggers ");
	for (int i = 0; i < size; i++) {
	    // System.err.println("My relation is: " +
                // sentence.getLogicalRelation());
	    this.setDelimiter(i, size, sentence.getLogicalRelation());
	    this.printAttr(OplColorScheme.PROCESS_STYLE, (String) procs.get(i));
	}
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " when it enters ");
	this.printAttr(OplColorScheme.STATE_STYLE, sentence.getStateName());
	this.bf.append(this.endLine);
    }

    public void visit(StateTimeoutSentence sentence) {
	java.util.List procs = sentence.getTriggeredProcess();
	int size = procs.size();
	this.bf.append(this.margin);
	this.printPath(sentence.getPathLabel());
	this.printAttr(OplColorScheme.OBJECT_STYLE, sentence.getObjectName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " triggers ");
	for (int i = 0; i < size; i++) {
	    // System.err.println("My relation is: " +
                // sentence.getLogicalRelation());
	    this.setDelimiter(i, size, sentence.getLogicalRelation());
	    this.printAttr(OplColorScheme.PROCESS_STYLE, (String) procs.get(i));
	}
	String date = null;
	if (sentence.getMaxTimeoutValue() != null) {
	    date = OPLGeneral.visit(sentence.getMaxTimeoutValue());
	}
	if ((date != null) && (date.length() > 0)) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " when ");
	    this.printAttr(OplColorScheme.STATE_STYLE, sentence.getStateName());
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " lasts more than ");
	    // System.err.println(" Lasts max: "+);
	    this.printAttr(OplColorScheme.TAG_STYLE, date);

	}
	// if (sentence.getMinReactionTime() != null && sentence.)
	// date = OPLGeneral.visit(sentence.getMinReactionTime());
	// if (date != null && date.length()>0) {
	// bf.append(" with reaction time from ");
	// printAttr(OplColorScheme.TAG_STYLE,
	// OPLGeneral.visit(date));
	// printAttr(OplColorScheme.STATE_STYLE, sentence.getStateName());
	// bf.append(" lasts more than ");
	// //System.err.println(" Lasts max: "+);
	// printAttr(OplColorScheme.TAG_STYLE,
	// OPLGeneral.visit(date));
	// }
	this.bf.append(this.endLine);
    }

    public void visit(GeneralEventSentence sentence, boolean hasStates) {
	java.util.List procs = sentence.getTriggeredProcessName();
	int size = procs.size();
	this.bf.append(this.margin);
	this.printPath(sentence.getPathLabel());
	this.printAttr(OplColorScheme.OBJECT_STYLE, sentence.getObjectName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " triggers ");
	for (int i = 0; i < size; i++) {
	    this.setDelimiter(i, size, sentence.getLogicalRelation());
	    this.printAttr(OplColorScheme.PROCESS_STYLE, (String) procs.get(i));
	}
	if (hasStates) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE,
		    " when its state changes");
	}
	this.bf.append(this.endLine);
    }

    public void visit(EnablingSentenceType sentence) {
	java.util.List clauses = sentence.getEnablingClause();
	int size = clauses.size();
	EnablingClause cl;
	this.bf.append(this.margin);
	this.printPath(sentence.getPathLabel());
	this.printAttr(OplColorScheme.PROCESS_STYLE, sentence.getProcessName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " requires ");
	for (int i = 0; i < size; i++) {
	    this.setDelimiter(i, size, sentence.getLogicalRelation());
	    cl = (EnablingClause) clauses.get(i);
	    if (cl.getStateName() != null) {
		this.printAttr(OplColorScheme.STATE_STYLE, cl.getStateName());
		this.bf.append(" ");
	    }
	    this.printAttr(OplColorScheme.OBJECT_STYLE, cl.getObjectName());
	}
	this.bf.append(this.endLine);
    }

    public void visit(ResultSentenceType sentence) {
	java.util.List clauses = sentence.getResultClause();
	int size = clauses.size();
	ResultClause cl;
	this.bf.append(this.margin);
	this.printPath(sentence.getPathLabel());
	this.printAttr(OplColorScheme.PROCESS_STYLE, sentence.getProcessName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " yields ");
	for (int i = 0; i < size; i++) {
	    this.setDelimiter(i, size, sentence.getLogicalRelation());
	    cl = (ResultClause) clauses.get(i);
	    if (cl.getStateName() != null) {
		this.printAttr(OplColorScheme.STATE_STYLE, cl.getStateName());
		this.bf.append(" ");
	    }
	    this.printAttr(OplColorScheme.OBJECT_STYLE, cl.getObjectName());
	}
	this.bf.append(this.endLine);

    }

    // public java.util.Hashtable getPaths(ThingSentenceSetType e){
    // java.util.Hashtable paths = new java.util.Hashtable();
    // java.util.List oldPath=new java.util.LinkedList();
    // if(e.getStateEntranceSentence().size()>0){
    // oldPath =
        // ((StateEntranceSentence)e.getStateEntranceSentence().get(0)).getPathLabel();
    // }
    // int j=0;
    // for(int i=0;i<e.getStateEntranceSentence().size();i++){
    // StateEntranceSentence tmp =
        // (StateEntranceSentence)e.getStateEntranceSentence().get(i);
    // if(tmp.getPathLabel().equals(oldPath)){
    // j++;
    // }else {
    // paths.put(oldPath,new Integer(j));
    // oldPath = tmp.getPathLabel();
    // j=1;
    // }
    // }
    // return paths;
    // }

    // public void visitStateTriggers(ThingSentenceSetType e){
    // if(e.getStateEntranceSentence().size()==0)
    // return;
    // java.util.List oldPath =
        // ((StateEntranceSentence)e.getStateEntranceSentence().get(0)).getPathLabel();
    // for(int i=0;i<e.getStateEntranceSentence().size();){
    // int start = i;
    // int end = start;
    // int size=0;
    // java.util.List currPath =
        // ((StateEntranceSentence)e.getStateEntranceSentence().get(i)).getPathLabel();
    // while(currPath.equals(oldPath)){
    // end++;
    // if( end <
    // e.getStateEntranceSentence().size())
    // currPath =
        // ((StateEntranceSentence)e.getStateEntranceSentence().get(end)).getPathLabel();
    // }
    // i=end;
    // size = end-start;
    // end--;
    // StateEntranceSentence tmp =
        // (StateEntranceSentence)e.getStateEntranceSentence().get(start);
    // bf.append(margin);
    // printPath(tmp.getPathLabel());
    // printAttr(OplColorScheme.OBJECT_STYLE, tmp.getObjectName());
    // printAttr(OplColorScheme.DEFAULT_STYLE," triggers ");
    // for(int k=start;k<=end;k++){
    // setDelimiter(k-start, size, AND);
    // tmp = (StateEntranceSentence) e.getStateEntranceSentence().get(k);
    // visit(tmp);
    // }
    // bf.append(endLine);
    // oldPath =
        // ((StateEntranceSentence)e.getStateEntranceSentence().get(i)).getPathLabel();
    // }
    // }

    // public void visitStateTriggers(ThingSentenceSetType e){
    // Hashtable paths = getPaths(e);
    // boolean firstTime = true;
    // int size =0;
    // int j=0;
    // for(int i=0;i<e.getStateEntranceSentence().size();i++){
    // StateEntranceSentence tmp = (StateEntranceSentence)
        // e.getStateEntranceSentence().get(i);
    // if(firstTime){
    // size = ((Integer)paths.get(tmp.getPathLabel())).intValue();
    // bf.append("my size is"+size);
    // j=0;
    // firstTime = false;
    // bf.append(margin);
    // printPath(tmp.getPathLabel());
    // printAttr(OplColorScheme.OBJECT_STYLE, tmp.getObjectName());
    // printAttr(OplColorScheme.DEFAULT_STYLE," triggers ");
    // setDelimiter(j, size, AND);
    // visit(tmp);
    // bf.append("my size is"+size);
    // if(size==1)bf.append(endLine);
    // }else{
    // if(j+1==size){
    // setDelimiter(j, size, AND);
    // visit(tmp);
    // bf.append(endLine);
    // firstTime = true;
    // }else {
    // setDelimiter(j, size, AND);
    // visit(tmp);
    // }
    // }
    // j++;
    // paths.put(tmp.getPathLabel(), new
        // Integer(((Integer)paths.get(tmp.getPathLabel())).intValue()-1));
    // }
    //
    // }

    public void visitExistential(ThingSentenceSetType e) {
	this.bf.append(this.margin);
	String exist = e.getExistential();
	if (e.equals("exists")) {
	    this.printAttr(OplColorScheme.PROCESS_STYLE, e
		    .getSubjectThingName());
	} else {
	    this
		    .printAttr(OplColorScheme.OBJECT_STYLE, e
			    .getSubjectThingName());
	}
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " " + exist);
	this.bf.append(this.endLine);
    }

    // Recursive visit of all the sentences
    public void visit(ThingSentenceSetType e) {
	int i;
	boolean hasStates = false;
	if (e.getExistential() != null) {
	    // visitExistential(e);
	    return;
	}
	if (e.getTypeDeclarationSentence() != null) {
	    this.visit(e.getTypeDeclarationSentence());
	    this.printRoles(e);
	    return;
	}
	if (e.getObjectEnvironmentalPhysicalSentence() != null) {
	    this.visit(e.getObjectEnvironmentalPhysicalSentence());
	}
	if (e.getObjectInheritanceSentenceSet() != null) {
	    this.visit1((ObjectInheritanceSentenceSet) e
		    .getObjectInheritanceSentenceSet());
	}
	if (e.getObjectInstanceSentence() != null) {
	    this.visit(e.getObjectInstanceSentence());
	}
	if (e.getObjectStateSentence() != null) {
	    this.visit(e.getObjectStateSentence());
	    hasStates = true;
	}
	if (e.getObjectExhibitionSentenceSet() != null) {
	    this.visit(e.getObjectExhibitionSentenceSet());
	}
	if (e.getObjectAggregationSentenceSet() != null) {
	    this.visit(e.getObjectAggregationSentenceSet());
	}
	if (e.getObjectUniDirectionalRelationSentence() != null) {
	    for (i = 0; i < e.getObjectUniDirectionalRelationSentence().size(); i++) {
		this.visit((ObjectUniDirectionalRelationSentenceType) e
			.getObjectUniDirectionalRelationSentence().get(i));
	    }
	}
	if (e.getObjectBiDirectionalRelationSentence() != null) {
	    for (i = 0; i < e.getObjectBiDirectionalRelationSentence().size(); i++) {
		this.visit((ObjectBiDirectionalRelationSentence) e
			.getObjectBiDirectionalRelationSentence().get(i), e
			.getSubjectThingName());
	    }
	}
	for (i = 0; i < e.getAgentSentence().size(); i++) {
	    this.visit((AgentSentence) e.getAgentSentence().get(i));
	}
	for (i = 0; i < e.getGeneralEventSentence().size(); i++) {
	    this.visit((GeneralEventSentence) e.getGeneralEventSentence()
		    .get(i), hasStates);
	}
	// visitStateTriggers(e);
	for (i = 0; i < e.getStateEntranceSentence().size(); i++) {
	    this.visit((StateEntranceSentence) e.getStateEntranceSentence()
		    .get(i));
	}
	for (i = 0; i < e.getStateTimeoutSentence().size(); i++) {
	    this.visit((StateTimeoutSentence) e.getStateTimeoutSentence()
		    .get(i));
	}
	if (e.getObjectInZoomingSentenceSet() != null) {
	    this.visit(e.getObjectInZoomingSentenceSet());
	}
	if (e.getProcessEnvironmentalPhysicalSentence() != null) {
	    this.visit(e.getProcessEnvironmentalPhysicalSentence());
	}
	if (e.getProcessInheritanceSentenceSet() != null) {
	    this
		    .visit1((extensionTools.opl2.generated.ProcessInheritanceSentenceSet) e
			    .getProcessInheritanceSentenceSet());
	}
	if (e.getProcessInstanceSentence() != null) {
	    this.visit(e.getProcessInstanceSentence());
	}
	if (e.getProcessExhibitionSentenceSet() != null) {
	    this.visit(e.getProcessExhibitionSentenceSet());
	}
	if (e.getProcessAggregationSentenceSet() != null) {
	    this.visit(e.getProcessAggregationSentenceSet());
	}
	if (e.getProcessUniDirectionalRelationSentence() != null) {
	    for (i = 0; i < e.getProcessUniDirectionalRelationSentence().size(); i++) {
		this.visit((ProcessUniDirectionalRelationSentence) e
			.getProcessUniDirectionalRelationSentence().get(i));
	    }
	}
	if (e.getProcessBiDirectionalRelationSentence() != null) {
	    for (i = 0; i < e.getProcessBiDirectionalRelationSentence().size(); i++) {
		this.visit((ProcessBiDirectionalRelationSentence) e
			.getProcessBiDirectionalRelationSentence().get(i), e
			.getSubjectThingName());
	    }
	}
	for (i = 0; i < e.getConditionSentence().size(); i++) {
	    this.visit((ConditionSentenceType) e.getConditionSentence().get(i));
	}
	for (i = 0; i < e.getEnablingSentence().size(); i++) {
	    this.visit((EnablingSentenceType) e.getEnablingSentence().get(i));
	}
	for (i = 0; i < e.getEffectSentence().size(); i++) {
	    this.visit((EffectSentenceType) e.getEffectSentence().get(i));
	}
	for (i = 0; i < e.getChangingSentence().size(); i++) {
	    this.visit((ChangingSentenceType) e.getChangingSentence().get(i));
	}
	for (i = 0; i < e.getConsumptionSentence().size(); i++) {
	    this.visit((ConsumptionSentenceType) e.getConsumptionSentence()
		    .get(i));
	}
	for (i = 0; i < e.getResultSentence().size(); i++) {
	    this.visit((ResultSentenceType) e.getResultSentence().get(i));
	}
	for (i = 0; i < e.getProcessTimeoutSentence().size(); i++) {
	    this.visit((ProcessTimeoutSentenceType) e
		    .getProcessTimeoutSentence().get(i));
	}
	for (i = 0; i < e.getProcessInvocationSentence().size(); i++) {
	    this.visit((ProcessInvocationSentenceType) e
		    .getProcessInvocationSentence().get(i));
	}
	if (e.getProcessInZoomingSentenceSet() != null) {
	    this.visit(e.getProcessInZoomingSentenceSet());
	}
	this.printRoles(e);
    }

    public void printRoles(ThingSentenceSetType e) {
	if ((e.getRole() == null) || e.getRole().isEmpty()) {
	    return;
	}
	this.bf.append(this.margin);
	this.printAttr(OplColorScheme.OBJECT_STYLE, e.getSubjectThingName());
	if (e.getRole().size() > 1) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " plays roles");
	} else {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " plays the role");
	}
	for (int i = 0; i < e.getRole().size(); i++) {
	    Role rl = (Role) e.getRole().get(i);
	    this.setDelimiter(i, e.getRole().size(), AND);
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " of "
		    + rl.getRoleName());
	    if ((rl.getLibrary() != null) && !rl.getLibrary().equals("")) {
		this.printAttr(OplColorScheme.DEFAULT_STYLE, " of "
			+ rl.getLibrary());
	    }
	}
	this.bf.append(this.endLine);
    }

    public void visit(TypeDeclarationSentenceType e) {
	this.bf.append(this.margin);
	this.printAttr(OplColorScheme.OBJECT_STYLE, e.getObjectName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " is of type ");
	char firstChar = e.getObjectType().charAt(0);
	if (('0' <= firstChar) && (firstChar <= '9')) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE,
		    "character string of legth " + e.getObjectType());
	} else {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, e.getObjectType());
	}
	this.bf.append(this.endLine);
	if (e.getObjectScope().equals("public")) {
	    return;
	} else {
	    this.bf.append(this.margin);
	    this.printAttr(OplColorScheme.OBJECT_STYLE, e.getObjectName());
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " is "
		    + e.getObjectScope());
	    this.bf.append(this.endLine);
	}
	if (e.getInitialValue() != null) {
	    if (e.getInitialValue().equals("")) {
		return;
	    }
	} else {
	    return;
	}
	this.bf.append(this.margin);
	this.printAttr(OplColorScheme.OBJECT_STYLE, e.getObjectName());
	this.printAttr(OplColorScheme.DEFAULT_STYLE, " is "
		+ e.getInitialValue() + " by default");
	this.bf.append(this.endLine);

    }

    public void printParagraph(java.util.List things, boolean isPerState) {
	if (things.isEmpty()) {
	    return;
	}
	this.margin.append("&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp ");
	if (!isPerState) {
	    for (java.util.ListIterator itr = things.listIterator(); itr
		    .hasNext();) {
		this.visit((ThingSentenceSet) itr.next());
	    }
	} else {
	    StateClause cl;
	    StateClause def = this.getDefaultState(things);
	    if (def != null) {
		this.visit(def);
	    }
	    for (java.util.ListIterator itr = things.listIterator(); itr
		    .hasNext();) {
		cl = (StateClause) itr.next();
		if (cl != def) {
		    this.visit(cl);
		}
	    }
	}
	this.margin.delete(this.margin.length() - 36, this.margin.length());
    }

    public void printAttr(int attributeName, String value) {
	/*
         * if (colors.containsKey(attributeName)){ bf.append(value); return; }
         */
	this.bf.append(this.colorScheme.getAttribute(attributeName)
		.openingHTMLFontTag());
	this.bf.append(value);
	this.bf.append(this.colorScheme.getAttribute(attributeName)
		.closingHTMLFontTag());
    }

    public void printPath(java.util.List pathList) {
	if (pathList.isEmpty()) {
	    return;
	}
	this.printAttr(OplColorScheme.DEFAULT_STYLE, "Following path");
	for (int i = 0; i < pathList.size(); i++) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " ");
	    this.printAttr(OplColorScheme.TAG_STYLE, (String) pathList.get(i));
	}
	this.printAttr(OplColorScheme.DEFAULT_STYLE, ", ");
    }

    protected void setDelimiter(int index, int size, int op) {
	if (size == 1) {
	    return;
	}
	if (index == size - 1) {
	    if (size > 2) {
		this.printAttr(OplColorScheme.DEFAULT_STYLE, ",");
	    }
	    if (op != XOR) {
		this.printAttr(OplColorScheme.DEFAULT_STYLE, " " + prop[op]
			+ " ");
	    } else {
		this.printAttr(OplColorScheme.DEFAULT_STYLE, " " + prop[OR]
			+ " ");
	    }
	} else if ((index == size - 2) && (op == XOR)) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, " " + prop[op] + " ");
	} else if (index > 0) {
	    this.printAttr(OplColorScheme.DEFAULT_STYLE, ", ");
	}
    }

    protected void setDelimiter(int index, int size, String op) {
	if (op.equals("and")) {
	    this.setDelimiter(index, size, AND);
	} else if (op.equals("or")) {
	    this.setDelimiter(index, size, XOR);
	} else {
	    this.setDelimiter(index, size, OR);
	}
    }

    protected void setEndLine() {
	this.endLine = this.colorScheme.getAttribute(
		OplColorScheme.DEFAULT_STYLE).openingHTMLFontTag()
		+ "."
		+ this.colorScheme.getAttribute(OplColorScheme.DEFAULT_STYLE)
			.closingHTMLFontTag() + "<BR>";

    }

    protected ISystemStructure elements;

    protected String startHTML;

    protected String endHTML;

    protected String endLine;

    protected StringBuffer margin = new StringBuffer();
}