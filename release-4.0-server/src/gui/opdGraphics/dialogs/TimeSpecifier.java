package gui.opdGraphics.dialogs;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


class TimeSpecifier extends JPanel implements SwingConstants{

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		/**
	 * 
	 */
	 
		private JLabel[] labels = {null, null, null, null, null, null, null};
		private String[] strings = {"msec", "sec", "min", "hours", "days", "months", "years"};
		private JTextField[] fields = {null, null, null, null, null, null, null};
		private JCheckBox infinity;
		private JPanel cellsPanel, infinityPanel;

		public TimeSpecifier(int direction, int position, int hGap, int vGap){
			this.infinity = new JCheckBox("Infinity");
			this.infinity.addItemListener(new InfinityListener());
			for (int i = 0; i < 7; i++){
				this.labels[i] = new JLabel(this.strings[i]);
				this.fields[i] = new JTextField("0",4);
			}
			GridLayout layout;
			this.cellsPanel = new JPanel();
			this.infinityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			this.infinityPanel.add(this.infinity);

			if ((direction == HORIZONTAL) && ((position == TOP) || (position == BOTTOM)))
			{
				layout = new GridLayout(2, 7);
				layout.setHgap(hGap);
				layout.setVgap(vGap);
				this.cellsPanel.setLayout(layout);

				if (position == TOP){
					for(int i = 0; i < 7; i++) {
						this.cellsPanel.add(this.labels[i]);
					}
					for(int i = 0; i < 7; i++) {
						this.cellsPanel.add(this.fields[i]);
					}
				}
				if (position == BOTTOM){
					for(int i = 0; i < 7; i++) {
						this.cellsPanel.add(this.fields[i]);
					}
					for(int i = 0; i < 7; i++) {
						this.cellsPanel.add(this.labels[i]);
					}
				}
			}

			if ((direction == HORIZONTAL) && ((position == LEFT) || (position == RIGHT)))
			{
				layout = new GridLayout(1, 14);
				layout.setHgap(hGap);
				layout.setVgap(vGap);
				this.setLayout(layout);
				if (position == LEFT){
					for (int i = 0; i < 7; i++){
						this.cellsPanel.add(this.labels[i]);
						this.cellsPanel.add(this.fields[i]);
					}
				}

				if (position == RIGHT){
					for (int i = 0; i < 7; i++){
						this.cellsPanel.add(this.fields[i]);
						this.cellsPanel.add(this.labels[i]);
					}
				}
			}
			if (direction == VERTICAL)
			{
				layout = new GridLayout(14, 1);
				layout.setHgap(hGap);
				layout.setVgap(vGap);
				this.setLayout(layout);

				if ((position == TOP) || (position == LEFT)){
					for (int i = 0; i < 7; i++){
						this.cellsPanel.add(this.labels[i]);
						this.cellsPanel.add(this.fields[i]);
					}
				}
				if ((position == BOTTOM) || (position == RIGHT)){
					for (int i = 0; i < 7; i++){
						this.cellsPanel.add(this.fields[i]);
						this.cellsPanel.add(this.labels[i]);
					}
				}
			}
			this.setLayout(new BoxLayout(TimeSpecifier.this, BoxLayout.Y_AXIS));
			this.add(this.infinityPanel);
			this.add(this.cellsPanel);
		}//ctor

		public void setTime(String time){
			if (time.compareTo("infinity") == 0)
			{
				this.infinity.setSelected(true);
				return;
			}
			StringTokenizer st = new StringTokenizer(time, ";");
			String tmp = new String();
			int i = 0;
			while (st.hasMoreTokens()){
				tmp = st.nextToken();
				Integer.parseInt(tmp);
				this.fields[i].setText(tmp);
				this.fields[i].repaint();
				i++;
			}
		}

		public String getTime(){
			String str = new String();
			for (int i = 0; i < 7; i++){
				try
				{
					Integer.parseInt(this.fields[i].getText());
				}catch(Exception e)
				{
					this.fields[i].setText("0");
				}
				str = str.concat(this.fields[i].getText()+";");
			}
			str = str.substring(0, str.length()-1);
			if (this.infinity.isSelected() == true) {
				str = "infinity";
			}
			return str;
		}

		public void setInfinity()
		{
			this.setTime(" ; ; ; ; ; ; ");
		}

		class InfinityListener implements ItemListener{
			public void itemStateChanged(ItemEvent e)
			{
				if (TimeSpecifier.this.infinity.isSelected() == true)
				{
					for(int i = 0; i < 7; i++) {
						TimeSpecifier.this.fields[i].setEditable(false);
					//setInfinity();
					}
				}
				if (TimeSpecifier.this.infinity.isSelected() == false)
				{
					for(int i = 0; i < 7; i++) {
						TimeSpecifier.this.fields[i].setEditable(true);
					}
				}
			return;
			}
		};
	}// end TimeSpecifier
