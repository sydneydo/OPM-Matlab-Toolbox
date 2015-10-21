package messages;

import gui.actions.messages.OpcatMessagesConsoleRefreshAction;
import gui.images.svn.SvnImages;
import gui.util.OpcatLogger;
import gui.util.opcatGrid.GridPanel;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;

import messages.OpcatMessagesConstants.OPCAT_MESSAGES_OP;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SEVIRITY;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SUBSYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_SYSTEMS;
import messages.OpcatMessagesConstants.OPCAT_MESSAGES_TYPE;

import com.sciapp.renderers.DateRenderer;
import com.sciapp.renderers.NumberRenderer;
import com.sciapp.table.SortTableModel;
import com.sciapp.table.SortTableRenderer;

import database.Lookup;
import database.OpcatDatabaseConnection;
import database.OpcatDatabaseConstants;
import database.OpcatDatabaseLookupDAO;

public class OpcatMessagesManager {

	private static Connection c;

	private OpcatMessagesFilter lastFilter = null;

	private static OpcatMessagesManager myMessages = null;

	private OpcatMessagesManager() {

		try {

			c = OpcatDatabaseConnection.getInstance().getConnection();

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

	}

	public OpcatMessagesFilter getLastFilter() {
		return lastFilter;
	}

	public static OpcatMessagesManager getInstance() {
		if (myMessages == null) {
			myMessages = new OpcatMessagesManager();
		}
		return myMessages;
	}

	/**
	 * Send the {@link OpcatMessage msg} immediately to the server.
	 * 
	 * @param msg
	 *            {@link OpcatMessage} to be sent
	 * @throws SQLException
	 */
	public void sendMessage(OpcatMessage msg) throws SQLException {

		Lookup typelookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatMessagesConstants.type_table), OPCAT_MESSAGES_TYPE.class);

