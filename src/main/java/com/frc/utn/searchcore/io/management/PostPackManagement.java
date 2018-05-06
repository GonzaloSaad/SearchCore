package com.frc.utn.searchcore.io.management;


import com.frc.utn.searchcore.io.util.DLCObjectReader;
import com.frc.utn.searchcore.io.util.DLCObjectWriter;
import com.frc.utn.searchcore.model.PostEntry;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class PostPackManagement{


    public static final String POST_FOLDER_PATH = "src/main/resources/dlc/post/post";
    public static final String POST_FILE_EXTENSION = ".post";

    private static PostPackManagement instancePck;

    private PostPackManagement(){

    }

    public static PostPackManagement getInstance(){
        if (instancePck==null){
            instancePck = new PostPackManagement();
        }
        return instancePck;
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
