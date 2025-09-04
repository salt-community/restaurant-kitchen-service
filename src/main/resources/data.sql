-- Hamburger ingredients (1-5 units)
INSERT INTO ingredients (id, available_quantity, name) VALUES (1, 3, 'Beef Patty');
INSERT INTO ingredients (id, available_quantity, name) VALUES (2, 5, 'Burger Bun');
INSERT INTO ingredients (id, available_quantity, name) VALUES (3, 4, 'Lettuce');
INSERT INTO ingredients (id, available_quantity, name) VALUES (4, 2, 'Tomato');
INSERT INTO ingredients (id, available_quantity, name) VALUES (5, 3, 'Cheese Slice');
INSERT INTO ingredients (id, available_quantity, name) VALUES (6, 1, 'Onion');
INSERT INTO ingredients (id, available_quantity, name) VALUES (7, 4, 'Pickles');
INSERT INTO ingredients (id, available_quantity, name) VALUES (8, 2, 'Bacon');

-- Pizza ingredients (1-5 units)
INSERT INTO ingredients (id, available_quantity, name) VALUES (9, 1, 'Pizza Dough');
INSERT INTO ingredients (id, available_quantity, name) VALUES (10, 5, 'Tomato Sauce');
INSERT INTO ingredients (id, available_quantity, name) VALUES (11, 4, 'Mozzarella Cheese');
INSERT INTO ingredients (id, available_quantity, name) VALUES (12, 3, 'Pepperoni');
INSERT INTO ingredients (id, available_quantity, name) VALUES (13, 2, 'Mushrooms');
INSERT INTO ingredients (id, available_quantity, name) VALUES (14, 1, 'Bell Peppers');
INSERT INTO ingredients (id, available_quantity, name) VALUES (15, 5, 'Olives');

-- Drinks (1-5 units)
INSERT INTO ingredients (id, available_quantity, name) VALUES (16, 5, 'Soda');

-- Recipes
INSERT INTO recipes (id, name, description) VALUES
                                                (1, 'Classic Burger', 'A classic beef burger with fresh ingredients'),
                                                (2, 'Pepperoni Pizza', 'Traditional pizza with pepperoni and cheese');

-- Recipe Ingredients (assumes 1 unit of each ingredient)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id) VALUES
-- Burger: 1 patty, 1 bun, 1 lettuce, 1 tomato, 1 cheese
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
-- Pizza: 1 dough, 1 sauce, 1 cheese, 1 pepperoni
(2, 9), (2, 10), (2, 11), (2, 12);