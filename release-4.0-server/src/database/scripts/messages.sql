USE opcat ; 

DROP TABLE IF EXISTS   messages_type ;
CREATE TABLE   messages_type  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT ,
				 NAME  VARCHAR(254) NOT NULL , 
				 DESCRIPTION  VARCHAR(254) NOT NULL,
				 COLOR INT(10) , 
				PRIMARY KEY ( ID ));
				
DROP TABLE IF EXISTS   messages_systems ;
CREATE TABLE   messages_systems  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT ,
				 NAME  VARCHAR(254) NOT NULL , 
				 DESCRIPTION  VARCHAR(254)NOT NULL ,
				 COLOR INT(10) , 
				PRIMARY KEY ( ID ));
				
DROP TABLE IF EXISTS   messages_subsystems ;
CREATE TABLE   messages_subsystems  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT ,
				 NAME  VARCHAR(254) NOT NULL , 
				 DESCRIPTION  VARCHAR(254)NOT NULL ,
				 COLOR INT(10) , 
				PRIMARY KEY ( ID ));				
				
DROP TABLE IF EXISTS   messages_severity ;
CREATE TABLE   messages_severity  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT ,
				 NAME  VARCHAR(254) NOT NULL , 
				 DESCRIPTION  VARCHAR(254) NOT NULL,
				 COLOR INT(10) , 
				PRIMARY KEY ( ID ));
				
DROP TABLE IF EXISTS   messages_operation ;
CREATE TABLE   messages_operation  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT ,
				 NAME  VARCHAR(254) NOT NULL , 
				 DESCRIPTION  VARCHAR(254) NOT NULL,
				 COLOR INT(10) , 
				PRIMARY KEY ( ID ));				
				
DROP TABLE IF EXISTS   messages ;
CREATE TABLE   messages  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT 
				, SYSTEM_ID  INT(10) NOT NULL 
				, DESTINATION_ID  VARCHAR(254) NOT NULL
				, DESTINATION_TYPE_ID  INT(10) NOT NULL
				, DESTINATION_SUBSYSTEM_ID  INT(10) NOT NULL
				,  SENDER_ID  VARCHAR(254) NOT NULL
				,  SENDER_TYPE_ID  INT(10) NOT NULL
				,  SENDER_SUBSYSTEM_ID  INT(10) NOT NULL
				,  MESSAGE_DATE  TIMESTAMP  NOT NULL
				,  SEVIRITY_ID  INT(10) NOT NULL
				,  MESSAGE_DESCRIPTION  MEDIUMTEXT  NOT NULL 
				,  MESSAGE_SUBJECT  MEDIUMTEXT  
				,  MESSAGE_OP_ID  INT(10)  
		   		, PRIMARY KEY ( ID )
		   		);
		   		
		   		
ALTER TABLE   messages 
ADD CONSTRAINT  FK_messages_type_destination_id 
      FOREIGN KEY ( DESTINATION_TYPE_ID )
      REFERENCES   messages_type  ( ID );
				
ALTER TABLE   messages 
ADD CONSTRAINT  FK_messages_type_sender_id 
      FOREIGN KEY ( SENDER_TYPE_ID )
      REFERENCES   messages_type  ( ID );      
      
ALTER TABLE   messages 
ADD CONSTRAINT  FK_messages_system_system_id 
      FOREIGN KEY ( SYSTEM_ID )
      REFERENCES   messages_systems  ( ID );     
      
ALTER TABLE   messages 
ADD CONSTRAINT  FK_messages_subsystems_DESTINATION_SUBSYSTEM_ID 
      FOREIGN KEY ( DESTINATION_SUBSYSTEM_ID )
      REFERENCES   messages_subsystems  ( ID );  
      
ALTER TABLE   messages 
ADD CONSTRAINT  FK_messages_subsystems_SENDER_SUBSYSTEM_ID 
      FOREIGN KEY ( SENDER_SUBSYSTEM_ID )
      REFERENCES   messages_subsystems  ( ID );   
      
            
