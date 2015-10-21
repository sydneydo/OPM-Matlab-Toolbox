classdef OPMobject < handle
    % By: Sydney Do
    % Date created: October 18, 2015
    % Last modified: October 18, 2015
    
    % SUMMARY
    % Object class for MATLAB OPM Toolbox
    
    properties
        name
        states
        id
        environment = 0
        physical = 0
        ParentObjects       % array of OPMobjects - Decomposition Link
        ChildObjects        % array of OPMobjects - Decomposition Link
        ParentClasses       % Parents through Specialization Link
        ChildClasses        % Children through Specialization Link
        UniqueAttributes    % define with Exhibition Link - combination of both objects and processes
        InheritedAttributes % transfered into object when this object is subclassed or is an instance of a parent object
        Instances           % array of Instances (Instantiation Link)
        VisualX
        VisualY
        VisualWidth
        VisualHeight
    end
    
    methods
        %% Constructor
        function obj = OPMobject(Name,ID,VisualData,States)
            % VisualData for objects and processes
            % [x,y,width,height]
            
            
            % If Name is a cell, ensure that each element of the cell is a
            % char string - note that cells will be used for multiline
            % names
            if iscell(Name)
                for i = 1:length(Name)
                    if ~ischar(Name{i})
                        error('Input value must be a character string')
                    end            
                end
            else         
                % Ensure that "Name" is a char string
                if ~ischar(Name)
                    error('Input value must be a character string')
                end
            end
            
            obj.name = Name;
            obj.id = ID;
            
            if nargin >= 3
                obj.VisualX = VisualData(1);
                obj.VisualY = VisualData(2);
                obj.VisualWidth = VisualData(3);
                obj.VisualHeight = VisualData(4);
            end
            
            if nargin == 4
                
                % Ensure that States is of class OPMstate
                if ~strcmpi(class(States),'OPMstate')
                    error('4th input must be of class "OPMstate"')
                end
                
                obj.states = States;
            end
            
            
        end
        
        %% Plotting Command
        function obj = plotOPD(obj)
            
           patch([obj.VisualX*ones(1,2),(obj.VisualX+obj.VisualWidth)*ones(1,2)],...
               [obj.VisualY,(obj.VisualY+obj.VisualHeight)*ones(1,2),obj.VisualY],...
               [230, 230, 230]/255,'EdgeColor',[0, 110, 0]/255,'LineWidth',3);
            axis equal
            
            set(gca,'YDir','reverse');
            hold on
            
            % Plot States if any - presence of states also affects location
            % of text
            if ~isempty(obj.states)
                for i = 1:length(obj.states)
                    obj.states(i).plotOPD;
                end
                
                % Base text position on relative vertical position of first
                % state only
                text(obj.VisualX+obj.VisualWidth/2,obj.VisualY+abs(obj.states(1).VisualY)/2,obj.name,...
                'FontUnits','normalized','FontSize',0.05,'HorizontalAlignment','center');
            else
                text(obj.VisualX+obj.VisualWidth/2,obj.VisualY+obj.VisualHeight/2,...
                obj.name,'FontUnits','normalized','FontSize',0.05,'HorizontalAlignment','center');
            end
            
        end
        
        %% Append Child Objects
        function obj = appendChildren(obj,childObjects)
           
            % Ensure that input is an OPMobject
            if ~strcmpi(class(childObjects),'OPMobject')
                error('Input must be of class OPMobject')
            end
            
            obj.ChildObjects = [obj.ChildObjects, childObjects];
        
        end

        %% Append Parent Objects
        function obj = appendParents(obj,parentObjects)
           
            % Ensure that input is an OPMobject
            if ~strcmpi(class(parentObjects),'OPMobject')
                error('Input must be of class OPMobject')
            end
            
            obj.ParentObjects = [obj.ParentObjects, parentObjects];
        
        end
        
        %% Append Instances
        function obj = appendInstances(obj,additionalInstances)
           
            % Ensure that input is an OPMobject
            if ~strcmpi(class(additionalInstances),'OPMobject')
                error('Input must be of class OPMobject')
            end
            
            obj.Instances = [obj.Instances, additionalInstances];
        
        end
        
        
    end
    
end

