package com.winterhack.altimetrik.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.winterhack.altimetrik.dao.PropertyDAO;
import com.winterhack.altimetrik.manager.SolrDocumentManager;
import com.winterhack.altimetrik.to.DocumentTO;
import com.winterhack.altimetrik.util.CryptUtil;
import com.winterhack.altimetrik.util.StringUtil;


@Controller
public class DocumentController {

	@Autowired
	private PropertyDAO propertyDAO;
	
	@Autowired
	private CryptUtil cryptUtil;
	
	@Autowired
	private SolrDocumentManager solrDocumentManager;
   
    private final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @RequestMapping(value = "/getDocuments", method = RequestMethod.GET)
    public @ResponseBody JsonArray getDocuments(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) {
        try {
        	List<DocumentTO> documentTOList = solrDocumentManager.getDataFromSolr(); 
        	String nativeStoreLocation = propertyDAO.getNativeStoreLocation();
            JsonArray jsonArray = new JsonArray();
            int i = 0;
            for(DocumentTO document: documentTOList){
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("index", i++);
                jsonObject.addProperty("name", StringUtil.removeBracket(document.getName()));
                jsonObject.addProperty("emailid", StringUtil.removeBracket(document.getEmailId()));
                jsonObject.addProperty("phonenumber", StringUtil.removeBracket(document.getPhoneNumber()));
                jsonObject.addProperty("analysticskey", StringUtil.removeBracket(document.getAnalyticsKey()));
                if(document.getFileName().startsWith(nativeStoreLocation)){                	
                	String filename = StringUtil.removeBracket(document.getFileName()).substring(nativeStoreLocation.length());                	
                	jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                }else{
                	String filename = StringUtil.removeBracket(document.getFileName());                	
                	jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                }
                jsonObject.addProperty("status", StringUtil.removeBracket(document.getStatus()));
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return new JsonArray();
        }
    }
    
    @RequestMapping(value = "/downloadFile/{file_name}", method = RequestMethod.GET)
    public void downloadFile(@PathVariable("file_name") String fileName, HttpServletResponse response) {
        try {
        	String decryptedFileName = cryptUtil.decrypt(fileName);
        	String nativeStoreLocation = propertyDAO.getNativeStoreLocation();
        	String fullPath = nativeStoreLocation + File.separatorChar +decryptedFileName;
        	File file = new File(fullPath);
        	String filename = file.getName();
        	String extention = FilenameUtils.getExtension(filename);

        	FileInputStream fileInputStream = (FileInputStream) new FileInputStream(file);
    		response.setContentType("text/html");
    		PrintWriter out = response.getWriter();
    		response.setContentType("APPLICATION/OCTET-STREAM");
    		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    		int i;
    		while ((i = fileInputStream.read()) != -1) {
    			out.write(i);
    		}
    		fileInputStream.close();
    		out.close();
            
        } catch (IOException ex) {
        	logger.error(ex.toString(), ex);
          throw new RuntimeException("IOError writing file to output stream");
        }
        catch (Exception ex) {
        	logger.error(ex.toString(), ex);
        }
        
    }
    
    @RequestMapping(value = "/searchDocument", method = RequestMethod.GET)
    public @ResponseBody JsonArray searchDocument(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
        try {
        	String filter = request.getParameter("filter") != null ? request.getParameter("filter") : "";
            String filtervalue = request.getParameter("filtervalue") != null ? request.getParameter("filtervalue") : "";
            List<DocumentTO> documentTOList = solrDocumentManager.getDataFromSolr(); 
        	String nativeStoreLocation = propertyDAO.getNativeStoreLocation();
            JsonArray jsonArray = new JsonArray();
            int i = 0;
            for(DocumentTO document: documentTOList){
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("index", i++);
                jsonObject.addProperty("name", StringUtil.removeBracket(document.getName()));
                jsonObject.addProperty("analysticskey", StringUtil.removeBracket(document.getAnalyticsKey()));
                jsonObject.addProperty("phonenumber", StringUtil.removeBracket(document.getPhoneNumber()));
                jsonObject.addProperty("emailid", StringUtil.removeBracket(document.getEmailId()));
                if(document.getFileName().startsWith(nativeStoreLocation)){                	
                	String filename = StringUtil.removeBracket(document.getFileName()).substring(nativeStoreLocation.length());                	
                	jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                }else{
                	String filename = StringUtil.removeBracket(document.getFileName());                	
                	jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                }
                jsonObject.addProperty("status", StringUtil.removeBracket(document.getStatus()));
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        } 
        catch (Exception ex) {
        	logger.error(ex.toString(), ex);
            return new JsonArray();
        }
        
    }
    
    @RequestMapping(value = "/getCandidateDetails/{analysticskey}", method = RequestMethod.GET)
    public @ResponseBody JsonObject getCandidateDetails(@PathVariable("analysticskey") String analysticsKey, HttpServletRequest request, HttpServletResponse response,HttpSession session) {
        try {
        	String filter = request.getParameter("filter") != null ? request.getParameter("filter") : "";
            String filtervalue = request.getParameter("filtervalue") != null ? request.getParameter("filtervalue") : "";
            List<DocumentTO> documentTOList = solrDocumentManager.getDataFromSolr(); 
        	String nativeStoreLocation = propertyDAO.getNativeStoreLocation();
            int i = 0;
            JsonObject jsonObject = new JsonObject();
            for(DocumentTO document: documentTOList){
                jsonObject.addProperty("name", StringUtil.removeBracket(document.getName()));
                jsonObject.addProperty("analysticskey", StringUtil.removeBracket(document.getAnalyticsKey()));
                jsonObject.addProperty("phonenumber", StringUtil.removeBracket(document.getPhoneNumber()));
                jsonObject.addProperty("emailid", StringUtil.removeBracket(document.getEmailId()));
                if(document.getFileName().startsWith(nativeStoreLocation)){                	
                	String filename = StringUtil.removeBracket(document.getFileName()).substring(nativeStoreLocation.length());                	
                	jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                }else{
                	String filename = StringUtil.removeBracket(document.getFileName());                	
                	jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                }
                jsonObject.addProperty("status", StringUtil.removeBracket(document.getStatus()));
                
            }
            return jsonObject;
        } 
        catch (Exception ex) {
        	logger.error(ex.toString(), ex);
            return new JsonObject();
        }        
    }

	public PropertyDAO getPropertyDAO() {
		return propertyDAO;
	}

	public void setPropertyDAO(PropertyDAO propertyDAO) {
		this.propertyDAO = propertyDAO;
	}

	public CryptUtil getCryptUtil() {
		return cryptUtil;
	}

	public void setCryptUtil(CryptUtil cryptUtil) {
		this.cryptUtil = cryptUtil;
	}

	public SolrDocumentManager getSolrDocumentManager() {
		return solrDocumentManager;
	}

	public void setSolrDocumentManager(SolrDocumentManager solrDocumentManager) {
		this.solrDocumentManager = solrDocumentManager;
	}    
}
