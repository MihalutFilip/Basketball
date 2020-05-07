package basketball.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.converter.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import model.Client;
import model.Match;
import model.Ticket;
import service.ClientService;
import service.MatchService;
import service.SellerService;
import service.TicketService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MatchesManagementController {
    private ClientService clientService;
    private MatchService matchService;
    private TicketService ticketService;

    @FXML
    private ComboBox clientsComboBox;
    @FXML
    private TextField clientName;
    @FXML
    private TextField placesInput;

    @FXML
    private TableView<Match> table;
    @FXML
    private TableColumn<Match, String> status;
    @FXML
    private TableColumn<Match, String> name;
    @FXML
    private TableColumn<Match, Integer> places;
    @FXML
    private TableColumn<Match, Integer> price;

    public MatchesManagementController() {
    }

    public void setService(ClientService clientService, MatchService matchService, TicketService ticketService) {
        this.clientService = clientService;
        this.matchService = matchService;
        this.ticketService = ticketService;
        initClientsComboxBox();
        initTableView();
        initTextFields();
    }

    private void initTableView() {
        List<Match> matchesList =  StreamSupport.stream(matchService.findAll().spliterator(), false)
                .sorted((match1, match2) -> match1.getPlacesRemaining() > match2.getPlacesRemaining() ? -1 : 1)
                .collect(Collectors.toList());

        status.setCellValueFactory(new PropertyValueFactory<Match, String>("status"));
        name.setCellValueFactory(new PropertyValueFactory<Match, String>("name"));
        price.setCellValueFactory(new PropertyValueFactory<Match, Integer>("price"));
        places.setCellValueFactory(new PropertyValueFactory<Match, Integer>("placesRemaining"));
        table.getItems().setAll(matchesList);

        setColorForEachRow();
    }

    private void setColorForEachRow() {
        table.setRowFactory(tv -> new TableRow<Match>() {
            @Override
            public void updateItem(Match item, boolean empty) {
                super.updateItem(item, empty) ;
                if (item == null) {
                    setStyle("");
                } else if (item.getPlacesRemaining() == 0) {
                    setStyle("-fx-background-color: tomato;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void initClientsComboxBox() {
        List<String> clients = StreamSupport.stream(clientService.findAll().spliterator(), false)
                .map(client -> client.getName())
                .collect(Collectors.toList());

        clientsComboBox.getItems().clear();
        clientsComboBox.getItems().addAll(clients);
        clientsComboBox.setValue(clients.get(0));
    }

    private void initTextFields() {
        placesInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    placesInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    private void handleAddClientButton(ActionEvent event) {
        clientService.save(new Client(clientName.getText()));
        initClientsComboxBox();
    }

    @FXML
    private void handleBuyTicket(ActionEvent event) {
        int places = Integer.parseInt(placesInput.getText());
        Match match = table.getSelectionModel().getSelectedItem();
        String clientName = (String) clientsComboBox.getSelectionModel().getSelectedItem();
        Client client = StreamSupport.stream(clientService.findAll().spliterator(), false)
                .filter(c -> c.getName().equals(clientName))
                .collect(Collectors.toList()).get(0);

        if(match == null || client == null)
            return;

        Ticket ticket = new Ticket(places);
        ticket.setId(new Pair(client.getId(), match.getId()));
        ticketService.save(ticket);
        match.setPlacesRemaining(match.getPlacesRemaining() - places);
        matchService.update(match);

        initTableView();
    }
}
