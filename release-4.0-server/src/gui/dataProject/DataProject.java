package gui.dataProject;

import gui.metaLibraries.logic.DataHolder;
import gui.metaLibraries.logic.MetaLibrary;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class DataProject implements MetaLoader {

	private DataCreatorType sourceType = new DataCreatorType(
			DataCreatorType.DATA_TYPE_UNDEFINED, 0);

	InputStream fileStream;

	HashMap hm = null;

	MetaStatus status = new MetaStatus();

	MetaProjectLoader loader;

	DataHolder holder;

	public DataHolder getDataHolder() {
		return holder;
	}

	public DataProject(Object source, DataCreatorType type) {
		sourceType = type;
		// globalID = generateGlobalID() ;
		loader = new MetaProjectLoader(sourceType, source);
	}

	public String getName() {
		return loader.getName();
	}

	public String getID() {
		return loader.getID();
	}

	public Collection values() {
		return this.hm.values();
	}

	public String getPath() {
		return loader.getPath();
	}

	protected String generateGlobalID() {
		Calendar rightNow = Calendar.getInstance();
		long nowMillis = rightNow.getTimeInMillis();
		Random generator = new Random(nowMillis);
		long randomLong = generator.nextLong();
		if (randomLong < 0) {
			randomLong *= -1;
		}
		String globalID = String.valueOf(randomLong) + "."
				+ String.valueOf(nowMillis);
		return globalID;
	}

	public MetaDataItem getMetaDataItem(long reqID) {
		return (MetaDataItem) this.hm.get(new Long(reqID));
	}

	public Vector getHeaders() {
		return loader.getHeaders();
	}

	public MetaStatus getStatus() {
		return loader.getStatus();
	}

	public void setStatus(MetaStatus status) {
		this.status = status;
	}

	/**
	 * return the maximum Level value of items in this library
	 * 
	 * @return
	 */
	public int getMaxColoringLevelValue() {

		int max = -1;
		Iterator iter = hm.values().iterator();

		while (iter.hasNext()) {
			MetaDataItem item = (MetaDataItem) iter.next();
			int level = item.getColoringLevel();
			if (max < level)
				max = level;
		}

		return max;
	}

	public boolean isColoring() {
		return loader.hasColoringData();
	}

	public int getColoringIndex() {
		return loader.getColoringIndex();
	}

	public String getType() {
		return loader.getName();
	}

	public DataCreatorType getSourceType() {
		return sourceType;
	}

	public int getColoringData(Vector row) {
		return loader.getColoringData(row);
	}

	public String getExtID(Vector row) {
		return loader.getExtID(row);
	}

	public String getName(Vector row) {
		return loader.getName();
	}

	public Vector getRowAt(int i) {
		return loader.getRowAt(i);
	}

	public Iterator getRowsIterator() {
		return loader.getRowsIterator();
	}

	public int getSize() {
		return loader.getSize();
	}

	public boolean hasColoringData() {
		return true;
	}

	public boolean hasIDData() {
		return loader.hasIDData();
	}

	public boolean hasNameData() {
		return loader.hasNameData();
	}

	public boolean isShowColorValueAsPrograssBar() {
		return loader.isShowColorValueAsPrograssBar();
	}

	public void load() {
		loader.load();

		this.hm = new HashMap();
		if (!loader.getStatus().isLoadFail() && loader.hasIDData()) {
			Iterator rows = loader.getRowsIterator();
			while (rows.hasNext()) {
				Vector row = (Vector) rows.next();
				MetaDataItem req = new MetaDataItem(loader.getExtID(row),
						loader.getID());
				req.setAllData(row);
				req.setName(loader.getName(row));
				req.setColoringLevel(loader.getColoringData(row));
				this.hm.put(new Long(req.getId()), req);
			}
		}

		/**
		 * THE ONLY PLACE we depend on the data type in a data project. this
		 * should go away
		 */
		if (loader.getLoader() instanceof LibraryFileLoader) {
			holder = new DataHolder(((LibraryFileLoader) loader.getLoader())
					.getProject(), MetaLibrary.TYPE_POLICY);
		} else {
			holder = new DataHolder(this, MetaLibrary.TYPE_CLASSIFICATION);
		}
	}

}
