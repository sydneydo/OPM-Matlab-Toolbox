classdef OPMstructuralRelation < handle
    % By: Sydney Do
    % Date created: October 21, 2015
    % Last modified: October 21, 2015
    
    % SUMMARY
    % Structural Relation class for MATLAB OPM Toolbox
    % Types of Structural Relations within OPM include: specialization,
    % exhibition, instantiation, aggregation, uni-directional, and
    % bi-direction relations
        
    properties
        type
        id
        SourceNode
        SourceID
        SourceName
        SourceConnectionSide
        SourceConnectionParameter
        DestinationNode
        DestinationID
        DestinationName
        DestinationConnectionSide
        DestinationConnectionParameter
        SymbolX
        SymbolY
        SymbolWidth
        SymbolHeight
        environment = 0
        physical = 0
    end
    
    methods
        %% Constructor
        function obj = OPMstructuralRelation(Type,ID,FromNode,ToNode,VisualData)
            
%             if nargin ~= 3
%                 error('Three input values required. Input format: proceduralLink(Type,Source Node,Destination Node)')
%             end
                
            % Control type of input type for link
            if ~(strcmpi(Type,'Specialization')||...
                    strcmpi(Type,'Exhibition')||...
                    strcmpi(Type,'Instantiation')||...
                    strcmpi(Type,'Aggregation')||...
                    strcmpi(Type,'Unidirectional')||...
                    strcmpi(Type,'Bidirectional'))
                error('Link type must be one of the following: {Specialization, Exhibition, Instantiation, Aggregation, Unidirectional, Bidirectional}')
            end
                       
            obj.type = Type;
            obj.id = ID;
            
            obj.SourceNode = FromNode;
            obj.SourceID = FromNode.id;
            obj.SourceName = FromNode.name;
            obj.SourceConnectionSide = VisualData(1);
            obj.SourceConnectionParameter = VisualData(2);
            
            obj.DestinationNode = ToNode;
            obj.DestinationID = ToNode.id;
            obj.DestinationName = ToNode.name;            
            obj.DestinationConnectionSide = VisualData(3);
            obj.DestinationConnectionParameter = VisualData(4);
            
            % Add symbol visual data if not a unidirectional or
            % bidirectional relation
            if ~(strcmpi(Type,'Unidirectional')||...
                    strcmpi(Type,'Bidirectional'))
                obj.SymbolX = VisualData(5);
                obj.SymbolY = VisualData(6);
                obj.SymbolWidth = VisualData(7);
                obj.SymbolHeight = VisualData(8);
            end
            
            % Distribute Source and Destination Objects to appropriate
            % locations
            
            if strcmpi(Type,'Aggregation')
               % If aggregation link, distribute objects/processes to
               % appropriate parent/child attributes of each corresponding
               % object/process
               
               % Add DestinationNode as child of SourceNode
               obj.SourceNode.appendChildren(obj.DestinationNode);
               
               % Add SourceNode as psrent of DestinationNode
               obj.DestinationNode.appendParents(obj.SourceNode);
                
            elseif strcmpi(Type,'Exhibition')
                % add DestinationNode to the UniqueAttributes of the
                % SourceNode
                obj.SourceNode.appendUniqueProperties(obj.DestinationNode);
                
                
            elseif strcmpi(Type,'Specialization')
                % append parent and child classes
                % transfer unique attributes of source node to inherited
                % attributes of destination node - ensure zero value?
                
                % Add DestinationNode as child class of SourceNode
                obj.SourceNode.appendChildrenClasses(obj.DestinationNode);
                
                % Add SourceNode as psrent class of DestinationNode
                obj.DestinationNode.appendParentClasses(obj.SourceNode);
                
                % Transfer parent unique and inherited attributes to child node
                obj.DestinationNode.appendInheritedProperties(obj.SourceNode.UniqueAttributes);
                obj.DestinationNode.appendInheritedProperties(obj.SourceNode.InheritedAttributes);
                % Transfer parent unique and inherited operations to child node
                obj.DestinationNode.appendInheritedProperties(obj.SourceNode.UniqueOperations);
                obj.DestinationNode.appendInheritedProperties(obj.SourceNode.InheritedOperations);
                
                
            elseif strcmpi(Type,'Instantiation')
                % append instances property of source node
                % transfer unique attributes of source node to inherited
                % attributes of destination node - ensure zero value?

                % Identify instances in parent node
                obj.SourceNode.appendInstances(obj.DestinationNode);
                
                % Transfer parent unique and inherited attributes to child node
                obj.DestinationNode.appendInheritedProperties(obj.SourceNode.UniqueAttributes);
                obj.DestinationNode.appendInheritedProperties(obj.SourceNode.InheritedAttributes);
                % Transfer parent unique and inherited operations to child node
                obj.DestinationNode.appendInheritedProperties(obj.SourceNode.UniqueOperations);
                obj.DestinationNode.appendInheritedProperties(obj.SourceNode.InheritedOperations);
                
            end
            
            
        end
        
        %% Plotting Code
        % Baseline plot about (0,0)
        function obj = plotOPD(obj)           
           
            % Determine Connection Point of Source Node
            [x1,y1] = obj.findConnectionPoint(obj.SourceNode,obj.SourceConnectionSide,obj.SourceConnectionParameter);           
            
            % Determine Connection Point of Destination Node
            [x2,y2] = obj.findConnectionPoint(obj.DestinationNode,obj.DestinationConnectionSide,obj.DestinationConnectionParameter);
            
            % If not a unidirectional or bidirectional relation
            if ~(strcmpi(obj.type,'Unidirectional')||...
                    strcmpi(obj.type,'Bidirectional'))
                
                % Location of top vertex triangle symbol
                xTriTop = obj.SymbolX + obj.SymbolWidth/2;
                yTriTop = obj.SymbolY;
                
                % Location of center base of triangle symbol
                xTriBottom = obj.SymbolX + obj.SymbolWidth/2;
                yTriBottom = obj.SymbolY + obj.SymbolHeight;
                
                % Coordinates of Triangle
                xTriPlot = [obj.SymbolX + obj.SymbolWidth/2, obj.SymbolX + obj.SymbolWidth, obj.SymbolX];
                yTriPlot = [obj.SymbolY, obj.SymbolY + obj.SymbolHeight, obj.SymbolY + obj.SymbolHeight];
                
                %% Plot Triangle
                if strcmpi(obj.type,'Aggregation')
                
                    patch(xTriPlot,yTriPlot,'k','EdgeColor','k','LineWidth',2);
                    axis equal
                    hold on
                    set(gca,'YDir','reverse');
                    
                elseif strcmpi(obj.type,'Specialization')
                
                    patch(xTriPlot,yTriPlot,[230, 230, 230]/255,'EdgeColor','k','LineWidth',2);
                    axis equal
                    hold on
                    set(gca,'YDir','reverse');
                    
                elseif strcmpi(obj.type,'Exhibition')
                    
                    % Plot outside triangle
                    patch(xTriPlot,yTriPlot,[230, 230, 230]/255,'EdgeColor','k','LineWidth',2);
                    axis equal
                    hold on
                    
                    % Plot inside triangle
                    % determine centroid of triangle
                    xCentroid = mean(xTriPlot);
                    yCentroid = mean(yTriPlot);
                    
                    % Scale inner triangle vertices to occur halfway
                    % between centroid and outer triangle vertices
                    innerTriScale = 0.5;
                    innerAngleRatio = (yTriPlot(2)-yCentroid)/(obj.SymbolWidth/2);
                    lengthOfCentroidToBottomVertex = sqrt((xTriPlot(2)-xCentroid)^2+(yTriPlot(2)-yCentroid)^2);
                    
                    % Differential height of inner triangle vertex from
                    % centroid
                    % Derived using Pythagoras' theorem and angle ratio
                    dh = innerTriScale*lengthOfCentroidToBottomVertex/sqrt(1/innerAngleRatio^2+1);
                    
                    % Differential length of inner triangle vertex from
                    % centroid
                    dw = innerTriScale*lengthOfCentroidToBottomVertex/sqrt(1+innerAngleRatio^2);
                    
                    % Draw inner triangle
                    patch([xCentroid,xCentroid+dw,xCentroid-dw],...
                        [yCentroid-(yCentroid-yTriPlot(1))*innerTriScale,yCentroid+dh,yCentroid+dh],'k')
                    set(gca,'YDir','reverse');
                    
                elseif strcmpi(obj.type,'Instantiation')
                    
                    % Plot outside triangle
                    patch(xTriPlot,yTriPlot,[230, 230, 230]/255,'EdgeColor','k','LineWidth',2);
                    axis equal
                    hold on
                    
                    % Plot inside circle
                    % determine centroid of circle
                    xCentroid = mean(xTriPlot);
                    yCentroid = mean(yTriPlot);
                    
                    % Scale inner triangle vertices to occur halfway
                    % between centroid and outer triangle vertices
                    innerTriScale = 1/3.5;
                    circleRad = innerTriScale*(yCentroid-yTriPlot(1));
                    theta = linspace(0,2*pi*1.01,100);
                    
                    % Draw inner circle
                    patch(xCentroid+circleRad*cos(theta),yCentroid+circleRad*sin(theta),'k')
                    set(gca,'YDir','reverse');
                    
                else
                   % Other symbols for other conditions 
                end
                
                %% Bent Line between Source Node and Triangle
                
                % Shape of line is dependent on connection side of object
                % as well as position of triangle relative to object
                
                % If connection side is south, and triangle is beneath
                % source object
                  
                if strcmpi(class(obj.SourceNode),'OPMobject')
                
                    % For a SourceNode connection point on the south side
                    if obj.SourceConnectionSide == 4
                        
                        % if connection point on source element is above top of
                        % triangle
                        if yTriTop >= y1
                            vertMidway = (yTriTop+y1)/2;
                            
                            % Draw bent line
                            line([x1,x1,xTriTop,xTriTop],[y1,vertMidway,vertMidway,yTriTop],'Color','k','LineWidth',2);
                            
                        else
                            % Determine horizontal midway point
                            horizMidway = (xTriTop+x1)/2;
                            
                            % Bent line from source node to top of triangle
                            % Note that a buffer of 16 units exists beneath the
                            % object, and a buffer of 24 units exists above the
                            % triangle
                            sourceLowerBuffer = 16;
                            triUpperBuffer = 24;
                            
                            line([x1,x1,horizMidway,horizMidway,xTriTop,xTriTop],...
                                [y1,y1+sourceLowerBuffer,y1+sourceLowerBuffer,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],...
                                'Color','k','LineWidth',2);
                        end
                        
                        % For a SourceNode connection point on the north side
                    elseif obj.SourceConnectionSide == 1
                        
                        sourceUpperBuffer = 16;
                        triUpperBuffer = 24;
                        
                        % if connection point on source element is above top of
                        % triangle
                        if yTriTop >= y1
                            line([x1,x1,xTriTop,xTriTop],[y1,y1-sourceUpperBuffer,y1-sourceUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        else
                            line([x1,x1,xTriTop,xTriTop],[y1,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        end
                        
                        % For a SourceNode connection point on the east side
                    elseif obj.SourceConnectionSide == 8
                        
                        sourceEastBuffer = 16;
                        triUpperBuffer = 24;
                        
                        % if connection point on source element is above top of
                        % triangle
                        if yTriTop >= y1 && xTriTop > x1
                            line([x1,xTriTop,xTriTop],[y1,y1,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop >= y1 && xTriTop <= x1
                            vertMidway = (y1+yTriTop)/2;
                            line([x1,x1+sourceEastBuffer,x1+sourceEastBuffer,xTriTop,xTriTop],[y1,y1,vertMidway,vertMidway,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop < y1 && xTriTop > x1
                            horizMidway = (x1+xTriTop)/2;
                            line([x1,horizMidway,horizMidway,xTriTop,xTriTop],[y1,y1,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop < y1 && xTriTop <= x1
                            line([x1,x1+sourceEastBuffer,x1+sourceEastBuffer,xTriTop,xTriTop],[y1,y1,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        else
                            error('Problem drawing connecting line')
                        end
                        
                        % For a SourceNode connection point on the west side
                    elseif obj.SourceConnectionSide == 7
                        
                        sourceWestBuffer = 16;
                        triUpperBuffer = 24;
                        
                        % if connection point on source element is above top of
                        % triangle
                        if yTriTop >= y1 && xTriTop < x1
                            line([x1,xTriTop,xTriTop],[y1,y1,yTriTop],'Color','k','LineWidth',3);
                        elseif yTriTop >= y1 && xTriTop >= x1
                            vertMidway = (y1+yTriTop)/2;
                            line([x1,x1-sourceWestBuffer,x1-sourceWestBuffer,xTriTop,xTriTop],[y1,y1,vertMidway,vertMidway,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop < y1 && xTriTop < x1
                            horizMidway = (x1+xTriTop)/2;
                            line([x1,horizMidway,horizMidway,xTriTop,xTriTop],[y1,y1,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop < y1 && xTriTop >= x1
                            line([x1,x1-sourceWestBuffer,x1-sourceWestBuffer,xTriTop,xTriTop],[y1,y1,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],'Color','k','LineWidth',3);
                        else
                            error('Problem drawing connecting line')
                        end
                        
                    else
                        error('Problem drawing connecting line')
                    end
                    
                elseif strcmpi(class(obj.SourceNode),'OPMprocess')
                    
                    % For a SourceNode connection point on the south side
                    if obj.SourceConnectionSide == 4 && obj.SourceConnectionParameter > 0.25 && obj.SourceConnectionParameter < 0.75
                        
                        % if connection point on source element is above top of
                        % triangle
                        if yTriTop >= y1
                            vertMidway = (yTriTop+y1)/2;
                            
                            % Draw bent line
                            line([x1,x1,xTriTop,xTriTop],[y1,vertMidway,vertMidway,yTriTop],'Color','k','LineWidth',2);
                            
                        else
                            % Determine horizontal midway point
                            horizMidway = (xTriTop+x1)/2;
                            
                            % Bent line from source node to top of triangle
                            % Note that a buffer of 16 units exists beneath the
                            % object, and a buffer of 24 units exists above the
                            % triangle
                            sourceLowerBuffer = 16;
                            triUpperBuffer = 24;
                            
                            line([x1,x1,horizMidway,horizMidway,xTriTop,xTriTop],...
                                [y1,y1+sourceLowerBuffer,y1+sourceLowerBuffer,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],...
                                'Color','k','LineWidth',3);
                        end
                        
                        % For a SourceNode connection point on the north side
                    elseif obj.SourceConnectionSide == 1 && obj.SourceConnectionParameter > 0.25 && obj.SourceConnectionParameter < 0.75
                        
                        sourceUpperBuffer = 16;
                        triUpperBuffer = 24;
                        
                        % if connection point on source element is above top of
                        % triangle
                        if yTriTop >= y1
                            line([x1,x1,xTriTop,xTriTop],[y1,y1-sourceUpperBuffer,y1-sourceUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        else
                            line([x1,x1,xTriTop,xTriTop],[y1,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        end
                        
                        % For a SourceNode connection point on the east side
                    elseif obj.SourceConnectionParameter >= 0.75
                        
                        sourceEastBuffer = 16;
                        triUpperBuffer = 24;
                        
                        % if connection point on source element is above top of
                        % triangle
                        if yTriTop >= y1 && xTriTop > x1
                            line([x1,xTriTop,xTriTop],[y1,y1,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop >= y1 && xTriTop <= x1
                            vertMidway = (y1+yTriTop)/2;
                            line([x1,x1+sourceEastBuffer,x1+sourceEastBuffer,xTriTop,xTriTop],[y1,y1,vertMidway,vertMidway,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop < y1 && xTriTop > x1
                            horizMidway = (x1+xTriTop)/2;
                            line([x1,horizMidway,horizMidway,xTriTop,xTriTop],[y1,y1,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop < y1 && xTriTop <= x1
                            line([x1,x1+sourceEastBuffer,x1+sourceEastBuffer,xTriTop,xTriTop],[y1,y1,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        else
                            error('Problem drawing connecting line')
                        end
                        
                    % For a SourceNode connection point on the west side
                    elseif obj.SourceConnectionParameter <= 0.25
                        
                        sourceWestBuffer = 16;
                        triUpperBuffer = 24;
                        
                        % if connection point on source element is above top of
                        % triangle
                        if yTriTop >= y1 && xTriTop < x1
                            line([x1,xTriTop,xTriTop],[y1,y1,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop >= y1 && xTriTop >= x1
                            vertMidway = (y1+yTriTop)/2;
                            line([x1,x1-sourceWestBuffer,x1-sourceWestBuffer,xTriTop,xTriTop],[y1,y1,vertMidway,vertMidway,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop < y1 && xTriTop < x1
                            horizMidway = (x1+xTriTop)/2;
                            line([x1,horizMidway,horizMidway,xTriTop,xTriTop],[y1,y1,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        elseif yTriTop < y1 && xTriTop >= x1
                            line([x1,x1-sourceWestBuffer,x1-sourceWestBuffer,xTriTop,xTriTop],[y1,y1,yTriTop-triUpperBuffer,yTriTop-triUpperBuffer,yTriTop],'Color','k','LineWidth',2);
                        else
                            error('Problem drawing connecting line')
                        end
                        
                    else
                        error('Problem drawing connecting line')
                    end
                    
                else
                    error('Problem drawing connecting line')
                end
                %% Bent Line between Triangle and Destination Node
                
                if strcmpi(class(obj.DestinationNode),'OPMobject')
                    
                    % For a DestinationNode connection point on the north side
                    if obj.DestinationConnectionSide == 1
                        
                        if y2 >= yTriBottom
                            % Determine vertical midway point
                            vertMidway = (yTriBottom+y2)/2;
                            
                            % Bent line from bottom of trangle to destination node
                            line([xTriBottom,xTriBottom,x2,x2],[yTriBottom,vertMidway,vertMidway,y2],'Color','k','LineWidth',2);
                        else
                            % Determine horizontal midway point
                            horizMidway = (xTriBottom+x2)/2;
                            
                            % Bent line from bottom of trangle to destination node
                            % Note that a buffer of 40 units exists beneath the
                            % triangle, and a buffer of 24 units exists above the
                            % object
                            triLowerBuffer = 40;
                            destinationUpperBuffer = 24;
                            
                            line([xTriBottom,xTriBottom,horizMidway,horizMidway,x2,x2],...
                                [yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2-destinationUpperBuffer,y2-destinationUpperBuffer,y2],...
                                'Color','k','LineWidth',2);
                        end
                        
                        % For a DestinationNode connection point on the south side
                    elseif obj.DestinationConnectionSide == 4
                        triLowerBuffer = 40;
                        destinationLowerBuffer = 24;
                        if y2 >= yTriBottom
                            line([xTriBottom,xTriBottom,x2,x2],[yTriBottom,y2+destinationLowerBuffer,y2+destinationLowerBuffer,y2],'Color','k','LineWidth',2);
                        else
                            line([xTriBottom,xTriBottom,x2,x2],[yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2],'Color','k','LineWidth',2);
                        end
                        
                        % For a DestinationNode connection point on the east side
                    elseif obj.DestinationConnectionSide == 8
                        
                        destinationEastBuffer = 24;
                        triLowerBuffer = 40;
                        
                        % if connection point on source element is above top of
                        % triangle
                        if y2 >= yTriBottom && x2 < xTriBottom
                            line([xTriBottom,xTriBottom,x2],[yTriBottom,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 >= yTriBottom && x2 >= xTriBottom
                            vertMidway = (y2+yTriBottom)/2;
                            line([xTriBottom,xTriBottom,x2+destinationEastBuffer,x2+destinationEastBuffer,x2],[yTriBottom,vertMidway,vertMidway,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 < yTriBottom &&  x2 < xTriBottom
                            horizMidway = (x2+xTriBottom)/2;
                            line([xTriBottom,xTriBottom,horizMidway,horizMidway,x2],[yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 < yTriBottom && x2 >= xTriBottom
                            line([xTriBottom,xTriBottom,x2+destinationEastBuffer,x2+destinationEastBuffer,x2],[yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2,y2],'Color','k','LineWidth',2);
                        else
                            error('Problem drawing connecting line')
                        end
                        
                        % For a DestinationNode connection point on the west side
                    elseif obj.DestinationConnectionSide == 7
                        
                        destinationWestBuffer = 24;
                        triLowerBuffer = 40;
                        
                        % if connection point on source element is above top of
                        % triangle
                        if y2 >= yTriBottom && x2 > xTriBottom
                            line([xTriBottom,xTriBottom,x2],[yTriBottom,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 >= yTriBottom && x2 <= xTriBottom
                            vertMidway = (y2+yTriBottom)/2;
                            line([xTriBottom,xTriBottom,x2-destinationWestBuffer,x2-destinationWestBuffer,x2],[yTriBottom,vertMidway,vertMidway,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 < yTriBottom &&  x2 > xTriBottom
                            horizMidway = (x2+xTriBottom)/2;
                            line([xTriBottom,xTriBottom,horizMidway,horizMidway,x2],[yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 < yTriBottom && x2 <= xTriBottom
                            line([xTriBottom,xTriBottom,x2-destinationWestBuffer,x2-destinationWestBuffer,x2],[yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2,y2],'Color','k','LineWidth',2);
                        else
                            error('Problem drawing connecting line')
                        end
                    else
                        error('Problem drawing connecting line')
                    end
                    
                elseif strcmpi(class(obj.DestinationNode),'OPMprocess')
                    
                     % For a DestinationNode connection point on the north side
                    if obj.DestinationConnectionSide == 1 && obj.DestinationConnectionParameter > 0.25 && obj.DestinationConnectionParameter < 0.75
                        
                        if y2 >= yTriBottom
                            % Determine vertical midway point
                            vertMidway = (yTriBottom+y2)/2;
                            
                            % Bent line from bottom of trangle to destination node
                            line([xTriBottom,xTriBottom,x2,x2],[yTriBottom,vertMidway,vertMidway,y2],'Color','k','LineWidth',2);
                        else
                            % Determine horizontal midway point
                            horizMidway = (xTriBottom+x2)/2;
                            
                            % Bent line from bottom of trangle to destination node
                            % Note that a buffer of 40 units exists beneath the
                            % triangle, and a buffer of 24 units exists above the
                            % object
                            triLowerBuffer = 40;
                            destinationUpperBuffer = 24;
                            
                            line([xTriBottom,xTriBottom,horizMidway,horizMidway,x2,x2],...
                                [yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2-destinationUpperBuffer,y2-destinationUpperBuffer,y2],...
                                'Color','k','LineWidth',3);
                        end
                        
                        % For a DestinationNode connection point on the south side
                    elseif obj.DestinationConnectionSide == 4 && obj.DestinationConnectionParameter > 0.25 && obj.DestinationConnectionParameter < 0.75
                        triLowerBuffer = 40;
                        destinationLowerBuffer = 24;
                        if y2 >= yTriBottom
                            line([xTriBottom,xTriBottom,x2,x2],[yTriBottom,y2+destinationLowerBuffer,y2+destinationLowerBuffer,y2],'Color','k','LineWidth',2);
                        else
                            line([xTriBottom,xTriBottom,x2,x2],[yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2],'Color','k','LineWidth',2);
                        end
                        
                        % For a DestinationNode connection point on the east side
                    elseif obj.DestinationConnectionParameter >= 0.75
                        
                        destinationEastBuffer = 24;
                        triLowerBuffer = 40;
                        
                        % if connection point on source element is above top of
                        % triangle
                        if y2 >= yTriBottom && x2 < xTriBottom
                            line([xTriBottom,xTriBottom,x2],[yTriBottom,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 >= yTriBottom && x2 >= xTriBottom
                            vertMidway = (y2+yTriBottom)/2;
                            line([xTriBottom,xTriBottom,x2+destinationEastBuffer,x2+destinationEastBuffer,x2],[yTriBottom,vertMidway,vertMidway,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 < yTriBottom &&  x2 < xTriBottom
                            horizMidway = (x2+xTriBottom)/2;
                            line([xTriBottom,xTriBottom,horizMidway,horizMidway,x2],[yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 < yTriBottom && x2 >= xTriBottom
                            line([xTriBottom,xTriBottom,x2+destinationEastBuffer,x2+destinationEastBuffer,x2],[yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2,y2],'Color','k','LineWidth',2);
                        else
                            error('Problem drawing connecting line')
                        end
                        
                    % For a DestinationNode connection point on the west side
                    elseif obj.DestinationConnectionParameter <= 0.25
                        
                        destinationWestBuffer = 24;
                        triLowerBuffer = 40;
                        
                        % if connection point on source element is above top of
                        % triangle
                        if y2 >= yTriBottom && x2 > xTriBottom
                            line([xTriBottom,xTriBottom,x2],[yTriBottom,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 >= yTriBottom && x2 <= xTriBottom
                            vertMidway = (y2+yTriBottom)/2;
                            line([xTriBottom,xTriBottom,x2-destinationWestBuffer,x2-destinationWestBuffer,x2],[yTriBottom,vertMidway,vertMidway,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 < yTriBottom &&  x2 > xTriBottom
                            horizMidway = (x2+xTriBottom)/2;
                            line([xTriBottom,xTriBottom,horizMidway,horizMidway,x2],[yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2,y2],'Color','k','LineWidth',2);
                        elseif y2 < yTriBottom && x2 <= xTriBottom
                            line([xTriBottom,xTriBottom,x2-destinationWestBuffer,x2-destinationWestBuffer,x2],[yTriBottom,yTriBottom+triLowerBuffer,yTriBottom+triLowerBuffer,y2,y2],'Color','k','LineWidth',2);
                        else
                            error('Problem drawing connecting line')
                        end
                    else
                        error('Problem drawing connecting line')
                    end
                    
                else
                    error('Problem drawing connecting line')
                end
                
            else
               % Code for plotting unidirectional and bidirectional relations 
            end          
            
        end
        
    end
    
    %% Static Methods
    methods (Static) %Static method since it does not depend on class properties
        %% Function to determine connection point on each object/process
        function [absX, absY] = findConnectionPoint(entity,connectionSide,connectionParameter)
            
            % Extract reference point, and width and height of entity
            x = entity.VisualX;
            y = entity.VisualY;
            width = entity.VisualWidth;
            height = entity.VisualHeight;
            
            % if object, only possible connections are north, south, east,
            % west
            % Note that:
            % - For north and south faces, connection parameter is
            % aligned from left to right
            % - For east and west faces, connection parameter is aligned
            % from top to bottom
            
            if strcmpi(class(entity),'OPMobject')
                
                % if connection side is north
                if connectionSide == 1
                    absX = x + width*connectionParameter;
                    absY = y;
                    return
                % if connection side is south
                elseif connectionSide == 4
                    absX = x + width*connectionParameter;
                    absY = y+height;
                    return                
                % if connection side is west
                elseif connectionSide == 7
                    absX = x;
                    absY = y + height*connectionParameter;
                    return                   
                % if connection side is east
                elseif connectionSide == 8
                    absX = x + width;
                    absY = y + height*connectionParameter;
                    return                    
                else
                    error('Connection Side not detected!')
                end
                
            elseif strcmpi(class(entity),'OPMstate')
                
                % if connection side is north
                if connectionSide == 1
                    absX = entity.ParentVisualX + x + width*connectionParameter;
                    absY = entity.ParentVisualY + y;
                    return
                % if connection side is south
                elseif connectionSide == 4
                    absX = entity.ParentVisualX + x + width*connectionParameter;
                    absY = entity.ParentVisualY + y + height;
                    return                
                % if connection side is west
                elseif connectionSide == 7
                    absX = entity.ParentVisualX + x;
                    absY = entity.ParentVisualY + y + height*connectionParameter;
                    return                   
                % if connection side is east
                elseif connectionSide == 8
                    absX = entity.ParentVisualX + x + width;
                    absY = entity.ParentVisualY + y + height*connectionParameter;
                    return                    
                else
                    error('Connection Side not detected!')
                end
                
            elseif strcmpi(class(entity),'OPMprocess')
                
                % if connection side is north
                if connectionSide == 1
                    absX = x + width*connectionParameter;
                    absY = y + height/2 - height/2*sqrt(1-(width*connectionParameter-width/2)^2/(width/2)^2);
                    return
                % if connection side is south
                elseif connectionSide == 4
                    absX = x + width*connectionParameter;
                    absY = y + height/2 + height/2*sqrt(1-(width*connectionParameter-width/2)^2/(width/2)^2);
                    return
                else
                    error('Connection Side not detected!')
                end
                
                
            end
            
        end
        
        
        
    end
    
end

