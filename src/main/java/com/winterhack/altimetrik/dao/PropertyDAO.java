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

public class PropertyDAO {

    private SessionFactory sessionFactory;

    private final Logger logger = LoggerFactory.getLogger(PropertyDAO.class);

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Property> getPropertyList() {
        Session session = null;
        try {
            logger.info("PropertyDAO getPropertyList method");
            session = this.sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Property.class, "property");
            List<Property> propertiesList = criteria.list();
            return propertiesList;
        } catch (Exception ex) {
            return null;
        } finally {
            if (session != null && (session.isConnected() || session.isOpen())) {
                session.close();
            }
        }
    }

    public void updateProperties(List<Property> propertiesList) {
        Session session = null;
        Transaction tx = null;
        try {
            logger.info("PropertyDAO updateProperties method");
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();
            for (Property property : propertiesList) {
                Criteria criteria = session.createCriteria(Property.class);
                criteria.add(Restrictions.eq("name", property.getName()));
                List<Property> newPropertiesList = criteria.list();
                if (newPropertiesList.size() > 0) {
                    newPropertiesList.get(0).setValue(property.getValue());
                    session.update(newPropertiesList.get(0));
                }
            }
            tx.commit();
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
}
