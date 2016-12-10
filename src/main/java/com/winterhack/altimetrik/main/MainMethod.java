package com.winterhack.altimetrik.main;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.winterhack.altimetrik.manager.TikaExtractorManager;

public class MainMethod {
    public static void main(String[] args) throws Exception {
        SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/recrutement").build();
        SolrInputDocument document = new SolrInputDocument();
        document.addField("analyticsKey", "8");
        document.addField("emailId", "arulmanivel11@gmail.com");
        document.addField("name", "Arulmani M");
        document.addField("fileName", "MArulmani[4_6] (2).doc");
        document.addField("phoneNumber", "91-8870883030");
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
