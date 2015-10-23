classdef OPMprocess < handle
    % By: Sydney Do
    % Date created: October 18, 2015
    % Last modified: October 18, 2015
    
    % SUMMARY
    % Process class for MATLAB OPM Toolbox
    
    properties
        name
        id
        MaxTimeActivation = Inf
        MinTimeActivation = 0
        environment = 0
        physical = 0
        ParentProcesses = OPMprocess.empty(0)             % array of OPMprocesses - Decomposition Link
        ChildProcesses = OPMprocess.empty(0)              % array of OPMprocesses - Decomposition Link
        ParentClasses = OPMprocess.empty(0)               % Parents through Specialization Link
        ChildClasses = OPMprocess.empty(0)                 % Children through Specialization Link
        UniqueAttributes = OPMobject.empty(0)        % define with Exhibition Link - combination of both objects and processes
        InheritedAttributes = OPMobject.empty(0)     % transfered into object when this object is subclassed or is an instance of a parent object
        UniqueOperations = OPMprocess.empty(0)            % Contains only processes that are exhibited by this process         
        InheritedOperations = OPMprocess.empty(0)         % Contains only processes that are exhibited by this process
        Instances = OPMprocess.empty(0)                   % array of Instances (Instantiation Link)
        VisualX
        VisualY
        VisualWidth
        VisualHeight
    end
    
    methods
        %% Constructor
        function obj = OPMprocess(Name,ID,VisualData)
            % VisualData for objects and processes
            % [x,y,width,height]
                        
            % Ensure that "Name" is a char string
            if ~ischar(Name)
                error('Input value must be a character string')
            end
            
            obj.name = Name;
            obj.id = ID;
            
            if nargin == 3
                obj.VisualX = VisualData(1);
                obj.VisualY = VisualData(2);
                obj.VisualWidth = VisualData(3);
                obj.VisualHeight = VisualData(4);
            end
            
        end
        
        %% Plotting Command
        function obj = plotOPD(obj)
            
            delta = 0.05;
            r=0:delta:(2*pi+delta);
            p=[(obj.VisualWidth/2*cos(r))' (obj.VisualHeight/2*sin(r))'];
            
            patch(obj.VisualX+obj.VisualWidth/2+p(:,1),obj.VisualY+obj.VisualHeight/2+p(:,2),[230, 230, 230]/255,'EdgeColor',[0, 0, 170]/255,'LineWidth',3);
            axis equal
            text(obj.VisualX+obj.VisualWidth/2,obj.VisualY+obj.VisualHeight/2,obj.name,'FontSize',min([0.25*obj.VisualHeight,12]),'HorizontalAlignment','center');
            set(gca,'YDir','reverse');
        end
        
        %% Append Child Objects
        function obj = appendChildren(obj,childProcesses)
           
            % Ensure that input is an OPMobject
            if ~strcmpi(class(childProcesses),'OPMprocess')
                error('Input must be of class OPMprocess')
            end
            
            obj.ChildProcesses = [obj.ChildProcesses, childProcesses];
        
        end

        %% Append Parent Objects
        function obj = appendParents(obj,parentProcesses)
           
            % Ensure that input is an OPMobject
            if ~strcmpi(class(parentProcesses),'OPMprocess')
                error('Input must be of class OPMprocess')
            end
            
            obj.ParentProcesses = [obj.ParentProcesses, parentProcesses];
        
        end
        
        %% Append Child Classes
        function obj = appendChildrenClasses(obj,childClasses)
           
            % Ensure that input is an OPMobject
            if ~strcmpi(class(childClasses),'OPMprocess')
                error('Input must be of class OPMprocess')
            end
            
            obj.ChildClasses = [obj.ChildClasses, childClasses];
        
        end

        %% Append Parent Classes
        function obj = appendParentClasses(obj,parentClasses)
           
            % Ensure that input is an OPMobject
            if ~strcmpi(class(parentClasses),'OPMprocess')
                error('Input must be of class OPMprocess')
            end
            
            obj.ParentClasses = [obj.ParentClasses, parentClasses];
        
        end
        
        %% Append Unique Properties
        function obj = appendUniqueProperties(obj,additionalUniqueProperties)
           
            % Ensure that input is an OPMobject or OPMproces
            if ~(strcmpi(class(additionalUniqueProperties),'OPMprocess')||...
                    strcmpi(class(additionalUniqueProperties),'OPMobject'))
                error('Input must be of class OPMprocess or OPMobject')
            end
            
            if strcmpi(class(additionalUniqueProperties),'OPMobject')
                obj.UniqueAttributes = [obj.UniqueAttributes, additionalUniqueProperties];
            elseif strcmpi(class(additionalUniqueProperties),'OPMprocess')
                obj.UniqueOperations = [obj.UniqueOperations, additionalUniqueProperties];
            else
                error('Error in transferring attribute/operation')
            end

        end
        
        %% Append Inherited Properties
        function obj = appendInheritedProperties(obj,additionalInheritedProperties)
           
            % Ensure that input is an OPMobject or OPMproces
            if ~(strcmpi(class(additionalInheritedProperties),'OPMprocess')||...
                    strcmpi(class(additionalInheritedProperties),'OPMobject'))
                error('Input must be of class OPMprocess or OPMobject')
            end
            
            if strcmpi(class(additionalInheritedProperties),'OPMobject')
                obj.InheritedAttributes = [obj.InheritedAttributes, additionalInheritedProperties];
            elseif strcmpi(class(additionalInheritedProperties),'OPMprocess')
                obj.InheritedOperations = [obj.InheritedOperations, additionalInheritedProperties];
            else
                error('Error in transferring attribute/operation')
            end

        end
        
        %% Append Instances
        function obj = appendInstances(obj,additionalInstances)
           
            % Ensure that input is an OPMobject
            if ~strcmpi(class(additionalInstances),'OPMprocess')
                error('Input must be of class OPMprocess')
            end
            
            obj.Instances = [obj.Instances, additionalInstances];
        
        end
        
        
    end
    
end

