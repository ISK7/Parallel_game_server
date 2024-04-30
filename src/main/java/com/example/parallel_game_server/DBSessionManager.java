package com.example.parallel_game_server;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class DBSessionManager {
    private static SessionFactory sessionFactory;

    private DBSessionManager() {}

    public static SessionFactory getSessionFactory() {
        if(sessionFactory == null) {
            try {
                Configuration conf = new Configuration().configure();
                conf.addAnnotatedClass(DBPlayer.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(conf.getProperties());
                sessionFactory = conf.buildSessionFactory(builder.build());

            }
            catch (Exception e) {
                System.out.println("getSession Exception!: " + e);
            }
        }
        return sessionFactory;
    }


    public static boolean newPlayerInGame(String name, String password) {
        Session s = getSessionFactory().openSession();
        Transaction ta1 = s.beginTransaction();

        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<DBPlayer> criteria = builder.createQuery(DBPlayer.class);
        criteria.from(DBPlayer.class);
        List<DBPlayer> pls = s.createQuery(criteria).getResultList();

        for(DBPlayer p : pls) {
            if(p.getName().equals(name)) return true;
        }

        DBPlayer pl = new DBPlayer();
        pl.setId(pls.size()+1);
        pl.setName(name);
        pl.setPassword(password);
        pl.setWins(0);
        s.save(pl);

        ta1.commit();
        s.close();
        return true;
    }

    public static void addWin(String name) {
        Session s = getSessionFactory().openSession();
        Transaction ta1 = s.beginTransaction();

        String ask = "FROM DBPlayer where name = '" + name + "'";
        List<DBPlayer> pls = s.createQuery(ask, DBPlayer.class).getResultList();
        DBPlayer pl = pls.get(0);
        pl.setWins(pl.getWins() + 1);
        s.saveOrUpdate(pl);

        ta1.commit();
        s.close();
    }

    public static ArrayList<LeaderData> getWinners() {
        Session s = getSessionFactory().openSession();
        Transaction ta1 = s.beginTransaction();
        List<DBPlayer> pls = s.createQuery("FROM DBPlayer ORDER BY player_win_count DESC", DBPlayer.class).getResultList();
        ArrayList<LeaderData> leaders = new ArrayList<>();
        int i = 10;
        for(DBPlayer p : pls) {
            LeaderData ld = new LeaderData(p.getName(),p.getWins());
            leaders.add(ld);
            i--;
            if(i == 0) break;
        }
        ta1.commit();
        s.close();
        return leaders;
    }
}
