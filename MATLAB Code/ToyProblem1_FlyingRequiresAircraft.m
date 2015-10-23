%% OPM MATLAB TOOLBOX - TOY PROBLEM 1
% By: Sydney Do (sydneydo@mit.edu)
% Date Created: October 18, 2015
% Last Updated: October 18, 2015

%% Order of Operations
% 1. Read opz file
% 2. Insert relevant components of opz file (objects, processes, structural
% relations, and procedural links) into relevant classes in MATLAB
% 3. Render OPM in MATLAB figure

%% Code

% outputStruct = opzReader(opzFile);
% % outputStruct.objects       % Includes object ids
% % outputStruct.processes     
% % outputStruct.structuralRelations
% % outputStruct.proceduralLinks

% clear all
clc

%% Toy problems
% Hardcode input

testCase = 8;

switch testCase
    
    case 1 % Flying Requires Aircraft

        VisualData = cell(1,9);
        VisualData{1} = [444,271,135,154];
        VisualData{2} = [712,320,128,64];
        VisualData{3} = [4,0.78125,4,0.2628205];
        VisualData{8} = [712,504,128,64];
        VisualData{9} = [1,0.8,4,0.4903846];
        
        % VisualData for objects and processes
        % [x,y,width,height]
        
        % VisualData for procedural links
        % [SourceConnectionSide, SourceConnectionParameter,
        % DestinationConnectionSide, DestinationConnectionParameter]
        
        outputStruct.objects = {'a1',8,VisualData{8},{};'Aircraft',2,VisualData{2},{}};
        outputStruct.processes = {'Flying',1,VisualData{1}};
        outputStruct.structuralRelations = [];      % Each row is a from-to link to connect objects and processes
        outputStruct.proceduralLinks = {'Instrument',9,8,1,VisualData{9};'Instrument',3,2,1,VisualData{3}};       % Each row is a from-to link to connect objects and processes
        
        % Error checking code to ensure that id numbers declared in procedural
        % links are valid id numbers of objects and processes

    case 2 % Eating Consumes Food
        
        VisualData = cell(1,19);
        VisualData{1} = [856,440,136,80];
        VisualData{2} = [1168,520,136,80];
        VisualData{4} = [1160,392,136,80];
        VisualData{14} = [7,0.5,4,1];
        VisualData{15} = [7,0.2820513,4,0.958348];
        VisualData{16} = [591,376,225,64];
        VisualData{17} = [1,0.09132279,4,0.6736111];
        VisualData{18} = [719,595,134,152];
        VisualData{19} = [7,0.3223684,4,0.30442667];
        
        % VisualData for objects and processes
        % [x,y,width,height]
        
        % VisualData for procedural links
        % [SourceConnectionSide, SourceConnectionParameter,
        % DestinationConnectionSide, DestinationConnectionParameter]
        
        % Note that if input text has "&#xA;", this equivalent to starting
        % a new line between the words
        outputStruct.objects = {{'Satiation','Level'},18,VisualData{18},StateData{1};'Food',4,VisualData{4},{};'Mouth',2,VisualData{2},{};'Waste',16,VisualData{16},{}};
        outputStruct.processes = {'Eating',1,VisualData{1}};
        outputStruct.structuralRelations = [];      % Each row is a from-to link to connect objects and processes
        outputStruct.proceduralLinks = {'Effect',19,18,1,VisualData{19};'Result',17,1,16,VisualData{17};'Instrument',15,2,1,VisualData{15};'Consumption',14,4,1,VisualData{14}};       % Each row is a from-to link to connect objects and processes

    case 3 % PersonHasStates.opx
        
        VisualData = cell(1,3);
        VisualData{1} = [960,314,204,92];
        VisualData{2} = [20,42,72,42];
        VisualData{3} = [112,42,72,42];

        
        % VisualData for objects and processes
        % [x,y,width,height]
        
        % VisualData for procedural links
        % [SourceConnectionSide, SourceConnectionParameter,
        % DestinationConnectionSide, DestinationConnectionParameter]
        
        
        StateData{1} = {'Full',3,VisualData{3};'Hungry',2,VisualData{2}};       
        
        % Note that if input text has "&#xA;", this equivalent to starting
        % a new line between the words
        outputStruct.objects = {'Person',1,VisualData{1},StateData{1}};
        outputStruct.processes = [];
        outputStruct.structuralRelations = [];      % Each row is a from-to link to connect objects and processes
        outputStruct.proceduralLinks = [];

    case 4 % PersonEatingWithStates.opx
        
        VisualData = cell(1,3);
        VisualData{1} = [960,314,204,92];
        VisualData{2} = [20,42,72,42];
        VisualData{3} = [112,42,72,42];
        VisualData{4} = [1008,499,111,72];       
        VisualData{6} = [1,0.58887976,8,0.30952382];
        VisualData{7} = [4,0.31944445,1,0.40527108];
        
        % VisualData for objects and processes
        % [x,y,width,height]
        
        % VisualData for procedural links
        % [SourceConnectionSide, SourceConnectionParameter,
        % DestinationConnectionSide, DestinationConnectionParameter]
        
        
        StateData{1} = {'Full',3,VisualData{3};'Hungry',2,VisualData{2}};
                
        % Note that if input text has "&#xA;", this equivalent to starting
        % a new line between the words
        outputStruct.objects = {'Person',1,VisualData{1},StateData{1}};
        outputStruct.processes = {'Feeding',4,VisualData{4}};
        outputStruct.structuralRelations = {};      % Each row is a from-to link to connect objects and processes
        outputStruct.proceduralLinks = {'Consumption',7,2,4,VisualData{7}; 'Result',6,4,3,VisualData{6}};
        
    case 5  % SpaceShuttle.opx - Decomposition Link example
        
        VisualData = cell(1,3);
        VisualData{1} = [1185,286,240,80];
        VisualData{2} = [961,595,144,80];
        VisualData{3} = [1177,657,240,80];
        VisualData{4} = [1459,504,368,80];       
        VisualData{5} = [4,0.425,1,0.5,1211,471,56,48];
        VisualData{6} = [4,0.425,1,0.5,1211,471,56,48];
        VisualData{7} = [4,0.425,1,0.5,1211,471,56,48];
        
        % VisualData for objects and processes
        % [x,y,width,height]
        
        % VisualData for procedural links
        % [SourceConnectionSide, SourceConnectionParameter,
        % DestinationConnectionSide, DestinationConnectionParameter]
        
        % VisualData for structural links
        % [SourceConnectionSide, SourceConnectionParameter,
        % DestinationConnectionSide, DestinationConnectionParameter,
        % symbol_x, symbol_y, symbol_width, symbol_height]
        % Note that symbol_x and symbol_y denote the top left hand corner
        % of the triangle in structural relations
                        
        % Note that if input text has "&#xA;", this equivalent to starting
        % a new line between the words
        outputStruct.objects = {'Solid Rocket Boosters',4,VisualData{4},{};'External Tank',3,VisualData{3},{};...
            'Orbiter',2,VisualData{2},{};'Space Shuttle',1,VisualData{1},{}};
        outputStruct.processes = {};
        outputStruct.structuralRelations = {'Aggregation',7,1,2,VisualData{7};'Aggregation',6,1,4,VisualData{6};...
            'Aggregation',5,1,3,VisualData{5}};      % Each row is a from-to link to connect objects and processes
        outputStruct.proceduralLinks = {};
        
    case 6      % HumanAttributes.opx - testing exhibition structural relation for both processes and objects
        
        VisualData = cell(1,8);
        VisualData{1} = [819,278,108,62];
        VisualData{2} = [886,389,153,97];
        VisualData{3} = [908,510,108,62];
        VisualData{4} = [907,591,108,62];       
        VisualData{5} = [];
        VisualData{6} = [4,0.5,7,0.54761904,807,363,42,36];
        VisualData{7} = [4,0.5,7,0.54761904,807,363,42,36];
        VisualData{8} = [4,0.5,1,0.067961164,807,363,42,36];
        
        % VisualData for objects and processes
        % [x,y,width,height]
        
        % VisualData for procedural links
        % [SourceConnectionSide, SourceConnectionParameter,
        % DestinationConnectionSide, DestinationConnectionParameter]
        
        % VisualData for structural links
        % [SourceConnectionSide, SourceConnectionParameter,
        % DestinationConnectionSide, DestinationConnectionParameter,
        % symbol_x, symbol_y, symbol_width, symbol_height]
        % Note that symbol_x and symbol_y denote the top left hand corner
        % of the triangle in structural relations
                        
        % Note that if input text has "&#xA;", this equivalent to starting
        % a new line between the words
        outputStruct.objects = {'Weight',4,VisualData{4},{};'Age',3,VisualData{3},{};...
            'Human',1,VisualData{1},{}};
        outputStruct.processes = {'Metabolizing',2,VisualData{2}};
        outputStruct.structuralRelations = {'Exhibition',8,1,2,VisualData{8};'Exhibition',7,1,3,VisualData{7};...
            'Exhibition',6,1,4,VisualData{6}};      % Each row is a from-to link to connect objects and processes
        outputStruct.proceduralLinks = {};
        
    case 7      % TypesOfCooking.opx - testing specialization structural relation for processes
        
        VisualData = cell(1,8);
        VisualData{4} = [1160,136,164,107];
        VisualData{8} = [877,230,162,95];
        VisualData{9} = [1105,323,228,137];
        VisualData{10} = [1393,309,126,126];       
        VisualData{5} = [];
        VisualData{13} = [4,0.5,1,0.5,1275,288,64,55];
        VisualData{12} = [4,0.5,1,0.5,1275,288,64,55];
        VisualData{11} = [4,0.5,1,0.5,1275,288,64,55];
        
        % VisualData for objects and processes
        % [x,y,width,height]
        
        % VisualData for procedural links
        % [SourceConnectionSide, SourceConnectionParameter,
        % DestinationConnectionSide, DestinationConnectionParameter]
        
        % VisualData for structural links
        % [SourceConnectionSide, SourceConnectionParameter,
        % DestinationConnectionSide, DestinationConnectionParameter,
        % symbol_x, symbol_y, symbol_width, symbol_height]
        % Note that symbol_x and symbol_y denote the top left hand corner
        % of the triangle in structural relations
                        
        % Note that if input text has "&#xA;", this equivalent to starting
        % a new line between the words
        outputStruct.objects = {};
        outputStruct.processes = {'frying',10,VisualData{10};'steaming',9,VisualData{9};...
            'baking',8,VisualData{8};'cooking',4,VisualData{4}};
        outputStruct.structuralRelations = {'Specialization',13,4,8,VisualData{13};'Specialization',12,4,10,VisualData{12};...
            'Specialization',11,4,9,VisualData{11}};      % Each row is a from-to link to connect objects and processes
        outputStruct.proceduralLinks = {};
        
    case 8
