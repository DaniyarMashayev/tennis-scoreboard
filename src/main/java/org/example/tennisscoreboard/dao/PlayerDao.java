package org.example.tennisscoreboard.dao;

import org.example.tennisscoreboard.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDao {
    Player save(Player player);

    Optional<Player> findById(Integer id);

    List<Player> findAll();

    Optional<Player> findByName(String name);

    void delete(Player player);

    void deleteById(Integer id);

    boolean existsByName(String name);
}
