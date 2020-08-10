package io.github.mkmax.opticview.scene;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class AuthorPane extends StackPane {

    private final HBox container = new HBox ();
    private final Label copyright = new Label ();
    private final Hyperlink
        website = new Hyperlink ("Website"),
        github = new Hyperlink ("Github");

    private URI
        websiteURI = null,
        githubURI = null;

    public AuthorPane (
        final String copyrightMessage,
        final String websiteURL,
        final String githubURL)
    {
        /* setup container */
        getChildren ().add (container);
        container.getChildren ().addAll (copyright, website, github);
        container.setAlignment (Pos.CENTER);
        container.setSpacing (12d);

        /* setup uris */
        try {
            websiteURI = new URL (websiteURL).toURI ();
            githubURI = new URL (githubURL).toURI ();
        }
        catch (Exception e) {
            e.printStackTrace ();
        }

        /* setup the labels and links */
        copyright.setText (copyrightMessage);
        website.setOnAction (e -> new Thread (() -> {
            try {
                Desktop.getDesktop ().browse (websiteURI);
            }
            catch (Exception a) {
                a.printStackTrace ();
            }
        }).start ());
        github.setOnAction (e -> new Thread (() -> {
            try {
                Desktop.getDesktop ().browse (githubURI);
            }
            catch (Exception b) {
                b.printStackTrace ();
            }
        }).start ());
    }
}
