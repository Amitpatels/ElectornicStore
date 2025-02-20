package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.exceptions.BadApiRequest;
import com.lcwd.electronic.store.ElectronicStore.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileService.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        //abc.png
        String orignalFilename = file.getOriginalFilename();
        logger.info("Filename : {} ",orignalFilename);
        String filename = UUID.randomUUID().toString();
        String extension = orignalFilename.substring(orignalFilename.lastIndexOf("."));
        String fileNameWithExtension = filename + extension;
        String fullPathWithFileName = path + File.separator+fileNameWithExtension;

        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")){
            File folder = new File(path);
            if(!folder.exists()){
                //create the folder
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }else {
            throw new BadApiRequest("File with this "+extension+" not allowed.");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + name;
        logger.info("Image serve path : {} ",fullPath);
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