		Lookup systemlookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatMessagesConstants.systems_table),
				OPCAT_MESSAGES_SYSTEMS.class);

		Lookup subsystemlookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatMessagesConstants.subsystems_table),
				OPCAT_MESSAGES_SUBSYSTEMS.class);

		Lookup severitylookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatMessagesConstants.sevirity_table),
				OPCAT_MESSAGES_SEVIRITY.class);

		Lookup oplookup = new Lookup(new OpcatDatabaseLookupDAO(
				OpcatMessagesConstants.op_table), OPCAT_MESSAGES_OP.class);

		String fieldsNames = OpcatMessagesConstants.FIELD_NAME_SYSTEM + ","
				+ OpcatMessagesConstants.FIELD_NAME_DESTINATION_ID
				+ ","
				+ OpcatMessagesConstants.FIELD_NAME_DESTINATION_SUBSYSTEM
				+ ","
				+ OpcatMessagesConstants.FIELD_NAME_DESTINATION_TYPE
				+ ","
				+ OpcatMessagesConstants.FIELD_NAME_MESSAGE
				+ ","
				// + OpcatMessagesConstants.FIELD_NAME_MESSAGE_DATE + ","
				+ OpcatMessagesConstants.FIELD_NAME_SENDER_ID + ","
				+ OpcatMessagesConstants.FIELD_NAME_SENDER_SUBSYSTEM + ","
				+ OpcatMessagesConstants.FIELD_NAME_SENDER_TYPE + ","
				+ OpcatMessagesConstants.FIELD_NAME_SEVIRITY + ","
				+ OpcatMessagesConstants.FIELD_NAME_MESSAGE_SUBJECT + ","
				+ OpcatMessagesConstants.FIELD_NAME_MESSAGE_OP;

		String values = systemlookup.getId(msg.getSystem()) + ",'"
				+ msg.getDestinationID() + "',"
				+ subsystemlookup.getId(msg.getDestinationSubsystem()) + ","
				+ typelookup.getId(msg.getDestinationType()) + ",'"
				+ msg.getMsg() + "','" + msg.getSenderID() + "',"
				+ subsystemlookup.getId(msg.getSenderSubsystem()) + ","
				+ typelookup.getId(msg.getSenderType()) + ","
				+ severitylookup.getId(msg.getSevirity()) + ",'"
				+ msg.getSubject() + "'," + oplookup.getId(msg.getOp());

		String insert = "INSERT INTO " + OpcatMessagesConstants.messages_table
				+ " (" + fieldsNames + ") values (" + values + ")";

		try {
			Statement stat = c.createStatement();
			stat.executeUpdate(insert);
		} catch (SQLException e) {
			OpcatLogger.logError(e, false);
		}

	}

	public GridPanel getMessages(OpcatMessagesFilter filter) {

		// create the panel
		ArrayList<String> cols = new ArrayList<String>();
		cols.add(OpcatMessagesConstants.GRID_NAME_ID);
		cols.add(OpcatMessagesConstants.GRID_NAME_MESSAGE);
		cols.add(OpcatMessagesConstants.GRID_NAME_SENDER_ID);
		cols.add(OpcatMessagesConstants.GRID_NAME_DESTINATION_ID);
		cols.add(OpcatMessagesConstants.GRID_NAME_MESSAGE_DATE);
		cols.add(OpcatMessagesConstants.GRID_NAME_SYSTEM);
		cols.add(OpcatMessagesConstants.GRID_NAME_SENDER_SUBSYSTEM);
		cols.add(OpcatMessagesConstants.GRID_NAME_MESSAGE_OP);
		cols.add(OpcatMessagesConstants.GRID_NAME_MESSAGE_SUBJECT);
		cols.add(OpcatMessagesConstants.GRID_NAME_SEVIRITY);
		GridPanel treePanel = new GridPanel(cols);

		treePanel.GetColumnsModel().getColumn(0).setCellRenderer(
				new NumberRenderer());

		treePanel.GetColumnsModel().getColumn(4).setCellRenderer(
				new DateRenderer());

		// treePanel.getGrid().so
		// treePanel.getGrid().getSortedModel().setSortMode(arg0)
		// treePanel.getGrid().getSortedModel().sort(0, Sort);
		// treePanel.getGrid().getSortedModel().sort(0,
		// SortTableModel.ADD_SORT);

		try {

			HashMap<String, Color> colorMap = new HashMap<String, Color>();

			Lookup severitylookup = new Lookup(new OpcatDatabaseLookupDAO(
					OpcatMessagesConstants.sevirity_table),
					OPCAT_MESSAGES_SEVIRITY.class);

			OPCAT_MESSAGES_SEVIRITY[] sevs = OPCAT_MESSAGES_SEVIRITY.values();
			for (int i = 0; i < sevs.length; i++) {
				colorMap.put(severitylookup.getDescription(sevs[i]), new Color(
						severitylookup.getColor(sevs[i])));
			}

			Statement stat = c.createStatement(ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);

			String query = filter.getQuery();

			String head = " SELECT t1." + OpcatDatabaseConstants.FIELD_NAME_ID
					+ ",t1." + OpcatMessagesConstants.FIELD_NAME_MESSAGE
					+ ",t1." + OpcatMessagesConstants.FIELD_NAME_SENDER_ID
					+ ",t1." + OpcatMessagesConstants.FIELD_NAME_MESSAGE_DATE
					+ ",t2." + OpcatDatabaseConstants.FIELD_NAME_DESCRIPTION
					+ ",t3." + OpcatDatabaseConstants.FIELD_NAME_DESCRIPTION
					+ ",t5." + OpcatDatabaseConstants.FIELD_NAME_DESCRIPTION
					+ ",t1."
					+ OpcatMessagesConstants.FIELD_NAME_MESSAGE_SUBJECT
					+ ",t4." + OpcatDatabaseConstants.FIELD_NAME_DESCRIPTION
					+ ",t1." + OpcatMessagesConstants.FIELD_NAME_DESTINATION_ID;

			if (query != null) {
				query = head + " FROM ( select * from "
						+ OpcatMessagesConstants.messages_table + " " + query
						+ " ) t1 ";
			} else {
				query = head + " FROM ( select * from "
						+ OpcatMessagesConstants.messages_table + " ) t1 ";
			}

			query = query + " left join (select "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + ", "
					+ OpcatDatabaseConstants.FIELD_NAME_DESCRIPTION + " from "
					+ OpcatMessagesConstants.systems_table + " ) t2  on t1."
					+ OpcatMessagesConstants.FIELD_NAME_SYSTEM + "= t2."
					+ OpcatDatabaseConstants.FIELD_NAME_ID;

			query = query + " left join (select "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + ", "
					+ OpcatDatabaseConstants.FIELD_NAME_DESCRIPTION + " from "
					+ OpcatMessagesConstants.subsystems_table + " ) t3  on t1."
					+ OpcatMessagesConstants.FIELD_NAME_SENDER_SUBSYSTEM
					+ "= t3." + OpcatDatabaseConstants.FIELD_NAME_ID;

			query = query + " left join (select "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + ", "
					+ OpcatDatabaseConstants.FIELD_NAME_DESCRIPTION + " from "
					+ OpcatMessagesConstants.sevirity_table + " ) t4  on t1."
					+ OpcatMessagesConstants.FIELD_NAME_SEVIRITY + "= t4."
					+ OpcatDatabaseConstants.FIELD_NAME_ID;

			query = query + " left join (select "
					+ OpcatDatabaseConstants.FIELD_NAME_ID + ", "
					+ OpcatDatabaseConstants.FIELD_NAME_DESCRIPTION + " from "
					+ OpcatMessagesConstants.op_table + " ) t5  on t1."
					+ OpcatMessagesConstants.FIELD_NAME_MESSAGE_OP + "= t5."
					+ OpcatDatabaseConstants.FIELD_NAME_ID;

			query = query + " ORDER BY t1."
					+ OpcatDatabaseConstants.FIELD_NAME_ID + " DESC ";

			ResultSet rs = stat.executeQuery(query);

			try {
				treePanel.setColorStyle(9, colorMap);
			} catch (SQLException e) {

			}

			/**
			 * fill data
			 */
			try {

				while (rs.next()) {
					Object[] row = new Object[10];
					Object[] rowTag = new Object[2];

					row[0] = rs.getBigDecimal(1);
					row[1] = rs.getString(2);
					row[2] = rs.getString(3);
					row[3] = rs.getString(10);
					row[4] = rs.getTimestamp(4);
					row[5] = rs.getString(5);
					row[6] = rs.getString(6);
					row[7] = rs.getString(7);
					row[8] = rs.getString(8);
					row[9] = rs.getString(9);

					rowTag[0] = " "; // rs.getBigDecimal(1)
					rowTag[1] = " ";

					treePanel.getGrid().addRow(row, rowTag, false);
				}
			} catch (Exception ex) {

			}
			treePanel.setTabName("Messages");
			JButton refresh = new JButton("Refresh");
			refresh.setIcon(SvnImages.ACTION_REFRESH);
			refresh.setAction(new OpcatMessagesConsoleRefreshAction(
					"    Refresh    ", SvnImages.ACTION_REFRESH, treePanel));
			treePanel.getButtonPane().add(refresh);

			lastFilter = filter;

			return treePanel;
		} catch (Exception ex) {
			// OpcatLogger.logError(ex);
			return treePanel;
		}
	}

	public static String getMessagesGridName() {
		return "Messages";
	}
}
