package gui.dataProject;

import java.util.Iterator;
import java.util.Vector;

public interface MetaLoader {

	public Iterator getRowsIterator();

	public Vector getRowAt(int i);

	public Vector getHeaders();

	public int getSize();

	public String getID();

	public String getExtID(Vector row);

	public int getColoringData(Vector row);

	public String getName(Vector row);

	public String getName();

	public int getColoringIndex();

	boolean hasColoringData();

	boolean hasNameData();

	boolean hasIDData();

	public MetaStatus getStatus();

	public String getPath();

	public void load();

	public boolean isShowColorValueAsPrograssBar();

}
