package main.java.dev.sprint;

import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public final class Files {
    public static final long MAX_FILE_SIZE = 10L * 1024 * 1024;
    // Choisir les types de fichiers autorisés ici.
    // Exemple: Set.of("image/png", "image/jpeg", "application/pdf")
    // Pour l'instant, on garde l'enregistrement sans blocage pour tester.
    public static final Set<String> ALLOWED_CONTENT_TYPES = Collections.emptySet();

    private Files() {
    }

    public static UploadedFile save(Part part) throws IOException {
        if (part.getSize() <= 0) {
            throw new IllegalArgumentException("Le fichier est vide.");
        }
        if (part.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Le fichier dépasse la taille maximale de 10 Mo.");
        }

        String contentType = part.getContentType();
        if (contentType == null || contentType.isBlank()) {
            contentType = "application/octet-stream";
        }

        if (!ALLOWED_CONTENT_TYPES.isEmpty() && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Type de fichier non autorisé. Types autorisés : " + ALLOWED_CONTENT_TYPES);
        }

        String originalName = normalizeFileName(part.getSubmittedFileName());
        if (originalName.isBlank()) {
            throw new IllegalArgumentException("Le nom du fichier est invalide.");
        }

        String storedName = UUID.randomUUID().toString();
        Path uploadDirectory = Path.of(
                System.getProperty("sprint.upload.dir", "uploads")).toAbsolutePath().normalize();
        java.nio.file.Files.createDirectories(uploadDirectory);
        Path destination = uploadDirectory.resolve(storedName);

        long actualSize = 0;
        try (InputStream input = part.getInputStream();
             OutputStream output = java.nio.file.Files.newOutputStream(
                     destination, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                actualSize += bytesRead;
                if (actualSize > MAX_FILE_SIZE) {
                    throw new IllegalArgumentException("Le fichier dépasse la taille maximale de 10 Mo.");
                }
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException | RuntimeException exception) {
            try {
                java.nio.file.Files.deleteIfExists(destination);
            } catch (IOException cleanupException) {
                exception.addSuppressed(cleanupException);
            }
            throw exception;
        }

        return new UploadedFile(originalName, storedName, actualSize, contentType);
    }

    private static String normalizeFileName(String submittedFileName) {
        if (submittedFileName == null) {
            return "";
        }
        String pathStrippedName = submittedFileName.replace('\\', '/');
        return pathStrippedName.substring(pathStrippedName.lastIndexOf('/') + 1).trim();
    }
}
