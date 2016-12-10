package com.winterhack.altimetrik.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.winterhack.altimetrik.manager.TikaExtractorManager;

public class Entrys {
    public static void main(String[] args) throws Exception {
        File folder = new File("/Users/ashokram/Ashok/data/");
        File[] listOfFiles = folder.listFiles();
        int i = 1;
        for (File file : listOfFiles) {
            if (!(file.getName().endsWith("docx") || file.getName().endsWith("pdf") || file.getName().endsWith("doc"))
                    || file.getName().endsWith("PDF")) {
                continue;
            }
            System.out.println(file.getName());
            SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/recrutement").build();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            SolrInputDocument document = new SolrInputDocument();
            document.addField("analyticsKey", i + "");
            System.out.print("Enter Email : ");
            document.addField("emailId", reader.readLine());
            System.out.print("Enter Name : ");
            document.addField("name", reader.readLine());
            document.addField("fileName", file.getName());
            System.out.print("Enter Ph. No : ");
            document.addField("phoneNumber", reader.readLine());
            document.addField("status", "new");
            TikaExtractorManager extractorManager = new TikaExtractorManager();
            String content = extractorManager
                    .getContentFromFile("/Users/ashokram/Ashok/data/" + document.getFieldValue("fileName"));
            document.addField("content", content);
            solr.add(document);
            solr.commit();
            solr.close();
            i++;
        }
    }
}