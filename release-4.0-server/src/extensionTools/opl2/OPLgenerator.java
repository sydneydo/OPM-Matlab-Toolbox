package extensionTools.opl2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import exportedAPI.OpcatExtensionTool;
import exportedAPI.opcatAPI.ISystem;
import extensionTools.opl2.alg.OPLContainer;
import extensionTools.opl2.generated.OPLscript;
import extensionTools.opl2.generated.ObjectFactory;


public class OPLgenerator implements OpcatExtensionTool
{
	public OPLgenerator(ISystem opcatSystem_){
          this.opcatSystem = opcatSystem_;

          try{
          this.jc = JAXBContext.newInstance( "extensionTools.opl2.generated" );
          }catch(Exception e){e.printStackTrace();}
          this.objFactory = new ObjectFactory();
          this.alg = new OPLContainer(this.opcatSystem,this.objFactory);
	}

        public OPLgenerator(){
          try{
          this.jc = JAXBContext.newInstance( "extensionTools.opl2.generated" );
          }catch(Exception e){e.printStackTrace();}
          this.objFactory = new ObjectFactory();
	}

	public String getName()
	{
		return "Simple OPL";
	}

	public JPanel getAboutBox(){ return null; }

	public String getHelpURL(){ return null;}


        public StringBuffer extractOPLScript(){
          StringWriter writer = new StringWriter(4096);
          try{
            this.alg.refresh();
            this.opl = this.alg.getOPLTree();

            Marshaller m = this.jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(this.opl, writer);
            writer.flush();
          }catch(Exception e){e.printStackTrace();}
          finally{  }
          return writer.getBuffer();
        }

        public StringBuffer getOplHTML() {
          StringBuffer bf = new StringBuffer();
          try {
            this.alg.refresh();
            this.opl = this.alg.getOPLTree();
            this.printer = new PrettyPrinter(this.opcatSystem.getISystemStructure(),
                                        this.opcatSystem);
            this.printer.visit(this.opl);
            //PlainTextPrinter pr = new PlainTextPrinter(opcatSystem.getISystemStructure(),
                                //        opcatSystem);
            //pr.visit(opl);
            //System.out.println("My Plain Text: "+pr.bf.toString());
            bf = this.printer.getBuffer();
            //initFileName();
            //extractOPLFile(filename, opl);
          }
          catch (Exception e) {
            e.printStackTrace();
          }
          return bf;
        }

        public StringBuffer getOplText(){
          StringBuffer bf = new StringBuffer();
          try {
            this.alg.refresh();
            this.opl = this.alg.getOPLTree();
            PlainTextPrinter pr = new PlainTextPrinter(this.opcatSystem.getISystemStructure(),
                                        this.opcatSystem);
            pr.visit(this.opl);
            //System.out.println("My Plain Text: "+pr.bf.toString());
            bf = pr.getBuffer();

          }
          catch (Exception e) {
            e.printStackTrace();
          }
          return bf;
        }

        public StringBuffer getOplText(long opd) {
      StringBuffer bf = new StringBuffer();
      try {
        this.alg.refresh();
        this.opl = this.alg.getOPLTreePerOPD(opd);
        PlainTextPrinter pr = new PlainTextPrinter(this.opcatSystem.getISystemStructure(),
                                        this.opcatSystem);
            pr.visit(this.opl);
            //System.out.println("My Plain Text: "+pr.bf.toString());
            bf = pr.getBuffer();


      }
      catch (Exception e) {
        e.printStackTrace();
      }
      return bf;
    }



        public StringBuffer getOplHTML(long opd) {
           // System.err.println(""+opcatSystem.getName());
          StringBuffer bf = new StringBuffer();
          try {
            this.alg.refresh();
            this.opl = this.alg.getOPLTreePerOPD(opd);
            this.printer = new PrettyPrinter(this.opcatSystem.getISystemStructure(),
                                        this.opcatSystem);
            this.printer.visit(this.opl);
            bf = this.printer.getBuffer();
          }
          catch (Exception e) {
            e.printStackTrace();
          }
          return bf;
        }




