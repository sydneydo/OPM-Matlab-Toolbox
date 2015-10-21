package extensionTools.uml.umlDiagrams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.IInstance;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IObjectEntry;
import exportedAPI.opcatAPI.IObjectInstance;
import exportedAPI.opcatAPI.IProcessEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.IStateEntry;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.ISystemStructure;
import exportedAPI.opcatAPI.IThingInstance;
import gui.metaLibraries.logic.MetaLibrary;
import gui.util.OpcatException;
import gui.util.OpcatLogger;

/**
 * The class handles generation of class diagrams.
 */
public class ClassDiagram {

	private Enumeration baseEnum = null;
	private ISystemStructure localStructure;
	private XmiWriter writer = null;
	private HashMap<Long, IObjectInstance> objectInstances = null;

	private final String diagramID = "G.0000.11";

	/**
	 * Loads the class diagram createor with a given
	 * 
	 * @param baseEnum
	 */
	public ClassDiagram(Enumeration baseEnum) {
		this.baseEnum = baseEnum;
	}

	/**
	 * This function creates the part of Class Diagram in XML code
	 * 
	 * @param ISystem
	 *            sys
	 * @param Vector
	 *            tagsVec - responsible for outside printing of the part
	 *            TaggedValue in XML
	 * @param Vector
	 *            classDiagramElements - responsible for outside printing of
	 *            classes in XML
	 * @param Vector
	 *            specVec - responsible for outside printing in XML
	 * @param Vector
	 *            assEndVec - responsible for outside printing of assosiationEnd
	 *            in class diagram
	 * @param Vector
	 *            assVec - responsible for outside printing of assosiations in
	 *            class diagram
	 */

	public void createClassDiagram(ISystem sys, XmiWriter writer,
			Vector<String> tagsVec, Vector<String> classDiagramElements,
			Vector specVec, Vector assEndVec, Vector assVec,
			HashMap<Long, IObjectInstance> objectInstances)
			throws OpcatException {
		this.writer = writer;
		this.localStructure = sys.getISystemStructure();
		this.objectInstances = objectInstances;
		if (objectInstances == null) {
			throw new OpcatException("objectInstances was passed null");
		}

		if (baseEnum == null) {
			generateClassDiagram(localStructure.getElements(), tagsVec,
					classDiagramElements, specVec, assEndVec, assVec);
		} else {
			generateClassDiagram(baseEnum, tagsVec, classDiagramElements,
					specVec, assEndVec, assVec);
		}
	}

