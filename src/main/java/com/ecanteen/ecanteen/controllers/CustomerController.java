package com.ecanteen.ecanteen.controllers;

import com.ecanteen.ecanteen.Main;
import com.ecanteen.ecanteen.dao.CategoryDaoImpl;
import com.ecanteen.ecanteen.dao.CustomerDaoImpl;
import com.ecanteen.ecanteen.entities.Category;
import com.ecanteen.ecanteen.entities.Customer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML
    private Button productMenuButton;
    @FXML
    private Button categoryMenuButton;
    @FXML
    private Button supplierMenuButton;
    @FXML
    private Button promotionMenuButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableColumn<Customer, String> idTableColumn;
    @FXML
    private TableColumn<Customer, String> nameTableColumn;
    @FXML
    private Button customerMenuButton;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> phoneTableColumn;

    private ObservableList<Customer> customers;
    private CustomerDaoImpl customerDao;
    private Customer selectedCustomer;

    @FXML
    private void addButtonAction(ActionEvent actionEvent) {
        if (idTextField.getText().trim().isEmpty() || nameTextField.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Silakan isi id dan nama supplier!");
            alert.showAndWait();
        } else {
            Customer customer = new Customer();
            customer.setId(idTextField.getText().trim());
            customer.setName(nameTextField.getText().trim());
            customer.setPhone(phoneTextField.getText().trim());

            try {
                if (customerDao.addData(customer) == 1) {
                    customers.clear();
                    customers.addAll(customerDao.fetchAll());
                    resetCustomer();
                    idTextField.requestFocus();
                    infoLabel.setText("Data berhasil ditambahkan!");
                    infoLabel.setStyle("-fx-text-fill: green");

                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void resetCustomer() {
        idTextField.clear();
        nameTextField.clear();
        phoneTextField.setText(null);
        selectedCustomer = null;
        idTextField.setDisable(false);
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        resetButton.setDisable(true);
        idTextField.requestFocus();
        infoLabel.setText("");
    }

    @FXML
    private void updateButtonAction(ActionEvent actionEvent) {
        if (nameTextField.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Silakan isi nama supplier!");
            alert.showAndWait();
        } else {
            selectedCustomer.setName(nameTextField.getText().trim());
            selectedCustomer.setPhone(phoneTextField.getText().trim());

            try {
                if (customerDao.updateData(selectedCustomer) == 1) {
                    customers.clear();
                    customers.addAll(customerDao.fetchAll());
                    resetCustomer();
                    customerTableView.requestFocus();
                    infoLabel.setText("Data berhasil diubah!");
                    infoLabel.setStyle("-fx-text-fill: green");
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteButtonAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Konfirmasi");
        alert.setContentText("Anda yakin ingin menghapus?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            try {
                if (customerDao.deleteData(selectedCustomer) == 1) {
                    customers.clear();
                    customers.addAll(customerDao.fetchAll());
                    resetCustomer();
                    customerTableView.requestFocus();
                    infoLabel.setText("Data berhasil dihapus!");
                    infoLabel.setStyle("-fx-text-fill: green");
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void resetButtonAction(ActionEvent actionEvent) {
        resetCustomer();

    }

    @FXML
    private void searchTextFieldKeyPressed(KeyEvent keyEvent) {
        FilteredList<Customer> filteredList = new FilteredList<>(customers, b -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(customer -> {
            if (newValue.isEmpty()) {
                return true;
            }

            String searchKeyword = newValue.toLowerCase().trim();

            if (customer.getId().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (customer.getName().toLowerCase().contains(searchKeyword)){
                return true;
            }
            else return customer.getPhone().toLowerCase().contains(searchKeyword);
        }));

        SortedList<Customer> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(customerTableView.comparatorProperty());
        customerTableView.setItems(sortedList);
    }

    @FXML
    private void productMenuButtonAction(ActionEvent actionEvent) throws IOException {
        Stage productStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("product-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        productStage.setTitle("Produk | e-Canteen");
        productStage.setMaximized(true);
        productStage.setScene(scene);
        productStage.show();

        Stage stage = (Stage) productMenuButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void categoryMenuButtonAction(ActionEvent actionEvent) throws IOException {
        Stage categoryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("category-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        categoryStage.setTitle("Category | e-Canteen");
        categoryStage.setMaximized(true);
        categoryStage.setScene(scene);
        categoryStage.show();

        Stage stage = (Stage) supplierMenuButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void supplierMenuButtonAction(ActionEvent actionEvent) throws IOException {
        Stage supplierStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("supplier-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        supplierStage.setTitle("Supplier | e-Canteen");
        supplierStage.setMaximized(true);
        supplierStage.setScene(scene);
        supplierStage.show();

        Stage stage = (Stage) supplierMenuButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void customerTableViewClicked(MouseEvent mouseEvent) {
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            idTextField.setText(selectedCustomer.getId());
            nameTextField.setText(selectedCustomer.getName());
            phoneTextField.setText(selectedCustomer.getPhone());
            idTextField.setDisable(true);
            addButton.setDisable(true);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
            resetButton.setDisable(false);
        }
    }

    @FXML
    private void promotionMenuButtonAction(ActionEvent actionEvent) throws IOException {
        Stage promotionStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("promotion-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        promotionStage.setTitle("Promotion | e-Canteen");
        promotionStage.setMaximized(true);
        promotionStage.setScene(scene);
        promotionStage.show();

        Stage stage = (Stage) supplierMenuButton.getScene().getWindow();
        stage.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerDao = new CustomerDaoImpl();
        customers = FXCollections.observableArrayList();

        try {
            customers.addAll(customerDao.fetchAll());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        customerTableView.setItems(customers);
        idTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        nameTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        phoneTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
    }
    }
