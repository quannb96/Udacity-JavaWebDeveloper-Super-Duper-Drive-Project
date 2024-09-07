package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;
    private EncryptionService encryptionService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService,
            UserService userService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        String userName = (String) authentication.getPrincipal();
        User user = userService.getUser(userName);

        model.addAttribute("files", fileService.getAllFilesByUserId(user.getUserId()));
        model.addAttribute("notes", noteService.getAllNotesByUserId(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentialsByUserid(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
