-- foreign keys
-- Reference: Table_4_game (table: game_genre)
ALTER TABLE game_genre
    ADD CONSTRAINT Table_4_game
        FOREIGN KEY (game_id)
            REFERENCES game (id)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: Table_4_genre (table: game_genre)
ALTER TABLE game_genre
    ADD CONSTRAINT Table_4_genre
        FOREIGN KEY (genre_id)
            REFERENCES genre (id)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: Table_5_game (table: game_platform)
ALTER TABLE game_platform
    ADD CONSTRAINT Table_5_game
        FOREIGN KEY (game_id)
            REFERENCES game (id)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: Table_5_platform (table: game_platform)
ALTER TABLE game_platform
    ADD CONSTRAINT Table_5_platform
        FOREIGN KEY (platform_id)
            REFERENCES platform (id)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: refresh_token_person (table: refresh_token)
ALTER TABLE refresh_token
    ADD CONSTRAINT refresh_token_person
        FOREIGN KEY (person_id)
            REFERENCES person (id)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;
