package basketball.sceneManagement;

import basketball.controller.LoginController;
import basketball.controller.MatchesManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.ClientService;
import service.MatchService;
import service.SellerService;
import service.TicketService;

import java.io.IOException;

public class SceneMangerFactory {
    private static final String dependenciesFileName = "dependencies.xml";
    private static SceneMangerFactory instance = null;
    private Stage stage;

    private SceneMangerFactory() {

    }

    public static SceneMangerFactory getInstance(){
        if(instance == null)
            instance = new SceneMangerFactory();
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public void setScene(Scenes scene) throws IOException {
        switch (scene) {
            case Login:
                setLoginScene();
                break;
            case MatchManagement:
                setMatchesManagementScene();
                break;
        }
    }

    private void setMatchesManagementScene() throws IOException {
        stage.setTitle("Matches");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/matchesManagement.fxml"));
        AnchorPane myPane = loader.load();
        MatchesManagementController controller = loader.getController();
        controller.setService(getClientService(), getMatchService(), getTicketService());
        Scene scene = new Scene(myPane);
        stage.setScene(scene);
    }

    private void setLoginScene() throws IOException {
        stage.setTitle("Log in");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        AnchorPane myPane = loader.load();
        LoginController controller = loader.getController();
        controller.setService(getSellerService());
        Scene scene = new Scene(myPane);
        stage.setScene(scene);
    }

    static SellerService getSellerService() {
        ApplicationContext context = new ClassPathXmlApplicationContext(dependenciesFileName);
        SellerService service = context.getBean(SellerService.class);
        return service;
    }

    static ClientService getClientService() {
        ApplicationContext context = new ClassPathXmlApplicationContext(dependenciesFileName);
        ClientService service = context.getBean(ClientService.class);
        return service;
    }

    static MatchService getMatchService() {
        ApplicationContext context = new ClassPathXmlApplicationContext(dependenciesFileName);
        MatchService service = context.getBean(MatchService.class);
        return service;
    }

    static TicketService getTicketService() {
        ApplicationContext context = new ClassPathXmlApplicationContext(dependenciesFileName);
        TicketService service = context.getBean(TicketService.class);
        return service;
    }
}
