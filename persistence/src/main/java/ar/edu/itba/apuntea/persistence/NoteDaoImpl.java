package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Note;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Repository
public class NoteDaoImpl implements NoteDao{

    private String fileUploadPath = "C:\\Users\\Tomas\\Documents\\GitHub\\Apuntea\\files\\";

    @Override
    public Note create(MultipartFile multipartFile, String university, String career, String subject, String type) {
        String fileName = university + "_" + career + "_" + subject + ".pdf"; // Genera un nombre de archivo único
        String fullPath = fileUploadPath + fileName;

        System.out.println("Creado en: " + fullPath);


        try {
            multipartFile.transferTo(new File(fullPath));
        } catch (IOException e) {
            System.out.println("No se pudo transferir el archivo");
            System.out.println(e.getMessage());
        }

        return new Note(fullPath, university, career, subject, type);
    }


}
