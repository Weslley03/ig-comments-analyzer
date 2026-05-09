package com.ig.comments.web.scraping.dto;

import com.ig.comments.web.scraping.validation.ValidInstagramUrl;

public class InstagramRequestDTO {

    @ValidInstagramUrl
    private String url;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
