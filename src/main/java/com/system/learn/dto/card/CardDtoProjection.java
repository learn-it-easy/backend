package com.system.learn.dto.card;

public interface CardDtoProjection {
    Long getCardId();
    Long getFolderId();
    String getText();
    String getTextTranslation();
    boolean getIsImage();
}