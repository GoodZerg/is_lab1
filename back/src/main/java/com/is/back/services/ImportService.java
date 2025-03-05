package com.is.back.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImportService {

    @Autowired
    private MinioService minioService;

    @Autowired
    private CityService cityService;

    @Autowired
    private ImportHistoryService importHistoryService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void importCities(MultipartFile file, Long userId) throws Exception {
        String objectName = "import-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();

        try {
            // Фаза 1: Подготовка
            minioService.uploadFile(objectName, file);
            int num = cityService.importCitiesFromJson(file, userId);

            // Фаза 2: Фиксация
            importHistoryService._saveImportHistory(userId, objectName, "SUCCESS", num);
        } catch (Exception e) {
            // Откат

            minioService.deleteFile(objectName);
            throw e;
        }
    }
}