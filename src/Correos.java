
import java.io.IOException;
import java.security.Security;
import java.util.Properties;
import java.util.Scanner;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;



public class Correos {

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

    static String correoDesti;//="45858000w@iespoblenou.org";
    static String correoOrig;//="cristianjavier2394@hotmail.com";
    static String pass;//="";
    static String asunto;//= "holaaaa";
    private static String cuerpo;//"buenas, que tal?";
    private static String ruta;//"/media/45858000w/SAMSUNG/DAM 2º/GITBBDD/M09/m09-gmail_api/src/consulta.pdf";
    // static String correo;


    public static void main(String args[]) throws AddressException, MessagingException, IOException {

        generarDatos();

        int opcion;

        do {

            System.out.println("Enviar correo con archivo desde gmail (1) o sin archivo desde gmail (2) o enviar correo desde hotmail (3) o leer correos (4)");
            Scanner sc = new Scanner(System.in);
            opcion = sc.nextInt();

            if (opcion == 1) {
                System.out.println("Introduce la ruta del archivo");
                ruta = sc.nextLine();
                sendfile();
            } else if (opcion == 2) {
                generateAndSendEmail();
            }
            else if (opcion == 3) {
                generateAndSendHotmail();
            }
            else if (opcion == 4)
            {
                readMail();
            }

        }while (opcion <1 && opcion >4);


        System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
    }



    public static void generarDatos()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el correo de destinatario con el @servidor");
        correoDesti = sc.nextLine();

        System.out.println("Introduce el usuario del correo de emissor(sin la @servidor)");
        correoOrig = sc.nextLine();

        System.out.println("Introduce el password del emissor");
        pass = sc.nextLine();

        System.out.println("Introduce el asunto del mensaje");
        asunto = sc.nextLine();

        System.out.println("Introduce el cuerpo del mensaje");
        cuerpo = sc.nextLine();

    }

    public static void generateAndSendEmail() throws AddressException, MessagingException {
        // Step1 para gmail
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDesti));//destinatario--"x3727349s@iespoblenou.org"
        generateMailMessage.setSubject(asunto);//asunto del mensaje
        String emailBody = cuerpo + "<br><br> Records!, <br> Cristian J. ";//contenido del correo
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");

        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", correoOrig, pass);//quien envia el correo ( servidor,correo,pass)
        //transport.connect("smtp.gmail.com","dremon.iespoblenou","mirandolamon");//quien envia el correo ( servidor,correo,pass)
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();

    }

    public static void sendfile() throws MessagingException {

        // Step1 para gmail
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDesti));//destinatario--"x3727349s@iespoblenou.org"

        // Asignamos el asunto
        generateMailMessage.setSubject(asunto);

        // Creamos un cuerpo del correo con ayuda de la clase BodyPart
        BodyPart cuerpoMensaje = new MimeBodyPart();

        // Asignamos el texto del correo
        cuerpoMensaje.setText(cuerpo);

        // Creamos un multipart al correo
        Multipart multiparte = new MimeMultipart();

        // Agregamos el texto al cuerpo del correo multiparte
        multiparte.addBodyPart(cuerpoMensaje);

        // Ahora el proceso para adjuntar el archivo
        cuerpoMensaje = new MimeBodyPart();
        String nombreArchivo = ruta;
        DataSource fuente = new FileDataSource(nombreArchivo);
        cuerpoMensaje.setDataHandler(new DataHandler(fuente));
        cuerpoMensaje.setFileName(nombreArchivo);
        multiparte.addBodyPart(cuerpoMensaje);

        // Asignamos al mensaje todas las partes que creamos anteriormente
        generateMailMessage.setContent(multiparte);

        // Enviamos el correo
        System.out.println("Mensaje enviado");

        // Step3
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", correoOrig, pass);//quien envia el correo ( servidor,correo,pass)
        //transport.connect("smtp.gmail.com","dremon.iespoblenou","mirandolamon");//quien envia el correo ( servidor,correo,pass)
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();

    }

    private static void generateAndSendHotmail() throws MessagingException {
        Properties mailProperties;
        Session mailSession;
        MimeMessage genMailMessage;

        mailProperties = System.getProperties();
        // port de hotmail
        mailProperties.put("mail.smtp.port", "587");

        // para hotmail hay que especificar el host en las propiedades ( con gmail no hace falta esta linea )
        mailProperties.setProperty("mail.host", "smtp.live.com");

        // autenticacio
        mailProperties.put("mail.smtp.auth", "true");

        // seguretat
        mailProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        mailSession = Session.getDefaultInstance( mailProperties, null);

        genMailMessage = new MimeMessage( mailSession );

        // añadir email del destinatario en InternetAddress
        genMailMessage.addRecipient( Message.RecipientType.TO, new InternetAddress(correoDesti) );
        genMailMessage.setSubject( asunto );
        genMailMessage.setContent(cuerpo, "text/html");
        System.out.println("Mail Session has been created successfully..");

        Transport transport = mailSession.getTransport("smtp");
        // añadir el correo hotmail y el pass de la cuenta de la que se enviara el correo
        transport.connect("smtp.live.com",correoOrig,pass);
        transport.sendMessage( genMailMessage, genMailMessage.getAllRecipients() );
        transport.close();
    }

    private static void readMail() throws MessagingException, IOException {

        String host = "pop.gmail.com";// change accordingly
        String mailStoreType = "pop3";

        //create properties field
        Properties properties = new Properties();

        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        Session emailSession = Session.getDefaultInstance(properties);

        //create the POP3 store object and connect with the pop server
        Store store = emailSession.getStore("pop3s");

        store.connect(host, correoOrig, pass);

        //create the folder object and open it
        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        // retrieve the messages from the folder in an array and print it
        Message[] messages = emailFolder.getMessages();
        System.out.println("messages.length---" + messages.length);

        for (int i = 0, n = messages.length; i < n; i++) {
            Message message = messages[i];
            System.out.println("---------------------------------");
            System.out.println("Email Number " + (i + 1));
            System.out.println("Subject: " + message.getSubject());
            System.out.println("From: " + message.getFrom()[0]);
            System.out.println("Text: " + message.getContent().toString());
            System.out.println("FLAG -> "+message.getFlags().toString());

        }

        //close the store and folder objects
        emailFolder.close(false);
        store.close();
    }
}
