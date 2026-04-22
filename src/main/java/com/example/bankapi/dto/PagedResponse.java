package com.example.bankapi.dto;

import java.util.List;

public class PagedResponse<T> {

    private int total;
    private int page;
    private int limit;
    private List<T> data;

    public PagedResponse(int total, int page, int limit, List<T> data) {
        this.total = total;
        this.page = page;
        this.limit = limit;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}