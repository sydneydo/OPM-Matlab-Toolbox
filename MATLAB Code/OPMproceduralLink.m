classdef OPMproceduralLink < handle
    % By: Sydney Do
    % Date created: October 18, 2015
    % Last modified: October 18, 2015
    
    % SUMMARY
    % proceduralLink class for MATLAB OPM Toolbox
    % Types of Procedural Links within OPM include: instrument, agent,
    % consumption/result, effect links
    
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
        environment = 0
        physical = 0
    end
    
    methods
        %% Constructor
        function obj = OPMproceduralLink(Type,ID,FromNode,ToNode,VisualData)
            
%             if nargin ~= 3
%                 error('Three input values required. Input format: proceduralLink(Type,Source Node,Destination Node)')
%             end
                
            % Control type of input type for link
            if ~(strcmpi(Type,'Instrument')||...
                    strcmpi(Type,'Agent')||...
                    strcmpi(Type,'Consumption')||...
                    strcmpi(Type,'Result')||...
                    strcmpi(Type,'Effect'))
                error('Link type must be one of the following: {Instrument, Agent, Consumption, Result, Effect}')
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
            
        end
        
        %% Plotting Code
        % Baseline plot about (0,0)
        function obj = plotOPD(obj)
            
            % Determine Connection Point of Source Node
            [x1,y1] = obj.findConnectionPoint(obj.SourceNode,obj.SourceConnectionSide,obj.SourceConnectionParameter);           
            
            % Determine Connection Point of Destination Node
            [x2,y2] = obj.findConnectionPoint(obj.DestinationNode,obj.DestinationConnectionSide,obj.DestinationConnectionParameter);
            
            line([x1,x2],[y1,y2],'Color','k','LineWidth',2)
            axis equal
            set(gca,'YDir','reverse');
            hold on
            
            % Plotting ends of curves
            % Plotting if instrument link
            if strcmpi(obj.type,'Instrument')
                % Plot from left to right, process first then object
                rad = 10;       % radius of circle for instrument link
                delta = 0.05;
                r = 0:delta:(2*pi+delta);
                patch(x2+rad*cos(r),y2+rad*sin(r),[230, 230, 230]/255,'EdgeColor','k','LineWidth',2);
                axis equal
                set(gca,'YDir','reverse');
                
            elseif strcmpi(obj.type,'Agent')
                % Plot from left to right, process first then object
                rad = 10;       % radius of circle for instrument link
                delta = 0.05;
                r = 0:delta:(2*pi+delta);
                patch(x2+rad*cos(r),y2+rad*sin(r),'k','EdgeColor','k','LineWidth',2);
                axis equal
                set(gca,'YDir','reverse');
                
            elseif strcmpi(obj.type,'Consumption') || strcmpi(obj.type,'Result')     % Process Consumes Object
                
                lengthOfSymLine = 20;       % length of line of symmetry
                centerAngle = 45*pi/180;
                arrowDihedralAngle = (pi/2 - centerAngle/2)/2;
                commonEdge = lengthOfSymLine*sin(centerAngle/2)/sin(arrowDihedralAngle);        %Sine rule to determine length of diagonal line
                gapVertDist = commonEdge*cos(centerAngle/2+arrowDihedralAngle);
                gapHorizDist = commonEdge*sin(centerAngle/2+arrowDihedralAngle);
                
                x = [0, gapHorizDist,                0,               -gapHorizDist,                0];
                y = [0, lengthOfSymLine+gapVertDist, lengthOfSymLine, lengthOfSymLine+gapVertDist, 0];  
                
                % Determine which quadrant angle is in to determine correct
                % rotation angle of element
                if (x2-x1) <= 0 % 1st and 4th quadrants                
                    rotAngle = (atan((y2-y1)/(x2-x1)))-pi/2;      % Add pi/2 to rotate image to align with arrow head. Negative angle since y is upside down in the coordinate system
                else % 2nd and 3rd quadrants - this condition assumes dx < 0                   
                    rotAngle = pi+(atan((y2-y1)/(x2-x1)))-pi/2;                    
                end
        
                % Rotation Matrix
                alpha=[cos(rotAngle) -sin(rotAngle);
                    sin(rotAngle) cos(rotAngle)];
                
                rotatedCoords = alpha*[x;y];       
                
                patch(rotatedCoords(1,:)+x2,rotatedCoords(2,:)+y2,[230, 230, 230]/255,'EdgeColor','k','LineWidth',2);
                axis equal
                set(gca,'YDir','reverse');
                
            elseif strcmpi(obj.type,'Effect')   % Double headed arror 
                
                lengthOfSymLine = 20;       % length of line of symmetry
                centerAngle = 45*pi/180;
                arrowDihedralAngle = (pi/2 - centerAngle/2)/2;
                commonEdge = lengthOfSymLine*sin(centerAngle/2)/sin(arrowDihedralAngle);        %Sine rule to determine length of diagonal line
                gapVertDist = commonEdge*cos(centerAngle/2+arrowDihedralAngle);
                gapHorizDist = commonEdge*sin(centerAngle/2+arrowDihedralAngle);
                
                x = [0, gapHorizDist,                0,               -gapHorizDist,                0];
                y = [0, lengthOfSymLine+gapVertDist, lengthOfSymLine, lengthOfSymLine+gapVertDist, 0];  
                
                % Determine which quadrant angle is in to determine correct
                % rotation angle of element
                if (x2-x1) <= 0 % 1st and 4th quadrants              
                    rotAngle = (atan((y2-y1)/(x2-x1)))-pi/2;      % Add pi/2 to rotate image to align with arrow head. Negative angle since y is upside down in the coordinate system               
                else % 2nd and 3rd quadrants - this condition assumes dx < 0   
                    rotAngle = pi+(atan((y2-y1)/(x2-x1)))-pi/2;     
                end
                
                % Destination Rotation Matrix
                alphaDestination=[cos(rotAngle) -sin(rotAngle);
                    sin(rotAngle) cos(rotAngle)];
                
                rotatedCoordsDestination = alphaDestination*[x;y];       
                
                % Source Rotation Matrix
                alphaSource=[cos(rotAngle+pi) -sin(rotAngle+pi);
                    sin(rotAngle+pi) cos(rotAngle+pi)];
                
                rotatedCoordsSource = alphaSource*[x;y]; 
                
                patch(rotatedCoordsDestination(1,:)+x2,rotatedCoordsDestination(2,:)+y2,[230, 230, 230]/255,'EdgeColor','k','LineWidth',2);
                hold on
                patch(rotatedCoordsSource(1,:)+x1,rotatedCoordsSource(2,:)+y1,[230, 230, 230]/255,'EdgeColor','k','LineWidth',2);
                axis equal
                set(gca,'YDir','reverse');
                
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

