package com.winterhack.altimetrik.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winterhack.altimetrik.entity.Login;
import com.winterhack.altimetrik.entity.Role;
import com.winterhack.altimetrik.exception.UserAlreadyExistsException;

public class LoginDAO {

    private SessionFactory sessionFactory;

    private final Logger logger = LoggerFactory.getLogger(LoginDAO.class);

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Login findByUsernameAndPassword(Login login) {
        Session session = null;
        try {
            logger.info("LoginDAO findByUsernameAndPassword method");
            session = this.sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Login.class, "login");
            criteria.add(Restrictions.eq("login.username", login.getUsername()));
            criteria.add(Restrictions.eq("login.password", login.getPassword()));
            criteria.createAlias("login.role", "role");
            criteria.add(Restrictions.eq("role.rolename", login.getRole().getRolename()));
            List<Login> loginList = criteria.list();
            return loginList.get(0);
        } catch (Exception ex) {
            // logger.error(ex.toString(),ex);
            return null;
        } finally {
            if (session != null && (session.isConnected() || session.isOpen())) {
                session.close();
            }
        }
    }

    public List<Login> getLoginList() {
        Session session = null;
        try {
            logger.info("LoginDAO getLoginList method");
            session = this.sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Login.class, "login");
            criteria.createAlias("login.role", "role");
            List<Login> loginList = criteria.list();
            return loginList;
        } catch (Exception ex) {
            // logger.error(ex.toString(),ex);
            return null;
        } finally {
            if (session != null && (session.isConnected() || session.isOpen())) {
                session.close();
            }
        }
    }

    public void save(Login login) throws UserAlreadyExistsException {
        Session session = null;
        Transaction tx = null;
        try {
            logger.info("LoginDAO save method");
            session = this.sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Role.class, "role");
            criteria.add(Restrictions.eq("role.rolename", login.getRole().getRolename()));
            List<Role> roleList = criteria.list();
            if (roleList.size() > 0) {
                login.setRole(roleList.get(0));
            }
            tx = session.beginTransaction();
            session.save(login);
            tx.commit();
        } catch (ConstraintViolationException ex) {
            throw new UserAlreadyExistsException("User already exist, So we can not create user with same name");
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            if (session != null && (session.isConnected() || session.isOpen()) && tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null && (session.isConnected() || session.isOpen())) {
                session.close();
            }
        }
    }
}
