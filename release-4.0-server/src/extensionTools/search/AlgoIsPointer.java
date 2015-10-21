package extensionTools.search;

import java.util.Enumeration;
import java.util.Vector;

import exportedAPI.opcatAPIx.IXEntry;
import exportedAPI.opcatAPIx.IXSystem;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;

public class AlgoIsPointer implements AlgoInterface {

	OptionsIsPointer options;

	public AlgoIsPointer(OptionsIsPointer options) {
		this.options = options;
	}

	@Override
	public Vector PreformSearch(Entry parent, IXSystem opcat) {
		Vector out = new Vector();
		Enumeration entEnum = opcat.getIXSystemStructure().getElements();
		while (entEnum.hasMoreElements()) {
			IXEntry ent = (IXEntry) entEnum.nextElement();
			Enumeration<Instance> instances = ent.getInstances();

			while (instances.hasMoreElements()) {
				Instance instance = instances.nextElement();
				if (instance.isPointerInstance()) {
					if (instance.getPointer().equals(
							options.getExposedEntity().getKey())) {
						out.add(ent);
						break;
					}
				}
			}

		}
		return out;
	}
}
