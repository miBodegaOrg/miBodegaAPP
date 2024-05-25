package com.mibodega.mystore.models.Responses;

import java.util.ArrayList;

public class PagesProductResponse {
    private ArrayList<ProductResponse> docs = new ArrayList<>();
    private int totalDocs;
    private int limit;
    private int totalPages;
    private int page;
    private int pagingCounter;
    private boolean hasPrevPage;
    private boolean hasNextPage;
    private int prevPage;
    private int nextPage;

    public PagesProductResponse(ArrayList<ProductResponse> docs, int totalDocs, int limit, int totalPages, int page, int pagingCounter, boolean hasPrevPage, boolean hasNextPage, int prevPage, int nextPage) {
        this.docs = docs;
        this.totalDocs = totalDocs;
        this.limit = limit;
        this.totalPages = totalPages;
        this.page = page;
        this.pagingCounter = pagingCounter;
        this.hasPrevPage = hasPrevPage;
        this.hasNextPage = hasNextPage;
        this.prevPage = prevPage;
        this.nextPage = nextPage;
    }

    public ArrayList<ProductResponse> getDocs() {
        return docs;
    }

    public void setDocs(ArrayList<ProductResponse> docs) {
        this.docs = docs;
    }

    public int getTotalDocs() {
        return totalDocs;
    }

    public void setTotalDocs(int totalDocs) {
        this.totalDocs = totalDocs;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagingCounter() {
        return pagingCounter;
    }

    public void setPagingCounter(int pagingCounter) {
        this.pagingCounter = pagingCounter;
    }

    public boolean isHasPrevPage() {
        return hasPrevPage;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
}
