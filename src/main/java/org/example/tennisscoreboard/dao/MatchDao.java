package org.example.tennisscoreboard.dao;

import org.example.tennisscoreboard.model.Match;
import org.example.tennisscoreboard.model.Player;

import java.util.List;
import java.util.Optional;

public interface MatchDao {
    void save(Match match);

    Optional<Match> findById(Integer id);

//    Optional<Player> findWinner();

    List<Match> findAll();

    void delete(Match match);

    boolean exists(Match match);
}
