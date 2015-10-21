package gui.actions.expose;

import expose.OpcatExposeConstants.OPCAT_EXPOSE_CHANGE_TYPE;
import expose.OpcatExposeConstants.OPCAT_EXPOSE_OP;
import gui.projectStructure.Entry;

public class OpcatExposeChange {

	private long id;
	private boolean privateAction = false;
	private OPCAT_EXPOSE_OP op;

	public OpcatExposeChange(long entryID, boolean privateAction,
			OPCAT_EXPOSE_OP op) {

		super();
		this.id = entryID;
		this.privateAction = privateAction;
		this.op = op;

	}

	public long getEntryID() {
		return id;
	}

	public boolean isPrivateAction() {
		return privateAction;
	}

	public OPCAT_EXPOSE_OP getOp() {
		return op;
	}

	public boolean equals(Object that) {
		if (that instanceof OpcatExposeChange) {
			OpcatExposeChange checking = (OpcatExposeChange) that;
			return ((this.getEntryID() == checking.getEntryID()) && (this
					.isPrivateAction() == checking.isPrivateAction()));
		} else {
			return false;
		}

	}
}
