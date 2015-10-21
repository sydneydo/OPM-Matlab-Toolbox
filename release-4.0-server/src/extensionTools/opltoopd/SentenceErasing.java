package extensionTools.opltoopd;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JOptionPane;

import exportedAPI.OpcatConstants;
import exportedAPI.opcatAPIx.IXCheckResult;
import exportedAPI.opcatAPIx.IXEntry;
import exportedAPI.opcatAPIx.IXInstance;
import exportedAPI.opcatAPIx.IXObjectInstance;
import exportedAPI.opcatAPIx.IXProcessInstance;
import exportedAPI.opcatAPIx.IXRelationEntry;
import exportedAPI.opcatAPIx.IXRelationInstance;
import exportedAPI.opcatAPIx.IXStateInstance;
import exportedAPI.opcatAPIx.IXSystem;
import exportedAPI.opcatAPIx.IXSystemStructure;

/**
 * Represents class which takes care
 *  of old sentence errasing from the OPD.
 */

public class SentenceErasing {

  /**
   * @param system the running IXSystem
   */

  public SentenceErasing(IXSystem system) {
    this.iXSystem = system;
    this.opdId = this.iXSystem.getCurrentIXOpd().getOpdId();
    this.systStruct = this.iXSystem.getIXSystemStructure();
  }
/**
 * Erase the old Object Instance
 * @param oldSent Object sentence for erasing
 */
  public void objectSentErase(ObjectStruct oldSent){
    IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getObjectName());
    IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
    if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
    if (res.getResult()==IXCheckResult.WARNING){
      int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
          if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
    }
    if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
    return ;
  }

  /**
   * Takes care of the case when both old and  new sentences are Object sentences,
   * erasing an old Object if it becomes unnecessary.
   * @param oldSent Object sentence for erasing
   * @param newSent Object sentence for erasing
   * @return newSent the sentence for adding without any changes
   */

  public ObjectStruct objectObjectErase(ObjectStruct newSent, ObjectStruct oldSent){

    if(newSent.getObjectName().compareTo(oldSent.getObjectName())!=0){
      IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getObjectName());
      IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
      if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
      if (res.getResult()==IXCheckResult.WARNING){
        int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
        if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
      }
      if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
    }
    else{
      Iterator newStates = newSent.States.iterator();
      Iterator oldStates = oldSent.States.iterator();
      while(oldStates.hasNext()){
        boolean neededState = true;
        State oldState = (State)oldStates.next();
        while(newStates.hasNext()){
          State newState = (State)newStates.next();
          if(/*oldState.equals(newState*/oldState.getStateName().equals(newState.getStateName())){
            neededState = false;
          }
        }
        if (!neededState){
          IXStateInstance oldStateInst = (IXStateInstance)this.getThingInstance(oldState.getStateName());
          IXCheckResult res =  this.iXSystem.checkDeletion(oldStateInst);
          if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(oldStateInst);
		}
          if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
          if (res.getResult()==IXCheckResult.WARNING){
            int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(oldStateInst);
			}
          }
          neededState = true;
        }
      }
    }

    return newSent;
  }

  /**
   * Takes care of the case when both old and  new sentences are Process sentences,
   * erasing an old Process if it becomes unnecessary.
   * @param oldSent Process sentence for erasing
   * @param newSent Process sentence for erasing
   * @return newSent the sentence for adding without any changes
   */
  public ProcessStruct processProcessErase(ProcessStruct newSent, ProcessStruct oldSent){

    IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getProcessName());
    IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
    if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(processInstance);
	}
    if (res.getResult()==IXCheckResult.WARNING){
      int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
      if (conf==JOptionPane.OK_OPTION) {
		this.iXSystem.delete(processInstance);
	}
    }
    if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
  return newSent;
}

/**
 * Takes care of the case when old sentence is a Process sentence and new sentence
 * is an Object, erasing an unnecessary Process.
 * @param oldSent Process sentence for erasing
 * @param newSent Object sentence for adding
 * @return newSent the sentence for adding without any changes
 */
  public ObjectStruct objectProcessErase(ObjectStruct newSent, ProcessStruct oldSent){
    IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getProcessName());
    IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
    if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(processInstance);
	}
    if (res.getResult()==IXCheckResult.WARNING){
      int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
      if (conf==JOptionPane.OK_OPTION) {
		this.iXSystem.delete(processInstance);
	}
    }
    if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}

    return newSent;
  }

  /**
   * Erase the old Process Instance
   * @param oldSent Process sentence for erasing

 */
  public void processSentErase(ProcessStruct oldSent){
    IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getProcessName());
    IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
    if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(processInstance);
	}
    if (res.getResult()==IXCheckResult.WARNING){
      int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
      if (conf==JOptionPane.OK_OPTION) {
		this.iXSystem.delete(processInstance);
	}
    }
    if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}

    return;
  }

