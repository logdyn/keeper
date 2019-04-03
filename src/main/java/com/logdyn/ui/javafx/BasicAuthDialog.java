package com.logdyn.ui.javafx;

import com.logdyn.core.authentication.BasicAuthenticator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.UncheckedIOException;

public class BasicAuthDialog extends Dialog<BasicAuthenticator>
{
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    public BasicAuthDialog()
    {
        try
        {
            this.setDialogPane(FXMLLoader.load(getClass().getResource("/fxml/basicAuthDialog.fxml"), null, null, c -> this));
            this.setTitle("Authenticate");

            this.setResultConverter(b -> {
                if (b.equals(ButtonType.OK))
                {
                    return new BasicAuthenticator(username.getText(), password.getText());
                }
                return null;
            });
        }
        catch (final IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    @FXML
    private void initialize()
    {
        this.username.requestFocus();

    }
}
