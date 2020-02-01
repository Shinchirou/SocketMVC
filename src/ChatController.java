import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.net.Socket;

public class ChatController implements AutoCloseable {
    @FXML
    TextField messageTextField;
    @FXML
    Label welcomeLabel;
    @FXML
    WebView webViewMessages;
    @FXML
    Circle circleImage;
    @FXML
    ImageView sendImageView;

    @FXML
    private void initialize() {
        String welcome = "Nice to see you there!This is a welcome message. " + "Say hello to other users.";
        messagesLayout = Jsoup.parse("<html><head><meta charset='UTF-8'>"
                + "</head><body><ul><li class=\"welcome\"><div class=\"message\"><div class=\"content\">" + welcome
                + "</div></div></li></ul></body></html>", "UTF-16", Parser.xmlParser());
        webViewMessages.getEngine().loadContent(messagesLayout.html());
        webViewMessages.getEngine().setUserStyleSheetLocation(getClass().getResource("application.css").toString());
    }


    private String userName = "";
    private String senderName;
    private String host;
    private int port;

    private Socket socket;
    private BufferedReader inputBufferedReader;
    private PrintWriter outputPrintWriter;
    private final int PROTOCOL_PREFIX_LENGTH = 3;
    private Document messagesLayout;
    Task<Void> task;

    public String getUserName() {
        return userName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        welcomeLabel.setText("Hello " + this.userName + "!");
        Image myImage = new Image(getClass().getResourceAsStream("harveyspecter.png"));
        ImagePattern pattern = new ImagePattern(myImage);
        circleImage.setFill(pattern);
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }

    private String receiveMessage() throws IOException {
        return inputBufferedReader.readLine();
    }

    public void run() throws IOException {
        socket = new Socket(host, port);
        inputBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputPrintWriter = new PrintWriter(socket.getOutputStream(), true);
        sendMessage(userName);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws IOException, InterruptedException {
                try {
                    while (true) {
                        if (isCancelled()) {
                            return null;
                        }
                        String msg = receiveMessage();
                        showMessage(toHTML(decodeUID(msg), "response"));
                        System.out.println(msg);
                        Thread.sleep(100);
                    }
                } catch (IOException | InterruptedException ex) {
                    if (isCancelled()) {
                        return null;
                    }
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void sendMessage(String message) {
        outputPrintWriter.println(message);
    }

    @FXML
    private void sendImageView_MouseReleased() {
        if (messageTextField.getLength() == 0) {
            return;
        }
        sendMessage(messageTextField.getText());
        showMessage(toHTML(messageTextField.getText(), "request"));
        messageTextField.clear();
    }

    @FXML
    private void messageTextField_KeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            sendImageView_MouseReleased();
        }
    }

    private void showMessage(Element message) {
        Element wrapper = messagesLayout.getElementsByTag("ul").first();
        wrapper.appendChild(message);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webViewMessages.getEngine().loadContent(messagesLayout.html());
            }
        });
    }

    private Element toHTML(String message, String msgClass) {
        System.out.println("toHTML:" + message);
        Element wrapper = new Element("li").attr("class", msgClass);
        Element image = new Element("img").attr("class", "avatar").attr("src",
                new File("src/mikeross.png").toURI().toString());
        if (msgClass.equals("request")) {

            image.attr("src", new File("src/harveyspecter.png").toURI().toString());
            new Element("span").attr("class", "author").append(senderName).appendTo(wrapper);
        }
        image.appendTo(wrapper);
        Element message_div = new Element("div").attr("class", "message").appendTo(wrapper);
        new Element("div").attr("class", "content").append(message).appendTo(message_div);
        return wrapper;
    }

    private String decodeUID(String msg) {
        msg = msg.substring(PROTOCOL_PREFIX_LENGTH);
        char sep = (char) 31;
        String[] param = msg.split(String.valueOf(sep));
        senderName = param[0];
        return msg.substring(param[0].length() + 1);
    }
}