/**
 * Takes care of the case when old sentence is a Link sentence and new sentence
 * is an Object sentence, erasing an unnecessary Link with its edge Objects and Processes.
 * @param oldSent Link sentence for erasing
 * @param newSent Object sentence for adding
 * @return newSent the sentence for adding without any changes
 */
  public ObjectStruct objectLinkErase(ObjectStruct newSent, LinkStruct oldSent){
    if(oldSent.getSourceObject()!=null){
       IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
       IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}

     }

     else{
       if((oldSent.getSourceProcess()!=null)){
         IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
         IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
         if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
         if (res.getResult()==IXCheckResult.WARNING){
           int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
           if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
         }
         if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}

       }
     }
     Iterator iter = oldSent.getDestObjects();
     while (iter.hasNext()){
       IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(((ObjectStruct)iter.next()).getObjectName());
       IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
     }

     iter = oldSent.getDestProcesses();
     while (iter.hasNext()){
       IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(((ProcessStruct)iter.next()).getProcessName());
       IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(processInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}

     }
     return newSent;
  }

  /**
   * Erase an old Link Instance
   * @param oldSent Link sentence for erasing
   */
  public void linkSentErase(LinkStruct oldSent){
    if(oldSent.getSourceObject()!=null){
       IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
       IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}

     }
     else{
       if((oldSent.getSourceProcess()!=null)){
         IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
         IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
         if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
         if (res.getResult()==IXCheckResult.WARNING){
           int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
           if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
         }
         if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
       }
     }
     Iterator iter = oldSent.getDestObjects();
     while (iter.hasNext()){
       IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(((ObjectStruct)iter.next()).getObjectName());
       IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
     }

     iter = oldSent.getDestProcesses();
     while (iter.hasNext()){
       IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(((ProcessStruct)iter.next()).getProcessName());
       IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(processInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
     }
     return;
  }

