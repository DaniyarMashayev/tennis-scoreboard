package org.example.tennisscoreboard.dao.implementation; import org.example.tennisscoreboard.dao.MatchDao; import org.example.tennisscoreboard.model.Match;
import org.example.tennisscoreboard.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class HibernateMatchDao implements MatchDao {

    @Override
    public void save(Match match) {
        Session session = SessionManager.getCurrentSession();
        if (match.getId() == null) {
            session.persist(match);
        }
    }

    @Override
    public Optional<Match> findById(Integer id) {
        Session session = SessionManager.getCurrentSession();
        Match match = session.get(Match.class, id);
        return Optional.ofNullable(match);
    }

//    public Optional<Player> findWinner() {
//        Session session = SessionManager.getCurrentSession();
//
//    }

    @Override
    public List<Match> findAll() {
        Session session = SessionManager.getCurrentSession();
        Query<Match> query = session.createQuery(
                "FROM Match", Match.class
        );
        return query.getResultList();
    }

    @Override
    public void delete(Match match) {
        Session session = SessionManager.getCurrentSession();
        session.remove(match);
    }

    @Override
    public boolean exists(Match match) {
        Session session = SessionManager.getCurrentSession();
        Query<Integer> query = session.createQuery(
                "SELECT COUNT(*) FROM Match WHERE match = :match", Integer.class
        );
        query.setParameter("match", match);
        return query.getSingleResult() > 0;
    }


}
