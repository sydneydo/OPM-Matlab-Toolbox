USE opcat ; 

DROP TABLE IF EXISTS   expose_usage ;
DROP TABLE IF EXISTS   expose_data ;
DROP TABLE IF EXISTS   opm_entity_type ;
DROP TABLE IF EXISTS   expose_allowed ;
DROP TABLE IF EXISTS   expose_op ;
DROP TABLE IF EXISTS   expose_status ;
DROP TABLE IF EXISTS   expose ;

CREATE TABLE   expose_usage  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT 
				, EXPOSE_ID  INT(10) NOT NULL
				, USING_INSTANCE_IN_OPD_ID  INT(10) NOT NULL  
				, USING_INSTANCE_OPD_ID  INT(10) NOT NULL
				, USING_ENTITY_NAME  TEXT NOT NULL 
				, USING_MODEL_NAME  TEXT NOT NULL 
				, USING_MODEL_URI  TEXT NOT NULL
		   		, PRIMARY KEY ( ID )
		   		);	
		   		
ALTER TABLE `opcat`.`expose_usage` ENGINE = InnoDB;

CREATE TABLE   expose_data  (
				  EXPOSE_ID  INT(10)  
				, MODEL_ID  TEXT NOT NULL 
				, MODEL_NAME  TEXT NOT NULL
				, ENTITY_TYPE_ID  INT(10) NOT NULL
				, ENTITY_DESCRIPTION  TEXT NOT NULL
				, DESCRIPTION  TEXT NOT NULL				
				, ENTITY_NAME  TEXT NOT NULL
				, EXPOSE_OP_ID int(10) 
				, OP_USER_ID TEXT NOT NULL  
				, EXPOSE_STATUS_ID int(10) NOT NULL  
				, MODEL_URI  TEXT  
				, ENTITY_ID  int(10)  
				, EXPOSE_DATETIME TIMESTAMP 
		   		);		   		
ALTER TABLE `opcat`.`expose_data` ENGINE = InnoDB;

CREATE TABLE   expose_allowed  (
				 EXPOSE_ID  INT(10) NOT NULL AUTO_INCREMENT 
				, MODEL_URI  TEXT NOT NULL
		   		, PRIMARY KEY ( EXPOSE_ID )
		   		);	 
		   		
ALTER TABLE `opcat`.`expose_allowed` ENGINE = InnoDB;		

CREATE TABLE   expose  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT 
				, MODEL_URI  TEXT NOT NULL
				, ENTITY_ID  int(10) NOT NULL
		   		, PRIMARY KEY ( ID )
		   		);
	   		
ALTER TABLE `opcat`.`expose` ENGINE = InnoDB;

CREATE TABLE   opm_entity_type  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT ,
				 NAME  VARCHAR(254) NOT NULL , 
				 DESCRIPTION  VARCHAR(254) NOT NULL,
				 COLOR INT(10), 
				PRIMARY KEY ( ID ));
				
ALTER TABLE `opcat`.`opm_entity_type` ENGINE = InnoDB;
				
CREATE TABLE   expose_op  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT ,
				 NAME  VARCHAR(254) NOT NULL , 
				 DESCRIPTION  VARCHAR(254) NOT NULL,
				 COLOR INT(10), 
				PRIMARY KEY ( ID ));	
				
ALTER TABLE `opcat`.`expose_op` ENGINE = InnoDB;

				
CREATE TABLE   expose_status  (
				 ID  INT(10) NOT NULL AUTO_INCREMENT ,
				 NAME  VARCHAR(254) NOT NULL , 
				 DESCRIPTION  VARCHAR(254) NOT NULL,
				 COLOR INT(10), 
				PRIMARY KEY ( ID ));	

ALTER TABLE `opcat`.`expose_status` ENGINE = InnoDB;
		   		
ALTER TABLE   expose_allowed 
ADD CONSTRAINT  FK_expose_allowed_EXPOSE_ID 
      FOREIGN KEY ( EXPOSE_ID )
      REFERENCES   expose  ( ID );	
		   					   		
ALTER TABLE `opcat`.`expose_data` 
ADD CONSTRAINT `FK_expose_data_EXPOSE_ID` 
	FOREIGN KEY `FK_expose_data_EXPOSE_ID` (`EXPOSE_ID`)
    REFERENCES `expose` (`ID`)
    ON DELETE SET NULL
    ON UPDATE RESTRICT;

ALTER TABLE   expose_data 
ADD CONSTRAINT  FK_expose_data_EXPOSE_STATUS_ID 
      FOREIGN KEY ( EXPOSE_STATUS_ID )
      REFERENCES   expose_status  ( ID );	      
      
ALTER TABLE   expose_data 
ADD CONSTRAINT  FK_expose_data_EXPOSE_OP_ID 
      FOREIGN KEY ( EXPOSE_OP_ID )
      REFERENCES   expose_op  ( ID );
      
ALTER TABLE   expose_data 
ADD CONSTRAINT  FK_expose_data_ENTITY_TYPE_ID 
      FOREIGN KEY ( ENTITY_TYPE_ID )
      REFERENCES   opm_entity_type  ( ID );		      
		   		
ALTER TABLE   expose_usage 
ADD CONSTRAINT  FK_expose_usage_EXPOSE_ID 
      FOREIGN KEY ( EXPOSE_ID )
      REFERENCES   expose  ( ID );      
      
INSERT INTO   opm_entity_type  ( NAME , DESCRIPTION )   values('OBJECT', 'Object');
INSERT INTO   opm_entity_type  ( NAME , DESCRIPTION )   values('PROCESS', 'Process'); 
INSERT INTO   opm_entity_type  ( NAME , DESCRIPTION )   values('LINK', 'Link');
INSERT INTO   opm_entity_type  ( NAME , DESCRIPTION )   values('STATE', 'State');  

INSERT INTO   expose_op  ( NAME , DESCRIPTION )   values('NONE', 'Not Changed');
INSERT INTO   expose_op  ( NAME , DESCRIPTION )   values('UPDATE', 'Update');
INSERT INTO   expose_op  ( NAME , DESCRIPTION )   values('DELETE', 'Delete'); 
INSERT INTO   expose_op  ( NAME , DESCRIPTION )   values('ADD', 'Add');
INSERT INTO   expose_op  ( NAME , DESCRIPTION )   values('CHANGE_INTERFACE', 'Interface Change');  

INSERT INTO   expose_status  ( NAME , DESCRIPTION )   values('NORMAL_CHANGE_REQUEST', 'Change Requested');
INSERT INTO   expose_status  ( NAME , DESCRIPTION )   values('PRIVATE_CHANGE_REQUEST', 'Change Requested');
INSERT INTO   expose_status  ( NAME , DESCRIPTION )   values('NORMAL', 'Normal'); 
INSERT INTO   expose_status  ( NAME , DESCRIPTION )   values('PRIVATE', 'Private');
