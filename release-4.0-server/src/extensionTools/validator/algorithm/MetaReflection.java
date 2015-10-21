package extensionTools.validator.algorithm;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPI.IEntry;
import exportedAPI.opcatAPI.ILinkEntry;
import exportedAPI.opcatAPI.IRelationEntry;
import exportedAPI.opcatAPI.ISystem;
import exportedAPI.opcatAPI.IConnectionEdgeEntry;
import extensionTools.validator.ValidationException;
import extensionTools.validator.finder.Finder;
import extensionTools.validator.finder.GeneralizedFinder;
import extensionTools.validator.finder.StateFinder;
import gui.opdProject.Opd;

/**
 * Represent the constraints reflection for a meta-library.
 * @author Eran Toch
 * Created: 06/05/2004
 */
public class MetaReflection extends Reflection	{
	
	private final long libraryID;
	
	public MetaReflection(ISystem sys, long id)	{
		super(sys);
		this.libraryID = id;
	}

	
	/**
	 * Adds a <code>VRole</code>/<code>ConstraintSet</code> pair to the
	 * <code>constraintMap</code>. For meta reflection, the attached role is 
	 * the inspected thing itself. 
	 */
	public void addPair(IConnectionEdgeEntry thing) throws ValidationException {
		VRole vrole = new VRole(this.libraryID, thing.getId(), Opd.getEntryType(thing));
		Finder vThing = new Finder();
		//Adding finders
		vThing.addFinder(new GeneralizedFinder());
		vThing.addFinder(new StateFinder());
		
		try	{
			this.constraintMap.put(vrole, this.findConstraints(vThing, thing));
		}
		catch (ValidationException ve)	{
			throw ve;
		}
	}
	
	/**
	 * Creates an applicant for the given thing.
	 */
	protected MultipleApplicant createApplicant(IConnectionEdgeEntry thing)	{
		VRole vrole = new VRole(this.libraryID, thing.getId(), Opd.getEntryType(thing));
		MultipleApplicant app = new MultipleApplicant();
		app.addApplicant(vrole);
		
		//Adding all the applicants which are specialization of the current VRole
		if (AlgorithmConfiguration.DO_IS_A) {
			app.addAllApplicants(this.getSpecializationRoles(vrole));
		}
		return app;
	}
	
	/**
	 * Returns a set of all the specialized things of a given role. The method 
	 * goes down the specialization (is-a) tree, returning all things which are
	 * a specialzation of the current thing, in a DFS mode.
	 * @return	A <code>HashSet</code> object containing all specialized things.
	 */
	protected Set getSpecializationRoles(VRole vrole)	{
		HashSet set = new HashSet();
		IConnectionEdgeEntry thing = (IConnectionEdgeEntry)this.model.getISystemStructure().getIEntry(vrole.getThingID());
		Enumeration connections = thing.getRelationBySource();
		while (connections.hasMoreElements())	{
			IRelationEntry rel = (IRelationEntry)connections.nextElement();
			if (rel.getRelationType() == OpcatConstants.SPECIALIZATION_RELATION) {
				IConnectionEdgeEntry specialization =(IConnectionEdgeEntry)this.model.getISystemStructure().getIEntry(
													rel.getDestinationId());
				VRole role = new VRole(this.libraryID, specialization.getId(), Opd.getEntryType(specialization));
				set.add(role);
				set.addAll(this.getSpecializationRoles(role));
			}					
		}
		return set;
	}
	/**
	 * Creates a Law object.
	 */
	protected Constraint createConstraintObject(
		int connectionType,
		int dir,
		MultipleApplicant applicant,
		IEntry connectionObject) throws ValidationException
	{
		int min = 1;
		int max = 1;
		try	{
			if (connectionObject instanceof IRelationEntry)	{
				String cardinality = "";
				if (dir == Finder.DESTINATION_DIRECTION)	{
					cardinality = ((IRelationEntry)connectionObject).getDestinationCardinality();
				}
				else {
					cardinality = ((IRelationEntry)connectionObject).getSourceCardinality();
				}
				if (cardinality.equals("1"))	{
					min = 1;
					max = 1;
				}
				else if (cardinality.equals("m"))	{
					min = 0;
					max = Law.MAX_COUNTS;
				}
				else if (cardinality.indexOf("..") > 0)	{
					StringTokenizer tokenizer = new StringTokenizer(cardinality, "..");
					if (!tokenizer.hasMoreElements())	{
						throw new ValidationException("strange cardinality: "+ cardinality);					
					}
					String firstNumber = tokenizer.nextToken();
					String secondNumber = tokenizer.nextToken();
					
					//here both could not be a number as you can write 1..m, n..m, 1..K
					try {
						Double.parseDouble(firstNumber) ;
						min = Integer.parseInt(firstNumber);
					}	catch (Exception e ) {
						//not a number 
						min = 0 ; 
					}
					try { 
						//if not a number will trhow an exeption
						Double.parseDouble(secondNumber) ; 
						max = Integer.parseInt(secondNumber);
					} catch (Exception e ){
						//not a number 
						max = Law.MAX_COUNTS ; 
					}
				}
			}
			else if (connectionObject instanceof ILinkEntry)	{
				if (AlgorithmConfiguration.LINKS_AS_M)	{
					min = 0;
					max = Law.MAX_COUNTS;
				}
				else	{
					min = 1;
					max = 1;
				}
			}
		}
		catch (Exception ex)	{
			ValidationException ve = new ValidationException(ex.getMessage());
			throw ve;
		}
		Law law = new Law(applicant, connectionType, dir, min, max);
		return law;
	}
}
