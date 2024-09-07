package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/home/files")
public class FileController {

    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication,
            RedirectAttributes redirectAttributes) throws IOException {
        String username = (String) authentication.getPrincipal();
        User user = userService.getUser(username);
        if (fileUpload.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please choose a file that is not empty.");
            return "redirect:/result?error";
        }
        if (!fileService.fileExists(fileUpload.getOriginalFilename(), user.getUserId())) {
            redirectAttributes.addFlashAttribute("error", "The file is already present.");
            return "redirect:/result?error";
        }
        fileService.addFile(fileUpload, user.getUserId());
        return "redirect:/result?success";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Integer fileId) {
        File file = fileService.getFileById(fileId);
        ByteArrayResource resource = new ByteArrayResource(file.getFiledata());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .contentLength(Long.parseLong(file.getFilesize()))
                .body(resource);
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int fileId, RedirectAttributes redirectAttributes) {
        if (fileId > 0) {
            fileService.deleteFile(fileId);
            return "redirect:/result?success";
        }
        redirectAttributes.addFlashAttribute("error", "Cannot remove the file.");
        return "redirect:/result?error";
    }

}
