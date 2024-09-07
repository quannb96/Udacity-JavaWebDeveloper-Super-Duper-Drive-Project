package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/credential")
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String AddOrUpdateCredential(Authentication authentication, Credential credential) {
        String userName = (String) authentication.getPrincipal();
        User user = userService.getUser(userName);
        Integer userId = user.getUserId();
        if (credential.getCredentialid() != null) {
            credentialService.updateCredential(credential);
        } else {
            credentialService.addCredential(credential, userId);
        }
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteCredential(@RequestParam("id") int credentialid, RedirectAttributes redirectAttributes) {
        if (credentialid > 0) {
            credentialService.deleteCredential(credentialid);
            return "redirect:/result?success";
        } else {
            redirectAttributes.addFlashAttribute("error", "Cannot remove the credential.");
            return "redirect:/result?error";
        }
    }
}