	private void generateClassDiagram(Enumeration inEnum,
			Vector<String> tagsVec, Vector<String> classDiagramElements,
			Vector specVec, Vector<String> assEndVec, Vector<String> assVec)
			throws OpcatException {
		try {

			IEntry abstractEntry = null, abstractEntry2;
			IObjectEntry obj, obj1, obj2;
			IRelationEntry agg_rel;
			Enumeration e;
			Enumeration data, datas;
			Enumeration agg_list, uni_list, inst_list, childrenList;
			String temp = "1";
			HashMap dataTypes = new HashMap();
			IObjectInstance objInst;
			int flag;
			ArrayList done = new ArrayList();
			ArrayList<IObjectEntry> seconedRun = new ArrayList<IObjectEntry>();
			Vector generalizationVec = new Vector();
			Vector dataType = new Vector();

			temp = "<!--  ===============[Class Model] ====================  --> \n";
			temp += "<UML:Model xmi.id=\""
					+ diagramID
					+ "\" name=\"Design Model\" visibility=\"public\" isRoot=\"false\" isSpecification=\"false\" isLeaf=\"false\" isAbstract=\"false\" namespace=\""
					+ DiagramsCreator.MAIN_MODEL_NAME + "\">\n";
			temp += "<UML:Namespace.ownedElement>\n";
			writer.write(temp.getBytes());

			e = inEnum;

			while (e.hasMoreElements()) {
				Object tempObj = e.nextElement();
				while (!(tempObj instanceof IEntry)) {
					tempObj = e.nextElement();
				}
				abstractEntry = (IEntry) tempObj;
				if (abstractEntry instanceof IObjectEntry) { // if object
					obj = (IObjectEntry) abstractEntry;
					if (!done.contains(obj)) {
						String umlType = "Class";
						if ((obj.getRolesString(MetaLibrary.TYPE_POLICY) != null)) {
							if (obj.getRolesString(
									MetaLibrary.TYPE_POLICY)
									.toLowerCase().contains("package")) {
								umlType = "Package";
							}

							if (obj.getRolesString(
									MetaLibrary.TYPE_POLICY)
									.toLowerCase().contains("interface")) {
								umlType = "Interface";
							}
						}

						if (umlType.equalsIgnoreCase("package")) {
							generateClass(obj, specVec, umlType, done, tagsVec,
									classDiagramElements, dataTypes,
									generalizationVec, dataType);
						} else {
							seconedRun.add(obj);
						}
					}// end of if obj is physical

				}// end of if instanceof obj
				/**
				 * *************************************************************
				 * ************************
				 */
			}
			e = Collections.enumeration(seconedRun);
			while (e.hasMoreElements()) {
				Object tempObj = e.nextElement();
				while (!(tempObj instanceof IEntry)) {
					tempObj = e.nextElement();
				}
				abstractEntry = (IEntry) tempObj;
				if (abstractEntry instanceof IObjectEntry) { // if object
					obj = (IObjectEntry) abstractEntry;
					if (!done.contains(obj)) {
						String umlType = "Class";
						if ((obj.getRolesString(MetaLibrary.TYPE_POLICY) != null)) {
							if (obj.getRolesString(
									MetaLibrary.TYPE_POLICY)
									.toLowerCase().contains("package")) {
								umlType = "Package";
							}

							if (obj.getRolesString(
									MetaLibrary.TYPE_POLICY)
									.toLowerCase().contains("interface")) {
								umlType = "Interface";
							}
						}

						// if (umlType.equalsIgnoreCase("package")) {
						generateClass(obj, specVec, umlType, done, tagsVec,
								classDiagramElements, dataTypes,
								generalizationVec, dataType);
						// } else {
						// seconedRun.add(obj) ;
						// }
					}// end of if obj is physical

				}// end of if instanceof obj
				/**
				 * *************************************************************
				 * ************************
				 */
			}
			/**
			 * *****************************************************************
			 * ********************
			 */
			// =====printouts of a datatype
			// void DataType
			temp = "<!--  ============ <void>    [DataType] ============== -->\n";
			temp += "<UML:DataType xmi.id=\"G.00\" name=\"void\" visibility=\"public\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\" />\n";
			// undefined DataType
			temp += "<!--  ============ <undefined>    [DataType] ============== -->\n";
			temp += "<UML:DataType xmi.id=\"G.0\" name=\"undefined\" visibility=\"public\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\" />\n";
			writer.write(temp.getBytes());

			data = Collections.enumeration(dataTypes.values());
			while (data.hasMoreElements()) {
				obj = (IObjectEntry) data.nextElement();
				datas = obj.getStates(); // if the datatype is a range of
				// states
				flag = 0;
				if (datas.hasMoreElements()) { // list of states
					temp = "<!--  ============ [DataType] ============== -->\n";
					writer.write(temp.getBytes());
					temp = "<UML:DataType xmi.id=\"G." + obj.getId()
							+ "\" name=\"{";
					writer.write(temp.getBytes());
					while (datas.hasMoreElements()) { // going trought all the
						// states
						if (flag == 1) {
							temp = ",";
							writer.write(temp.getBytes());
						}
						flag = 1;
						temp = ((IStateEntry) datas.nextElement()).getName();
						writer.write(temp.getBytes());
					}// end of while
					temp = "}\" visibility=\"public\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\" />\n";
					writer.write(temp.getBytes());
				}// end of if list of states
				else { // not states but defined (boolean, int, string...
					temp = "<!--  ============ [DataType] ============== -->\n";
					writer.write(temp.getBytes());
					temp = "<UML:DataType xmi.id=\"G."
							+ obj.getId()
							+ "\" name=\""
							+ obj.getType()
							+ "\" visibility=\"public\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\" />\n";
					writer.write(temp.getBytes());
				}// end of else
			} // the end of while the data type

			// Handling Aggregation
			agg_list = localStructure.getElements();
			while (agg_list.hasMoreElements()) {
				abstractEntry2 = (IEntry) agg_list.nextElement();
				if (abstractEntry2 instanceof IRelationEntry) { // if relation
					agg_rel = (IRelationEntry) abstractEntry2;
					if (agg_rel.getRelationType() == OpcatConstants.AGGREGATION_RELATION) {
						long s_id = agg_rel.getSourceId();
						long d_id = agg_rel.getDestinationId();
						if ((localStructure.getIEntry(s_id) instanceof IObjectEntry)
								&& (localStructure.getIEntry(d_id) instanceof IObjectEntry)) {
							obj1 = (IObjectEntry) localStructure
									.getIEntry(s_id);
							obj2 = (IObjectEntry) localStructure
									.getIEntry(d_id);

							boolean isPackage = false;
							if ((obj1
									.getRolesString(MetaLibrary.TYPE_POLICY) != null)) {
								if (obj1.getRolesString(
										MetaLibrary.TYPE_POLICY)
										.toLowerCase().contains("package")) {
									isPackage = true;
								}
							}
							if ((!obj1.isPhysical()) && (!obj2.isPhysical())
									&& (!obj1.isEnvironmental())
									&& (!obj2.isEnvironmental()) && !isPackage) {
								obj1 = this.ReturnClass(localStructure,
										(IEntry) obj1);// =====//
								obj2 = this.ReturnClass(localStructure,
										(IEntry) obj2);// =====//
								if ((!obj1.isPhysical())
										&& (!obj2.isPhysical())
										&& (!obj1.isEnvironmental())
										&& (!obj2.isEnvironmental())) {
									assVec
											.addElement("" + agg_rel.getId()
													+ "");
									assEndVec.addElement("" + agg_rel.getId()
											+ ".1");
									assEndVec.addElement("" + agg_rel.getId()
											+ ".2");
									generateAssociation(
											agg_rel.getId(),
											"",
											obj1.getId(),
											obj2.getId(),
											agg_rel.getSourceCardinality(),
											agg_rel.getDestinationCardinality(),
											false, true);
								}
							}
						}// end of if obj is physical
					}// end of if agg_rel
				}// end of if instance of rel
			}// end of while

			// Uni-directional relation
			uni_list = localStructure.getElements();
			while (uni_list.hasMoreElements()) {
				abstractEntry2 = (IEntry) uni_list.nextElement();
				if (abstractEntry2 instanceof IRelationEntry) { // if relation
					agg_rel = (IRelationEntry) abstractEntry2;
					if (agg_rel.getRelationType() == OpcatConstants.UNI_DIRECTIONAL_RELATION) {
						long s_id = agg_rel.getSourceId();
						long d_id = agg_rel.getDestinationId();
						String text = agg_rel.getForwardRelationMeaning();
						obj1 = (IObjectEntry) localStructure.getIEntry(s_id);
						obj2 = (IObjectEntry) localStructure.getIEntry(d_id);
						if ((!obj1.isPhysical()) && (!obj2.isPhysical())
								&& (!obj1.isEnvironmental())
								&& (!obj2.isEnvironmental())) {
							obj1 = this.ReturnClass(localStructure,
									(IEntry) obj1);// =====//
							obj2 = this.ReturnClass(localStructure,
									(IEntry) obj2);// =====//
							if ((!obj1.isPhysical()) && (!obj2.isPhysical())
									&& (!obj1.isEnvironmental())
									&& (!obj2.isEnvironmental())) {
								assVec.addElement("" + agg_rel.getId() + "");
								assEndVec.addElement("" + agg_rel.getId()
										+ ".1");
								assEndVec.addElement("" + agg_rel.getId()
										+ ".2");
								generateAssociation(agg_rel.getId(), text, obj1
										.getId(), obj2.getId(), agg_rel
										.getSourceCardinality(), agg_rel
										.getDestinationCardinality(), false,
										true);

							}
						}// end of if
					}// end of if
				}// end of if
			}// end of while

			// ====bi-directional relation
			uni_list = localStructure.getElements();
			while (uni_list.hasMoreElements()) {
				abstractEntry2 = (IEntry) uni_list.nextElement();
				if (abstractEntry2 instanceof IRelationEntry) { // if relation
					agg_rel = (IRelationEntry) abstractEntry2;
					if (agg_rel.getRelationType() == OpcatConstants.BI_DIRECTIONAL_RELATION) {
						long s_id = agg_rel.getSourceId();
						long d_id = agg_rel.getDestinationId();
						obj1 = (IObjectEntry) localStructure.getIEntry(s_id);
						obj2 = (IObjectEntry) localStructure.getIEntry(d_id);
						if ((!obj1.isPhysical()) && (!obj2.isPhysical())
								&& (!obj1.isEnvironmental())
								&& (!obj2.isEnvironmental())) {
							obj1 = this.ReturnClass(localStructure,
									(IEntry) obj1);// =====//
							obj2 = this.ReturnClass(localStructure,
									(IEntry) obj2);// =====//
							if ((!obj1.isPhysical()) && (!obj2.isPhysical())
									&& (!obj1.isEnvironmental())
									&& (!obj2.isEnvironmental())) {
								assVec.addElement("" + agg_rel.getId() + "");
								assEndVec.addElement("" + agg_rel.getId()
										+ ".1");
								assEndVec.addElement("" + agg_rel.getId()
										+ ".2");
								String text = agg_rel
										.getForwardRelationMeaning();
								generateAssociation(agg_rel.getId(), text, obj1
										.getId(), obj2.getId(), agg_rel
										.getSourceCardinality(), agg_rel
										.getDestinationCardinality(), true,
										true);

							}
						}// end of if
					}// end of if
				}// end of if
			}// end of while

			e = localStructure.getElements();
			while (e.hasMoreElements()) {
				abstractEntry = (IEntry) e.nextElement();
				if (abstractEntry instanceof IObjectEntry) {
					obj = (IObjectEntry) abstractEntry;
					if ((!obj.isPhysical()) && (!obj.isEnvironmental())
							&& (!this.isFeature(localStructure, obj))) {
						inst_list = obj.getInstances();
						while (inst_list.hasMoreElements()) {
							objInst = (IObjectInstance) inst_list.nextElement();
							childrenList = objInst.getChildThings();
							while (childrenList.hasMoreElements()) {
								abstractEntry = ((IInstance) childrenList
										.nextElement()).getIEntry();
								if (abstractEntry instanceof IObjectEntry) {
									obj1 = (IObjectEntry) abstractEntry;
									if ((!obj1.isPhysical())
											&& (!obj1.isEnvironmental())) {
										obj1 = this.ReturnClass(localStructure,
												obj1);
										if ((!obj1.isPhysical())
												&& (!obj1.isEnvironmental())
												&& (!this.isFeature(
														localStructure, obj1))) {
											assVec.addElement("" + obj1.getId()
													+ ".3");
											assEndVec.addElement(""
													+ obj1.getId() + ".3.1");
											assEndVec.addElement(""
													+ obj1.getId() + ".3.2");
											generateAssociation(obj1.getId(),
													"", obj1.getId(), obj
															.getId(), "1", "1",
													true, true);
										}
									}
								}
							}
						}
					}
				}// end of if
			}// end of while

			temp = "</UML:Namespace.ownedElement>\n";
			temp += "</UML:Model>\n";
			writer.write(temp.getBytes());

		}// end of try
		catch (Exception e) {
			System.out.println("error");
			return;
		}// end of catch

	}// end of func

