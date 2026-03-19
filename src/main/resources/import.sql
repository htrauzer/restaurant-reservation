-- Main Hall 
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (1, 2, 'MAIN_HALL', 15, 8);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (2, 2, 'MAIN_HALL', 35, 8);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (3, 2, 'MAIN_HALL', 87, 22);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (4, 6, 'MAIN_HALL', 80, 8);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (5, 8, 'MAIN_HALL', 15, 25);

-- Private Rooms 
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (6, 4, 'PRIVATE_ROOMS', 15, 42);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (7, 4, 'PRIVATE_ROOMS', 15, 55);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (8, 4, 'PRIVATE_ROOMS', 80, 55);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (9, 2, 'PRIVATE_ROOMS', 80, 42);

-- Terrace 
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (10, 2, 'TERRACE', 15, 75);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (11, 2, 'TERRACE', 85, 75);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (12, 4, 'TERRACE', 30, 90);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (13, 4, 'TERRACE', 70, 90);
INSERT INTO restaurant_tables (id, capacity, zone, pos_x, pos_y) VALUES (14, 8, 'TERRACE', 50, 75);

-- Features
INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (1, 'WINDOW');
INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (1, 'CORNER');

INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (2, 'WINDOW');

INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (3, 'CORNER');

INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (4, 'WINDOW');
INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (4, 'CORNER');

INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (5, 'CORNER');

INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (6, 'SOFA');
INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (6, 'CORNER'); 

INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (7, 'SOFA');
INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (7, 'CORNER');

INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (8, 'SOFA');
INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (8, 'CORNER');

INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (9, 'CORNER');

INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (10, 'CORNER');

INSERT INTO restaurant_table_features (restaurant_table_id, features) VALUES (11, 'CORNER');