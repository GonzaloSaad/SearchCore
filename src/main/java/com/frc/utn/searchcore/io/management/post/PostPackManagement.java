package com.frc.utn.searchcore.io.management.post;


import com.frc.utn.searchcore.io.util.DLCObjectReader;
import com.frc.utn.searchcore.io.util.DLCObjectWriter;
import com.frc.utn.searchcore.model.PostEntry;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class PostPackManagement{


    public static final String POST_FOLDER_PATH = "src/main/resources/dlc/post/save/post";
    public static final String POST_FILE_EXTENSION = ".post";
    public static final String POST_INDEX_PATH = "src/main/resources/dlc/post/post.index";

    private static PostPackManagement instancePck;

    private PostPackManagement(){

    }

    public static PostPackManagement getInstance(){
        if (instancePck==null){
            instancePck = new PostPackManagement();
        }
        return instancePck;
    }

    public Map<String,Integer> getIndex(){
        DLCObjectReader<Map<String,Integer>> or = new DLCObjectReader<>();
        return or.read(POST_INDEX_PATH);
    }

    public void saveIndex(Map<String,Integer> index){
        DLCObjectWriter<Map<String,Integer>> ow = new DLCObjectWriter<>();
        ow.write(index,POST_INDEX_PATH);
    }

    public Map<String,PostEntry> getPostPack(int postNumber){
        DLCObjectReader<Map<String,PostEntry>> or = new DLCObjectReader<>();
        return or.read(createPath(postNumber));
    }

    public void savePostPack(Map<String,PostEntry> postPack,int postNumber){
        DLCObjectWriter<Map<String,PostEntry>> ow = new DLCObjectWriter<>();
        ow.write(postPack, createPath(postNumber));
    }

    private String createPath(int postNumber){
        return POST_FOLDER_PATH + StringUtils.leftPad(Integer.toString(postNumber),3,"0") + POST_FILE_EXTENSION;
    }

}
