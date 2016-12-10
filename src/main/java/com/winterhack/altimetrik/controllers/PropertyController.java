package com.winterhack.altimetrik.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.winterhack.altimetrik.dao.PropertyDAO;
import com.winterhack.altimetrik.entity.Property;
import com.winterhack.altimetrik.util.JsonUtil;

@Controller
public class PropertyController {

    @Autowired
    private PropertyDAO propertyDAO;

    @Autowired
    private JsonUtil jsonUtil;

    private final Logger logger = LoggerFactory.getLogger(PropertyController.class);

    @RequestMapping(value = "/getProperties", method = RequestMethod.GET)
    public @ResponseBody JsonArray getProperties(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) {
        try {
            List<Property> propertiesList = propertyDAO.getPropertyList();
            JsonArray jsonArray = new JsonArray();
            int i = 0;
            for (Property property : propertiesList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("index", i++);
                jsonObject.addProperty("name", property.getName());
                jsonObject.addProperty("value", property.getValue());
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return new JsonArray();
        }
    }

    @RequestMapping(value = "/updateProperties", method = RequestMethod.POST)
    public @ResponseBody String updateProperties(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) {
        try {
            List<Property> propertiesList = propertyDAO.getPropertyList();
            List<Property> newPropertiesList = propertyDAO.getPropertyList();
            for (Property property : propertiesList) {
                Property newProperty = new Property();
                newProperty.setName(property.getName());
                newProperty.setValue(request.getParameter(property.getName()) != null
                        ? request.getParameter(property.getName()) : "");
                newPropertiesList.add(newProperty);
            }
            propertyDAO.updateProperties(newPropertiesList);
            return "Successfully Updated";
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return "Error";
        }
    }

    public PropertyDAO getPropertyDAO() {
        return propertyDAO;
    }

    public void setPropertyDAO(PropertyDAO propertyDAO) {
        this.propertyDAO = propertyDAO;
    }

    public JsonUtil getJsonUtil() {
        return jsonUtil;
    }

    public void setJsonUtil(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
    }
}
