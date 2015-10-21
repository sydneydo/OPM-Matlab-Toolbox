package gui.util.opcatGrid;

import gui.util.OpcatLogger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.swing.table.TableModel;

import com.sciapp.table.io.ExportManager;

public class CSVExport implements ExportManager {

    private String delimiter = ",";

    private boolean writeHeader = true;

    public CSVExport(String delimiter) {
	if (delimiter != null) {
	    this.delimiter = delimiter;
	}
    }

    public void setWriteHeader(boolean writeHeader) {
	this.writeHeader = writeHeader;
    }

    public void write(TableModel model, OutputStream outstream) {

    }

    public void write(TableModel model, Writer writer) {
	if (writer instanceof FileWriter) {
	    try {
		FileWriter out = (FileWriter) writer;

		if (writeHeader) {
		    for (int i = 0; i < model.getColumnCount(); i++) {
			out.write(model.getColumnName(i).replaceAll("\n", " "));
			if (i != model.getColumnCount() - 1)
			    out.write(delimiter);
		    }
		    out.write("\n");
		}

		for (int i = 0; i < model.getRowCount(); i++) {
		    for (int j = 0; j < model.getColumnCount(); j++) {
			out.write("\""
				+ model.getValueAt(i, j).toString().replaceAll(
					"\n", " ") + "\"");
			if (j != model.getColumnCount() - 1)
			    out.write(delimiter);
		    }
		    out.write("\n");
		}
		out.close();
	    } catch (IOException ex) {
		OpcatLogger.logError(ex);
	    }

	}

    }

}
