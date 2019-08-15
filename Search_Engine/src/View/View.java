package View;

import Controller.Controller;
import Model.Document;
import Model.Parse;
import Model.Term;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class View {
    public javafx.scene.control.Button btn_startUp;
    public javafx.scene.control.Button btn_search;
    public javafx.scene.control.Button btn_reset;
    public javafx.scene.control.Button btn_browse;
    public javafx.scene.control.Button btn_browse_posting;
    public javafx.scene.control.Button btn_browse_queries;
    public javafx.scene.control.Button btn_browse_queriesResults;
    public javafx.scene.control.Button btn_showDic;
    public javafx.scene.control.Button btn_loadDic;
    public javafx.scene.control.Button btn_entity;
    public javafx.scene.control.Button btn_save;
    public javafx.scene.control.TextField corpus_path;
    public javafx.scene.control.TextField posting_path;
    public javafx.scene.control.TextField singleQuery;
    public javafx.scene.control.TextField queriesFile;
    public javafx.scene.control.TextField fileToSave;
    public javafx.scene.control.CheckBox btn_stemming;
    public javafx.scene.control.CheckBox btn_semantic;
    public javafx.scene.control.ComboBox btn_language;
    public CheckBox btn_filter;
    public SplitMenuButton citiesMenu;
    public SplitMenuButton docsMenu;
    public Controller conection_layer;

    DirectoryChooser directoryChooser = new DirectoryChooser();
    List<CheckMenuItem> citiesOptions = new ArrayList<>();
    List<CheckMenuItem> docsOptions = new ArrayList<>();

    public void setController(Controller conection_layer) {
        this.conection_layer = conection_layer;
        btn_search.setDisable(true);
        btn_save.setDisable(true);
        btn_entity.setDisable(true);
    }

    //Adds all cities to a button so you can choose from there for filtering
    public void citiesOptions(HashSet<String> cities){
        citiesOptions = new ArrayList<>();
        for (String city : cities )
            citiesOptions.add(new CheckMenuItem(city));
        citiesMenu.getItems().addAll(citiesOptions);
    }

    //Adds all documents to the button so that you can select from there for entity selection
    public void docsOptions(List<String> returnDocs){
        docsOptions = new ArrayList<>();
        for (String doc : returnDocs)
            docsOptions.add(new CheckMenuItem(doc));
        docsMenu.getItems().addAll(docsOptions);
    }
    //Filters the cities the user has selected from all cities
    public void filterCities(){
        if(btn_filter.isSelected()){
            for (int i = 0; i < citiesOptions.size(); i++) {
                if(citiesOptions.get(i).isSelected())
                    conection_layer.cityAfterFilter.add(citiesOptions.get(i).getText());
            }
        }
    }
    //Checks whether should search for a single query or query document,
    // and sends in accordance with the function of searching a single query or function to search for a query file
    // At the end, the search results are displayed and the option to click the View Entities button or save the results is opened
    public void search() throws IOException {
        if(singleQuery.getText().equals("") && queriesFile.getText().equals("")) {
            showAlert("you must write one query or choose a queries file");
            return;
        }
        if(!singleQuery.getText().equals("") && !queriesFile.getText().equals("")){
            showAlert("We can not search both a single query and a query file. Please empty one of the fields");
            return;
        }
        if(!singleQuery.getText().equals("")) {
            showAlert(conection_layer.search(singleQuery.getText(), conection_layer.cityAfterFilter, btn_stemming.isSelected(), btn_semantic.isSelected(), posting_path.getText()).toString());
            docsOptions(conection_layer.getReturnDocs());
        }
        else {
            boolean check=conection_layer.searchFromfile(queriesFile.getText(), conection_layer.cityAfterFilter, btn_stemming.isSelected(), btn_semantic.isSelected(), posting_path.getText());
            if(check==false) {
                showAlert("file queries does not exist in your selected path");
                return;
            }
            docsOptions(conection_layer.rq.getDocsThatReturned());
        }
        btn_save.setDisable(false);
        btn_entity.setDisable(false);
    }
    //Select a path to the corpus
    public void browseButtonForCorpus (){
        Stage stage = new Stage();
        stage.setTitle("File Chooser");
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            corpus_path.setText(file.getPath());
        }
    }
    //Displays the entities for the document or documents that the user has selected
    public void entityRecognition(){
        if(docsOptions.size() < 1)
            showAlert("you must select any document first");
        boolean isSelected = false;
        for (int i = 0; i < docsOptions.size(); i++) {
            if (docsOptions.get(i).isSelected()) {
                showAlert(conection_layer.sotr5things(docsOptions.get(i).getText()));
                isSelected = true;
            }
        }
        if (!isSelected)
            showAlert("you must select any document first");
    }
    //Save search results to file on disk
    public void saveing() throws IOException {
        if(singleQuery.getText().equals("") && queriesFile.getText().equals("")) {
            showAlert("you must write one query or choose a queries file to save the results");
            return;
        }
        String file=fileToSave.getText();
        if(file.equals("")){
            showAlert("please enter a path to the place you want to save the results");
            return;
        }
        if (!singleQuery.getText().equals(""))
            conection_layer.save(file,true);
        else
            conection_layer.save(file,false);
        showAlert("the results saved");
    }
    //Select a path to the queries file
    public void browseButtonForQueries (){
        Stage stage = new Stage();
        stage.setTitle("File Chooser");
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            queriesFile.setText(file.getPath());
        }
    }
    //Select a path to save the results
    public void browseButtonForResults (){
        Stage stage = new Stage();
        stage.setTitle("File Chooser");
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            fileToSave.setText(file.getPath());
        }
    }
    //Select a path to the postings
    public void browseButtonForPosting (){
        Stage stage = new Stage();
        stage.setTitle("File Chooser Sample");
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            posting_path.setText(file.getPath());
        }
    }
    //Adds all languages ​​from the documents in the corpus to the Language button
    public void language(){
        HashSet<String> test = new HashSet<>();
        test.addAll(conection_layer.getLanguages());
        Iterator value = conection_layer.getLanguages().iterator();
        while (value.hasNext()) {
            btn_language.getItems().add(value.next());
        }
    }
    //Running the parser and indexer on the corpus
    public void startUp() throws IOException {
        if(corpus_path.getText().equals("") || posting_path.getText().equals("")) {
            showAlert("you must fill the fields of corpus and posting");
            return;
        }
        conection_layer.start(corpus_path.getText(), posting_path.getText(), btn_stemming.isSelected());
        showAlert(conection_layer.getAlert());
    }
    //Displays the dictionary after the first part of the project
    public void showDic() throws IOException {
        File f = new File(posting_path.getText() + "/sorted_Dictionary.txt");
        if(f.exists() && !posting_path.getText().equals(""))
            Desktop.getDesktop().open(f);
        else
            showAlert("first you need to press \"Start!\"");
    }
    //Resetting the objects to restart the program
    public void reset() throws IOException {
        File f = new File(posting_path.getText() + "/sorted_Dictionary.txt");
        if(f.exists() && !posting_path.getText().equals("")) {
            conection_layer.reset(posting_path.getText());
            showAlert("finished to reset");
            btn_search.setDisable(true);
        }
        else
            showAlert("first you need to press \"Start!\"");
    }
    //Loads the objects into memory
    public void loadDic() throws IOException, ClassNotFoundException {
        File f = new File(posting_path.getText() + "/posting/docs.txt");
        if(posting_path.getText().equals("")) {
            showAlert("first you need to write a path in the place you write the path to postings files");
            return;
        }
        else if(!f.exists()) {
            showAlert("this path is not proper");
            return;
        }
        else {
            conection_layer.loadDic(posting_path.getText(), btn_stemming.isSelected());
            btn_search.setDisable(false);
        }
        citiesOptions(conection_layer.cities);
        language();
        showAlert("finish loading");
    }
    //Displays a message after first part of the project is finished to run
    public void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();

    }

}
