package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNotesByUserId(int userId) {
        return noteMapper.getAllNotesByUserId(userId);
    }

    public void addNote(Note note, int userid) {
        Note newNote = new Note();
        newNote.setUserid(userid);
        newNote.setNotedescription(note.getNotedescription());
        newNote.setNotetitle(note.getNotetitle());

        noteMapper.addNote(newNote);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }

    public void deleteNote(int noteId) {
        noteMapper.deleteNote(noteId);
    }
}
