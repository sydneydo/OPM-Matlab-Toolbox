package extensionTools.sqlGeneration.util;
import java.io.File;
/**
 *                              Class Constants
 *                              ***************
 *                              
 * This class holds all the constants of the program.
 */
public interface Constants 
{
    // FILES
    String WORKING_DIR = "C:\\XML Project\\";
    String OUTPUT_FILENAME = WORKING_DIR + "script.txt";
    String DEFAULT_DB_FILENAME = WORKING_DIR + "test.mdb";
    String DEFAULT_XML_FILENAME = WORKING_DIR + "testXML.xml";
    File OUTPUT_SCRIPT_FILE = new File (OUTPUT_FILENAME);
    Logger logger = new Logger("C:\\xmldb.log", false, true);
    
    // SETS
    String TSS = "ThingSentenceSet";
    String EXB_SET = "ObjectExhibitionSentenceSet";
    String AGG_SET = "ObjectAggregationSentenceSet";
    String INHER_SET = "ObjectInheritanceSentenceSet";
    
    // SENTENCES
    String UNIDIR_SENTENCE = "ObjectUniDirectionalRelationSentence";
    String BIDIR_SENTENCE = "ObjectBiDirectionalRelationSentence";
    String AGG_SENTENCE = "ObjectAggregationSentence";
    String EXB_SENTENCE = "ObjectExhibitionSentence";   
    String INH_SENTENE = "ObjectInheritanceSentence";
    String STATE_SENTENCE = "ObjectStateSentence";
    String AGENT_SENTENCE = "AgentSentence";
    
    // OBJECTS
    String EXB_OBJECT = "ExhibitedObject";
    String AGG_OBJECT = "AggregatedObject";
    
    
    // ATTRIBUTES
    String AGG_ATTRIBUTE_NAME = "ObjectName";
    String EXB_ATTRIBUTE_NAME = "AttributeName";
    String INH_ATTRIBUTE_NAME = "InheritanceFatherName";
    String AGENT_ATTRIBUTE_NAME = "TriggeredProcessName";
    
    // FATHERS
    String AGG_FATHER = "subjectAggregationFatherName";
    String EXB_FATHER = "subjectExhibitionFatherName";
    
    String STATE_OBJECT = "StateClause";
    String STATE_ATT_NAME = "StateName";
    
    // STATE
    String COLUMN_STATE_ID = "_stateID";
    String COLUMN_STATE_DESC = "_stateDesc";
    String STATE_TABLE = "_states";
    
    // TSS
    String TSS_NAME = "subjectThingName";
    String TRANS_ID = "transID";
    String MAX_CARDINALITY = "MaximalCardinality";
    
    // RELATIONS
    String RELATION_NAME = "DestinationName";
    String SRC_NAME = "SourceName";
    String SRC_MAX_CARDINALITY = "SourceMaximalCardinality";
    String DEST_NAME = "DestinationName";
    String DEST_MAX_CARDINALITY = "DestinationMaximalCardinality";
    
    String CARDINALITY_ONE = "1";
    
    String UNIDIR_SET = "ObjectUniDirectionalRelationSentence";
    String BIDIR_STE = "ObjectBiDirectionalRelationSentence";
      
    String OBJECT_NAME = "ObjectName";    
    String INH_FATHER = "InheritanceFatherName";
    
    // Colomn ID 
    //String COLUMN_ID = "ID" ; 
    
}