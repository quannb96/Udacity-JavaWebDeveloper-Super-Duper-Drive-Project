package com.udacity.jwdnd.course1.cloudstorage.mapper;

import org.apache.ibatis.annotations.*;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid= #{userid} ")
    List<Note> getAllNotesByUserId(Integer userid);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    void addNote(Note note);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    void updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid}")
    int deleteNote(int noteid);
}
