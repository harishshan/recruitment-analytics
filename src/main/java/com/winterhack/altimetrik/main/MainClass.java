package com.winterhack.altimetrik.main;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class MainClass {
    public static void main123(String[] args) throws SolrServerException, IOException {
        // String location = "/Users/ashokram/Ashok/Application/Resumes/AlbanM[9_0].doc";
        // FileInputStream fileInputStream = FileUtils.openInputStream(new File(location));
        SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/recrutement").build();
        SolrInputDocument document = new SolrInputDocument();
        document.addField("analyticsKey", "1");
        document.addField("emailId", "albankumar1@gmail.com");
        document.addField("name", "AlbanM");
        document.addField("fileName", "AlbanM[9_0]1.doc");
        document.addField("status", "new");

        SolrInputDocument document1 = new SolrInputDocument();
        document1.addField("analyticsKey", "2");
        document1.addField("emailId", "albankumar2@gmail.com");
        document1.addField("name", "AlbanM");
        document1.addField("fileName", "AlbanM[9_0]2.doc");
        document1.addField("status", "processing");
        document1.addField("Round1",
                "4@_@4@_@4@_@4@_@selected@_@In ancient manuscripts, another means to divide sentences into paragraphs was a line break (newline) followed by an initial at the beginning of the next paragraph. An initial is an oversize capital letter, sometimes outdented beyond the margin of text. This style can be seen, for example, in the original Old English manuscript of Beowulf. Outdenting is still used in English typography, though not commonly.[4] Modern English typography usually indicates a new paragraph by indenting the first line. This style can be seen in the (handwritten) United States Constitution from 1787. For additional ornamentation, a hedera leaf or other symbol can be added to the inter-paragraph whitespace, or put in the indentation space. A second common modern English style is to use no indenting, but add vertical whitespace to create \"block paragraphs.\" On a typewriter, a double carriage return produces a blank line for this purpose; professional typesetters (or word processing software) may put in an word.@_@@_@In ancient manuscripts, another means to divide sentences into paragraphs was a line break (newline) followed by an initial at the beginning of the next paragraph. An initial is an oversize capital letter, sometimes outdented beyond the margin of text. This style can be seen, for example, in the original Old English manuscript of Beowulf. Outdenting is still used in English typography, though not commonly.[4] Modern English typography usually indicates a new paragraph by indenting the first line. This style can be seen in the (handwritten) United States Constitution from 1787. For additional ornamentation, a hedera leaf or other symbol can be added to the inter-paragraph whitespace, or put in the indentation space. A second common modern English style is to use no indenting, but add vertical whitespace to create \"block paragraphs.\" On a typewriter, a double carriage return produces a blank line for this purpose; professional typesetters (or word processing software) may put in an word.");

        SolrInputDocument document2 = new SolrInputDocument();
        document2.addField("analyticsKey", "3");
        document2.addField("emailId", "albankumar3@gmail.com");
        document2.addField("name", "AlbanM");
        document2.addField("fileName", "AlbanM[9_0]3.doc");
        document2.addField("status", "processing");
        document2.addField("Round1", "4@_@4@_@4@_@4@_@selected");
        document2.addField("Round2", "4@_@4@_@4@_@4@_@selected");

        SolrInputDocument document3 = new SolrInputDocument();
        document3.addField("analyticsKey", "4");
        document3.addField("emailId", "albankumar4@gmail.com");
        document3.addField("name", "AlbanM");
        document3.addField("fileName", "AlbanM[9_0]4.doc");
        document3.addField("status", "rejected");
        document3.addField("Round1", "3@_@3@_@2@_@4@_@selected");
        document3.addField("Round2", "2@_@2@_@2@_@2@_@rejected");

        SolrInputDocument document4 = new SolrInputDocument();
        document4.addField("analyticsKey", "5");
        document4.addField("emailId", "albankumar4@gmail.com");
        document4.addField("name", "AlbanM");
        document4.addField("fileName", "AlbanM[9_0]5.doc");
        document4.addField("status", "selected");
        document4.addField("Round1", "3@_@3@_@2@_@4@_@selected@_@Interviewr Comment@_@RecruterComment");
        document4.addField("Round2", "4@_@4@_@4@_@4@_@selected@_@Interviewr Comment@_@RecruterComment");
        document4.addField("Round3", "4@_@4@_@4@_@4@_@selected@_@Interviewr Comment@_@RecruterComment");
        document4.addField("Round4", "4@_@4@_@4@_@4@_@selected@_@Interviewr Comment@_@RecruterComment");

        solr.add(document);
        solr.add(document1);
        solr.add(document2);
        solr.add(document3);
        solr.add(document4);
        solr.commit();
        solr.close();
    }

    public static void main(String[] args) throws SolrServerException, IOException {
        SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/recrutement").build();
        SolrQuery query = new SolrQuery();
        query.set("q", "analyticsKey:4");
        QueryResponse response = solr.query(query);
        SolrDocumentList list = response.getResults();
        for (SolrDocument document : list) {
            System.out.println(document.getFieldNames());
            String solrID = (String) document.getFieldValue("id");
            solr.deleteById(solrID);
            solr.commit();
            SolrInputDocument document3 = new SolrInputDocument();
            document3.addField("analyticsKey", "4");
            document3.addField("emailId", "albankumar4@gmail.com");
            document3.addField("name", "AlbanM");
            document3.addField("fileName", "AlbanM[9_0]4.doc");
            document3.addField("status", "selected");
            document3.addField("Round1", "3@_@3@_@2@_@4@_@selected");
            document3.addField("Round2", "4@_@3@_@4@_@3@_@selected");
            document3.addField("Round3", "4@_@3@_@4@_@3@_@selected");
            solr.add(document3);
            solr.commit();
        }
    }
}
