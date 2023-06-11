/*
 * Para ejecutar este archivo jar en la CMD o en algun IDE en especifico, se debe
 * agregar en sus parametros de ejecución los siguientes atributos
 * java -jar -XX:NativeMemoryTracking=summary -Dlog4j.configurationFile=C:\Users\..\log4j2.xml AsistenciaUltrasist.java
 *Usa estos comandos para validar la memoria usada por el arhcivo
 *CMD> jps 
 *CMD> jcmd puerto VM.native_memory summary
 *tasklist | findstr /i "java"
 *Usa este comando para matar todos los precos Java que se quedaron abiertos
 *CMD> taskkill /F /im "javaw.exe"
 */
package asistenciaultrasist;

import asistenciaultrasist.Util.AsistenteHora;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EliteBook8460p
 */
public class AsistenciaUltrasist {
    
    private static final Logger logger = LogManager.getLogger(AsistenciaUltrasist.class);
    /**
     * @param args the command line arguments
     */
    
    public static final  String HORA_ENTRADA =     "01:55";
    public static final  String HORA_COMIDA =      "15:00";
    public static final  String HORA_FIN_COMIDA =  "17:00";
    public static final  String HORA_SALIDA =      "19:00";
    
        
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        AsistenteHora clock = new AsistenteHora();
        System.out.println("Inicia Asistente de Evidencias: "+clock.getRestarHora(new Date()));
        logger.info("Inicia Asistente de Evidencias: "+clock.getRestarHora(new Date()));
        // Ruta y nombre del archivo Word
        String wordFilePath = "";
        // Ruta y nombre del archivo de salida de los screenshots
        String screenshotFilePath = "";
        Properties prop = new Properties();
        InputStream propFile = null;
        try{
            propFile = new FileInputStream("configuration.properties");
            prop.load(propFile);
            wordFilePath = prop.getProperty("wordFilePath");
            screenshotFilePath = prop.getProperty("screenshotFilePath");
        }catch(FileNotFoundException ex){
            System.out.println("No se encontro configuración: "+ex.getMessage());
            logger.error("No se encontro configuración: "+ex.getMessage());
        }catch(IOException e){
            e.printStackTrace();
        }
        // Define las horas en las que deseas tomar los screenshots
        //Por el error de hora adelanta, cuando se quiera programar una hora
        //se debe sumar un valor porque el sistema lo toma diferente.
        LocalTime[] screenshotTimes = {
                LocalTime.of(10, 0, 30),   // 9:00+Sumar 1hora a la deseada
                LocalTime.of(16, 0, 30),  // 3:00
                LocalTime.of(18, 0, 30),  // 5:00
                LocalTime.of(20, 0, 30)   // 7:00
        };
        
        // Verifica la hora actual y realiza la captura de pantalla si coincide con los tiempos definidos
        /* */
        while (true) {
            LocalTime currentTime = LocalTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);
            //System.out.println(currentTime);
            for (LocalTime screenshotTime : screenshotTimes) {
                if (currentTime.equals(screenshotTime)) {
                    System.out.println("Tomando captura: "+clock.getRestarHora(new Date()));
                    logger.info("Tomando captura: "+clock.getRestarHora(new Date()));
                    //Validamos que existan el archivo de asistencia del dia de hoy
                    DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                    String nombreArchivo = "Evidencia "+dateFormat.format(new Date())+".docx";
                    File file = new File(wordFilePath+nombreArchivo);
                    if (file.exists()) {
                            System.out.println("Archivo Encontrado: "+nombreArchivo+" - "+clock.getRestarHora(new Date()));
                            logger.info("Archivo Encontrado: "+nombreArchivo+" - "+clock.getRestarHora(new Date()));
                            //Si existe se procede a realizar las capturas
                            captureScreenshot(screenshotFilePath);
                            replaceScreenshotInWord(screenshotFilePath, wordFilePath + nombreArchivo);
                    }else{
                        logger.error("No se encontro archivo Word - "+clock.getRestarHora(new Date()));
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "No se encontró el archivo de Evidencia del dia de hoy: \n\n\""+"Evidencia 101929"+"\"\n\nDebes generarlo manualmente y añadir las capturas correspondientes", "Archivo no encontrado", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        //System.out.println("Terminó Asistente de Evidencias: "+clock.getRestarHora(new Date()));
    }
    
    private static void captureScreenshot(String filePath) {
        try {
            // Captura la pantalla completa
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenshot = robot.createScreenCapture(screenRect);

            // Guarda el screenshot en un archivo
            ImageIO.write(screenshot, "png", new File(filePath));
            
            logger.info("Screenshot capturado: " + filePath);
            System.out.println("Screenshot capturado: " + filePath);
        } catch (AWTException | IOException ex) {
            logger.error("Error al capturar el screenshot: " + ex.getMessage());
            System.err.println("Error al capturar el screenshot: " + ex.getMessage());
        }
    }
    
    private static void replaceScreenshotInWord(String screenshotFilePath, String wordFilePath) {
         // Implementa aquí la lógica para reemplazar el screenshot en el archivo Word
        // Puedes usar librerías como Apache POI para trabajar con archivos Word en Java
        // Aquí tienes un ejemplo básico para reemplazar el contenido de un archivo Word:
        try {
            FileInputStream fis = new FileInputStream(wordFilePath);
            XWPFDocument document = new XWPFDocument(fis);
            fis.close();
            
            //Agregamos el Switch para cambiar la variable de la hora para agregar
            String hora = "";
            AsistenteHora horaWord = new AsistenteHora();
            switch(horaWord.getFormatoTextWord(new Date())){
                case HORA_ENTRADA:
                    hora = HORA_ENTRADA;
                break;
                case HORA_COMIDA:
                    hora = HORA_COMIDA;
                break;
                case HORA_FIN_COMIDA:
                    hora = HORA_FIN_COMIDA;
                break;
                case HORA_SALIDA:
                    hora = HORA_SALIDA;
                break;
                default:
                    hora = horaWord.getFormatoTextWord(new Date());
                break;
            }
            addText(document, hora);
            
            addImage(document, screenshotFilePath);
            // Realiza las modificaciones necesarias en el documento Word
            // Por ejemplo, reemplaza una imagen existente con el nuevo screenshot

            FileOutputStream fos = new FileOutputStream(wordFilePath);
            document.write(fos);
            fos.close();
            logger.info("Captura Exitosa! - "+horaWord.getFormatoTextWord(new Date()));
            System.out.println("Captura Exitosa! - "+horaWord.getFormatoTextWord(new Date()));
        } catch (IOException ex) {
            logger.error("Error al reemplazar el screenshot en el archivo Word: " + ex.getMessage());
            System.err.println("Error al reemplazar el screenshot en el archivo Word: " + ex.getMessage());
        } 
    }
    
    private static void addText(XWPFDocument document, String textToAdd) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(textToAdd);
    }
    
    private static void addImage(XWPFDocument document, String imagePath) {
        try {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            int width = 442; // Ajusta el ancho de la imagen según sea necesario
            int height = 248; // Ajusta la altura de la imagen según sea necesario
            run.addPicture(new FileInputStream(imagePath), XWPFDocument.PICTURE_TYPE_PNG, imagePath, Units.toEMU(width), Units.toEMU(height));
        } catch (Exception ex) {
            logger.error("Error al agregar la imagen: " + ex.getMessage());
            System.err.println("Error al agregar la imagen: " + ex.getMessage());
        }
    }
    
    
    
}
