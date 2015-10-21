package gui.undo;

import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.undo.AbstractUndoableEdit;

public class CompoundUndoAction extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private LinkedList actions;

	public CompoundUndoAction() {
		this.actions = new LinkedList();
	}

	public String getPresentationName() {
		return "CompoundUndoAction";
	}

	public void addAction(AbstractUndoableEdit nextAction) {
		if (nextAction == null)
			return;
		this.actions.add(nextAction);
	}

	public void undo() {
		super.undo();

		ListIterator it = this.actions.listIterator(this.actions.size());

		while (it.hasPrevious()) {
			AbstractUndoableEdit prevAction = (AbstractUndoableEdit) it
					.previous();
			prevAction.undo();
		}

	}

	public void redo() {
		super.redo();

		ListIterator it = this.actions.listIterator();
		while (it.hasNext()) {
			AbstractUndoableEdit nextAction = (AbstractUndoableEdit) it.next();
			nextAction.redo();
		}
	}
}
