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
        ParentProcesses         % array of OPMprocesses - Decomposition Link
        ChildProcesses          % array of OPMprocesses - Decomposition Link
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
        
        
    end
    
end

