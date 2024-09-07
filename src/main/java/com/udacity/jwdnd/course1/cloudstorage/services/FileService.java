package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getAllFilesByUserId(Integer userid) {
        return fileMapper.getAllFilesByUserId(userid);
    }

    public void addFile(MultipartFile fileUpload, int userid) throws IOException {
        File file = new File();
        try {
            file.setContenttype(fileUpload.getContentType());
            file.setFilename(fileUpload.getOriginalFilename());
            file.setUserid(userid);
            file.setFiledata(fileUpload.getBytes());
            file.setFilesize(Long.toString(fileUpload.getSize()));
        } catch (IOException e) {
            throw e;
        }
        fileMapper.addFile(file);
    }

    public int deleteFile(int fileId) {
        return fileMapper.deleteFile(fileId);
    }

    public boolean fileExists(String filename, Integer userid) {
        return fileMapper.getFileByNameAndUserid(userid, filename) == null;
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }
}
