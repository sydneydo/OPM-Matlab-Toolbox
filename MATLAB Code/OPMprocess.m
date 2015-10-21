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
        ParentProcesses     % array of OPMprocesses - Decomposition Link
        ChildProcesses      % array of OPMprocesses - Decomposition Link
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
            text(obj.VisualX+obj.VisualWidth/2,obj.VisualY+obj.VisualHeight/2,obj.name,'FontUnits','normalized','FontSize',0.05,'HorizontalAlignment','center');
            set(gca,'YDir','reverse');
        end
        
        %% Append Child Objects
        function obj = appendChildren(obj,childProcesses)
           
            % Ensure that input is an OPMobject
            if ~strcmpi(class(childProcesses),'OPMprocess')
                error('Input must be of class OPMprocess')
            end
            
            obj.ChildProcesses = [obj.ChildObjects, childProcesses];
        
        end

        %% Append Parent Objects
        function obj = appendParents(obj,parentProcesses)
           
            % Ensure that input is an OPMobject
            if ~strcmpi(class(parentProcesses),'OPMprocess')
                error('Input must be of class OPMprocess')
            end
            
            obj.ParentProcesses = [obj.ParentObjects, parentProcesses];
        
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

