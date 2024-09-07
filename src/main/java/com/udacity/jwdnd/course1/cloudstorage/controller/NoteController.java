package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/home/note")
@Controller
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public String AddOrUpdateNote(Authentication authentication, Note note) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userService.getUser(loggedInUserName);
        Integer userId = user.getUserId();
        if (note.getNoteid() != null) {
            noteService.updateNote(note);
        } else {
            noteService.addNote(note, userId);
        }
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") int noteid, RedirectAttributes redirectAttributes) {
        if (noteid > 0) {
            noteService.deleteNote(noteid);
            return "redirect:/result?success";
        } else {
            redirectAttributes.addFlashAttribute("error", "Cannot remove the note.");
            return "redirect:/result?error";
        }
    }
}
