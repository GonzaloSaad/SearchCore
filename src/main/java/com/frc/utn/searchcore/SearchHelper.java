package com.frc.utn.searchcore;

import com.frc.utn.searchcore.files.FileParser;
import com.frc.utn.searchcore.io.cache.Cache;
import com.frc.utn.searchcore.io.cache.SearchCache;
import com.frc.utn.searchcore.io.management.DocumentManagement;
import com.frc.utn.searchcore.model.Document;
import com.frc.utn.searchcore.model.PostList;
import com.frc.utn.searchcore.model.PostListItem;
import com.frc.utn.searchcore.model.VocabularyEntry;

import java.util.*;
import java.util.logging.Logger;

public class SearchHelper {

    private static Logger logger = Logger.getLogger(SearchHelper.class.getName());
    private Cache cache;

    public SearchHelper() {
        startCache();
    }

    private void startCache() {
        cache = new SearchCache(DLCConstants.SEARCH_CACHE_SIZE);
    }

    public List<Document> handle(String query) {
        Set<DocumentResult> docSet = getBestRDocumentsForQuery(query);
        List<Document> documentList = new ArrayList<>();
        int documents = 0;

        if (docSet!=null && !docSet.isEmpty()){
            for(DocumentResult dr: docSet){
                Document doc = DocumentManagement.getInstance().getDocument(dr.getDocID());
                if (doc!=null){
                    documents++;
                    documentList.add(doc);
                }

                if (documents==DLCConstants.R){
                    break;
                }
            }
        }
        return documentList;

    }

    private Set<VocabularyEntry> getTermsForVocabularyInNrDescendentOrder(String query) {
        Set<VocabularyEntry> set = new TreeSet<>(new VocabularyComparator());

        FileParser fp = new FileParser(query);
        for (String term : fp) {
            VocabularyEntry ve = EngineModel.getInstance().getFromVocabulary(term);
            if (ve == null) {
                continue;
            }
            set.add(ve);
        }
        return set;
    }

    private Set<DocumentResult> getBestRDocumentsForQuery(String query) {

        int N = EngineModel.getInstance().getDocMap().size();

        Map<Integer, DocumentResult> docMap = new HashMap<>();
        Set<VocabularyEntry> terms = getTermsForVocabularyInNrDescendentOrder(query);


        if (terms.isEmpty()) {
            return null;
        }


        for (VocabularyEntry ve : terms) {

            PostList pl = getPostList(ve);
            if (pl == null) {
                continue;
            }

            int Nr = ve.getNr();
            double idf = Math.log((double) N / (double) Nr);

            for (PostListItem pli : pl.getListOfDocument()) {

                DocumentResult dr = docMap.get(pli.getDocID());
                if (dr == null) {
                    dr = new DocumentResult(pli.getDocID());
                    docMap.put(dr.getDocID(), dr);

                }
                double valueOfTermInDoc = pli.getTf() * idf;
                dr.addToValue(valueOfTermInDoc);



            }
        }

        Set<DocumentResult> orderedDocuments = new TreeSet<>(new DocumentResultComparator());
        orderedDocuments.addAll(docMap.values());
        return orderedDocuments;


    }

    private PostList getPostList(VocabularyEntry ve) {


        Map<String, PostList> postPack = cache.getPostPack(ve.getPostFile());

        if (postPack == null) {
            return null;
        }

        return postPack.get(ve.getTerm());
    }

    private static class DocumentResult implements Comparable<DocumentResult> {
        private final int DOCID;
        private double value;

        public DocumentResult(int docID) {
            DOCID = docID;
            value = 0;
        }

        public void addToValue(double toAdd) {
            value += toAdd;
        }

        public int getDocID() {
            return DOCID;
        }

        public double getValue() {
            return value;
        }

        @Override
        public int compareTo(DocumentResult otherDoc) {
            return (getValue() < otherDoc.getValue() ? 1 : -1);
        }
    }

    private static class VocabularyComparator implements Comparator<VocabularyEntry> {

        @Override
        public int compare(VocabularyEntry v1, VocabularyEntry v2) {
            return v1.compareTo(v2);
        }
    }

    private static class DocumentResultComparator implements Comparator<DocumentResult> {

        @Override
        public int compare(DocumentResult d1, DocumentResult d2) {
            return d1.compareTo(d2);
        }
    }


}