end

%% Initialize Objects Arrays
object = OPMobject.empty(0,size(outputStruct.objects,1));

% Create Objects
% objectIDs = [outputStruct.objects{:,2}];

c = 0;
states = OPMstate.empty(0,length(VisualData));
for i = 1:size(outputStruct.objects,1)
    
    % If there is state information associated with the object
    if ~isempty(outputStruct.objects{i,4})
        StateSet = OPMstate.empty(0,size(outputStruct.objects{i,4},1));
        
        % Build array of states
        for j = 1:size(outputStruct.objects{i,4},1)
            StateSet(j) = OPMstate(outputStruct.objects{i,4}{j,1},outputStruct.objects{i,4}{j,2},outputStruct.objects{i,4}{j,3},outputStruct.objects{i,3});
            c = c+1;
            states(c) = StateSet(j);    
        end
        
        object(i) = OPMobject(outputStruct.objects{i,1},outputStruct.objects{i,2},outputStruct.objects{i,3},StateSet);
        
    else
        object(i) = OPMobject(outputStruct.objects{i,1},outputStruct.objects{i,2},outputStruct.objects{i,3},OPMstate.empty(0));
    end
    
end

%% Initialize Processes Arrays
proc = OPMprocess.empty(0,size(outputStruct.processes,1));

