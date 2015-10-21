function plot_ellipse(a,b,cx,cy,angle,label,fillcolor,edgecolor)
%a: width in pixels
%b: height in pixels
%cx: horizontal center
%cy: vertical center
%angle: orientation ellipse in degrees
%color: color code (e.g., 'r' or [0.4 0.5 0.1])

angle=angle/180*pi;
delta = 0.05;
r=0:delta:(2*pi+delta);
p=[(a*cos(r))' (b*sin(r))'];

alpha=[cos(angle) -sin(angle)
       sin(angle) cos(angle)];
   
 p1=p*alpha;
 
patch(cx+a+p1(:,1),cy+b+p1(:,2),fillcolor,'EdgeColor',edgecolor,'LineWidth',3);
% text(cx+a,cy+b,label,'FontSize',20,'HorizontalAlignment','center')
axis equal
% TextZoomable(cx+a,cy+b,label,'FontUnits','normalized','FontSize',0.01,'HorizontalAlignment','center');
text(cx+a,cy+b,label,'FontUnits','normalized','FontSize',0.05,'HorizontalAlignment','center');

 
   
   