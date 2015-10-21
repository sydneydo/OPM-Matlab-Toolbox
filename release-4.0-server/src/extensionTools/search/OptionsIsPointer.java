package extensionTools.search;

import expose.OpcatExposeEntity;

public class OptionsIsPointer extends OptionsBase {

	OpcatExposeEntity exposedEntity;

	protected OpcatExposeEntity getExposedEntity() {
		return exposedEntity;
	}

	public OptionsIsPointer(OpcatExposeEntity exposedEntity) {
		this.exposedEntity = exposedEntity;

	}

}
