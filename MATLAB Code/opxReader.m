%% OPM-MATLAB Toolbox .opx Reader Code
% By: Sydney Do (sydneydo@mit.edu)
% Date Created: 10/22/2015
% Last Updated: 10/22/2015

% function outputStruct = opxReader(opxfile.opx)

clear all
clc

addpath('C:\Users\Sydney Do\Desktop\MIT\PhD Research\OPM-Matlab Toolbox Project\OPM-Matlab-Toolbox\MATLAB Code');
addpath('C:\Users\Sydney Do\Desktop\MIT\PhD Research\OPM-Matlab Toolbox Project\OPM-Matlab-Toolbox\Test Files');

filename = 'IntegratedTestFile.opx';

% fileMap = xmlread(filename);

fileMap = xml2struct(filename);


outputStruct.filename = fileMap.OPX.OPMSystem.Attributes.name;
outputStruct.author = fileMap.OPX.OPMSystem.Attributes.author;

% Extract Visual Data
% Thing Section - things - objects - check for states - VisualState
% Fundamental Relation Section - structural relations
% Visual Link Section - procedural links



%% Extract visual data for all elements

% Initialize visual data set
visualdata = cell(1,200);   % Arbitrarily assign 200 elements to visualdata cell

% Visual Things Dataset
visualThingSet = fileMap.OPX.OPMSystem.VisualPart.OPD.ThingSection.VisualThing;

% Cycle through all visual things (objects, processes, states)
for i = 1:length(visualThingSet)
    if strcmpi(char(fieldnames(visualThingSet{i}.ThingData)),'VisualObject')
        % Code to process
        % may have states - encoded under VisualState - note 17th item has
        % states (for example)
        
        itemID = str2double(visualThingSet{i}.ThingData.VisualObject.InstanceAttr.Attributes.entityId);   % id location
        x = str2double(visualThingSet{i}.ThingData.VisualObject.ConnectionEdgeAttr.Attributes.x);
        y = str2double(visualThingSet{i}.ThingData.VisualObject.ConnectionEdgeAttr.Attributes.y);
        width = str2double(visualThingSet{i}.ThingData.VisualObject.ConnectionEdgeAttr.Attributes.width);
        height = str2double(visualThingSet{i}.ThingData.VisualObject.ConnectionEdgeAttr.Attributes.height);
        
        visualdata{itemID} = [x,y,width,height];
        
        %% Search for states
        subfieldnames = fieldnames(visualThingSet{i}.ThingData.VisualObject);       % nx1 cell of fieldnames
        
        % If the object has states
        if cell2mat(strfind(subfieldnames,'VisualState')) == 1
            
            % Extract State Visual Information
            for j = 1:length(visualThingSet{i}.ThingData.VisualObject.VisualState)
                %             visualThingSet{i}.ThingData.VisualObject.VisualState{j}
                
                %             counter = counter+1;
                itemID = str2double(visualThingSet{i}.ThingData.VisualObject.VisualState{j}.InstanceAttr.Attributes.entityId);
                x = str2double(visualThingSet{i}.ThingData.VisualObject.VisualState{j}.ConnectionEdgeAttr.Attributes.x);
                y = str2double(visualThingSet{i}.ThingData.VisualObject.VisualState{j}.ConnectionEdgeAttr.Attributes.y);
                width = str2double(visualThingSet{i}.ThingData.VisualObject.VisualState{j}.ConnectionEdgeAttr.Attributes.width);
                height = str2double(visualThingSet{i}.ThingData.VisualObject.VisualState{j}.ConnectionEdgeAttr.Attributes.height);
                visualdata{itemID} = [x,y,width,height];
            end
            
        end
    elseif strcmpi(char(fieldnames(visualThingSet{i}.ThingData)),'VisualProcess')
        % code to process if visual process
        itemID = str2double(visualThingSet{i}.ThingData.VisualProcess.InstanceAttr.Attributes.entityId);   % id location
        x = str2double(visualThingSet{i}.ThingData.VisualProcess.ConnectionEdgeAttr.Attributes.x);
        y = str2double(visualThingSet{i}.ThingData.VisualProcess.ConnectionEdgeAttr.Attributes.y);
        width = str2double(visualThingSet{i}.ThingData.VisualProcess.ConnectionEdgeAttr.Attributes.width);
        height = str2double(visualThingSet{i}.ThingData.VisualProcess.ConnectionEdgeAttr.Attributes.height);
        visualdata{itemID} = [x,y,width,height];
    else
        % Other conditions
        error('Error in extracting visual data for objects, processes, and/or states')
    end