       /**
        * Creates OPL script file in XML format
        * @param name - full path to place where
        * the sript will be extracted
        * @param opl - OPLscript
        * * */
        public void extractOPLFile(String name, OPLscript opl){
          try{
            File f = new File(name);
            //System.out.println("Name of file: "+name);
            if(f.exists()) {
				f.delete();
			}
            f.createNewFile();
            // true - makes append mode
            Writer fw = new BufferedWriter(new FileWriter(f),4096);
            Marshaller m = this.jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(opl, fw);
            fw.close();
          }
     /*     catch(PropertyException e){
            System.err.println("\n Couldn't create new file, Bad property !!!");
            e.printStackTrace();
          }
          catch(JAXBException e){
            System.err.println("\n Couldn't create new file, JAXB problem !!!");
            e.printStackTrace();
          }
          catch(IOException e){
            System.err.println("\n Couldn't create new file, IOException !!!");
            e.printStackTrace();
          }*/
          catch(Exception e){
            //System.err.println("\n Couldn't create new file !!!");
            e.printStackTrace();
          }
        }

        public OPLscript getOPLTree(){
          try{
            this.alg.refresh();
            return this.alg.getOPLTree();
          }catch (Exception e){return null;}
        }

        public OPLscript getOPLTreePerOPD(long opd)throws Exception{
          try{
            return this.alg.getOPLTreePerOPD(opd);
          } catch(Exception e){
           // System.out.println("The given OPD name does not exist!");
            e.printStackTrace();
            throw e;
          }
       }

	// the "main" of your tool
	public JPanel execute(ISystem opcatSystem_)
	{

//                StringBuffer bf;
//                PrettyPrinter printer = new PrettyPrinter();
//                opcatSystem = opcatSystem_;
//                initFileName();
//		JPanel returnPanel = new JPanel(new BorderLayout()); // this panel we will return
//		JPanel contentPanel = new JPanel(); // this panel will hold all inf
//                alg = new OPLContainer(opcatSystem);
//                opl = getOPLTree();
//                extractOPLFile(filename, opl);
//                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
//		contentPanel.setBackground(new Color(230, 230, 230));
//                label = new JLabel();
//                bf = printer.getBuffer();
//                //label.setText(bf.toString());
//                contentPanel.add(label);
//		JScrollPane sp = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		returnPanel.add(sp);
//                returnPanel.add(refreshButton);
//		return returnPanel;
               StringBuffer bf;
               this.opcatSystem = opcatSystem_;
               this.alg = new OPLContainer(this.opcatSystem,this.objFactory);
               this.opl = this.getOPLTree();
               this.initFileName();
               JPanel returnPanel = new JPanel(new BorderLayout()); // this panel we will return
               JTextPane contentPanel = new JTextPane();
               contentPanel.setContentType("text/html");
               contentPanel.setEditable(false);
               //opl = getOPLTree();
               this.extractOPLFile(this.filename, this.opl);
               //StringBuffer b = extractOPLScript();
               //System.out.println(b.toString());
               contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
               contentPanel.setBackground(new Color(230, 230, 230));
               this.printer = new PrettyPrinter(this.opcatSystem.getISystemStructure(),this.opcatSystem);
               this.printer.visit(this.opl);
               bf = this.printer.getBuffer();
               //System.err.println(bf);
               contentPanel.setText(bf.toString());
               JScrollPane sp = new JScrollPane(contentPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
               returnPanel.add(sp);
               return returnPanel;

	}

        private void initFileName(){

          this.filename = this.opcatSystem.getName();
          this.filename = this.filename.concat(".xml");
        }


        // The variable is used while marshalling OPLscript
        protected JAXBContext jc;
        protected OPLContainer alg;
        private String filename;
        private OPLscript opl;
        private ISystem opcatSystem;
        private PrettyPrinter printer;
        private ObjectFactory objFactory;


}

