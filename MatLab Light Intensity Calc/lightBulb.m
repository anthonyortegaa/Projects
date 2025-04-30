% Dimensions of the room in meters
width = 10;
length = 4;
height = 3;

% define the bulb
bulb = 300;
num_bulb = 1;

% Coordinates of the bulbs
bulb_x = width / 2;
bulb_y = length / 2;
bulb_z = height;

% meshgrid for floor illumination
[x, y] = meshgrid(0:0.1:width, 0:0.1:length);

%calc intensity/distance
intensity = zeros(size(x));
p = pi;

for i = 1: numel(x)
    distance = sqrt((x(i) - bulb_x)^2 + (y(i) - bulb_y)^2 + bulb_z^2);
    intensity(i) = bulb / ((4*p) * distance^2);
end

% Plot the data
surf(x,y, intensity);
title('Light Intensity Distribution');
xlabel('X (Meters)');
ylabel('Y (Meters)');
zlabel('Intensity (Watts per square meter)');
colormap('bone');

%Intensity of the corners and the center
bl_corner = intensity(1,1);
tl_corner = intensity(1,end);
br_corner = intensity(end,1);
tr_corner = intensity(end,end);
x_center = round(size(x, 1) / 2);
y_center = round(size(x, 2) / 2);
center_intensity = intensity(x_center, y_center);

disp(['The intensity in the top left corner is ',num2str(tl_corner)]);
disp(['The intensity in the top right corner is ',num2str(tr_corner)]);
disp(['The intensity in the bottom left corner is ',num2str(bl_corner)]);
disp(['The intensity in the bottom left corner is ',num2str(br_corner)]);
disp(['The intensity in the center is ', num2str(center_intensity)]);