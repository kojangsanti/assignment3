/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayvideo;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.Status.PLAYING;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Administrator
 */
public class FXMLDocumentController implements Initializable {
    
       @FXML
    private Slider volume;

    @FXML
    private Button pauseButton;
    private Timer timer;
    private TimerTask task;
    private boolean running;

    @FXML
    private Button stopButton;

    @FXML
    private ProgressBar progress;

    @FXML
    private Button playButoon;
   
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;
    @FXML
    private MediaView mediaView;
    
     @FXML
    private Button ChooseButton;
   
    
    @FXML
    void Play(ActionEvent event) {
        
        beginTimer();
        mediaPlayer.setVolume(volume.getValue() *0.01);

        if(mediaPlayer.getStatus()==PLAYING){
            mediaPlayer.stop();
            mediaPlayer.play();
        }else{
        mediaPlayer.play();
        }
    }
  @FXML
    private Button fastBack;
  
    @FXML
    private Button FastForward;
    @FXML
    void Pause(ActionEvent event) {
        cancelTimer();
        mediaPlayer.pause();
    }

    @FXML
    void Stop(ActionEvent event) {
        progress.setProgress(0);
         mediaPlayer.stop();
    }
      @FXML
    private void onFastForwardButtonClick(ActionEvent event) {
        if (mediaPlayer != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration fastForwardTime = currentTime.add(Duration.seconds(5)); // Fast forward by 5 seconds
            mediaPlayer.seek(fastForwardTime);
        }
    }
        @FXML
    void onFastBackwardButtonClick(ActionEvent event) {
           if (mediaPlayer != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration rewindTime = currentTime.subtract(Duration.seconds(5)); // Rewind by 5 seconds
            mediaPlayer.seek(rewindTime);
        }
    }

      @FXML
    void ChooseVideo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video File");
        Stage stage = (Stage) ChooseButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Perform actions with the selected video file, e.g., play the video
            String videoUrl = selectedFile.toURI().toString();
            media = new Media(videoUrl);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            
            System.out.println("Selected video file: " + selectedFile.getAbsolutePath());
        }
    
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       volume.valueProperty().addListener(new ChangeListener<Number>(){
           @Override
           public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
              mediaPlayer.setVolume(volume.getValue() *0.01);
           }
       }); 
       progress.setStyle("-fx-accent: #00ff00;");
    }   
    public void beginTimer(){
        timer=new Timer();
        
       task=new TimerTask(){
            
            public void run() {
              running=true;
              double current=mediaPlayer.getCurrentTime().toSeconds();
              double end=media.getDuration().toSeconds();
              
              progress.setProgress(current/end);
              
              if(current/end==1){
                  cancelTimer();
              }
            } 
       };
       timer.scheduleAtFixedRate(task, 0, 1000);
    }
    public void cancelTimer(){
        running=false;
        timer.cancel();
    }
}
