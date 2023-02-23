package com.disl.starter.features.db_file.service;

import com.disl.starter.constants.AppUtils;
import com.disl.starter.exceptions.ResponseException;
import com.disl.starter.features.db_file.enums.FileUploadType;
import com.disl.starter.features.db_file.entity.DbFile;
import com.disl.starter.features.db_file.repository.DbFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class DbFileService {

    @Autowired
    private DbFileRepository dbFileRepository;

//    @Autowired
//    private AWSProperties awsProperties;
//
//    @Autowired
//    private S3Service s3Service;

    public DbFile storeFile(MultipartFile file, String fileName, String fileType, FileUploadType uploadType) {
        throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload controller is not implemented yet. Please complete your implementation first");

//        try {
//            if (fileName.contains("..")) {
//                throw new ResponseException("Sorry! Filename contains invalid path sequence " + fileName);
//            }
//
//            //AWS file upload
//            String originalFileName = file.getOriginalFilename();
//            String fileNameForAws = Files.getNameWithoutExtension(originalFileName);
//            String extension = Files.getFileExtension(originalFileName);
//            String awsFolderName = getAwsFolderName(uploadType, extension);
//
//            File convertedFile = new File(originalFileName);
//            try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
//                fileOutputStream.write(file.getBytes());
//                fileOutputStream.close();
//            }
//
//            String fileKey;
//            fileKey = awsFolderName + System.currentTimeMillis() + "." + extension;
//            PutObjectRequest request = new PutObjectRequest(awsProperties.getBucketName(),
//            fileKey, convertedFile).withCannedAcl(CannedAccessControlList.PublicRead);
//
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentType(file.getContentType());
//            metadata.addUserMetadata("title", fileName);
//            request.setMetadata(metadata);
//            System.out.println("file uploader");
//            s3Service.getAwsClient().putObject(request);
//            // AWS file upload ENDS
//
//            DbFile dbFile = new DbFile();
//            dbFile.setFileType(fileType);
//            dbFile.setFileName(fileNameForAws);
//            dbFile.setFileKey(fileKey);
//            Pair<String, String> extensionAndMimeType = AppUtils.getFileExtensionAndMimeType(file);
//            dbFile.setFileExtension(extensionAndMimeType.getLeft());
//            dbFile.setMimeType(extensionAndMimeType.getRight());
//            dbFile.setUploadType(uploadType);
//            dbFile = dbFileRepository.save(dbFile);
//
//            try {
//                convertedFile.delete();
//            } catch (Exception ignored) {}
//
//            return dbFile;
//        } catch (IOException | MimeTypeException | NotFoundException ex) {
//            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//        }
    }

    public DbFile storeFileFromUrl(String fileName, String fileType, FileUploadType uploadType) {
        DbFile dbFile = new DbFile();
        dbFile.setFileType(fileType);
        dbFile.setFileName(fileName);
        dbFile.setFileExtension(".jpeg");
        dbFile.setMimeType("image/jpeg");
        dbFile.setUploadType(uploadType);
        return dbFileRepository.save(dbFile);
    }

    public Boolean deleteDbFile(DbFile dbFile) {
        try{
            this.dbFileRepository.delete(dbFile);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Async
    public void deleteDbFileAsync(DbFile dbFile) {
        if(Objects.isNull(dbFile)) return;
//        deleteAwsFile(dbFile.getFileKey());
        dbFileRepository.delete(dbFile);
    }

    @Async
    public void deleteDbFileByIdAsync(Long dbFileId) {
        if (AppUtils.isNotNullAndGreaterZero(dbFileId)) {
            DbFile dbFile = dbFileRepository.findById(dbFileId).orElse(null);

            if(dbFile != null) {
                dbFileRepository.delete(dbFile);
//                deleteAwsFile(dbFile.getFileKey());
            }
        }
    }

    public DbFile findById(long id){
        return dbFileRepository.findById(id).orElse(null);
    }

    public DbFile findByIdWithThrowException(long id){
        return dbFileRepository.findById(id).orElseThrow(() -> new ResponseException("No db file found with this id " + id));
    }

     @Async
     public void deletePreviousDbFileAsync(DbFile prevDbFile, DbFile newDbFile) {
         if(prevDbFile != null && newDbFile != null && !prevDbFile.getId().equals(newDbFile.getId())) {
//             deleteAwsFile(prevDbFile.getFileKey());
             dbFileRepository.delete(prevDbFile);
         }
     }

    private String getAwsFolderName(FileUploadType fileUploadType) {
        return switch (fileUploadType) {
            case CMS -> "";
            case USER -> "";
            default -> throw new ResponseException("Invalid file upload type");
        };
    }

    public void delete(DbFile dbFile) {
//        deleteAwsFile(dbFile.getFileKey());
        dbFileRepository.delete(dbFile);
    }

    public void deleteAll(List<DbFile> dbFiles) {
        for (DbFile dbFile : dbFiles) {
//            deleteAwsFile(dbFile.getFileKey());
            dbFileRepository.delete(dbFile);
        }
    }

//    private void deleteAwsFile(String fileKey) {
//        s3Service.getAwsClient().deleteObject(awsProperties.getBucketName(), fileKey);
//    }

}