/**
 * Takes care of the case when old sentence is a Relation sentence and
 * new sentence is an Object sentence, erasing an unnecessary Relation
 * with its edge Objects and Processes.
 * @param oldSent Relation sentence for erasing
 * @param newSent Object sentence for adding
 * @return newSent the sentence for adding without any changes
 */
  public ObjectStruct objectRelationErase(ObjectStruct newSent, RelationStruct oldSent){
    if(oldSent.getSourceObject()!=null){
       IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
       IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
     }
     else{
       if((oldSent.getSourceProcess()!=null)){
         IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
         IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
         if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
         if (res.getResult()==IXCheckResult.WARNING){
           int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
           if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
         }
         if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
       }
     }
     Iterator iter = oldSent.getDestObjects();
     while (iter.hasNext()){
       IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(((ObjectStruct)iter.next()).getObjectName());
       IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
     }
     iter = oldSent.getDestProcesses();
     while (iter.hasNext()){
       IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(((ProcessStruct)iter.next()).getProcessName());
       IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
            if (res.getResult()==IXCheckResult.RIGHT) {
				this.iXSystem.delete(processInstance);
			}
            if (res.getResult()==IXCheckResult.WARNING){
              int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
              if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(processInstance);
			}
            }
            if (res.getResult()==IXCheckResult.WRONG) {
				JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}
     }
     return newSent;
  }
  /**
   * Erase an old Relation Instance
   * @param oldSent Relation sentence for erasing
 */
  public void relationSentErase(RelationStruct oldSent){
  if(oldSent.getSourceObject()!=null){
     IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
     IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
     if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
     if (res.getResult()==IXCheckResult.WARNING){
       int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
       if (conf==JOptionPane.OK_OPTION) {
		this.iXSystem.delete(objectInstance);
	}
     }
     if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
   }
   else{
     if((oldSent.getSourceProcess()!=null)){
       IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
       IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
            if (res.getResult()==IXCheckResult.RIGHT) {
				this.iXSystem.delete(processInstance);
			}
            if (res.getResult()==IXCheckResult.WARNING){
              int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
              if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(processInstance);
			}
            }
            if (res.getResult()==IXCheckResult.WRONG) {
				JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}
     }
   }
   Iterator iter = oldSent.getDestObjects();
   while (iter.hasNext()){
     IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(((ObjectStruct)iter.next()).getObjectName());
     IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
     if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
     if (res.getResult()==IXCheckResult.WARNING){
       int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
       if (conf==JOptionPane.OK_OPTION) {
		this.iXSystem.delete(objectInstance);
	}
     }
     if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
   }
   iter = oldSent.getDestProcesses();
   while (iter.hasNext()){
     IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(((ProcessStruct)iter.next()).getProcessName());
     IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
          if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
          if (res.getResult()==IXCheckResult.WARNING){
            int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(processInstance);
			}
          }
          if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
   }
   return;
  }
 /**
 * Takes care of the case when old sentence is a Link sentence and new sentence
 * is an Process sentence, erasing an unnecessary Link with its edge Objects and Processes.
 * @param oldSent Link sentence for erasing
 * @param newSent Process sentence for adding
 * @return newSent the sentence for adding without any changes
 */
  public ProcessStruct processLinkErase(ProcessStruct newSent, LinkStruct oldSent){
    if(oldSent.getSourceObject()!=null){
       IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
       IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
     }
     else{
       if((oldSent.getSourceProcess()!=null)){
         IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
         IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
              if (res.getResult()==IXCheckResult.RIGHT) {
				this.iXSystem.delete(processInstance);
			}
              if (res.getResult()==IXCheckResult.WARNING){
                int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
                if (conf==JOptionPane.OK_OPTION) {
					this.iXSystem.delete(processInstance);
				}
              }
              if (res.getResult()==IXCheckResult.WRONG) {
				JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}
       }
     }
     Iterator iter = oldSent.getDestObjects();
     while (iter.hasNext()){
       IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(((ObjectStruct)iter.next()).getObjectName());
       IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
     }
     iter = oldSent.getDestProcesses();
     while (iter.hasNext()){
       IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(((ProcessStruct)iter.next()).getProcessName());
       IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
            if (res.getResult()==IXCheckResult.RIGHT) {
				this.iXSystem.delete(processInstance);
			}
            if (res.getResult()==IXCheckResult.WARNING){
              int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
              if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(processInstance);
			}
            }
            if (res.getResult()==IXCheckResult.WRONG) {
				JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}
     }
     return newSent;
  }

  /**
  * Takes care of the case when old sentence is a Relation sentence and new
  * sentence is an Process sentence, erasing an unnecessary Relation
  * with its edge Objects and Processes.
  * @param oldSent Relation sentence for erasing
  * @param newSent Process sentence for adding
  * @return newSent the sentence for adding without any changes
 */
  public ProcessStruct processRelationErase(ProcessStruct newSent, RelationStruct oldSent){
    if(oldSent.getSourceObject()!=null){
      IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
      IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
      if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
      if (res.getResult()==IXCheckResult.WARNING){
        int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
        if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
      }
      if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
    }
    else{
      if((oldSent.getSourceProcess()!=null)){
        IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
        IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
             if (res.getResult()==IXCheckResult.RIGHT) {
				this.iXSystem.delete(processInstance);
			}
             if (res.getResult()==IXCheckResult.WARNING){
               int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
               if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(processInstance);
			}
             }
             if (res.getResult()==IXCheckResult.WRONG) {
				JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}
      }
    }
    Iterator iter = oldSent.getDestObjects();
    while (iter.hasNext()){
      IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(((ObjectStruct)iter.next()).getObjectName());
      IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
      if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
      if (res.getResult()==IXCheckResult.WARNING){
        int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
        if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
      }
      if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
    }
    iter = oldSent.getDestProcesses();
    while (iter.hasNext()){
      IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(((ProcessStruct)iter.next()).getProcessName());
      IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
           if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
           if (res.getResult()==IXCheckResult.WARNING){
             int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
             if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(processInstance);
			}
           }
           if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
    }
    return newSent;
  }

  /**
  * Takes care of the case when old sentence is an Object sentence
  * and new sentence is a Link sentence, erasing the old Object if it becomes unnecessary .
  * @param oldSent Object sentence for erasing
  * @param newSent Link sentence for adding
  * @return newSent the sentence for adding without any changes
 */
  public LinkStruct linkObjectErase(LinkStruct newSent, ObjectStruct oldSent){
    IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getObjectName());
    IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
    if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
    if (res.getResult()==IXCheckResult.WARNING){
      int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
      if (conf==JOptionPane.OK_OPTION) {
		this.iXSystem.delete(objectInstance);
	}
    }
    if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
    return newSent;
  }

  /**
  * Takes care of the case when old sentence is a Object sentence and new
  * sentence is an Process, erasing an unnecessary Object.
  * @param oldSent Object sentence for erasing
  * @param newSent Process sentence for adding
  * @return newSent the sentence for adding without any changes
 */
  public ProcessStruct processObjectErase(ProcessStruct newSent, ObjectStruct oldSent){
    IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getObjectName());
    IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
    if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
    if (res.getResult()==IXCheckResult.WARNING){
      int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
      if (conf==JOptionPane.OK_OPTION) {
		this.iXSystem.delete(objectInstance);
	}
    }
    if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
    return newSent;
  }

  /**
  * Takes care of the case when old sentence is a Process sentence
  * and new sentence is a Link sentence, erasing the old Process if it becomes unnecessary.
  * @param oldSent Process sentence for erasing
  * @param newSent tLink sentence for adding
  * @return newSent the sentence for adding without any changes
 */
  public LinkStruct linkProcessErase(LinkStruct newSent, ProcessStruct oldSent){
    IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getProcessName());
    IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
         if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
         if (res.getResult()==IXCheckResult.WARNING){
           int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
           if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
         }
         if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
    return newSent;
  }

  /**
  * Takes care of the case when both old and  new sentences are  Link sentences,
  * erasing  an old Link with its destination Objects and Processes
  * and its source Object/Process if it becomes unnecessary.
  * @param oldSent Link sentence for erasing
  * @param newSent Link sentence for adding
  * @return newSent the sentence for adding without any changes
 */
  public LinkStruct linkLinkErase(LinkStruct newSent, LinkStruct oldSent){
    Iterator objectIter = oldSent.getDestObjects();
    Iterator processIter = oldSent.getDestProcesses();
    if((oldSent.getSourceObject()!=null)&&(newSent.getSourceObject()!=null)){/*if sources in both sentences are Objects*/
      if ((oldSent.getSourceObject().getObjectName()).compareTo ((newSent.getSourceObject().getObjectName()))!=0){
        IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
        while (objectIter .hasNext()){/*erasing object destination*/
          IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
        }
        while (processIter .hasNext()){/*erasing process destination*/
          IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
       }
        if (this.getObjectToErase(oldSent.getSourceObject(),newSent.DestObjects)){/*source erasing */
          IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
          if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(objectInstance);
		}
          if (res.getResult()==IXCheckResult.WARNING){
            int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(objectInstance);
			}
          }
          if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
        }
      } else{/*the same source but different type of link*/
        if (newSent.getLinkType().compareTo(oldSent.getLinkType())!=0){
          IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
          while (objectIter .hasNext()){/*erasing object destination*/
            IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
            this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
          }
          while (processIter .hasNext()){/*erasing process destination*/
            IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
            this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
          }
        }
      }
   }

   if((oldSent.getSourceProcess()!=null)&&(newSent.getSourceProcess()!=null)){/*if sources in both sentences are Processes*/
     if ((oldSent.getSourceProcess().getProcessName()).compareTo ((newSent.getSourceProcess().getProcessName()))!=0){
       IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
       objectIter = oldSent.getDestObjects();
       processIter = oldSent.getDestProcesses();
       while (objectIter .hasNext()){/*erasing object destination*/
         IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
         this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
       }
       while (processIter .hasNext()){/*erasing process destination*/
         IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
         this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
       }
       if (this.getProcessToErase(oldSent.getSourceProcess(),newSent.getDestProcesses())){/*source erasing */
         IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
         if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
         if (res.getResult()==IXCheckResult.WARNING){
           int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
           if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
         }
         if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
       }
     } else{/*the same source but different type of link*/
       if (newSent.getLinkType().compareTo(oldSent.getLinkType())!=0){
         IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
         while (objectIter .hasNext()){/*erasing object destination*/
           IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
           this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
         }
         while (processIter .hasNext()){/*erasing process destination*/
           IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
           this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
         }
        }
      }
   }

   if((oldSent.getSourceObject()!=null)&&(newSent.getSourceProcess()!=null)){/*if old source is Object and new - is Process*/
       IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
       objectIter = oldSent.getDestObjects();
       processIter = oldSent.getDestProcesses();
       while (objectIter .hasNext()){/*erasing object destination*/
         IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
           this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
       }
       while (processIter .hasNext()){/*erasing process destination*/
         IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
         this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
       }
       if (this.getObjectToErase(oldSent.getSourceObject(),newSent.DestObjects)){/*source erasing */
         IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
         if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(objectInstance);
		}
         if (res.getResult()==IXCheckResult.WARNING){
           int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
           if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
         }
         if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
       }
   }

   if((oldSent.getSourceProcess()!=null)&&(newSent.getSourceObject()!=null)){/*if new source is Object and old - is Process*/
       IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
       objectIter = oldSent.getDestObjects();
       processIter = oldSent.getDestProcesses();
       while (objectIter .hasNext()){/*erasing object destination*/
         IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
           this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
       }
       while (processIter .hasNext()){/*erasing process destination*/
         IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
         this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
       }
       if (this.getProcessToErase(oldSent.getSourceProcess(),newSent.getDestProcesses())){/*source erasing */
         IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
         if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
         if (res.getResult()==IXCheckResult.WARNING){
           int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
           if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
         }
         if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
       }
   }
   this.destProcessErase (oldSent.getDestProcesses(),newSent.getDestProcesses());
   return newSent;
  }

  /**
  * Takes care of the case when old sentence is a Relation sentence
  * and new sentence is a Link sentence, erasing the Relation with its destination
  * Objects and Processes and its source Object/Process if it becomes unnecessary.
  * @param oldSent Relation sentence for erasing
  * @param newSent Link sentence for adding
  * @return newSent the sentence for adding without any changes
 */
  public LinkStruct linkRelationErase(LinkStruct newSent, RelationStruct oldSent){
    Iterator objectIter = oldSent.getDestObjects();
    Iterator processIter = oldSent.getDestProcesses();
    if((oldSent.getSourceObject()!=null)&&(newSent.getSourceObject()!=null)){/*if sources in both sentences are Objects*/
      if ((oldSent.getSourceObject().getObjectName()).compareTo ((newSent.getSourceObject().getObjectName()))!=0){
        IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
        while (objectIter .hasNext()){/*erasing object destination*/
          this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                           ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
        }
        while (processIter .hasNext()){/*erasing process destination*/
          this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                           ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
        if (this.getObjectToErase(oldSent.getSourceObject(),newSent.DestObjects)){/*source erasing */
          IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
          if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(objectInstance);
		}
          if (res.getResult()==IXCheckResult.WARNING){
            int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(objectInstance);
			}
          }
          if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
        }
      } else{/*the same source */
        IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
        while (objectIter .hasNext()){/*erasing object destination*/
          this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                           ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
        }
        while (processIter .hasNext()){/*erasing process destination*/
          this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                           ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
      }
   }

   if((oldSent.getSourceProcess()!=null)&&(newSent.getSourceProcess()!=null)){/*if sources in both sentences are Processes*/
     if ((oldSent.getSourceProcess().getProcessName()).compareTo ((newSent.getSourceProcess().getProcessName()))!=0){
       IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
       objectIter = oldSent.getDestObjects();
       processIter = oldSent.getDestProcesses();
       while (objectIter .hasNext()){/*erasing object destination*/
         this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                          ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
       }
       while (processIter .hasNext()){/*erasing process destination*/
         this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                          ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
       if (this.getProcessToErase(oldSent.getSourceProcess(),newSent.getDestProcesses())){/*source erasing */
         IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
         if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
         if (res.getResult()==IXCheckResult.WARNING){
           int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
           if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
         }
         if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
       }
     }else{/*the same source */
       IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
       while (objectIter .hasNext()){/*erasing object destination*/
         this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                          ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
       }
       while (processIter .hasNext()){/*erasing process destination*/
         this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                          ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
     }
   }

   if((oldSent.getSourceObject()!=null)&&(newSent.getSourceProcess()!=null)){/*if old source is Object and new - is Process*/
       IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
       objectIter = oldSent.getDestObjects();
       processIter = oldSent.getDestProcesses();
       while (objectIter .hasNext()){/*erasing object destination*/
         this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                          ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
       }
       while (processIter .hasNext()){/*erasing process destination*/
         this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                          ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
       if (this.getObjectToErase(oldSent.getSourceObject(),newSent.DestObjects)){/*source erasing */
         IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
         if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(objectInstance);
		}
         if (res.getResult()==IXCheckResult.WARNING){
           int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
           if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
         }
         if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
       }
   }

   if((oldSent.getSourceProcess()!=null)&&(newSent.getSourceObject()!=null)){/*if new source is Object and old - is Process*/
       IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
       objectIter = oldSent.getDestObjects();
       processIter = oldSent.getDestProcesses();
       while (objectIter .hasNext()){/*erasing object destination*/
         this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                          ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
       }
       while (processIter .hasNext()){/*erasing process destination*/
         this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                          ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
       if (this.getProcessToErase(oldSent.getSourceProcess(),newSent.getDestProcesses())){/*source erasing */
         IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
       if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(processInstance);
	}
       if (res.getResult()==IXCheckResult.WARNING){
         int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
         if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
       }
       if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
       }
   }

   this.destProcessErase (oldSent.getDestProcesses(),newSent.getDestProcesses());
   return newSent;
  }

  /**
  * Takes care of the case when old sentence is an Object sentence and
  * new sentence is a Relation sentence, erasing the old Object if it becomes unnecessary .
  * @param oldSent Object sentence for erasing
  * @param newSent Relation sentence for adding
 */
  public RelationStruct relationObjectErase(RelationStruct newSent, ObjectStruct oldSent){
    IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getObjectName());
    IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
    if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(objectInstance);
	}
    if (res.getResult()==IXCheckResult.WARNING){
      int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
      if (conf==JOptionPane.OK_OPTION) {
		this.iXSystem.delete(objectInstance);
	}
    }
    if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
    return newSent;
  }

  /**
  * Takes care of the case when old sentence is an Process sentence and new
  * sentence is a Relation sentence, erasing the old Process if it becomes unnecessary .
  * @param oldSent Process sentence for erasing
  * @param newSent Relation sentence for adding
  * @return newSent the sentence for adding without any changes
 */
  public RelationStruct relationProcessErase(RelationStruct newSent, ProcessStruct oldSent){
    IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getProcessName());
    IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
    if (res.getResult()==IXCheckResult.RIGHT) {
		this.iXSystem.delete(processInstance);
	}
    if (res.getResult()==IXCheckResult.WARNING){
      int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
      if (conf==JOptionPane.OK_OPTION) {
		this.iXSystem.delete(processInstance);
	}
    }
    if (res.getResult()==IXCheckResult.WRONG) {
		JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
    return newSent;
  }

  /**
  * Takes care of the case when old sentence is a Link sentence and new sentence
  * is an Relation sentence, erasing  the Link with its destination Objects
  * and Processes and its source Object/Process if it becomes unnecessary.
  * @param oldSent Link sentence for erasing
  * @param newSent Relation sentence for adding
  * @return newSent the sentence for adding without any changes
 */
  public RelationStruct relationLinkErase(RelationStruct newSent, LinkStruct oldSent){

    Iterator objectIter = oldSent.getDestObjects();
    Iterator processIter = oldSent.getDestProcesses();
    if((oldSent.getSourceObject()!=null)&&(newSent.getSourceObject()!=null)){/*if sources in both sentences are Objects*/
      if ((oldSent.getSourceObject().getObjectName()).compareTo ((newSent.getSourceObject().getObjectName()))!=0){
        IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
        while (objectIter .hasNext()){/*erasing object destination*/
          IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));        }
        while (processIter .hasNext()){/*erasing process destination*/
          IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
        }
        if (this.getObjectToErase(oldSent.getSourceObject(),newSent.DestObjects)){/*source erasing */
          IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
          if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(objectInstance);
		}
          if (res.getResult()==IXCheckResult.WARNING){
            int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(objectInstance);
			}
          }
          if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
        }
      } else{/*the same source */
        IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
        while (objectIter .hasNext()){/*erasing object destination*/
          IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
        }
        while (processIter .hasNext()){/*erasing process destination*/
          IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
        }
      }
    }

    if((oldSent.getSourceProcess()!=null)&&(newSent.getSourceProcess()!=null)){/*if sources in both sentences are Processes*/
      if ((oldSent.getSourceProcess().getProcessName()).compareTo ((newSent.getSourceProcess().getProcessName()))!=0){
        IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
        objectIter = oldSent.getDestObjects();
        processIter = oldSent.getDestProcesses();
        while (objectIter .hasNext()){/*erasing object destination*/
          IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
        }
        while (processIter .hasNext()){/*erasing process destination*/
          IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
        }
        if (this.getProcessToErase(oldSent.getSourceProcess(),newSent.getDestProcesses())){/*source erasing */
          IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
          if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
          if (res.getResult()==IXCheckResult.WARNING){
            int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(processInstance);
			}
          }
          if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
        }
      }else{/*the same source */
        IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
        while (objectIter .hasNext()){/*erasing object destination*/
          IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
        }
        while (processIter .hasNext()){/*erasing process destination*/
          IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
        }
      }
    }

    if((oldSent.getSourceObject()!=null)&&(newSent.getSourceProcess()!=null)){/*if old source is Object and new - is Process*/
      IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
      objectIter = oldSent.getDestObjects();
      processIter = oldSent.getDestProcesses();
      while (objectIter .hasNext()){/*erasing object destination*/
        IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
        this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
      }
      while (processIter .hasNext()){/*erasing process destination*/
        IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
          this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(objectInstance,destInst));
      }
      if (this.getObjectToErase(oldSent.getSourceObject(),newSent.DestObjects)){/*source erasing */
        IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
        if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(objectInstance);
		}
        if (res.getResult()==IXCheckResult.WARNING){
          int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
          if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
        }
        if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
      }
    }

    if((oldSent.getSourceProcess()!=null)&&(newSent.getSourceObject()!=null)){/*if new source is Object and old - is Process*/
      IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
      objectIter = oldSent.getDestObjects();
      processIter = oldSent.getDestProcesses();
      while (objectIter .hasNext()){/*erasing object destination*/
        IXObjectInstance destInst = (IXObjectInstance)this.getThingInstance(((ObjectStruct)oldSent.getDestObjects().next()).getObjectName());
        this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
      }
      while (processIter .hasNext()){/*erasing process destination*/
        IXProcessInstance destInst = (IXProcessInstance)this.getThingInstance(((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName());
        this.iXSystem.delete(this.iXSystem.getIXLinkBetweenInstances(processInstance,destInst));
      }
      if (this.getProcessToErase(oldSent.getSourceProcess(),newSent.getDestProcesses())){/*source erasing */
        IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
        if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
        if (res.getResult()==IXCheckResult.WARNING){
          int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
          if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
        }
        if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
      }
    }
    this.destProcessErase (oldSent.getDestProcesses(),newSent.getDestProcesses());
    return newSent;
  }

  /**
  * Takes care of the case when both old and  new sentences are Relation sentences,
  * erasing an old Relation with its destination Objects and Processes and its
  * source Object/Process if it becomes unnecessary.
  * @param oldSent Relation sentence for erasing
  * @param newSent Relation sentence for adding
  * @return newSent the sentence for adding without any changes
 */
  public RelationStruct relationRelationErase(RelationStruct newSent, RelationStruct oldSent){
    Iterator objectIter = oldSent.getDestObjects();
    Iterator processIter = oldSent.getDestProcesses();
    if((oldSent.getSourceObject()!=null)&&(newSent.getSourceObject()!=null)){/*if sources in both sentences are Objects*/
      if ((oldSent.getSourceObject().getObjectName()).compareTo ((newSent.getSourceObject().getObjectName()))!=0){
        IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
        while (objectIter .hasNext()){/*erasing object destination*/
          this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                           ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
        }
        while (processIter .hasNext()){/*erasing process destination*/
          this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                           ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
        if (this.getObjectToErase(oldSent.getSourceObject(),newSent.DestObjects)){/*source erasing */
          IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
          if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(objectInstance);
		}
          if (res.getResult()==IXCheckResult.WARNING){
            int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(objectInstance);
			}
          }
          if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
        }
      } else{/*the same source but different type of relation*/
        if (newSent.getRelationType().compareTo(oldSent.getRelationType())!=0){
          IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
          while (objectIter .hasNext()){/*erasing object destination*/
            this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                             ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
          }
          while (processIter .hasNext()){/*erasing process destination*/
            this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                             ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
        }
      }
    }

    if((oldSent.getSourceProcess()!=null)&&(newSent.getSourceProcess()!=null)){/*if sources in both sentences are Processes*/
      if ((oldSent.getSourceProcess().getProcessName()).compareTo ((newSent.getSourceProcess().getProcessName()))!=0){
        IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
        objectIter = oldSent.getDestObjects();
        processIter = oldSent.getDestProcesses();
        while (objectIter .hasNext()){/*erasing object destination*/
          this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                           ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
        }
        while (processIter .hasNext()){/*erasing process destination*/
          this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                           ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
        if (this.getProcessToErase(oldSent.getSourceProcess(),newSent.getDestProcesses())){/*source erasing */
          IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
          if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
          if (res.getResult()==IXCheckResult.WARNING){
            int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            if (conf==JOptionPane.OK_OPTION) {
				this.iXSystem.delete(processInstance);
			}
          }
          if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
        }
      } else{/*the same source but different type of relation*/
        if (newSent.getRelationType().compareTo(oldSent.getRelationType())!=0){
          IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
          while (objectIter .hasNext()){/*erasing object destination*/
            this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                             ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
          }
          while (processIter .hasNext()){/*erasing process destination*/
            this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                             ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
        }
      }
    }

    if((oldSent.getSourceObject()!=null)&&(newSent.getSourceProcess()!=null)){/*if old source is Object and new - is Process*/
      IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(oldSent.getSourceObject().getObjectName());
      objectIter = oldSent.getDestObjects();
      processIter = oldSent.getDestProcesses();
      while (objectIter .hasNext()){/*erasing object destination*/
        this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                         ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
      }
      while (processIter .hasNext()){/*erasing process destination*/
        this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),objectInstance.getIXEntry().getName(),
                         ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
      if (this.getObjectToErase(oldSent.getSourceObject(),newSent.DestObjects)){/*source erasing */
        IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
        if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(objectInstance);
		}
        if (res.getResult()==IXCheckResult.WARNING){
          int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
          if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
        }
        if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
      }
    }

    if((oldSent.getSourceProcess()!=null)&&(newSent.getSourceObject()!=null)){/*if new source is Object and old - is Process*/
      IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(oldSent.getSourceProcess().getProcessName());
      objectIter = oldSent.getDestObjects();
      processIter = oldSent.getDestProcesses();
      while (objectIter .hasNext()){/*erasing object destination*/
        this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                         ((ObjectStruct)oldSent.getDestObjects().next()).getObjectName()));
      }
      while (processIter .hasNext()){/*erasing process destination*/
        this.iXSystem.delete(this.getRelationInstance(this.getType(oldSent.getRelationType()),processInstance.getIXEntry().getName(),
                         ((ProcessStruct)oldSent.getDestProcesses().next()).getProcessName()));
      }
      if (this.getProcessToErase(oldSent.getSourceProcess(),newSent.getDestProcesses())){/*source erasing */
        IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
        if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
        if (res.getResult()==IXCheckResult.WARNING){
          int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
          if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
        }
        if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
      }
    }

    this.destObjectErase (oldSent,newSent);
    return newSent;
  }

