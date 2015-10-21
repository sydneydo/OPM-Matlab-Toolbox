package extensionTools.validator;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import exportedAPI.OpcatExtensionTool;
import exportedAPI.opcatAPI.ISystem;
import extensionTools.validator.algorithm.VAlgorithm;
import extensionTools.validator.gui.ValidatorPanel;
import gui.Opcat2;

public class Validator implements OpcatExtensionTool {

	// The name of the validation tag
	public static final String VALIDATION_TAG_NAME = "Templates Validation";

	private VAlgorithm algorithm = null;

	public Validator() {
	}

	public String getName() {
		return "Ontology Validator";
	}

	public JPanel getAboutBox() {
		return null;
	}

	public String getHelpURL() {
		return null;
	}

	public JPanel execute(ISystem opcatSystem, Opcat2 myOpcat2, JFrame parent) {
		if (this.algorithm == null) {
			this.algorithm = new VAlgorithm();
		}
		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add("Library Name");
		columnNames.add("Type");
		columnNames.add("Thing");
		columnNames.add("Place");
		columnNames.add("Warning");
		return new ValidatorPanel(opcatSystem, myOpcat2, this.algorithm,
				columnNames, parent);
	}

	public JPanel execute(ISystem opcatSystem) {
		if (this.algorithm == null) {
			this.algorithm = new VAlgorithm();
		}
		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add("Library Name");
		columnNames.add("Type");
		columnNames.add("Thing");
		columnNames.add("Place");
		columnNames.add("Warning");
		return new ValidatorPanel(opcatSystem, null, this.algorithm,
				columnNames, null);
	}

	public JDialog validationResults(ISystem opcatSystem, Opcat2 myOpcat2,
			JFrame parent) {
		// return new ValidatorPanel(opcatSystem);
		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add("Library Name");
		columnNames.add("Type");
		columnNames.add("Thing");
		columnNames.add("Place");
		columnNames.add("Warning");
		JDialog dialog = new JDialog();
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(
				new ValidatorPanel(opcatSystem, myOpcat2, this.algorithm,
						columnNames, parent), BorderLayout.CENTER);
		return dialog;
	}

}
