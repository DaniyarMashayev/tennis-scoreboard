package org.example.tennisscoreboard.dao.implementation;

import org.example.tennisscoreboard.dao.PlayerDao;
import org.example.tennisscoreboard.model.Player;
import org.example.tennisscoreboard.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class HibernatePlayerDao implements PlayerDao {

    @Override
    public Player save(Player player) {
        Session session = SessionManager.getCurrentSession();
        if (player.getId() == null) {
            session.persist(player);
            return player;
        } else {
            return session.merge(player);
        }
    }

    @Override
    public Optional<Player> findById(Integer id) {
        Session session = SessionManager.getCurrentSession();
        Player player = session.get(Player.class, id);
        return Optional.ofNullable(player);
    }

    @Override
    public List<Player> findAll() {
        Session session = SessionManager.getCurrentSession();
        Query<Player> query = session.createQuery(
                "FROM Player", Player.class
        );
        return query.getResultList();
    }

    @Override
    public Optional<Player> findByName(String name) {
        Session session = SessionManager.getCurrentSession();
        Query<Player> query = session.createQuery(
                "FROM Player p WHERE p.name = :name", Player.class
        );
        query.setParameter("name", name);
        List<Player> players = query.getResultList();
        return players.stream().findFirst();
    }

    @Override
    public void delete(Player player) {
        Session session = SessionManager.getCurrentSession();
        session.remove(player);
    }

    @Override
    public void deleteById(Integer id) {
        Session session = SessionManager.getCurrentSession();
        MutationQuery query = session.createMutationQuery(
                "DELETE FROM Player WHERE id = :id"
        );
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public boolean existsByName(String name) {
        Session session = SessionManager.getCurrentSession();
        Query<Integer> query = session.createQuery(
                "SELECT COUNT(*) FROM Player WHERE name = :name", Integer.class
        );
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }

}