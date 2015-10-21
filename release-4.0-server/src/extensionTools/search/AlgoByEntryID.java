package extensionTools.search;

import java.util.Enumeration;
import java.util.Vector;

import exportedAPI.opcatAPIx.IXEntry;
import exportedAPI.opcatAPIx.IXLinkEntry;
import exportedAPI.opcatAPIx.IXObjectEntry;
import exportedAPI.opcatAPIx.IXProcessEntry;
import exportedAPI.opcatAPIx.IXRelationEntry;
import exportedAPI.opcatAPIx.IXStateEntry;
import exportedAPI.opcatAPIx.IXSystem;
import gui.projectStructure.Entry;
import gui.projectStructure.ObjectEntry;

public class AlgoByEntryID implements AlgoInterface {
	private OptionsByEntryID options;

	public AlgoByEntryID(OptionsByEntryID searchOptions) {
		this.options = searchOptions;
	}

	public Vector PreformSearch(Entry parent, IXSystem opcat) {
		Vector out = new Vector();
		boolean found = false;
		Enumeration entEnum = opcat.getIXSystemStructure().getElements();
		while (entEnum.hasMoreElements()) {
			IXEntry ent = (IXEntry) entEnum.nextElement();
			found = false;

			if (ent.getId() == options.getEntryID()
					&& this.options.inName) {
				if ((ent instanceof IXProcessEntry) && this.options.forProcess) {
					found = true;
				}

				if ((ent instanceof IXStateEntry) && this.options.forStates) {
					IXStateEntry state = (IXStateEntry) ent;

					if (parent != null) {
						if (parent instanceof ObjectEntry) {
							if (state.getParentIXObjectEntry().getId() == parent
									.getId()) {
								found = true;
							}
						} else {
							found = true;
						}
					} else {
						found = true;
					}
				}

				if ((ent instanceof IXObjectEntry) && this.options.forObjects) {
					found = true;
				}

				if (((ent instanceof IXLinkEntry) || (ent instanceof IXRelationEntry))
						&& this.options.forLinks) {
					found = true;
				}

			}

			if (found) {
				out.add(ent);
			}
		}
		return out;
	}

}
