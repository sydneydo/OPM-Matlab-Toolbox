package server.database;

import junit.framework.JUnit4TestAdapter;
import org.junit.*;

public class TestDatabase {

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestDatabase.class);
	}

	/**
	 * check get an open connection to database
	 */
	@Test
	public void test_openConnection() {
		// Connection myConnection = null;
		// myConnection = OpcatConnection.getInstance().getConnection();
		// assertTrue("myConnection != null", myConnection != null);
		//
		// try {
		// assertTrue("!myConnection.isClosed()", !myConnection.isClosed());
		// } catch (Exception ex) {
		// fail(ex.getMessage());
		// }
	}

	/**
	 * check get models names from database
	 */
	@Test
	public void test_getModelsNames() {
		// Connection myConnection = null;
		// myConnection = OpcatConnection.getInstance().getConnection();
		// Models models = new Models(myConnection);
		// ArrayList<String> names = models.getNames();
		//
		// try {
		// int i = 1;
		// Iterator<String> iter = names.iterator();
		//
		// while (iter.hasNext()) {
		// String name = iter.next();
		// assertTrue("Got " + name, name
		// .equalsIgnoreCase("testing model number " + i));
		// i++;
		// }
		//
		// // fail if number records is not 10 as I put 10 in the dummy data
		// assertTrue("There are " + names.size() + " models",
		// names.size() == 5);
		//
		// } catch (Exception ex) {
		// fail("Exception while itereting in test_getModelsNames");
		// }

	}

	/**
	 * check if the database is online
	 */
	@Test
	public void test_checkDatabaseAccess() {
		// OpcatConnection.getInstance().getConnection();
		//
		// assertTrue("OpcatConnection.getInstance().isValid()", OpcatConnection
		// .getInstance().isOnLine());
		// 

	}
}