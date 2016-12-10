package com.winterhack.altimetrik.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winterhack.altimetrik.constants.CommonConstants;
import com.winterhack.altimetrik.entity.Property;
import com.winterhack.altimetrik.entity.Round;
import com.winterhack.altimetrik.exception.UserAlreadyExistsException;

public class RoundDAO {

    private SessionFactory sessionFactory;

    private final Logger logger = LoggerFactory.getLogger(RoundDAO.class);

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Round> getRoundList() {
        Session session = null;
        try {
            logger.info("RoundDAO getRoundList method");
            session = this.sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Round.class, "round");
            List<Round> roundList = criteria.list();
            return roundList;
        } catch (Exception ex) {
            return null;
        } finally {
            if (session != null && (session.isConnected() || session.isOpen())) {
                session.close();
            }
        }
    }

        
    public String getNativeStoreLocation() {
        Session session = null;            
        try {
        	logger.info("PropertyDAO getNativeStoreLocation method");
            session = this.sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Property.class, "property");
            criteria.add(Restrictions.eq("name", CommonConstants.NATIVE_STORE_LOCATION));
            List<Property> propertiesList = criteria.list();
            if(propertiesList.size() > 0){
            	return propertiesList.get(0).getValue();
            }
            return "";                
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);    
            return "";
        } finally {
        	if (session != null && (session.isConnected() || session.isOpen())) {
                session.close();
            }            	
        }
    }
    public void save(Round round) throws UserAlreadyExistsException {
        Session session = null;
        Transaction tx = null;
        try {
            logger.info("RoundDAO save method");
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();
            session.save(round);
            tx.commit();
        }
         catch (Exception ex) {
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
