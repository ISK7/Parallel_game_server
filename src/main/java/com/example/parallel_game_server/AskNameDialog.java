package com.example.parallel_game_server;

import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class AskNameDialog extends Dialog<String> {
    @FXML
    TextField text;
    Stage dialogStage;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }



    public void onAseptClick() {

    }
}