end

% Cycle through all fundamental relations (structural relations)
% Fundamental Relations Dataset
FundamentalRelationsSet = fileMap.OPX.OPMSystem.VisualPart.OPD.FundamentalRelationSection.CommonPart;

for i = 1:length(FundamentalRelationsSet)
        
    SourceConnectionSide = str2double(FundamentalRelationsSet{i}.Attributes.sourceConnectionSide);
    SourceConnectionParameter = str2double(FundamentalRelationsSet{i}.Attributes.sourceConnectionParameter);
    symbol_x = str2double(FundamentalRelationsSet{i}.Attributes.x);
    symbol_y = str2double(FundamentalRelationsSet{i}.Attributes.y);
    symbol_width = str2double(FundamentalRelationsSet{i}.Attributes.width);
    symbol_height = str2double(FundamentalRelationsSet{i}.Attributes.height);
    
    % For each destination node connected to the current source node
    
    if length(FundamentalRelationsSet{i}.VisualFundamentalRelation) == 1
        itemID = str2double(FundamentalRelationsSet{i}.VisualFundamentalRelation.InstanceAttr.Attributes.entityId);
        DestinationConnectionSide = str2double(FundamentalRelationsSet{i}.VisualFundamentalRelation.Attributes.destinationSide);
        DestinationConnectionParameter = str2double(FundamentalRelationsSet{i}.VisualFundamentalRelation.Attributes.destinationParameter);
        
        visualdata{itemID} = [SourceConnectionSide,SourceConnectionParameter,...
            DestinationConnectionSide,DestinationConnectionParameter,symbol_x,symbol_y,...
            symbol_width,symbol_height];
    else
        % If more than one destination node
        for j = 1:length(FundamentalRelationsSet{i}.VisualFundamentalRelation)
            
            itemID = str2double(FundamentalRelationsSet{i}.VisualFundamentalRelation{j}.InstanceAttr.Attributes.entityId);
            DestinationConnectionSide = str2double(FundamentalRelationsSet{i}.VisualFundamentalRelation{j}.Attributes.destinationSide);
            DestinationConnectionParameter = str2double(FundamentalRelationsSet{i}.VisualFundamentalRelation{j}.Attributes.destinationParameter);
            
            visualdata{itemID} = [SourceConnectionSide,SourceConnectionParameter,...
                DestinationConnectionSide,DestinationConnectionParameter,symbol_x,symbol_y,...
                symbol_width,symbol_height];
        end
    end
end

% Visual Links (Procedural Links) Dataset
ProceduralLinksSet = fileMap.OPX.OPMSystem.VisualPart.OPD.VisualLinkSection.VisualLink;

for i = 1:length(ProceduralLinksSet)
    itemID = str2double(ProceduralLinksSet{i}.InstanceAttr.Attributes.entityId);
    SourceConnectionSide = str2double(ProceduralLinksSet{i}.LineAttr.Attributes.sourceConnectionSide);
    SourceConnectionParameter = str2double(ProceduralLinksSet{i}.LineAttr.Attributes.sourceConnectionParameter);
    DestinationConnectionSide = str2double(ProceduralLinksSet{i}.LineAttr.Attributes.destinationConnectionSide);
    DestinationConnectionParameter  = str2double(ProceduralLinksSet{i}.LineAttr.Attributes.destinationConnectionParameter);
    visualdata{itemID} = [SourceConnectionSide,SourceConnectionParameter,...
            DestinationConnectionSide,DestinationConnectionParameter];
end
    
% Other visual parts
%         <Unfolded />
%         <View />
%         <InZoomed />


%% Extract Objects
ObjectSet = fileMap.OPX.OPMSystem.LogicalStructure.ObjectSection.LogicalObject;
importedObjects = cell(length(ObjectSet),4);        % each row in object set is of the format ['Name',id,visualdata{id},StateSet]

for i = 1:length(ObjectSet)
    itemID = str2double(ObjectSet{i}.EntityAttr.Attributes.id);
    name = ObjectSet{i}.EntityAttr.OPMProperties.Property{1}.Attributes.value;
    
    % Search for state information
    % If state exists
    if cell2mat(strfind(fieldnames(ObjectSet{i}),'LogicalState')) == 1
        
        % For each state
        stateData = cell(length(ObjectSet{i}.LogicalState),3);
        for j = 1:length(ObjectSet{i}.LogicalState)
            stateID = str2double(ObjectSet{i}.LogicalState{j}.EntityAttr.Attributes.id);
            stateName = ObjectSet{i}.LogicalState{j}.EntityAttr.OPMProperties.Property{1}.Attributes.value;
            stateData(j,:) = {stateName,stateID,visualdata{stateID}};
        end
        importedObjects(i,:) = {name,itemID,visualdata{itemID},stateData};
    else %if no states exist
        importedObjects(i,:) = {name,itemID,visualdata{itemID},{}};
    end
    
