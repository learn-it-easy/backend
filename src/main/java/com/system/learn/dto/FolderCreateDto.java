package com.system.learn.dto;

public class FolderCreateDto {
    private String name;

    public FolderCreateDto(String name) {
        this.name = name;
    }

    public FolderCreateDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
