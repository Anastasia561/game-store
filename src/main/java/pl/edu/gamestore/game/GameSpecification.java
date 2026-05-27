package pl.edu.gamestore.game;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import pl.edu.gamestore.game.dto.GameFilterDto;
import pl.edu.gamestore.genre.Genre;
import pl.edu.gamestore.platform.Platform;

import java.util.ArrayList;
import java.util.List;

class GameSpecification {
    public static Specification<Game> buildFilter(GameFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.title() != null && !filter.title().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("title")),
                        "%" + filter.title().toLowerCase() + "%"
                ));
            }

            if (filter.genre() != null && !filter.genre().isBlank()) {
                Join<Game, Genre> genreJoin = root.join("genres");
                predicates.add(cb.equal(
                        cb.lower(genreJoin.get("name")),
                        filter.genre().toLowerCase()
                ));
            }

            if (filter.platform() != null && !filter.platform().isBlank()) {
                Join<Game, Platform> platformJoin = root.join("platforms");
                predicates.add(cb.equal(
                        cb.lower(platformJoin.get("name")),
                        filter.platform().toLowerCase()
                ));
            }

            if (query.getResultType() != Long.class) {
                root.fetch("genres", JoinType.LEFT);
                root.fetch("platforms", JoinType.LEFT);
                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
