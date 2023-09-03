package ar.edu.itba.apuntea.persistence;

import ar.edu.itba.apuntea.models.Note;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Repository
public class NoteDaoImpl implements NoteDao{

//    private String fileUploadPath = "/home/david/Desktop/output/";

    @Override
    public Note create(MultipartFile multipartFile, String institution, String career, String subject, String type) {
//        String fileName = university + "_" + career + "_" + subject + ".pdf"; // Genera un nombre de archivo único
//        String fullPath = fileUploadPath + fileName;

//        System.out.println("Creado en: " + fullPath);


//        try {
//            multipartFile.transferTo(new File(fullPath));
//        } catch (IOException e) {
//            System.out.println("No se pudo transferir el archivo");
//            System.out.println(e.getMessage());
//        }
        byte[] bytes = new byte[0];
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return new Note(institution, career, subject, "pdf", bytes);
    }

    public byte[] getNoteFileById(UUID noteId){
//        File file = new File("/home/david/Desktop/guia-practica-r0.pdf");
//        try {
//            return FileUtils.readFileToByteArray(file);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
        return null;
    }

}
