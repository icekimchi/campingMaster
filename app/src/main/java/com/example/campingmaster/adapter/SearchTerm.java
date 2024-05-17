package com.example.campingmaster.adapter;

public class SearchTerm {
    private String term;
    private int rank;

    public SearchTerm(String term, int rank) {
        this.term = term;
        this.rank = rank;
    }

    public String getTerm() {
        return term;
    }

    public int getRank() {
        return rank;
    }
}