ALTER TABLE   messages 
ADD CONSTRAINT  FK_messages_system_SEVIRITY_ID 
      FOREIGN KEY ( SEVIRITY_ID )
      REFERENCES   messages_severity  ( ID );               

ALTER TABLE   messages 
ADD CONSTRAINT  FK_messages_operation_MESSAGE_OP_ID 
      FOREIGN KEY ( MESSAGE_OP_ID )
      REFERENCES   messages_operation  ( ID );

INSERT INTO   messages_type  ( NAME , DESCRIPTION )   values('ALL', 'All');
INSERT INTO   messages_type  ( NAME , DESCRIPTION )   values('USER', 'User'); 
INSERT INTO   messages_type  ( NAME , DESCRIPTION )   values('MODEL', 'Model');
INSERT INTO   messages_type  ( NAME , DESCRIPTION )   values('ENTITY', 'Entity');
INSERT INTO   messages_systems  ( NAME , DESCRIPTION )   values('ALL',  'All');
INSERT INTO   messages_systems  ( NAME , DESCRIPTION )   values('FILES',  'Files');
INSERT INTO   messages_systems  ( NAME , DESCRIPTION )   values('MODELS', 'Model');
INSERT INTO   messages_subsystems  ( NAME , DESCRIPTION )   values('ALL', 'All') ;
INSERT INTO   messages_subsystems  ( NAME , DESCRIPTION )   values('COMMIT', 'Commit');
INSERT INTO   messages_subsystems  ( NAME , DESCRIPTION )   values('CHECKOUT', 'Checkout');
INSERT INTO   messages_subsystems  ( NAME , DESCRIPTION )   values('EXPOSE', 'Expose');
INSERT INTO   messages_severity  ( NAME , DESCRIPTION,COLOR )   values('INFORMATION', 'Information', 3407769);
INSERT INTO   messages_severity  ( NAME , DESCRIPTION,COLOR )   values('WARNING', 'Warning', 3407871);
INSERT INTO   messages_severity  ( NAME , DESCRIPTION,COLOR )   values('ACTION_NEEDED', 'Action Required', 16711782)  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_COMMIT_MODIFIED', 'Commit Modified')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_COMMIT_COMPLETED', 'Commit Completed')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_COMMIT_DELETED', 'Commit Deleted')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_COMMIT_REPLACED', 'Commit Replaced') ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_COMMIT_DELTA_SENT', 'Sending to server')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_COMMIT_ADDED', 'Commit Add')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_UPDATE_NONE', 'Nothing to Update')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_UPDATE_ADD', 'Update Add') ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_UPDATE_DELETE', 'Update Delete')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_UPDATE_UPDATE_CHANGED', 'Update Changed')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_UPDATE_UPDATE_CONFLICTED', 'Update Conflicted')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_UPDATE_UPDATE_MERGED', 'Merged') ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_UPDATE_EXTERNAL', 'Update Ext')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_RESTORE', 'Restore')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_UPDATE_COMPLETED', 'Update Completed')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_ADD', 'Add to MC') ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_DELETE', 'Delete')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_LOCKED', 'Lock')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_LOCK_FAILED', 'Lock Fail')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_UNLOCK_FAILED', 'Unlock Fail') ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_UNLOCKED', 'Unlock')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('FILES_REVERT', 'Revert')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('NONE', 'None')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('EXPOSE_INTERFACE_CHANGE_REQUEST', 'Expose Interface Change is needed')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('EXPOSE_CHANGE_DONE', 'Expose Change commited')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('EXPOSE_CHANGE_REQUEST', 'Expose change requested')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('EXPOSE_UPDATED', 'Expose Updated')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('EXPOSE_ADDED', 'Expose Added')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('EXPOSE_DELETED', 'Expose Deleted')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('EXPOSE_USED', 'Expose used')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('EXPOSED_UN_USED', 'Expose un-used')  ;
INSERT INTO   messages_operation  ( NAME , DESCRIPTION )   values('EXPOSE_RELEASE_REQUEST', 'Expose Release request')  ;