/*This function returns true if we need to erase the Object ********************************************************************/
  private boolean getObjectToErase(ObjectStruct oldObject,HashSet newObject){
    boolean flag = true;
    Iterator newObjIter = newObject.iterator();
    while (newObjIter.hasNext()){
      ObjectStruct newObj = (ObjectStruct)newObjIter.next();
       if (oldObject.getObjectName().compareTo (newObj.getObjectName())==0){
         /*where is no checs about into of the objects wityh the same name*/
         newObject.remove(newObj);
         flag = false;
       }
    }
    return flag;
  }

/*This function returns true if we need to erase the Process */
  private boolean getProcessToErase(ProcessStruct oldProcess,Iterator newProcessIter){
    boolean flag = true;
    while (newProcessIter.hasNext()){
       if (oldProcess.getProcessName().compareTo (((ProcessStruct)newProcessIter.next()).getProcessName())==0){
         flag = false;
       }
    }
    return flag;
  }

/* This function  erases old dest objects which don't exist in new sentence*/
  private void destObjectErase (RelationStruct oldSent,RelationStruct newSent){
    Iterator oldDest = oldSent.DestObjects.iterator();
    while (oldDest .hasNext()){
      ObjectStruct objForErase = (ObjectStruct)oldDest.next();
      if (this.getObjectToErase(objForErase,newSent.DestObjects)){/*****/
        IXObjectInstance objectInstance = (IXObjectInstance)this.getThingInstance(objForErase.getObjectName());
        IXCheckResult res = this.iXSystem.checkDeletion(objectInstance);
        if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(objectInstance);
		}
        if (res.getResult()==IXCheckResult.WARNING){
          int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
          if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(objectInstance);
		}
        }
        if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
      }
    }
  }

