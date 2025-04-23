package com.system.learn.dto.card;

import java.util.List;

public class CardPageDto {
    private List<CardGetForFolderDto> cards;
    private int currentPage;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;


    public CardPageDto(List<CardGetForFolderDto> cards, int currentPage, int totalPages, boolean hasNext, boolean hasPrevious) {
        this.cards = cards;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public CardPageDto() {

    }

    public List<CardGetForFolderDto> getCards() {
        return cards;
    }

    public void setCards(List<CardGetForFolderDto> cards) {
        this.cards = cards;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean getIsHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean getIsHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}