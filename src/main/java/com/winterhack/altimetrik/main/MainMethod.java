package com.winterhack.altimetrik.main;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.winterhack.altimetrik.manager.TikaExtractorManager;

public class MainMethod {
    public static void main(String[] args) throws Exception {
        SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/recrutement").build();
        SolrInputDocument document = new SolrInputDocument();
        document.addField("analyticsKey", "9");
        document.addField("emailId", "lekhathakur2007@gmail.com");
        document.addField("name", "LEKHA");
        document.addField("fileName", "Lekha2_8 (3)[1].pdf");
        document.addField("phoneNumber", "8802767982");
        document.addField("status", "new");
        TikaExtractorManager extractorManager = new TikaExtractorManager();
        String content = extractorManager
                .getContentFromFile("/Users/ashokram/Ashok/data/" + document.getFieldValue("fileName"));
        document.addField("content", content);
        solr.add(document);
        solr.commit();
        solr.close();
    }
}