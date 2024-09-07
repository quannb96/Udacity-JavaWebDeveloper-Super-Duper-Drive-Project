package com.udacity.jwdnd.course1.cloudstorage.mapper;

import org.apache.ibatis.annotations.*;

import com.udacity.jwdnd.course1.cloudstorage.model.File;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid= #{userid} ")
    List<File> getAllFilesByUserId(Integer userid);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    File getFileByName(String filename);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int addFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(int fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userid} AND filename = #{filename}")
    File getFileByNameAndUserid(Integer userid, String filename);
}
