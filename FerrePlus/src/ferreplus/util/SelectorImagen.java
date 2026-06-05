package ferreplus.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;

/**
 * Utilidades para elegir y mostrar imagenes de productos.
 *
 * <p>El proyecto guarda la foto del producto como {@code LONGBLOB} en la
 * base de datos. Esta clase ayuda a convertir entre archivos del disco
 * y arreglos de bytes, y a generar {@link ImageIcon} para los previews.</p>
 *
 * @author Sosa Fernandez Luis David
 */
public class SelectorImagen {

    /** Constructor privado: clase de utilidad. */
    private SelectorImagen() {
    }

    /**
     * Abre un dialogo para elegir una imagen del disco y devuelve sus
     * bytes para guardarlos como BLOB.
     *
     * @param padre componente sobre el que se centra el dialogo
     * @return bytes de la imagen o {@code null} si el usuario cancelo
     */
    public static byte[] elegirImagen(Component padre) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Elegir foto del producto");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Imagenes (jpg, jpeg, png, gif)", "jpg", "jpeg", "png", "gif"));

        if (chooser.showOpenDialog(padre) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File archivo = chooser.getSelectedFile();
        try {
            return Files.readAllBytes(archivo.toPath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(padre,
                    "No se pudo leer la imagen:\n" + ex.getMessage(),
                    "Error al leer foto",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Construye un {@link ImageIcon} escalado a las dimensiones dadas a
     * partir de los bytes guardados en la base de datos.
     *
     * @param bytes  bytes de la imagen (puede ser {@code null})
     * @param ancho  ancho deseado en pixeles
     * @param alto   alto deseado en pixeles
     * @return icono escalado, o {@code null} si los bytes son nulos
     */
    public static ImageIcon iconoDesdeBytes(byte[] bytes, int ancho, int alto) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ImageIcon original = new ImageIcon(bytes);
        Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }
}