	private long DataTypesUpdate(IObjectEntry obj, HashMap dataTypes) {
		long id = 0;
		Iterator iter = dataTypes.keySet().iterator();
		while (iter.hasNext()) {
			Long objID = (Long) iter.next();
			IObjectEntry type = (IObjectEntry) dataTypes.get(objID);
			if (type.getType().equalsIgnoreCase(obj.getType())) {
				id = objID.longValue();
				break;
			}
		}

		if (id == 0) {
			id = obj.getId();
			dataTypes.put(new Long(obj.getId()), obj);
		}
		return id;

	}

	private ArrayList getAggragationObjects(IEntry entry) {
		ArrayList aggragation = new ArrayList();
		Enumeration agg_list = localStructure.getElements();
		while (agg_list.hasMoreElements()) {
			IEntry entry2 = (IEntry) agg_list.nextElement();
			if (entry2 instanceof IRelationEntry) { // if relation
				IRelationEntry agg_rel = (IRelationEntry) entry2;
				if (agg_rel.getRelationType() == OpcatConstants.AGGREGATION_RELATION) {
					long s_id = agg_rel.getSourceId();
					long d_id = agg_rel.getDestinationId();
					if (s_id == entry.getId()) {
						aggragation.add(localStructure.getIEntry(d_id));
					}
				}
			}
		}
		return aggragation;
	}

