package com.example.fun;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Optional;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.application.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.awt.image.*;

/**
 * Hold down an arrow key to have your hero move around the screen.
 * Hold down the shift key to have the hero run.
 */
class Mushroom{
    double center_X,center_Y,radius_X,radius_Y,start_Angle,length_;
   // double widthRect, heightRect;
    AnchorPane root;
    GraphicsContext gc;
    public Mushroom(double centerX,double centerY,double radiusX,double radiusY, double startAngle,double length,AnchorPane roott,GraphicsContext gcc ){
        center_X=centerX;
        center_Y=centerY;
        radius_X=radiusX;
        radius_Y=radiusY;
        start_Angle=startAngle;
        length_=length;
        root = roott;
        gc = gcc;
    }

    public double getCenter_X(){
        return center_X;
    }
    public double getCenter_Y(){
        return center_Y;
    }
    public double getRadius_X(){
        return radius_X;
    }
    public double getRadius_Y(){
        return radius_Y;
    }
    public double getStart_Angle(){
        return start_Angle;
    }
    public double getLength_(){
        return length_;
    }
    public void drawMushroom(){
        Arc arc = new Arc();

        arc.setCenterX(this.center_X );
        arc.setCenterY(this.center_Y );
        arc.setRadiusX(this.radius_X);
        arc.setRadiusY(this.radius_Y);
        arc.setStartAngle(this.start_Angle);
        arc.setLength(this.length_);
        arc.setType(ArcType.ROUND);
        arc.setFill(Color.SADDLEBROWN);
        root.getChildren().add(arc);

        gc.setFill(Color.BURLYWOOD);
        gc.fillRect(arc.getCenterX() -(arc.getRadiusX())/3 , arc.getCenterY(), 2*(arc.getRadiusX())/3, 4*(arc.getRadiusX())/3);
    }
}
public class gameWeb extends Application {

    private static final double W = 600, H = 400;
    private static final String HERO_IMAGE_LOC =
            "https://icons.iconarchive.com/icons/iron-devil/ids-3d-icons-20/32/Bear-icon.png";
    private Image heroImage;

    private Node  hero;
    boolean running, goNorth, goSouth, goEast, goWest;

    GraphicsContext gc;
    Canvas canvas;
    Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("JavaFX App");
        heroImage = new Image(HERO_IMAGE_LOC);
        hero = new ImageView(heroImage);
        Group dungeon = new Group(hero);

        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #98FB98;");
        canvas = new Canvas(W, H);
        root.getChildren().add(dungeon);

        moveHeroTo(W-40 , H -40);  //ustawiam postać w prawym dolnym rogu

        gc=canvas.getGraphicsContext2D();
        //drawLines(gc);

        /*-------------Dodawanie grzybków--------------- */
        Random rand = new Random();

        Mushroom mushroom = new Mushroom(rand.nextInt(501)+20,rand.nextInt(300),10,10,0,180,root,gc);
        mushroom.drawMushroom();
        Mushroom mushroom2 = new Mushroom(rand.nextInt(501)+20,rand.nextInt(300),10,10,0,180,root,gc);
        mushroom2.drawMushroom();
        Mushroom mushroom3 = new Mushroom(rand.nextInt(501)+20,rand.nextInt(300),10,10,0,180,root,gc);
        mushroom3.drawMushroom();
        Mushroom mushroom4 = new Mushroom(rand.nextInt(501)+20,rand.nextInt(300),10,10,0,180,root,gc);
        mushroom4.drawMushroom();

        root.getChildren().add(canvas);

        Menu menu1 = new Menu("File");
        MenuItem menuItem1 = new MenuItem("Item 1");
        MenuItem menuItem2 = new MenuItem("Exit");
        menuItem2.setOnAction(e -> {
            System.out.println("Exit Selected");

            exit_dialog();

        });
        menu1.getItems().add(menuItem1);
        menu1.getItems().add(menuItem2);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);
        VBox vBox = new VBox(menuBar);   //przyciski jeden pod drugim
        root.getChildren().add(vBox);

        Scene scene = new Scene(root, W, H, Color.FORESTGREEN);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = true; break;
                    case DOWN:  goSouth = true; break;
                    case LEFT:  goWest  = true; break;
                    case RIGHT: goEast  = true; break;
                    case SHIFT: running = true; break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = false; break;
                    case DOWN:  goSouth = false; break;
                    case LEFT:  goWest  = false; break;
                    case RIGHT: goEast  = false; break;
                    case SHIFT: running = false; break;
                }
            }
        });

        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            e.consume();
            exit_dialog();
        });
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int dx = 0, dy = 0;
                if (goNorth) dy -= 1;
                if (goSouth) dy += 1;
                if (goEast)  dx += 1;
                if (goWest)  dx -= 1;
                if (running) { dx *= 3; dy *= 3; }

                moveHeroBy(dx, dy);
            }
        };
        timer.start();


    }

    private void moveHeroBy(int dx, int dy) {
        if (dx == 0 && dy == 0) return;

        final double cx = hero.getBoundsInLocal().getWidth()  / 2;
        final double cy = hero.getBoundsInLocal().getHeight() / 2;

        double x = cx + hero.getLayoutX() + dx;
        double y = cy + hero.getLayoutY() + dy;

        moveHeroTo(x, y);
    }
    private void moveHeroTo(double x, double y) {
        final double cx = hero.getBoundsInLocal().getWidth()  / 2;
        final double cy = hero.getBoundsInLocal().getHeight() / 2;

        if (x - cx >= 0 &&
                x + cx <= W &&
                y - cy >= 0 &&
                y + cy <= H) {
            hero.relocate(x - cx, y - cy);
        }
    }
    /*public void drawLines(GraphicsContext gc){
        gc.beginPath();
        gc.moveTo(0,30.5);
        gc.lineTo(150,30.5);
        gc.lineTo(150,350);
        //gc.lineTo(150,200);
        gc.stroke();

    }*/
    public void exit_dialog()
    {
        System.out.println("exit dialog");

        Alert alert = new Alert(AlertType.CONFIRMATION,
                "Do you really want to exit the program?.",
                ButtonType.YES, ButtonType.NO);

        alert.setResizable(true);
        alert.onShownProperty().addListener(e -> {
            Platform.runLater(() -> alert.setResizable(false));
        });

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES)
        {
            Platform.exit();
        }
        else
        {
        }

    }
    public static void main(String[] args) { launch(args); }
}