package gui.projectStructure;

import exportedAPI.OpcatConstants;
import gui.opdGraphics.opdBaseComponents.OpdConnectionEdge;
import gui.opdGraphics.opdBaseComponents.OpdState;
import gui.opdGraphics.opdBaseComponents.OpdThing;
import gui.opdProject.OpdProject;
import gui.opmEntities.Constants;
import gui.opmEntities.OpmProceduralLink;

import java.awt.Container;
import java.util.Enumeration;
import java.util.Vector;


public class LinkPrecedence
{
	/**
	 * Compares two links according to their precedence:
	 * 1. resut/consumption
	 * 2. effect
	 * 3. agent/instrument
	 * 4. event/instrument event/condition/exception/invocation
	 *
	 */

	public static boolean isEdgesZoomedIn(OpdConnectionEdge source,
											OpdConnectionEdge destination,
											OpdProject myProject)
	{
		boolean src = false;
		boolean dst = false;

		if(source instanceof OpdThing)
		{
			src = _isThingNeedsChecking((OpdThing)source);
		}
		if(destination instanceof OpdThing)
		{
			dst = _isThingNeedsChecking((OpdThing)destination);
		}

		return src || dst;
	}

	public static boolean hasDifferentContainers(OpdConnectionEdge source,
											OpdConnectionEdge destination,
											OpdProject myProject)
	{
		Container sParent = _getProperContainer(source);
		Container dParent = _getProperContainer(destination);
		return ! sParent.equals(dParent);
	}

	// this method returns 'proper' parent component.
	// for Thing it returns Container
	// for State it returns parent of containing OpdObject
	private static Container _getProperContainer(OpdConnectionEdge oce)
	{
		if(oce instanceof OpdThing)
		{
			return oce.getParent();
		}
		return oce.getParent().getParent();
	}


	// According to gui.opdProject.addLinkConsistent() comments
	public static int detectLinkActionCase(OpdConnectionEdge source,
											OpdConnectionEdge destination,
											OpdProject myProject)
	{
		OpdThing t;
		// source is a state or source is not In-Zoomed and does not has In-Zooming OPD
		// it could be cases 2, 4, 6.
		if( (source instanceof OpdState) ||
			(!(_isThingNeedsChecking((OpdThing)source)) &&
			!(source.getParent() instanceof OpdThing)) )
		{
			// lets check destination
			if(destination instanceof OpdState)
			{
				t = (OpdThing)destination.getParent();
			}
			else
			{
				t = (OpdThing)destination;
			}

			// cases 2,4,6
			if(t.isZoomedIn())
			{
				return 4;
			}
			else if(t.getParent() instanceof OpdThing)
			{
				return 6;
			}
			else
			{
				return 2;
			}
		}

		// destination is a state or destination is not In-Zoomed and does not has In-Zooming OPD
		// it could be cases 1, 3, 5.
		else if( (destination instanceof OpdState) ||
				 (!(_isThingNeedsChecking((OpdThing)destination)) &&
				 !(destination.getParent() instanceof OpdThing)))
		{
			// lets check source
			if(source instanceof OpdState)
			{
				t = (OpdThing)source.getParent();
			}
			else
			{
				t = (OpdThing)source;
			}

			// cases 6, 8
			if(t.isZoomedIn())
			{
				return 3;
			}
			else if(t.getParent() instanceof OpdThing)
			{
				return 5;
			}
			else
			{
				return 1;
			}
		}
		return 0;
	}


	private static boolean _isThingNeedsChecking(OpdThing t)
	{
		if(t.isZoomedIn() || (((ThingEntry)t.getEntry()).getZoomedInOpd() != null))
		{
			return true;
		}
		return false;
	}

	public static Vector combineLinks(Enumeration opmLinks)
	{
		Vector retLinks = new Vector();
		int currLink = -1;

		// currState
		// 0 - first time
		// 1 - one of consumption/result
		// 2 - agent/instrument
		// 3 - one or more of event/condition/exception/invocation but not instrument event
		// 4 - instrument event and 1
		// 5 - instrument event and 2
		// 6 - instrument event and 3
		// 7 - instrument event only

		// we do not push instrument_event link into the return vector
		// we remember its appearence using currState
		int currState = 0;
		while(opmLinks.hasMoreElements())
		{
			currLink = Constants.getType4Link((OpmProceduralLink)opmLinks.nextElement());
			switch(currLink)
			{
				case OpcatConstants.EFFECT_LINK:
					 retLinks.clear();
					 retLinks.addElement(new Integer(OpcatConstants.EFFECT_LINK));
					 return retLinks;

				case OpcatConstants.RESULT_LINK:
				case OpcatConstants.CONSUMPTION_LINK:
					 switch(currState)
					 {
						case 0:
							 currState = 1;
							 retLinks.addElement(new Integer(currLink));
							 break;
						case 1:
						case 4:
						case 5:
						case 6:
						case 7:
							 retLinks.clear();
							 retLinks.addElement(new Integer(OpcatConstants.EFFECT_LINK));
							 return retLinks;
						default:
							 retLinks.clear();
							 retLinks.addElement(new Integer(currLink));
							 currState = 1;
							 break;
					 }
					 break;

				case OpcatConstants.AGENT_LINK:
				case OpcatConstants.INSTRUMENT_LINK:
					 switch(currState)
					 {
						case 0:
							 currState = 2;
							 retLinks.addElement(new Integer(currLink));
							 break;
						case 1:
						case 4:
							 break;
						case 2:
						case 5:
							 retLinks.addElement(new Integer(currLink));
							 break;
						case 3:
							 currState = 2;
							 retLinks.clear();
							 retLinks.addElement(new Integer(currLink));
							 break;
						case 6:
						case 7:
							 currState = 5;
							 retLinks.clear();
							 retLinks.addElement(new Integer(currLink));
							 break;
					 }
					 break;

				case OpcatConstants.INSTRUMENT_EVENT_LINK:
				case OpcatConstants.CONDITION_LINK:
				case OpcatConstants.EXCEPTION_LINK:
				case OpcatConstants.INVOCATION_LINK:
					 switch(currState)
					 {
						case 0:
							 currState = 3;
							 retLinks.addElement(new Integer(currLink));
							 break;
						case 1:
						case 2:
						case 4:
						case 5:
							 break;
						case 3:
						case 6:
							 retLinks.addElement(new Integer(currLink));
							 break;
						case 7:
							 retLinks.addElement(new Integer(currLink));
							 currState = 6;
							 break;
					 }
					 break;

				case OpcatConstants.CONSUMPTION_EVENT_LINK:
					 switch(currState)
					 {
						case 0:
							 currState = 7;
							 break;
						case 1:
						case 2:
						case 3:
							 currState += 3;
							 break;
						case 4:
						case 5:
						case 6:
						case 7:
							 break;
					 }
			}
		}
		if(currState == 6)
		{
			retLinks.addElement(new Integer(OpcatConstants.CONSUMPTION_EVENT_LINK));
		}

		return retLinks;
	}
}

//class LinkComparator implements Comparator
//{
//	public int compare(Object o1, Object o2)
//	{
//		return LinkPrecedence._compareTwoLinks((OpmProceduralLink)o1, (OpmProceduralLink)o2);
//	}
//}
