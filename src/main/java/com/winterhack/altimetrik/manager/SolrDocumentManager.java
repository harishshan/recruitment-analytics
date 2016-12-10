package com.winterhack.altimetrik.manager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.winterhack.altimetrik.to.DocumentTO;

public class SolrDocumentManager {

    private SolrClient solr;

    public SolrDocumentManager() {
        solr = new HttpSolrClient.Builder("http://localhost:8983/solr/recrutement").build();
    }

    public List<DocumentTO> getDataFromSolr() throws SolrServerException, IOException {
        List<DocumentTO> documentTOs = new ArrayList<DocumentTO>();
        SolrQuery query = new SolrQuery();
        query.set("q", "*:*");
        QueryResponse response = solr.query(query);
        SolrDocumentList list = response.getResults();
        list.size();
        for (SolrDocument document : list) {
            DocumentTO documentTO = new DocumentTO();
            documentTO.setEmailId(String.valueOf(document.getFieldValue("emailId")));
            documentTO.setAnalyticsKey(String.valueOf(document.getFieldValue("analyticsKey")));
            documentTO.setFileName(String.valueOf(document.getFieldValue("fileName")));
            documentTO.setName(String.valueOf(document.getFieldValue("name")));
            documentTO.setStatus(String.valueOf(document.getFieldValue("status")));
            documentTO.setPhoneNumber(String.valueOf(document.getFieldValue("phoneNumber")));
            documentTOs.add(documentTO);
        }
        return documentTOs;
    }

    public void index(String emailId, String fileLocation)
            throws SolrServerException, IOException, SAXException, TikaException {
        SolrQuery query = new SolrQuery();
        query.set("q", "*:*");
        QueryResponse response = solr.query(query);
        SolrDocumentList list = response.getResults();
        int index = list.size();
        SolrInputDocument document = new SolrInputDocument();
        document.addField("analyticsKey", ++index + "");
        document.addField("name", "AlbanM");
        Path listing = Paths.get(fileLocation);
        document.addField("fileName", listing.getFileName());
        document.addField("status", "new");
        TikaExtractorManager extractorManager = new TikaExtractorManager();
        String content = extractorManager.getContentFromFile(fileLocation);
        document.addField("content", content);
        document.addField("emailId", extractorManager.getEmailId(content));
        document.addField("phoneNumber", extractorManager.getPhoneNum(content));
        solr.add(document);
        commit();
    }

    public void updateIndex(String solrId, Map<String, Object> fieldsToBeUpdated)
            throws SolrServerException, IOException {
        SolrInputDocument document = new SolrInputDocument();
        for (Map.Entry<String, Object> entry : fieldsToBeUpdated.entrySet()) {
            document.addField(entry.getKey(), entry.getValue());
        }
        solr.add(document);
        commit();
    }

    private void commit() throws SolrServerException, IOException {
        if (null != solr) {
            solr.commit();
        }
    }
}