% Create Processes
for i = 1:size(outputStruct.processes,1)
    proc(i) = OPMprocess(outputStruct.processes{i,1},outputStruct.processes{i,2},outputStruct.processes{i,3});
end

%% Initialize Procedural Links
procLink = OPMproceduralLink.empty(0,size(outputStruct.proceduralLinks,1));

% Create Procedural Links
for i = 1:size(outputStruct.proceduralLinks,1)
    % For each procedural link, find source and destination entities
    if strcmpi(outputStruct.proceduralLinks{i,1},'Instrument') ||...
            strcmpi(outputStruct.proceduralLinks{i,1},'Agent') ||...
            strcmpi(outputStruct.proceduralLinks{i,1},'Consumption')
        % SourceNode must be an object and DestinationNode must be a
        % process
   
        % If states present in objects, determine whether or not SourceNode
        % is connected to a state
        [~,ind] = max([~isempty(find([object.id]==outputStruct.proceduralLinks{i,3},1)),...
            ~isempty(find([states.id]==outputStruct.proceduralLinks{i,3},1))]);
        
        if ind == 1
            % If SourceNode is an object
            procLink(i) = OPMproceduralLink(outputStruct.proceduralLinks{i,1},outputStruct.proceduralLinks{i,2},...
                object(find([object.id]==outputStruct.proceduralLinks{i,3},1)),...
                proc(find([proc.id]==outputStruct.proceduralLinks{i,4},1)),outputStruct.proceduralLinks{i,5});
        elseif ind == 2
            % If SourceNode is a state
            procLink(i) = OPMproceduralLink(outputStruct.proceduralLinks{i,1},outputStruct.proceduralLinks{i,2},...
                states(find([states.id]==outputStruct.proceduralLinks{i,3},1)),...
                proc(find([proc.id]==outputStruct.proceduralLinks{i,4},1)),outputStruct.proceduralLinks{i,5}); 
        else
            % Error
            error(['Problem with extraction of ',outputStruct.proceduralLinks{i,1},' link']);
        end
    
    elseif strcmpi(outputStruct.proceduralLinks{i,1},'Result')
        % SourceNode must be a process and DestinationNode must be an
        % object
        
        % If states present in objects, determine whether or not DestinationNode
        % is connected to a state
        [~,ind] = max([~isempty(find([object.id]==outputStruct.proceduralLinks{i,4},1)),...
            ~isempty(find([states.id]==outputStruct.proceduralLinks{i,4},1))]);
        
        if ind == 1
            % If DestinationNode is an object
            procLink(i) = OPMproceduralLink(outputStruct.proceduralLinks{i,1},outputStruct.proceduralLinks{i,2},...
                proc(find([proc.id]==outputStruct.proceduralLinks{i,3},1)),...
                object(find([object.id]==outputStruct.proceduralLinks{i,4},1)),outputStruct.proceduralLinks{i,5});    
        elseif ind == 2
            % If DestinationNode is a state
            procLink(i) = OPMproceduralLink(outputStruct.proceduralLinks{i,1},outputStruct.proceduralLinks{i,2},...
                proc(find([proc.id]==outputStruct.proceduralLinks{i,3},1)),...
                states(find([states.id]==outputStruct.proceduralLinks{i,4},1)),outputStruct.proceduralLinks{i,5});       
        else
            % Error
            error(['Problem with extraction of ',outputStruct.proceduralLinks{i,1},' link']);
        end
           
    elseif strcmpi(outputStruct.proceduralLinks{i,1},'Effect')
        % No constraints on whether SourceNode is a process or object.
        % DestinationNode must be opposite to that of SourceNode
        
        % Determine if source is a process (ind=1) or an object (ind=2)
        [~,ind] = max([~isempty(find([proc.id]==outputStruct.proceduralLinks{i,3},1)),...
            ~isempty(find([object.id]==outputStruct.proceduralLinks{i,3},1))]);
        
        if ind == 1
            % If SourceNode is a process
            procLink(i) = OPMproceduralLink(outputStruct.proceduralLinks{i,1},outputStruct.proceduralLinks{i,2},...
            proc(find([proc.id]==outputStruct.proceduralLinks{i,3},1)),...
            object(find([object.id]==outputStruct.proceduralLinks{i,4},1)),outputStruct.proceduralLinks{i,5});
        elseif ind == 2
            % If SourceNode is an object
            procLink(i) = OPMproceduralLink(outputStruct.proceduralLinks{i,1},outputStruct.proceduralLinks{i,2},...
            object(find([object.id]==outputStruct.proceduralLinks{i,3},1)),...
            proc(find([proc.id]==outputStruct.proceduralLinks{i,4},1)),outputStruct.proceduralLinks{i,5});
        else
            % Error
            error('Problem with extraction of Effect link');
        end
        
    else
        error(['Problem with extraction of procedural link ID: ',num2str(outputStruct.proceduralLinks{i,2})]);
    end
    
