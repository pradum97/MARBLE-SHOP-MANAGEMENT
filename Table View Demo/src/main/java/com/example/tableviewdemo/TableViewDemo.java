package com.example.tableviewdemo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * TableView: multiple selection lost on set item.
 */
public class TableViewDemo extends Application {

    private int counter;

    private Parent getContent() {
        TableView<Locale> table = new TableView<>(FXCollections.observableArrayList(
                Locale.getAvailableLocales()));
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableColumn<Locale, String> countryCode = new TableColumn<>("CountryCode");
        countryCode.setCellValueFactory(new PropertyValueFactory<>("country"));
        TableColumn<Locale, String> language = new TableColumn<>("Language");
        language.setCellValueFactory(new PropertyValueFactory<>("language"));
        TableColumn<Locale, String> variant = new TableColumn<>("Variant");
        variant.setCellValueFactory(new PropertyValueFactory<>("variant"));
        table.getColumns().addAll(countryCode, language, variant);
        Button set = new Button("Set");
        set.setOnAction(e ->
                {
                    for(Object o : table.getSelectionModel().getSelectedItems()){
                       // table.getItems().remove(o);

                        System.out.println(o);
                    }
                   // table.getSelectionModel().clearSelection();
                });
        HBox buttons = new HBox(10, set);
        BorderPane pane = new BorderPane(table);
        pane.setBottom(buttons);
        return pane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getContent(), 800, 400));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
