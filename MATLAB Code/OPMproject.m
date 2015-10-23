classdef OPMproject
    %OPMproject Code reads in an .opx file, imports it into MATLAB objects,
    %and contains a plot command for visualization
    %   By: Sydney Do (sydneydo@mit.edu)
    %   Date Created: 10/23/2015
    %   Last Updated: 10/23/2015
    
    properties
        ProjectName
        Author
        Filename
        ObjectSummary
        Objects
        ProcessSummary
        Processes
        StructuralRelationSummary
        StructuralRelations
        ProceduralLinkSummary
        ProceduralLinks
    end
    
    properties (SetAccess = private)
        VisualData
    end
    
    methods
        %% Constructor
        function obj = OPMproject(filename)
            
            %% Check to ensure that file extension is .opx
            if ~strcmpi(filename(end-3:end),'.opx')
                error('File extension must be .opx')
            end
            
            obj.Filename = filename;
            
            % Convert file from xml to struct
            fileMap = xml2struct(filename);
            
            %% General Project Information
            obj.ProjectName = fileMap.OPX.OPMSystem.Attributes.name;
            obj.Author = fileMap.OPX.OPMSystem.Attributes.author;
            
            %% Extract Visual Data for all elements
            % Initialize visual data set
            visualdata = cell(1,200);   % Arbitrarily assign 200 elements to visualdata cell
            
            % Visual Things Dataset
            visualThingSet = fileMap.OPX.OPMSystem.VisualPart.OPD.ThingSection.VisualThing;
            
            % Cycle through all visual things (objects, processes, states)
            for i = 1:length(visualThingSet)
                if strcmpi(char(fieldnames(visualThingSet{i}.ThingData)),'VisualObject')
                    
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
            
            %% Extract all OPM Elements
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
            
            %% Save imported data into summary properties
            obj.ObjectSummary = importedObjects;
            obj.ProcessSummary = importedProcesses;
            obj.StructuralRelationSummary = importedRelations;
            obj.ProceduralLinkSummary = importedLinks;
            obj.VisualData = visualdata;
            
            %% Create Struct for conversion of imported data into MATLAB objects
            
            %% Initialize Objects Arrays
            object = OPMobject.empty(0,size(importedObjects,1));
                        
            c = 0;
            states = OPMstate.empty(0,length(visualdata));
            for i = 1:size(importedObjects,1)
                
                % If there is state information associated with the object
                if ~isempty(importedObjects{i,4})
                    StateSet = OPMstate.empty(0,size(importedObjects{i,4},1));
                    
                    % Build array of states
                    for j = 1:size(importedObjects{i,4},1)
                        StateSet(j) = OPMstate(importedObjects{i,4}{j,1},importedObjects{i,4}{j,2},importedObjects{i,4}{j,3},importedObjects{i,3});
                        c = c+1;
                        states(c) = StateSet(j);
                    end
                    
                    object(i) = OPMobject(importedObjects{i,1},importedObjects{i,2},importedObjects{i,3},StateSet);
                    
                else
                    object(i) = OPMobject(importedObjects{i,1},importedObjects{i,2},importedObjects{i,3},OPMstate.empty(0));
                end
                
            end
            
            %% Initialize Processes Arrays
            proc = OPMprocess.empty(0,size(importedProcesses,1));
            
            % Create Processes
            for i = 1:size(importedProcesses,1)
                proc(i) = OPMprocess(importedProcesses{i,1},importedProcesses{i,2},importedProcesses{i,3});
            end
            
            %% Initialize Procedural Links
            procLink = OPMproceduralLink.empty(0,size(importedLinks,1));
            
            % Create Procedural Links
            for i = 1:size(importedLinks,1)
                % For each procedural link, find source and destination entities
                if strcmpi(importedLinks{i,1},'Instrument') ||...
                        strcmpi(importedLinks{i,1},'Agent') ||...
                        strcmpi(importedLinks{i,1},'Consumption')
                    % SourceNode must be an object and DestinationNode must be a
                    % process
                    
                    % If states present in objects, determine whether or not SourceNode
                    % is connected to a state
                    [~,ind] = max([~isempty(find([object.id]==importedLinks{i,3},1)),...
                        ~isempty(find([states.id]==importedLinks{i,3},1))]);
                    
                    if ind == 1
                        % If SourceNode is an object
                        procLink(i) = OPMproceduralLink(importedLinks{i,1},importedLinks{i,2},...
                            object(find([object.id]==importedLinks{i,3},1)),...
                            proc(find([proc.id]==importedLinks{i,4},1)),importedLinks{i,5});
                    elseif ind == 2
                        % If SourceNode is a state
                        procLink(i) = OPMproceduralLink(importedLinks{i,1},importedLinks{i,2},...
                            states(find([states.id]==importedLinks{i,3},1)),...
                            proc(find([proc.id]==importedLinks{i,4},1)),importedLinks{i,5});
                    else
                        % Error
                        error(['Problem with extraction of ',importedLinks{i,1},' link']);
                    end
                    
                elseif strcmpi(importedLinks{i,1},'Result')
                    % SourceNode must be a process and DestinationNode must be an
                    % object
                    
                    % If states present in objects, determine whether or not DestinationNode
                    % is connected to a state
                    [~,ind] = max([~isempty(find([object.id]==importedLinks{i,4},1)),...
                        ~isempty(find([states.id]==importedLinks{i,4},1))]);
                    
                    if ind == 1
                        % If DestinationNode is an object
                        procLink(i) = OPMproceduralLink(importedLinks{i,1},importedLinks{i,2},...
                            proc(find([proc.id]==importedLinks{i,3},1)),...
                            object(find([object.id]==importedLinks{i,4},1)),importedLinks{i,5});
                    elseif ind == 2
                        % If DestinationNode is a state
                        procLink(i) = OPMproceduralLink(importedLinks{i,1},importedLinks{i,2},...
                            proc(find([proc.id]==importedLinks{i,3},1)),...
                            states(find([states.id]==importedLinks{i,4},1)),importedLinks{i,5});
                    else
                        % Error
                        error(['Problem with extraction of ',importedLinks{i,1},' link']);
                    end
                    
                elseif strcmpi(importedLinks{i,1},'Effect')
                    % No constraints on whether SourceNode is a process or object.
                    % DestinationNode must be opposite to that of SourceNode
                    
                    % Determine if source is a process (ind=1) or an object (ind=2)
                    [~,ind] = max([~isempty(find([proc.id]==importedLinks{i,3},1)),...
                        ~isempty(find([object.id]==importedLinks{i,3},1))]);
                    
                    if ind == 1
                        % If SourceNode is a process
                        procLink(i) = OPMproceduralLink(importedLinks{i,1},importedLinks{i,2},...
                            proc(find([proc.id]==importedLinks{i,3},1)),...
                            object(find([object.id]==importedLinks{i,4},1)),importedLinks{i,5});
                    elseif ind == 2
                        % If SourceNode is an object
                        procLink(i) = OPMproceduralLink(importedLinks{i,1},importedLinks{i,2},...
                            object(find([object.id]==importedLinks{i,3},1)),...
                            proc(find([proc.id]==importedLinks{i,4},1)),importedLinks{i,5});
                    else
                        % Error
                        error('Problem with extraction of Effect link');
                    end
                    
                else
                    error(['Problem with extraction of procedural link ID: ',num2str(importedLinks{i,2})]);
                end
                
            end
            
            %% Initialize Structural Relations
            structRel = OPMstructuralRelation.empty(0,size(importedRelations,1));
            
            % Create Structural Relations
            for i = 1:size(importedRelations,1)
                
                % If not exhibition link - all structural relations link to the same
                % type of thing
                if ~strcmpi(importedRelations{i,1},'Exhibition')
                    
                    % Determine if source is an object (ind=1) or a process (ind=2)
                    [~,ind] = max([~isempty(find([object.id]==importedRelations{i,3},1)),...
                        ~isempty(find([proc.id]==importedRelations{i,3},1))]);
                    
                    if ind == 1
                        % If SourceNode is an object
                        structRel(i) = OPMstructuralRelation(importedRelations{i,1},importedRelations{i,2},...
                            object(find([object.id]==importedRelations{i,3},1)),...
                            object(find([object.id]==importedRelations{i,4},1)),importedRelations{i,5});
                    elseif ind == 2
                        % If SourceNode is a process
                        structRel(i) = OPMstructuralRelation(importedRelations{i,1},importedRelations{i,2},...
                            proc(find([proc.id]==importedRelations{i,3},1)),...
                            proc(find([proc.id]==importedRelations{i,4},1)),importedRelations{i,5});
                    else
                        % Error
                        error('Problem with extraction of Structural Relation');
                    end
                    
                else
                    % Code for Exhibition relation
                    % Determine if source is an object (ind=1) or a process (ind=2)
                    [~,SourceInd] = max([~isempty(find([object.id]==importedRelations{i,3},1)),...
                        ~isempty(find([proc.id]==importedRelations{i,3},1))]);
                    
                    % Determine if detination is an object (ind=1) or a process (ind=2)
                    [~,DestinationInd] = max([~isempty(find([object.id]==importedRelations{i,4},1)),...
                        ~isempty(find([proc.id]==importedRelations{i,4},1))]);
                    
                    % If source and destination are both objects
                    if SourceInd == 1 && DestinationInd == 1
                        structRel(i) = OPMstructuralRelation(importedRelations{i,1},importedRelations{i,2},...
                            object(find([object.id]==importedRelations{i,3},1)),...
                            object(find([object.id]==importedRelations{i,4},1)),importedRelations{i,5});
                        % If source is an object and destination is a process
                    elseif SourceInd == 1 && DestinationInd == 2
                        structRel(i) = OPMstructuralRelation(importedRelations{i,1},importedRelations{i,2},...
                            object(find([object.id]==importedRelations{i,3},1)),...
                            proc(find([proc.id]==importedRelations{i,4},1)),importedRelations{i,5});
                        % If source is a process and destination is an object
                    elseif SourceInd == 2 && DestinationInd == 1
                        structRel(i) = OPMstructuralRelation(importedRelations{i,1},importedRelations{i,2},...
                            proc(find([proc.id]==importedRelations{i,3},1)),...
                            object(find([object.id]==importedRelations{i,4},1)),importedRelations{i,5});
                        % If source and destination are both processes
                    elseif SourceInd == 2 && DestinationInd == 2
                        
                        structRel(i) = OPMstructuralRelation(importedRelations{i,1},importedRelations{i,2},...
                            proc(find([proc.id]==importedRelations{i,3},1)),...
                            proc(find([proc.id]==importedRelations{i,4},1)),importedRelations{i,5});
                    else
                        % Error
                        error('Problem with extraction of Exhibition Relation');
                    end
                    
                end
                
            end
            
            %% Assign MATLAB converted elements to properties of OPMproject
            obj.Objects = object;
            obj.Processes = proc;
            obj.StructuralRelations = structRel;
            obj.ProceduralLinks = procLink; 
            
        end
        
        function obj = plotOPD(obj)
            figure,
            for i = 1:length(obj.Objects)
                hold on
                obj.Objects(i).plotOPD;
            end
            
            for j = 1:length(obj.Processes)
                hold on
                obj.Processes(j).plotOPD;
            end
            
            for k = 1:length(obj.StructuralRelations)
                hold on
                obj.StructuralRelations(k).plotOPD;
            end
            
            for m = 1:length(obj.ProceduralLinks)
                hold on
                obj.ProceduralLinks(m).plotOPD;
            end
            
            
        end
        
        
    end
    
end

