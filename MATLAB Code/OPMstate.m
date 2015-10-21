classdef OPMstate < handle
    % By: Sydney Do
    % Date created: October 18, 2015
    % Last modified: October 18, 2015
    
    % SUMMARY
    % Object class for MATLAB OPM Toolbox
    
    properties
        name
        id
        value  = []
        VisualX
        VisualY
        VisualWidth
        VisualHeight
        ParentVisualX
        ParentVisualY
    end
    
    methods
        %% Constructor
        function obj = OPMstate(Name,ID,VisualData,ParentVisualData)
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
            
            if nargin > 2
                obj.VisualX = VisualData(1);
                obj.VisualY = VisualData(2);
                obj.VisualWidth = VisualData(3);
                obj.VisualHeight = VisualData(4);
            end
            
            if nargin > 3
                obj.ParentVisualX = ParentVisualData(1);
                obj.ParentVisualY = ParentVisualData(2);
            end
                          
        end
        
        %% Plotting Command
        function obj = plotOPD(obj)
            
%            patch([(obj.VisualX+obj.ParentVisualX)*ones(1,2),...
%                (obj.VisualX+obj.ParentVisualX+obj.VisualWidth)*ones(1,2)],...
%                [obj.VisualY+obj.ParentVisualY,...
%                (obj.VisualY+obj.ParentVisualY+obj.VisualHeight)*ones(1,2),...
%                obj.VisualY+obj.ParentVisualY],...
%                [230, 230, 230]/255,'EdgeColor',[91, 91, 0]/255,'LineWidth',2);

            rectangle('Position',[obj.VisualX+obj.ParentVisualX,obj.VisualY+obj.ParentVisualY,...
               obj.VisualWidth,obj.VisualHeight],'Curvature',0.2,'FaceColor',...
               [230, 230, 230]/255,'EdgeColor',[91, 91, 0]/255,'LineWidth',2);
           
           axis equal
            text(obj.VisualX+obj.ParentVisualX+obj.VisualWidth/2,...
                obj.VisualY+obj.ParentVisualY+obj.VisualHeight/2,...
                obj.name,'FontSize',0.3*obj.VisualHeight,'HorizontalAlignment','center');
            set(gca,'YDir','reverse');
            
        end
        
    end
    
end

