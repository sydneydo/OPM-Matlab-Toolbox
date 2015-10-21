package extensionTools.search;

import expose.OpcatExposeEntity;

public class IsPointerOptions extends OptionsBase {

	OpcatExposeEntity exposed;

	protected OpcatExposeEntity getExposed() {
		return exposed;
	}

	public IsPointerOptions(OpcatExposeEntity entity) {
		this.exposed = entity;
	}
}
