import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
            Image myImage = new Image(new File("/res/harveyspecter.png").toURI().toString());
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

        private String receiveMessage() throws IOException { return  inputBufferedReader.readLine(); }
    }
