package com.system.learn.dto;

import java.util.List;

public class FolderPageDto {
    private List<FolderGetDto> folders;
    private int currentPage;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public FolderPageDto(List<FolderGetDto> folders, int currentPage, int totalPages, boolean hasNext, boolean hasPrevious) {
        this.folders = folders;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public List<FolderGetDto> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderGetDto> folders) {
        this.folders = folders;
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

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}