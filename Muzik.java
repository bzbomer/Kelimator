package Grup19Kelimator;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Muzik {

    private boolean muzikCaliyorMu=false;
    //ana muzik
    private static InputStream music;
    private static AudioStream audios;
    //clock icin
    private static InputStream music2;
    private static AudioStream audios2;


    public void setMuzikCaliyorMu(boolean muzikCaliyorMu) {
        this.muzikCaliyorMu = muzikCaliyorMu;
    }

    public static void durdur(){

        if(music!=null) {
            AudioPlayer.player.stop(audios);
        }
    }

    public static void oynat(String dosya){

        try{
            music= new FileInputStream(new File(dosya));
            audios = new AudioStream(music);
            AudioPlayer.player.start(audios);
        }catch (Exception e) {
            System.out.print("Muzik calinamadi");
        }
    }

    public static void clockOynat(){
        try{
            music2= new FileInputStream(new File("clock.wav"));
            audios2 = new AudioStream(music2);
            AudioPlayer.player.start(audios2);
        }catch (Exception e) {
            System.out.print("Muzik calinamadi");
        }
    }

    public static void clockDurdur(){
        AudioPlayer.player.stop(audios2);
    }

    public static void dogruCevapSound(){
        InputStream msc;
        AudioStream ses;
        try{

            msc= new FileInputStream(new File("DogruCevap.wav"));
            ses = new AudioStream(msc);
            AudioPlayer.player.start(ses);
        }catch (Exception e){
            System.out.print("Muzik calinamadi");
        }
    }

    public static void yanlisCevapSound(){
        InputStream msc;
        AudioStream ses;
        try{

            msc= new FileInputStream(new File("YanlisCevap.wav"));
            ses = new AudioStream(msc);
            AudioPlayer.player.start(ses);
        }catch (Exception e){
            System.out.print("Muzik calinamadi");
        }
    }

}
