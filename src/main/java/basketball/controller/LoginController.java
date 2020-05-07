package basketball.controller;

import basketball.sceneManagement.SceneMangerFactory;
import basketball.sceneManagement.Scenes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import service.IService;
import service.SellerService;

import java.io.IOException;

public class LoginController {
    private SellerService service;

    @FXML
    private Text errorMessage;
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;

    public LoginController() {

    }

    public void setService(SellerService service) {
        this.service = service;
        init();
    }

    private void init() {
        errorMessage.setVisible(false);
    }

    @FXML
    private void handleLoginButton(ActionEvent event) throws IOException {
        if(service.checkUserAndPassword(user.getText(), password.getText())) {
            SceneMangerFactory manager = SceneMangerFactory.getInstance();
            manager.setScene(Scenes.MatchManagement);
        }
        else
            errorMessage.setVisible(true);
    }
}
