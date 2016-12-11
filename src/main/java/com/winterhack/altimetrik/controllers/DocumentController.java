package com.winterhack.altimetrik.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.winterhack.altimetrik.dao.RoundDAO;
import com.winterhack.altimetrik.entity.Round;
import com.winterhack.altimetrik.manager.SolrDocumentManager;
import com.winterhack.altimetrik.to.DocumentTO;
import com.winterhack.altimetrik.to.RoundTO;
import com.winterhack.altimetrik.util.CryptUtil;
import com.winterhack.altimetrik.util.MailUtil;
import com.winterhack.altimetrik.util.StringUtil;

@Controller
public class DocumentController {

    @Autowired
    private PropertyDAO propertyDAO;

    @Autowired
    private CryptUtil cryptUtil;

    @Autowired
    private SolrDocumentManager solrDocumentManager;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private RoundDAO roundDAO;

    private final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @RequestMapping(value = "/getDocuments", method = RequestMethod.GET)
    public @ResponseBody JsonArray getDocuments(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) {
        try {
            List<DocumentTO> documentTOList = solrDocumentManager.getDataFromSolr();
            String nativeStoreLocation = propertyDAO.getNativeStoreLocation();
            JsonArray jsonArray = new JsonArray();
            int i = 0;
            for (DocumentTO document : documentTOList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("index", i++);
                jsonObject.addProperty("name", StringUtil.removeBracket(document.getName()));
                jsonObject.addProperty("emailid", StringUtil.removeBracket(document.getEmailId()));
                jsonObject.addProperty("phonenumber", StringUtil.removeBracket(document.getPhoneNumber()));
                jsonObject.addProperty("analysticskey", StringUtil.removeBracket(document.getAnalyticsKey()));
                if (document.getFileName().startsWith(nativeStoreLocation)) {
                    String filename = StringUtil.removeBracket(document.getFileName())
                            .substring(nativeStoreLocation.length());
                    jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                } else {
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
            String fullPath = nativeStoreLocation + File.separatorChar + decryptedFileName;
            File file = new File(fullPath);
            String filename = file.getName();

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
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }

    }

    @RequestMapping(value = "/searchDocument", method = RequestMethod.GET)
    public @ResponseBody JsonArray searchDocument(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) {
        try {
            String filter = request.getParameter("filter") != null ? request.getParameter("filter") : "";
            String filtervalue = request.getParameter("filtervalue") != null ? request.getParameter("filtervalue") : "";
            List<DocumentTO> documentTOList = solrDocumentManager.getDataFromSolrUsingSearch(filter, filtervalue);
            String nativeStoreLocation = propertyDAO.getNativeStoreLocation();
            JsonArray jsonArray = new JsonArray();
            int i = 0;
            for (DocumentTO document : documentTOList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("index", i++);
                jsonObject.addProperty("name", StringUtil.removeBracket(document.getName()));
                jsonObject.addProperty("analysticskey", StringUtil.removeBracket(document.getAnalyticsKey()));
                jsonObject.addProperty("phonenumber", StringUtil.removeBracket(document.getPhoneNumber()));
                jsonObject.addProperty("emailid", StringUtil.removeBracket(document.getEmailId()));
                if (document.getFileName().startsWith(nativeStoreLocation)) {
                    String filename = StringUtil.removeBracket(document.getFileName())
                            .substring(nativeStoreLocation.length());
                    jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                } else {
                    String filename = StringUtil.removeBracket(document.getFileName());
                    jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                }
                jsonObject.addProperty("status", StringUtil.removeBracket(document.getStatus()));
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return new JsonArray();
        }

    }

    @RequestMapping(value = "/getCandidateDetails/{analysticskey}", method = RequestMethod.GET)
    public @ResponseBody JsonObject getCandidateDetails(@PathVariable("analysticskey") String analysticsKey,
            HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            List<DocumentTO> documentTOList = solrDocumentManager.getDataFromSolrUsingAnalyticsKey(analysticsKey);
            String nativeStoreLocation = propertyDAO.getNativeStoreLocation();
            JsonObject jsonObject = new JsonObject();
            for (DocumentTO document : documentTOList) {
                jsonObject.addProperty("name", StringUtil.removeBracket(document.getName()));
                jsonObject.addProperty("analysticskey", StringUtil.removeBracket(document.getAnalyticsKey()));
                jsonObject.addProperty("phonenumber", StringUtil.removeBracket(document.getPhoneNumber()));
                jsonObject.addProperty("emailid", StringUtil.removeBracket(document.getEmailId()));
                if (document.getFileName().startsWith(nativeStoreLocation)) {
                    String filename = StringUtil.removeBracket(document.getFileName())
                            .substring(nativeStoreLocation.length());
                    jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                } else {
                    String filename = StringUtil.removeBracket(document.getFileName());
                    jsonObject.addProperty("filename", cryptUtil.encrypt(filename));
                }
                jsonObject.addProperty("status", StringUtil.removeBracket(document.getStatus()));
                if (document.getRoundTO() != null) {
                    jsonObject.addProperty("rountcount", document.getRoundTO().size());
                    int j = 0;
                    JsonArray roundArray = new JsonArray();
                    for (RoundTO round : document.getRoundTO()) {
                        JsonObject jo = new JsonObject();
                        jsonObject.addProperty("index", j++);
                        jsonObject.addProperty("analytics", round.getAnalyticalSkill());
                        jsonObject.addProperty("attitue", round.getAttitude());
                        jsonObject.addProperty("coding", round.getCodingSkill());
                        jsonObject.addProperty("problem", round.getProblemSolvingSkill());
                        jsonObject.addProperty("status", round.getRoundStatus());
                        jsonObject.addProperty("icomment", round.getInterviewerComment());
                        jsonObject.addProperty("rcomment", round.getRecruterComment());
                        // roundArray.add(jsonObject);

                    }

                    jsonObject.add("rounds", roundArray);
                } else
                    jsonObject.addProperty("rountcount", "0");

            }
            return jsonObject;
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return new JsonObject();
        }
    }

    @RequestMapping(value = "/scheduleInterview", method = RequestMethod.GET)
    public String scheduleInterview(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            String emailid = request.getParameter("emailid") != null ? request.getParameter("emailid") : "";
            String analysticskey = request.getParameter("analysticskey") != null ? request.getParameter("analysticskey")
                    : "";
            String msg = "Interveiw Link: http://localhost:8080/recruitmentanalytics/interview?analysticskey="
                    + analysticskey;
            mailUtil.sendMail("altirecrute@gmail.com", emailid, "Inverview Link", msg);
            return "scheduledSuccessfully";
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return "failed";
        }
    }

    @RequestMapping(value = "/interview", method = RequestMethod.GET)
    public void interview(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            String analysticskey = request.getParameter("analysticskey") != null ? request.getParameter("analysticskey")
                    : "";
            RequestDispatcher dispatcher = request.getSession().getServletContext().getRequestDispatcher(
                    "/interviewpage.jsp?analysticskey=" + URLEncoder.encode(analysticskey, "UTF-8"));
            dispatcher.forward(request, response);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);

        }
    }

    @RequestMapping(value = "/subitFeedback", method = RequestMethod.POST)
    public String subitFeedback(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            String score = request.getParameter("score") != null ? request.getParameter("score") : "";
            String feedback = request.getParameter("feedback") != null ? request.getParameter("feedback") : "";
            int count = roundDAO.getRoundList().size();
            Round round = new Round();
            round.setRound(count);
            round.setFeedback(feedback);
            round.setScore(Integer.parseInt(score));
            round.setEmployee("ashok");
            roundDAO.save(round);
            return "feedbackSubmitted";
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return "feedbackSubmitted";
        }
    }

    @RequestMapping(value = "/getRounds", method = RequestMethod.GET)
    public @ResponseBody JsonArray getRounds(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) {
        try {
            List<Round> roundList = roundDAO.getRoundList();
            JsonArray jsonArray = new JsonArray();
            int i = 0;
            for (Round round : roundList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("index", i++);
                jsonObject.addProperty("round", round.getRound());
                jsonObject.addProperty("score", round.getScore());
                jsonObject.addProperty("feedback", round.getFeedback());
                jsonObject.addProperty("employee", round.getEmployee());
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return new JsonArray();
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

    public MailUtil getMailUtil() {
        return mailUtil;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

}
