package com.tzw.noah.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Awesome Pojo Generator
 */
public class MediaAttach implements Serializable {

    public int articleRessourceType;
    public int articleSource;
    public int articleRessourceId;
    public String createTime = "";
    public int articleId;
    public String articleRessourceUrl = "";
    public String articleRessourceName = "";
    public int articleRessourceSize;

}