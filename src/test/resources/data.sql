INSERT INTO genre (name)
VALUES ('Action'),
       ('RPG'),
       ('Strategy'),
       ('Indie'),
       ('Adventure');


INSERT INTO platform (name)
VALUES ('PC'),
       ('PlayStation 5'),
       ('Xbox Series X'),
       ('Nintendo Switch'),
       ('Linux');

INSERT INTO person (password, first_name, last_name, email, role)
VALUES ('$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'Alice', 'Smith', 'alice@gamestore.com',
        'ADMIN'),
       ('$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'John', 'Doe', 'john@gamestore.com', 'ADMIN');
-- pass 111

INSERT INTO game (title, description, price, release_date, image_url, created_at, updated_at)
VALUES ('Elden Ring', 'A massive open-world RPG.', 59.99, '2022-02-25', 'http://store.com/elden.jpg', NOW(), NOW()),
       ('Stardew Valley', 'A peaceful farming simulator.', 14.99, '2016-02-26', 'http://store.com/stardew.jpg', NOW(),
        NOW()),
       ('Cyberpunk 2077', 'Open-world action in Night City.', 49.99, '2020-12-10', 'http://store.com/cp2077.jpg', NOW(),
        NOW()),
       ('Hollow Knight', 'Epic 2D metroidvania.', 15.00, '2017-02-24', 'http://store.com/hollow.jpg', NOW(), NOW()),
       ('Civilization VI', 'Turn-based strategy epic.', 59.99, '2016-10-21', 'http://store.com/civ6.jpg', NOW(), NOW()),
       ('Hades', 'God-like rogue-like dungeon crawler.', 24.99, '2020-09-17', 'http://store.com/hades.jpg', NOW(),
        NOW()),
       ('Baldur''s Gate 3', 'Party-based RPG set in D&D world.', 59.99, '2023-08-03', 'http://store.com/bg3.jpg', NOW(),
        NOW()),
       ('Factorio', 'Build and maintain factories.', 35.00, '2020-08-14', 'http://store.com/factorio.jpg', NOW(),
        NOW()),
       ('The Witcher 3', 'Story-driven open world RPG.', 39.99, '2015-05-19', 'http://store.com/witcher3.jpg', NOW(),
        NOW()),
       ('Slay the Spire', 'Roguelike deckbuilder.', 24.99, '2019-01-23', 'http://store.com/slay.jpg', NOW(), NOW());

-- Mapping Genres (Action=1, RPG=2, Strategy=3, Indie=4, Adventure=5)
INSERT INTO game_genre (game_id, genre_id)
VALUES (1, 2),
       (2, 4),
       (3, 1),
       (4, 4),
       (5, 3),
       (6, 1),
       (7, 2),
       (8, 3),
       (9, 2),
       (10, 4);

-- Mapping Platforms (PC=1, PS5=2, Xbox=3, Switch=4, Linux=5)
INSERT INTO game_platform (game_id, platform_id)
VALUES (1, 1),
       (1, 2),
       (1, 3), -- Elden Ring on PC, PS5, Xbox
       (2, 1),
       (2, 4), -- Stardew on PC, Switch
       (3, 1),
       (3, 2), -- Cyberpunk on PC, PS5
       (4, 1),
       (4, 4),
       (4, 5), -- Hollow Knight on PC, Switch, Linux
       (5, 1), -- Civ VI on PC
       (6, 1),
       (6, 4), -- Hades on PC, Switch
       (7, 1),
       (7, 2), -- BG3 on PC, PS5
       (8, 1),
       (8, 5), -- Factorio on PC, Linux
       (9, 1),
       (9, 2),
       (9, 3), -- Witcher 3 on PC, PS5, Xbox
       (10, 1),
       (10, 4); -- Slay the Spire on PC, Switch