end

%% Initialize Structural Relations
structRel = OPMstructuralRelation.empty(0,size(outputStruct.structuralRelations,1));

% Create Structural Relations
for i = 1:size(outputStruct.structuralRelations,1)
    
    % If not exhibition link - all structural relations link to the same
    % type of thing
    if ~strcmpi(outputStruct.structuralRelations{i,1},'Exhibition')
        
        % Determine if source is an object (ind=1) or a process (ind=2)
        [~,ind] = max([~isempty(find([object.id]==outputStruct.structuralRelations{i,3},1)),...
            ~isempty(find([proc.id]==outputStruct.structuralRelations{i,3},1))]);
        
        if ind == 1
            % If SourceNode is an object
            structRel(i) = OPMstructuralRelation(outputStruct.structuralRelations{i,1},outputStruct.structuralRelations{i,2},...
                object(find([object.id]==outputStruct.structuralRelations{i,3},1)),...
                object(find([object.id]==outputStruct.structuralRelations{i,4},1)),outputStruct.structuralRelations{i,5});
        elseif ind == 2
            % If SourceNode is a process
            structRel(i) = OPMstructuralRelation(outputStruct.structuralRelations{i,1},outputStruct.structuralRelations{i,2},...
                proc(find([proc.id]==outputStruct.structuralRelations{i,3},1)),...
                proc(find([proc.id]==outputStruct.structuralRelations{i,4},1)),outputStruct.structuralRelations{i,5});
        else
            % Error
            error('Problem with extraction of Structural Relation');
        end
    
    else
        % Code for Exhibition relation
        % Determine if source is an object (ind=1) or a process (ind=2)
        [~,SourceInd] = max([~isempty(find([object.id]==outputStruct.structuralRelations{i,3},1)),...
            ~isempty(find([proc.id]==outputStruct.structuralRelations{i,3},1))]);
        
        % Determine if detination is an object (ind=1) or a process (ind=2)
        [~,DestinationInd] = max([~isempty(find([object.id]==outputStruct.structuralRelations{i,4},1)),...
            ~isempty(find([proc.id]==outputStruct.structuralRelations{i,4},1))]);
        
        % If source and destination are both objects
        if SourceInd == 1 && DestinationInd == 1
            structRel(i) = OPMstructuralRelation(outputStruct.structuralRelations{i,1},outputStruct.structuralRelations{i,2},...
                object(find([object.id]==outputStruct.structuralRelations{i,3},1)),...
                object(find([object.id]==outputStruct.structuralRelations{i,4},1)),outputStruct.structuralRelations{i,5});
        % If source is an object and destination is a process    
        elseif SourceInd == 1 && DestinationInd == 2
            structRel(i) = OPMstructuralRelation(outputStruct.structuralRelations{i,1},outputStruct.structuralRelations{i,2},...
                object(find([object.id]==outputStruct.structuralRelations{i,3},1)),...
                proc(find([proc.id]==outputStruct.structuralRelations{i,4},1)),outputStruct.structuralRelations{i,5});
        % If source is a process and destination is an object
        elseif SourceInd == 2 && DestinationInd == 1
            structRel(i) = OPMstructuralRelation(outputStruct.structuralRelations{i,1},outputStruct.structuralRelations{i,2},...
                proc(find([proc.id]==outputStruct.structuralRelations{i,3},1)),...
                object(find([object.id]==outputStruct.structuralRelations{i,4},1)),outputStruct.structuralRelations{i,5});
        % If source and destination are both processes
        elseif SourceInd == 2 && DestinationInd == 2
            
            structRel(i) = OPMstructuralRelation(outputStruct.structuralRelations{i,1},outputStruct.structuralRelations{i,2},...
                proc(find([proc.id]==outputStruct.structuralRelations{i,3},1)),...
                proc(find([proc.id]==outputStruct.structuralRelations{i,4},1)),outputStruct.structuralRelations{i,5});
        else
            % Error
            error('Problem with extraction of Exhibition Relation');
        end
    
    end
    
end

%% Visualization
% Driven by procedural links and structural relations

% Plotting
figure,
for i = 1:length(object)
    hold on
    object(i).plotOPD;
end

for j = 1:length(proc)
    hold on
    proc(j).plotOPD;
end

for k = 1:length(procLink)
    hold on
    procLink(k).plotOPD;
end

for m = 1:length(structRel)
    hold on
    structRel(m).plotOPD;
end


%% Random Scribbles

% % Default Opcat OPD Space
% defaultOPDwidth = 1300;
% defaultOPDheight = 860;
% 
% % Default Opcat FontSize
% defaultThingFontSize = 16;
% defaultStateFontSize = 12;
% defaultLabelFontSize = 11;      % For structural links
% 
% defaultFont = 'Helvetica';  % note that this is Matlab's default text font
% 
% % Default Opcat Colors
% thingBackgroundColor = [230, 230, 230]/255;
% objectGreen = [0, 110, 0]/255;
% processBlue = [0, 0, 170]/255;
% stateBrown = [91, 91, 0]/255;


