INSERT INTO Orders (id, modificationCounter, idBooking, idInvitedGuest, idHost, orderStatus, paid) VALUES (0, 1, 0, null, 0, 'open', 0);
INSERT INTO Orders (id, modificationCounter, idBooking, idInvitedGuest, idHost, orderStatus, paid) VALUES (1, 1, 3, 0, null, 'open', 0);
INSERT INTO Orders (id, modificationCounter, idBooking, idInvitedGuest, idHost, orderStatus, paid) VALUES (2, 1, 3, 1, null, 'preparing', 0);
INSERT INTO Orders (id, modificationCounter, idBooking, idInvitedGuest, idHost, orderStatus, paid) VALUES (3, 1, 3, 2, null, 'preparing', 1);
INSERT INTO Orders (id, modificationCounter, idBooking, idInvitedGuest, idHost, orderStatus, paid) VALUES (4, 1, 3, 3, null, 'cancelled', 1);
INSERT INTO Orders (id, modificationCounter, idBooking, idInvitedGuest, idHost, orderStatus, paid) VALUES (5, 1, 3, 4, null, 'cancelled', 1);
INSERT INTO Orders (id, modificationCounter, idBooking, idInvitedGuest, idHost, orderStatus, paid) VALUES (6, 1, 4, 8, null, 'open', 1);
INSERT INTO Orders (id, modificationCounter, idBooking, idInvitedGuest, idHost, orderStatus, paid) VALUES (7, 1, 4, 9, null, 'open', 1);



INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (0, 1, 0, 2, 'please not too spicy', 0);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (1, 1, 4, 1, null, 0);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (2, 1, 2, 1, null, 0);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (3, 1, 4, 2, null, 1);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (4, 1, 2, 1, null, 1);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (5, 1, 3, 1, null, 1);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (6, 1, 2, 1, null, 2);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (7, 1, 5, 1, null, 3);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (8, 1, 5, 1, null, 4);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (9, 1, 3, 1, null, 5);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (10, 1, 5, 2, null, 6);
INSERT INTO OrderLine (id, modificationCounter, idDish, amount, comment, idOrder) VALUES (11, 1, 3, 1, null, 7);



INSERT INTO OrderDishExtraIngredient (id, modificationCounter, idOrderLine, idIngredient) VALUES (0, 1, 0, 1);
INSERT INTO OrderDishExtraIngredient (id, modificationCounter, idOrderLine, idIngredient) VALUES (1, 1, 1, 1);
INSERT INTO OrderDishExtraIngredient (id, modificationCounter, idOrderLine, idIngredient) VALUES (2, 1, 2, 0);
INSERT INTO OrderDishExtraIngredient (id, modificationCounter, idOrderLine, idIngredient) VALUES (3, 1, 2, 1);
INSERT INTO OrderDishExtraIngredient (id, modificationCounter, idOrderLine, idIngredient) VALUES (4, 1, 4, 0);
INSERT INTO OrderDishExtraIngredient (id, modificationCounter, idOrderLine, idIngredient) VALUES (5, 1, 5, 0);