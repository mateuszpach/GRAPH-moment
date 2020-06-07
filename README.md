# GRAPH-moment

### Environment setup:
1. Clone the repo.
    ```
    git clone https://github.com/mtdrewski/GRAPH-moment
    ```
2. Open GRAPH-moment.iml in IntelliJ IDEA.
3. Go to Project Structure>Project Settings>Project>Project SDK and download JDK Amazon Corretto 11.0.7
4. Create run configuration for Main.java class and add the following line to VM options in run configuration.
    ```
    --module-path <path to repo>/GRAPH-moment/libs/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml
    ```
5. Run Main.java class.