/* This function erases old dest processes which don't exist in new sentence*/
  private void destProcessErase (Iterator oldDest,Iterator newDest){
    while (oldDest.hasNext()){
      Iterator processIter = newDest;
      ProcessStruct procForErase = (ProcessStruct)oldDest.next();
      if (this.getProcessToErase(procForErase,processIter)){
        IXProcessInstance processInstance = (IXProcessInstance)this.getThingInstance(procForErase.getProcessName());
        IXCheckResult res = this.iXSystem.checkDeletion(processInstance);
        if (res.getResult()==IXCheckResult.RIGHT) {
			this.iXSystem.delete(processInstance);
		}
        if (res.getResult()==IXCheckResult.WARNING){
          int conf = JOptionPane.showConfirmDialog(this.oPLComboBox,res.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
          if (conf==JOptionPane.OK_OPTION) {
			this.iXSystem.delete(processInstance);
		}
        }
        if (res.getResult()==IXCheckResult.WRONG) {
			JOptionPane.showMessageDialog(this.oPLComboBox,res.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
      }
    }
}

//  private IXLinkInstance getLinkInstance (String link, String source, String dest){
//    Enumeration locenum = systStruct.getLinksInOpd (opdId);
//    IXLinkInstance linkInst;
//    IXLinkEntry linkEntry;
//    while (locenum.hasMoreElements()){
//      linkInst = (IXLinkInstance)locenum.nextElement();
//      linkEntry = ( IXLinkEntry)linkInst.getIXEntry();
//      String linkName = linkEntry.getName();
//      if (linkName.equals(link)){
//        long sourceId = getThingId(getThingInstance(source));
//        long destId = getThingId(getThingInstance(dest));
//        if (linkEntry.getDestinationId()==destId && linkEntry.getSourceId()==sourceId){
//          return linkInst;
//        }
//      }
//    }
//    return null;
//  }

  private IXRelationInstance getRelationInstance (int type, String source, String dest){
    Enumeration locenum = this.systStruct.getGeneralRelationsInOpd(this.opdId);
    IXRelationInstance relationInst;
    IXRelationEntry relationEntry;
    while (locenum.hasMoreElements()){
      relationInst =(IXRelationInstance) locenum.nextElement();
      relationEntry = (IXRelationEntry)relationInst.getIXEntry();
      if (relationEntry.getRelationType() == type){
        long sourceId = this.getThingId(this.getThingInstance(source));
        long destId = this.getThingId(this.getThingInstance(dest));
        if((relationEntry.getDestinationId()==destId) && (relationEntry.getSourceId()==sourceId)){
          return relationInst;
        }
      }
    }
    return null;
  }

  private IXInstance getThingInstance (String name){
       Enumeration locenum = this.systStruct.getInstancesInOpd(this.opdId);
       IXInstance thing;
       IXEntry entry;
       while (locenum.hasMoreElements()){
         thing = (IXInstance)locenum.nextElement();
         entry = thing.getIXEntry();
         String thingName = entry.getName();
         if(thingName.equals(name)){
           return thing;
         }
       }
      return null;
   }

   private long getThingId (IXInstance thing){
          IXEntry entry = thing.getIXEntry();
          return entry.getId();
   }

   private  int getType(String type){
     int intType = 0;
     if (type.compareTo("exhibits")==0) {
		return OpcatConstants.EXHIBITION_RELATION;
	}
     if (type.compareTo("consists of")==0) {
		return OpcatConstants.AGGREGATION_RELATION;
	}
     if (type.compareTo("is a")==0) {
		return OpcatConstants.SPECIALIZATION_RELATION;
	}
     if (type.compareTo("is an instance of")==0) {
		return OpcatConstants.INSTANTINATION_RELATION;
	}
     if (type.compareTo("relates to")==0) {
		return OpcatConstants.UNI_DIRECTIONAL_RELATION;
	}
     if (type.compareTo("are related")==0) {
		return OpcatConstants.BI_DIRECTIONAL_RELATION;
	}
     if (type.compareTo("are equivalent")==0) {
		return OpcatConstants.BI_DIRECTIONAL_RELATION;
	}
     if (type.compareTo("handles")==0) {
		return OpcatConstants.AGENT_LINK;
	}
     if (type.compareTo("requires")==0) {
		return OpcatConstants.INSTRUMENT_LINK;
	}
     if (type.compareTo("consumes")==0) {
		return OpcatConstants.CONSUMPTION_LINK;
	}
     if (type.compareTo("affects")==0) {
		return OpcatConstants.EFFECT_LINK;
	}
     if (type.compareTo("yields")==0) {
		return OpcatConstants.RESULT_LINK;
	}
     if (type.compareTo("triggers")==0) {
		return OpcatConstants.INVOCATION_LINK;
	}
     if (type.compareTo("inst_event")==0) {
		return OpcatConstants.INSTRUMENT_EVENT_LINK;
	}
     if (type.compareTo("exception")==0) {
		return OpcatConstants.EXCEPTION_LINK;
	}
     if (type.compareTo("occurs")==0) {
		return OpcatConstants.CONDITION_LINK;
	}
     if (type.compareTo("cons_event")==0) {
		return OpcatConstants.CONSUMPTION_EVENT_LINK;
	}

     return intType;
   }

   private IXSystem iXSystem;
   private long opdId;
   private IXSystemStructure systStruct;
   private OPLComboBox oPLComboBox;
}