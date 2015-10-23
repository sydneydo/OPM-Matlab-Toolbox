%% MATLAB OPM Toolbox Demo Code
%   Code to demo OPMproject MATLAB object
%   By: Sydney Do (sydneydo@mit.edu)
%   Date Created: 10/23/2015
%   Last Updated: 10/23/2015

clear all
clc

addpath('C:\Users\Sydney Do\Desktop\MIT\PhD Research\OPM-Matlab Toolbox Project\OPM-Matlab-Toolbox\MATLAB Code');
addpath('C:\Users\Sydney Do\Desktop\MIT\PhD Research\OPM-Matlab Toolbox Project\OPM-Matlab-Toolbox\Test Files');

filename = 'IntegratedTestFile.opx';

opm = OPMproject(filename);     % Read in filename

% Plot OPD
opm.plotOPD