	private void generateInnerClass(IObjectEntry obj, Vector<String> specVec,
			HashMap dataTypes, Vector<IRelationEntry> generalizationVec,
			Vector dataType) {

		IRelationEntry relx;
		Enumeration en, rel_list;
		Enumeration g_list;
		String temp = "1";

		long objId;
		int flag;
		String scope = "";
		try {

			// specialization
			rel_list = obj.getRelationBySource(); // list of relation in which
			// obj is the source
			flag = 0;
			while (rel_list.hasMoreElements()) {
				IRelationEntry spec_rel = (IRelationEntry) rel_list
						.nextElement();
				if (spec_rel.getRelationType() == OpcatConstants.SPECIALIZATION_RELATION) {// if
					// it
					// is
					// specialization
					long dist_id = spec_rel.getDestinationId();
					IObjectEntry dist = (IObjectEntry) localStructure
							.getIEntry(dist_id);// destination
					// object
					if ((!dist.isPhysical()) && (!dist.isEnvironmental())
							&& (!this.isFeature(localStructure, dist))) {
						if (flag == 0) {
							temp = "specialization=\" ";
							writer.write(temp.getBytes());
							flag = 1;
						} // end of if
						temp = "G." + spec_rel.getId() + " ";
						writer.write(temp.getBytes());
						specVec.addElement("" + spec_rel.getId() + "");
					}
				}
			}
			if (flag == 1) {// means there were an specalization
				temp = "\" "; // closing the specialization by "
				writer.write(temp.getBytes());
			}
			// generalization
			rel_list = obj.getRelationByDestination(); // list of relations in
			// which obj is the
			// destination
			flag = 0;
			while (rel_list.hasMoreElements()) {
				IRelationEntry gen_rel = (IRelationEntry) rel_list
						.nextElement();
				if (gen_rel.getRelationType() == OpcatConstants.SPECIALIZATION_RELATION) {
					long source_id = gen_rel.getSourceId();
					IObjectEntry dist = (IObjectEntry) localStructure
							.getIEntry(source_id); // source
					// object
					if ((!dist.isPhysical()) && (!dist.isEnvironmental())
							&& (!this.isFeature(localStructure, dist))) {
						if (flag == 0) {
							temp = "generalization=\" ";
							writer.write(temp.getBytes());
							flag = 1;
						}
						temp = "G." + gen_rel.getId() + " ";
						writer.write(temp.getBytes());
						relx = (IRelationEntry) localStructure
								.getIEntry(gen_rel.getId());
						generalizationVec.addElement(relx); // adding to the
						// vector
					}
				}
			}
			if (flag == 1) { // mens there were generalization
				temp = "\""; // closing the generalization by "
				writer.write(temp.getBytes());
			}
			temp = ">\n"; // closing the row of class
			writer.write(temp.getBytes());
			if (flag == 1) {// in case of generalization
				g_list = generalizationVec.elements(); // list of
				// all
				// generalization
				// relations
				while (g_list.hasMoreElements()) {
					relx = (IRelationEntry) g_list.nextElement();
					if (relx.getDestinationId() == obj.getId()) {
						temp = "<UML:Namespace.ownedElement>\n";
						writer.write(temp.getBytes());
						temp = "<UML:Generalization xmi.id=\"G."
								+ relx.getId()
								+ "\" name=\"\" visibility=\"public\" discriminator=\"\" child=\"S."
								+ relx.getDestinationId() + "\" parent=\"S."
								+ relx.getSourceId() + "\"/>\n";
						writer.write(temp.getBytes());
						temp = "</UML:Namespace.ownedElement>\n";
						writer.write(temp.getBytes());
					}
				}// end of while
			}// end of if
			// =======atributes
			temp = "<UML:Classifier.feature>\n";
			writer.write(temp.getBytes());

			if (obj.getStates().hasMoreElements()) { // if the
				// object
				// has
				// states -
				// add
				// attribute
				// "status"
				// add the type to the datatype list
				dataType.addElement(obj);
				temp = "<!-- === Attribute ====================  -->\n";
				writer.write(temp.getBytes());
				if (obj.getScope().compareTo("0") == 0) {
					scope = "public";
				}
				if (obj.getScope().compareTo("1") == 0) {
					scope = "protected";
				}
				if (obj.getScope().compareTo("2") == 0) {
					scope = "private";
				}
				temp = "<UML:Attribute xmi.id=\"S."
						+ obj.getId()
						+ ".state\" name=\"status\" visibility=\""
						+ scope
						+ "\" ownerScope=\"instance\" changeability=\"changeable\" targetScope=\"instance\" type=\"G."
						// + obj.getId() + "\">\n";
						+ DataTypesUpdate(obj, dataTypes) + "\">\n";
				writer.write(temp.getBytes());
				temp = "<UML:StructuralFeature.multiplicity>\n";
				writer.write(temp.getBytes());
				temp = "<UML:Multiplicity>\n";
				writer.write(temp.getBytes());
				temp = "<UML:Multiplicity.range>\n";
				writer.write(temp.getBytes());
				temp = "<UML:MultiplicityRange lower=\"1\" upper=\"1\" /> \n";
				writer.write(temp.getBytes());
				temp = "</UML:Multiplicity.range>\n";
				writer.write(temp.getBytes());
				temp = " </UML:Multiplicity>\n";
				writer.write(temp.getBytes());
				temp = "</UML:StructuralFeature.multiplicity>\n";
				writer.write(temp.getBytes());
				temp = "<UML:Attribute.initialValue>\n";
				writer.write(temp.getBytes());
				temp = " <UML:Expression language=\"\" body=\""
						+ obj.getInitialValue() + "\" /> \n";
				writer.write(temp.getBytes());
				temp = "</UML:Attribute.initialValue>\n";
				writer.write(temp.getBytes());
				temp = "</UML:Attribute>\n";
				writer.write(temp.getBytes());
			}// end of if atrib

			en = obj.getRelationBySource(); // list of all that
			// connected to the
			// given object,object
			// is the source
			while (en.hasMoreElements()) {
				IRelationEntry rel = (IRelationEntry) en.nextElement();

				if (rel.getRelationType() == OpcatConstants.EXHIBITION_RELATION) { // if
					// attribute
					if (localStructure.getIEntry(rel.getDestinationId()) instanceof IObjectEntry) { // and
						// if
						// object
						IObjectEntry dist = (IObjectEntry) localStructure
								.getIEntry(rel.getDestinationId());
						if ((!dist.isEnvironmental()) && (!dist.isPhysical())) {
							objId = 0; // if datatype is undefined:
							// type=G.0, else
							// type=G.obj_id
							if (((dist.getStates()).hasMoreElements())
									|| (dist.getType().compareTo("") != 0)) {
								dataType.addElement(dist);
								// objId = dist.getId(); // datatype
								objId = DataTypesUpdate(dist, dataTypes); // datatype
								// checking
							}
							temp = "<!-- === Attribute ====================  -->\n";
							writer.write(temp.getBytes());
							if (dist.getScope().compareTo("0") == 0) {
								scope = "public";
							}
							if (dist.getScope().compareTo("1") == 0) {
								scope = "protected";
							}
							if (dist.getScope().compareTo("2") == 0) {
								scope = "private";
							}
							String roles = dist
									.getRolesString(MetaLibrary.TYPE_POLICY);
							if (roles != null) {
								roles = roles + "," + dist.getFreeTextRole();
							} else {
								roles = dist.getFreeTextRole();
							}
							roles = roles.trim();
							if (!roles.equalsIgnoreCase("")) {
								roles = " { " + roles + " } ";
							}
							temp = "<UML:Attribute xmi.id=\"S."
									+ dist.getId()
									+ "\" name=\""
									+ dist.getName()
									+ roles
									+ "\" visibility=\""
									+ scope
									+ "\" ownerScope=\"instance\" changeability=\"changeable\" targetScope=\"instance\" type=\"G."
									// + objId + "\">\n";
									+ objId + "\">\n";
							writer.write(temp.getBytes());
							temp = "<UML:StructuralFeature.multiplicity>\n";
							writer.write(temp.getBytes());
							temp = "<UML:Multiplicity>\n";
							writer.write(temp.getBytes());
							temp = "<UML:Multiplicity.range>\n";
							writer.write(temp.getBytes());
							temp = "<UML:MultiplicityRange lower=\"1\" upper=\"1\" /> \n";
							writer.write(temp.getBytes());
							temp = "</UML:Multiplicity.range>\n";
							writer.write(temp.getBytes());
							temp = " </UML:Multiplicity>\n";
							writer.write(temp.getBytes());
							temp = "</UML:StructuralFeature.multiplicity>\n";
							writer.write(temp.getBytes());
							temp = "<UML:Attribute.initialValue>\n";
							writer.write(temp.getBytes());
							temp = " <UML:Expression language=\"\" body=\""
									+ dist.getInitialValue() + "\" /> \n";
							writer.write(temp.getBytes());
							temp = "</UML:Attribute.initialValue>\n";
							writer.write(temp.getBytes());
							temp = "</UML:Attribute>\n";
							writer.write(temp.getBytes());
						}// end of if
					}// end of if
				}
			}// end of while

			en = obj.getRelationBySource(); // list of all that
			// connected to the
			// given object,object
			// is the source
			while (en.hasMoreElements()) {
				IRelationEntry rel = (IRelationEntry) en.nextElement();
				if (rel.getRelationType() == OpcatConstants.EXHIBITION_RELATION) { // if
					// attribute
					if ((localStructure.getIEntry(rel.getDestinationId())) instanceof IProcessEntry) {
						// if process connected to object as
						// attribute==> metoda of a class
						IProcessEntry proc = (IProcessEntry) localStructure
								.getIEntry(rel.getDestinationId());
						if (proc.getScope().compareTo("0") == 0) {
							scope = "public";
						}
						if (proc.getScope().compareTo("1") == 0) {
							scope = "protected";
						}
						if (proc.getScope().compareTo("2") == 0) {
							scope = "private";
						}
						temp = "<!--  =============  [Operation] ====================  --> \n";
						writer.write(temp.getBytes());
						temp = "<UML:Operation xmi.id=\"S."
								+ rel.getId()
								+ "\" name=\""
								+ proc.getName()
								+ "\" visibility=\""
								+ scope
								+ "\" ownerScope=\"instance\" isQuery=\"false\" concurrency=\"sequential\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\" specification=\"\">\n";
						writer.write(temp.getBytes());
						temp = "<UML:BehavioralFeature.parameter>\n";
						writer.write(temp.getBytes());

						// input parameter of a methoda
						Enumeration link_list;
						ILinkEntry link;
						link_list = proc.getLinksByDestination();
						while (link_list.hasMoreElements()) {
							link = (ILinkEntry) link_list.nextElement();
							if (link.getLinkType() == OpcatConstants.INSTRUMENT_LINK) {
								long source_id = link.getSourceId();
								IObjectEntry param = (IObjectEntry) localStructure
										.getIEntry(source_id);
								if ((!obj.isPhysical())
										&& (!obj.isEnvironmental())) {
									objId = 0;
									if (((param.getStates()).hasMoreElements())
											|| (param.getType().compareTo("") != 0)) {
										objId = DataTypesUpdate(param,
												dataTypes);
									}
									temp = "<UML:Parameter xmi.id=\"XX."
											+ param.getId()
											+ "\" name=\""
											+ param.getName()
											+ "\" visibility=\"public\" kind=\"inout\" type=\"G."
											+ objId + "\">\n";
									writer.write(temp.getBytes());
									temp = "<UML:Parameter.defaultValue>\n";
									writer.write(temp.getBytes());
									temp = "<UML:Expression language=\"\" body=\"\" /> \n";
									writer.write(temp.getBytes());
									temp = "</UML:Parameter.defaultValue>\n";
									writer.write(temp.getBytes());
									temp = "</UML:Parameter>\n";
									writer.write(temp.getBytes());
								}// end of if
							}// end of if
						}// end of while
						// output type of a methoda=always void
						temp = "<UML:Parameter xmi.id=\"XX."
								+ proc.getId()
								+ "\" name=\""
								+ proc.getName()
								+ ".Return\" visibility=\"public\" kind=\"return\" type=\"G.00\" /> \n";
						writer.write(temp.getBytes());
						temp = "</UML:BehavioralFeature.parameter>\n";
						writer.write(temp.getBytes());
						temp = "</UML:Operation>\n";
						writer.write(temp.getBytes());
					}// end of if
				}// end of if
			}// end of while
			temp = "</UML:Classifier.feature>\n";
			writer.write(temp.getBytes());

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}

	/**
	 * The method
	 * 
	 * @param obj
	 * @param specVec
	 *            A list of specialization classes
	 * @param umlType
	 * @param done
	 * @param tagsVec
	 * @param classDiagramElements
	 * @param dataTypes
	 * @param generalizationVec
	 * @param dataType
	 */
	private void generateClass(IObjectEntry obj, Vector<String> specVec,
			String umlType, ArrayList<IObjectEntry> done,
			Vector<String> tagsVec, Vector<String> classDiagramElements,
			HashMap dataTypes, Vector generalizationVec, Vector dataType) {
		try {
			if ((!obj.isPhysical()) && (!obj.isEnvironmental())
					&& (!this.isFeature(localStructure, obj))) {// a
				// class
				// (by
				// definition)
				String scope = "";
				if (obj.getScope().compareTo("0") == 0) {
					scope = "public";
				}
				if (obj.getScope().compareTo("1") == 0) {
					scope = "protected";
				}
				if (obj.getScope().compareTo("2") == 0) {
					scope = "private";
				}

				// class printout
				String temp = "<!--  ================   [Class] ====================   -->\n";
				writer.write(temp.getBytes());
				// **********************************************************
				String parentName = this.getParentThing(obj);
				if (parentName == "-1") {
					parentName = "";
				} else {
					parentName = "_" + parentName + "";
				}

				temp = "<UML:"
						+ umlType
						+ " xmi.id=\"S."
						+ obj.getId()
						+ "\" name=\""
						+ obj.getName()
						+ ""
						+ parentName
						+ " \" visibility=\""
						+ scope
						+ "\" isRoot=\"true\" isLeaf=\"true\" isAbstract=\"false\" isActive=\"false\" namespace=\"G.0000\" ";
				writer.write(temp.getBytes());
				classDiagramElements.addElement("" + obj.getId() + "");
				tagsVec.addElement("" + obj.getId() + "");

				IObjectInstance instance = (IObjectInstance) obj.getInstances()
						.nextElement();
				Long id = new Long(obj.getId());
				objectInstances.put(id, instance);

				generateInnerClass(obj, specVec, dataTypes, generalizationVec,
						dataType);
				done.add(obj);
				// temp = "</UML:Classifier.feature>\n";
				// writer.write(temp.getBytes());
				if (umlType.equalsIgnoreCase("package")) {
					ArrayList agg = getAggragationObjects(obj);
					Iterator iter = agg.iterator();
					while (iter.hasNext()) {
						IObjectEntry inner = (IObjectEntry) iter.next();
						// if (!done.contains(inner)) {
						if ((!inner.isPhysical()) && (!inner.isEnvironmental())
								&& (!this.isFeature(localStructure, inner))) {// a
							// class
							// (by
							// definition)
							String innerType = "Class";
							if ((inner.getRolesString(MetaLibrary.TYPE_POLICY) != null)) {
								if (inner.getRolesString(
										MetaLibrary.TYPE_POLICY).toLowerCase()
										.contains("package")) {
									innerType = "Package";
								}

								if (inner.getRolesString(
										MetaLibrary.TYPE_POLICY).toLowerCase()
										.contains("interface")) {
									innerType = "Interface";
								}
							}
							scope = "";
							if (inner.getScope().compareTo("0") == 0) {
								scope = "public";
							}
							if (inner.getScope().compareTo("1") == 0) {
								scope = "protected";
							}
							if (inner.getScope().compareTo("2") == 0) {
								scope = "private";
							}
							// class printout
							temp = "<!--  ================   [Class] ====================   -->\n";
							writer.write(temp.getBytes());
							// **********************************************************
							parentName = this.getParentThing(inner);
							if (parentName == "-1") {
								parentName = "";
							} else {
								parentName = "_" + parentName + "";
							}
							// **********************************************************

							temp = "<UML:"
									+ innerType
									+ " xmi.id=\"S."
									+ inner.getId()
									+ "\" name=\""
									+ inner.getName().trim()
									+ ""
									+ parentName
									+ " \" visibility=\""
									+ scope
									+ "\" isRoot=\"true\" isLeaf=\"true\" isAbstract=\"false\" isActive=\"false\" namespace=\"G.0000\" ";
							writer.write(temp.getBytes());
							classDiagramElements.addElement("" + inner.getId()
									+ "");
							tagsVec.addElement("" + inner.getId() + "");

							generateInnerClass(inner, specVec, dataTypes,
									generalizationVec, dataType);
							done.add(inner);
							temp = "</UML:Classifier.feature>\n";
							writer.write(temp.getBytes());
							temp = "</UML:" + innerType + ">\n";
							writer.write(temp.getBytes());
							done.add(inner);
						}
						// }
					}
				}
				temp = "</UML:" + umlType + ">\n";
				writer.write(temp.getBytes());
			}
		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}
	}

	/**
	 * This function returns true if the given IObjectEntry obj is a feature of
	 * another IObjectEntry, (that is connected as a destination by
	 * EXHIBITION_RELATION to another IObjectEntry) else return false.
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param IObjectEntry
	 *            obj
	 * @return true-in case the given object is an feature, otherwise false.
	 */
	private boolean isFeature(ISystemStructure MyStructure, IObjectEntry obj) {
		Enumeration e;
		IEntry abstractEntry;
		IRelationEntry rel;

		e = MyStructure.getElements();
		while (e.hasMoreElements()) {
			abstractEntry = (IEntry) e.nextElement();
			if (abstractEntry instanceof IRelationEntry) {
				rel = (IRelationEntry) abstractEntry;
				if ((rel.getRelationType() == OpcatConstants.EXHIBITION_RELATION)
						&& (rel.getDestinationId() == obj.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This functions calculates which class represents the current IObjectEntry
	 * 
	 * @param ISystemStructure
	 *            MyStructure
	 * @param IEntry
	 *            ent-it can be an IStateEntry or an IObjectEntry only.
	 * @return IObjectEntry - an object which represents the class
	 */
	private IObjectEntry ReturnClass(ISystemStructure MyStructure, IEntry ent) {
		IEntry absEntry = ent;
		IObjectEntry obj, obj2;
		IStateEntry state;
		Enumeration list;
		IRelationEntry rel;

		if (absEntry instanceof IStateEntry) {// if ent is a stateEntry
			state = (IStateEntry) absEntry;
			absEntry = (IEntry) state.getParentIObjectEntry(); // get the
			// parent-the
			// object which
			// contains the
			// fiven state
		}
		obj2 = (IObjectEntry) absEntry; // absEntry is now an object for sure
		// (and not a state)
		if (absEntry instanceof IObjectEntry) {
			obj = (IObjectEntry) absEntry;
			list = obj.getRelationByDestination();// all relations that the
			// object is a destination
			// in them
			while (list.hasMoreElements()) {
				rel = (IRelationEntry) list.nextElement();
				if (rel.getRelationType() == OpcatConstants.EXHIBITION_RELATION) {
					obj2 = (IObjectEntry) MyStructure.getIEntry((rel
							.getSourceId()));// source of the relation
				}
			}
		}
		return obj2;
	}

	/**
	 * This function finds the "parent" of a given IObjectEntry
	 * 
	 * @param obj
	 *            -IObjectEntry to which need to be find a parent
	 * @return String name of the parent thing o "-1" if there is no parent
	 */
	private String getParentThing(IObjectEntry obj) {
		Enumeration locenum;
		IObjectInstance inst;
		IThingInstance parentThing;
		String result = "-1";

		locenum = obj.getInstances();
		while (locenum.hasMoreElements()) {
			inst = (IObjectInstance) locenum.nextElement();
			parentThing = inst.getParentIThingInstance();
			if (parentThing != null) {
				result = parentThing.getIEntry().getName();
			}
		}
		return result;
	}

	/**
	 * The method generates an association for a class diagram, including
	 * cardinality
	 * 
	 * @author Eran Toch
	 */
	private void generateAssociation(long associationId,
			String associationName, long sourceId, long destId,
			String sourceCardinality, String destCardinality,
			boolean sourceNavigable, boolean destinationNavigable) {
		String output = "";
		output = "<!-- =============== [Class Association] ===============  --> \n";
		String lineType = "Association";
		output += "<UML:"
				+ lineType
				+ " xmi.id=\"G."
				+ associationId
				+ "\" name=\""
				+ associationName.trim()
				+ "\" visibility=\"public\" isRoot=\"false\" isLeaf=\"false\" isAbstract=\"false\"> \n";
		output += "<UML:" + lineType + ".connection> \n";
		output += "<!--  ================[AssociationEnd] ===============  -->  \n";
		output += "<UML:"
				+ lineType
				+ "End xmi.id=\"G."
				+ associationId
				+ ".1\" name=\"\" visibility=\"public\" isNavigable=\"true\" aggregation=\"none\" targetScope=\"instance\" changeability=\"changeable\" type=\"S."
				+ destId + "\"> \n";
		output += "<UML:" + lineType + "End.multiplicity> \n";
		output += "<UML:Multiplicity> \n";
		output += "<UML:Multiplicity.range> \n";
		int low = 0;
		if (destCardinality.compareTo("1") == 0
				|| destCardinality.compareTo("m") == 0) {
			low = 1;
			output += "<UML:MultiplicityRange lower=\"" + low + "\" upper=\""
					+ destCardinality + "\" />";
		} else {
			output += "<UML:MultiplicityRange lower=\"" + low
					+ "\" upper=\"m\" />";
		}
		output += "</UML:Multiplicity.range> \n";
		output += "</UML:Multiplicity> \n";
		output += "</UML:" + lineType + "End.multiplicity> \n";
		output += "</UML:" + lineType + "End> \n";
		output += "<!--  ================[AssociationEnd] ===============  -->  \n";
		output += "<UML:"
				+ lineType
				+ "End xmi.id=\"G."
				+ associationId
				+ ".2\" name=\"\" visibility=\"public\" isNavigable=\"true\" aggregation=\"aggregate\" targetScope=\"instance\" changeability=\"changeable\" type=\"S."
				+ sourceId + "\"> \n";
		output += "<UML:" + lineType + "End.multiplicity> \n";
		output += "<UML:Multiplicity> \n";
		output += "<UML:Multiplicity.range> \n";
		low = 0;
		if ((sourceCardinality.compareTo("1") == 0)
				|| (sourceCardinality.compareTo("m") == 0)) {
			low = 1;
			output += "<UML:MultiplicityRange lower=\"" + low + "\" upper=\""
					+ sourceCardinality + "\" />";
		} else {
			output += "<UML:MultiplicityRange lower=\"" + low
					+ "\" upper=\"m\" />";
		}
		// temp += "<UML:MultiplicityRange
		// lower=\"1\" upper=\"1\" />"; //
		// "+agg_rel.getSourceCardinality()+"
		output += "</UML:Multiplicity.range> \n";
		output += "</UML:Multiplicity> \n";
		output += "</UML:" + lineType + "End.multiplicity> \n";
		output += "</UML:" + lineType + "End> \n";
		output += "</UML:" + lineType + ".connection> \n";
		output += "</UML:" + lineType + "> \n";
		writer.write(output.getBytes());
	}

	/**
	 * @return The diagram ID.
	 */
	public String getDiagramID() {
		return diagramID;
	}
}// end of class