end

%% Extract Processes
ProcessSet = fileMap.OPX.OPMSystem.LogicalStructure.ProcessSection.LogicalProcess;
importedProcesses = cell(length(ProcessSet),3);        % each row in object set is of the format ['Name',id,visualdata{id}]

for i = 1:length(ProcessSet)
    itemID = str2double(ProcessSet{i}.EntityAttr.Attributes.id);
    name = ProcessSet{i}.EntityAttr.OPMProperties.Property{2}.Attributes.value;
    importedProcesses(i,:) = {name,itemID,visualdata{itemID}};
end

%% Extract Structural Relations
RelationSet = fileMap.OPX.OPMSystem.LogicalStructure.RelationSection.LogicalRelation;
importedRelations = cell(length(RelationSet),5);        % each row in object set is of the format ['Type',id,SourceID,DestinationID,visualdata{id}]

for i = 1:length(RelationSet)
    
    % Determine what type of structural relation
    if str2double(RelationSet{i}.Attributes.relationType) == 201
        relationType = 'Specialization';
    elseif str2double(RelationSet{i}.Attributes.relationType) == 202
        relationType = 'Exhibition';
    elseif str2double(RelationSet{i}.Attributes.relationType) == 203
        relationType = 'Instantiation';
    elseif str2double(RelationSet{i}.Attributes.relationType) == 204
        relationType = 'Aggregation';
    elseif str2double(RelationSet{i}.Attributes.relationType) == 205
        relationType = 'Unidirectional';
    elseif str2double(RelationSet{i}.Attributes.relationType) == 206
        relationType = 'Bidirectional';
    end
    
    itemID = str2double(RelationSet{i}.EntityAttr.Attributes.id);
    SourceID = str2double(RelationSet{i}.Attributes.sourceId);
    DestinationID = str2double(RelationSet{i}.Attributes.destinationId);
    importedRelations(i,:) = {relationType,itemID,SourceID,DestinationID,visualdata{itemID}};
    
end

%% Extract Procedural Links
LinkSet = fileMap.OPX.OPMSystem.LogicalStructure.LinkSection.LogicalLink;
importedLinks = cell(length(LinkSet),5);        % each row in object set is of the format ['Type',id,SourceID,DestinationID,visualdata{id}]

for i = 1:length(LinkSet)
    
    % Determine what type of structural relation
    if str2double(LinkSet{i}.Attributes.linkType) == 301
        linkType = 'Consumption';
    elseif str2double(LinkSet{i}.Attributes.linkType) == 302
        linkType = 'Effect';
    elseif str2double(LinkSet{i}.Attributes.linkType) == 303
        linkType = 'Instrument';
    elseif str2double(LinkSet{i}.Attributes.linkType) == 304
        linkType = 'Condition';
    elseif str2double(LinkSet{i}.Attributes.linkType) == 305
        linkType = 'Agent';
    elseif str2double(LinkSet{i}.Attributes.linkType) == 306
        linkType = 'Result';
    elseif str2double(LinkSet{i}.Attributes.linkType) == 307
        linkType = 'Invocation';
    elseif str2double(LinkSet{i}.Attributes.linkType) == 308
        linkType = 'Instrument Event';
    elseif str2double(LinkSet{i}.Attributes.linkType) == 309
        linkType = 'Exception';
    elseif str2double(LinkSet{i}.Attributes.linkType) == 310
        linkType = 'Consumption Event';
    end
    
    itemID = str2double(LinkSet{i}.EntityAttr.Attributes.id);
    SourceID = str2double(LinkSet{i}.Attributes.sourceId);
    DestinationID = str2double(LinkSet{i}.Attributes.destinationId);
    importedLinks(i,:) = {linkType,itemID,SourceID,DestinationID,visualdata{itemID}};
    
end
        
     
VisualData = visualdata;
outputStruct.objects = importedObjects;
outputStruct.processes = importedProcesses;
outputStruct.structuralRelations = importedRelations;
outputStruct.proceduralLinks = importedLinks;
        
        
        
        
        
        
        
        
        