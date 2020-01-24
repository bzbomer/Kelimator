package Grup19Kelimator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 *  Oyunun tek kisilik modda calısacak olan class yapısıdır.
 */
public class TekKisilik extends Kelimator {

    /**
     *  Oyuncunun bilgilerinin update edilmesi için gereken private değiskendir.
     */
    protected Oyuncu oyuncu;
    protected String alan ;
    Timer timer;
    Timer timer2;
    Timer timer3; //p1 hamle suresi sayar
    Timer timer4;//p1 animasyon 2 saniye
    int sure = 30 , animasyonSure = 2 , kayanYaziTetiklemeSure=3 , kayanYaziSayac=0;
    boolean jokerKullaniliyor=false;
    boolean muzikCaliyorMu = true ,muzikIstek=true, clockCaliyorMu=false;
    boolean joker1=false,joker2=false;

    String ilkHarf="A";
    String anlamiGosterilecekKelime;

    JFrame frame;
    JPanel GUI;
    JLabel alanLabel;
    JLabel playerLabel;
    DefaultListModel<String> listModel;
    JList<String> jList;
    JButton ekSureJokerButon;
    JButton yeniHarfJokerButon;
    JButton muzikButton;
    JButton kaydet;
    JButton yukle;
    JTextField kelimeGiris;
    JButton kelimeGirButton;
    JButton kapatButton;
    JLabel timeLabel;
    JLabel puanLabel;
    JLabel basHarfLabel;
    JLabel anlamGosterLabel;


    /**
     *  No parameter constructor
     */
    public TekKisilik() {
        super();
        oyuncu = new Oyuncu();
    }

    public TekKisilik(String oyuncuAdi,String alan){
        super();
        oyuncu = new Oyuncu(oyuncuAdi);
        this.alan = alan;
        guiOlustur();
        Muzik.oynat("KelimatorMuzik.wav");
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                --sure;
                timeLabel.setText(":"+sure);
                if(sure<10 && !clockCaliyorMu){
                    if (muzikCaliyorMu){
                        Muzik.durdur();
                        muzikCaliyorMu=false;
                    }
                    Muzik.clockOynat();
                    clockCaliyorMu = true;
                }else if(sure>10) {
                    if(clockCaliyorMu) {
                        Muzik.clockDurdur();
                        clockCaliyorMu = false;
                    }
                    if(!muzikCaliyorMu && muzikIstek){
                        Muzik.oynat("KelimatorMuzik.wav");
                        muzikCaliyorMu=true;
                    }
                }
                if(sure==0){
                    timer.stop();
                    alanLabel.setText("OYUN BITTI");
                    yeniHarfJokerButon.setEnabled(false);
                    ekSureJokerButon.setEnabled(false);
                    kelimeGiris.setEnabled(false);
                    kelimeGirButton.setEnabled(false);
                    kaydet.setVisible(false);
                    yukle.setVisible(false);
                    anlamGosterLabel.setVisible(true);
                    anlamiGosterilecekKelime = ornekKelimeGoster(oyuncu.getHamleHaritasi(),ilkHarf.toLowerCase().charAt(0)).toUpperCase();
                    anlamiGosterilecekKelime += " : " +sozluk.get(anlamiGosterilecekKelime.toLowerCase()).toUpperCase();
                    System.out.println(".."+ilkHarf.toLowerCase().charAt(0)+".."+anlamiGosterilecekKelime);
                    anlamGosterLabel.setText(anlamiGosterilecekKelime);
                    timer3.start();
                    if(clockCaliyorMu)
                        Muzik.clockDurdur();
                    oyunuBitir();
                }
            }
        });
        timer.start();

        timer2 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                --animasyonSure;
                if(animasyonSure ==0 ) {
                    timer2.stop();
                    puanLabel.setText(""+oyuncu.getPuan());
                }
            }
        });

        timer3 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                --kayanYaziTetiklemeSure;
                if(kayanYaziTetiklemeSure <=0 ){
                    timer3.stop();
                    kayanYaziSayac = 0;
                    timer4.start();
                }
            }
        });

        timer4 = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sub = anlamiGosterilecekKelime.substring(kayanYaziSayac,anlamiGosterilecekKelime.length()-1);
                if(sub.length()<1 || sub==null){
                    timer4.stop();
                    anlamGosterLabel.setVisible(false);
                    kayanYaziSayac = 0;
                }
                kayanYaziSayac++;
                anlamGosterLabel.setText(sub);
            }
        });

        kaydet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        yukle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                load();
            }
        });


        kapatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                timer2.stop();
                timer3.stop();
                timer4.stop();
                oyunKurulu = false;
                Muzik.durdur();
                if(clockCaliyorMu)
                    Muzik.clockDurdur();
                frame.dispose();
            }
        });

        ekSureJokerButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sure += 15;
                jokerKullaniliyor=true;
                joker1=true;
                ekSureJokerButon.setEnabled(false);
            }
        });

        yeniHarfJokerButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                jokerKullaniliyor=true;
                joker2=true;
                yeniHarfJokerButon.setEnabled(false);
                char c = kelimeJokeri(alan);
                ilkHarf = "" + c;
                basHarfLabel.setText(ilkHarf.toUpperCase());
            }
        });

        muzikButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(muzikCaliyorMu) {
                    Muzik.durdur();
                    muzikIstek=false;
                    muzikButton.setText("Müziği Oynat");
                }
                else {
                    Muzik.oynat("kelimatorMuzik.wav");
                    muzikIstek=true;
                    muzikButton.setText("Müziği Kapat");
                }
                muzikCaliyorMu = !muzikCaliyorMu;
            }
        });
    }

    private void guiOlustur(){
        //textler icin font
        Font font = new Font("Courier", Font.BOLD,18);

        //frame
        frame = new JFrame("Tek Kisilik Kelimator");
        frame.setSize(420,720);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //panel
        GUI = new JPanel();
        GUI.setSize(410,720);
        GUI.setBackground(Color.PINK);
        GUI.setLayout(null);

        //alan label
        alanLabel = new JLabel();
        alanLabel.setFont(font);
        alanLabel.setText(alan.toUpperCase());
        alanLabel.setSize(330,18);
        alanLabel.setLocation(10,10);
        alanLabel.setVisible(true);
        GUI.add(alanLabel);

        //oyuncu adi label
        playerLabel = new JLabel();
        playerLabel.setFont(font);
        playerLabel.setText(oyuncu.getIsim().toUpperCase());
        playerLabel.setSize(330,18);
        playerLabel.setLocation(10,30);
        playerLabel.setVisible(true);
        GUI.add(playerLabel);

        //Ilk Harf label
        basHarfLabel = new JLabel();
        font = new Font("Courier", Font.BOLD,18);
        basHarfLabel.setFont(font);
        basHarfLabel.setText(ilkHarf.toUpperCase());
        basHarfLabel.setSize(50,18);
        basHarfLabel.setLocation(340,10);
        basHarfLabel.setVisible(true);
        GUI.add(basHarfLabel);

        //puan label
        puanLabel = new JLabel();
        font = new Font("Courier", Font.BOLD,18);
        puanLabel.setFont(font);
        puanLabel.setText(""+oyuncu.getPuan());
        puanLabel.setSize(50,18);
        puanLabel.setLocation(340,30);
        puanLabel.setVisible(true);
        GUI.add(puanLabel);


        //hamle haritasi liste
        listModel = new DefaultListModel<>();
        jList = new JList<String>();
        TitledBorder title;
        title = BorderFactory.createTitledBorder("Hamle Haritası");
        title.setTitleJustification(TitledBorder.CENTER);
        jList.setBorder(title);
        jList.setSize(380,495);
        jList.setLocation(10,50);
        jList.setVisible(true);
        GUI.add(jList);

        //ek sure joker
        ekSureJokerButon = new JButton();
        ekSureJokerButon.setText("+15 SANİYE");
        ekSureJokerButon.setBackground(Color.YELLOW);
        ekSureJokerButon.setSize(120,30);
        ekSureJokerButon.setLocation(10,585);
        ekSureJokerButon.setVisible(true);
        GUI.add(ekSureJokerButon);

        //yeni Harf joker
        yeniHarfJokerButon = new JButton();
        yeniHarfJokerButon.setText("YENİ HARF");
        yeniHarfJokerButon.setBackground(Color.YELLOW);
        yeniHarfJokerButon.setSize(120,30);
        yeniHarfJokerButon.setLocation(140,585);
        yeniHarfJokerButon.setVisible(true);
        GUI.add(yeniHarfJokerButon);

        //muzik ac kapa butonu
        muzikButton = new JButton();
        muzikButton.setText("Müziği Kapat");
        muzikButton.setBackground(Color.lightGray);
        muzikButton.setSize(120,30);
        muzikButton.setLocation(10,620);
        muzikButton.setVisible(true);
        GUI.add(muzikButton);

        //yukle butonu
        yukle = new JButton();
        yukle.setText("YÜKLE");
        yukle.setBackground(Color.yellow);
        yukle.setSize(120,30);
        yukle.setLocation(140,655);
        yukle.setVisible(true);
        GUI.add(yukle);

        //kaydet butonu
        kaydet = new JButton();
        kaydet.setText("KAYDET");
        kaydet.setBackground(Color.yellow);
        kaydet.setSize(120,30);
        kaydet.setLocation(10,655);
        kaydet.setVisible(true);
        GUI.add(kaydet);

        //kelime input giris
        kelimeGiris = new JTextField();
        kelimeGiris.setVisible(true);
        kelimeGiris.setSize(250,30);
        kelimeGiris.setLocation(10,550);
        GUI.add(kelimeGiris);

        //kelime gir buton
        kelimeGirButton = new JButton();
        kelimeGirButton.setText("Kelime Gir");
        kelimeGirButton.setBackground(Color.cyan);
        kelimeGirButton.setSize(120,30);
        kelimeGirButton.setLocation(270,550);
        kelimeGirButton.setVisible(true);
        GUI.add(kelimeGirButton);

        //kapat butonu
        kapatButton = new JButton();
        kapatButton.setText("Kapat");
        kapatButton.setBackground(Color.lightGray);
        kapatButton.setSize(120,30);
        kapatButton.setLocation(140,620);
        kapatButton.setVisible(true);
        GUI.add(kapatButton);

        //sure label
        timeLabel = new JLabel();
        font = new Font("Courier", Font.BOLD,65);
        timeLabel.setFont(font);
        timeLabel.setText(":30");
        timeLabel.setSize(120,65);
        timeLabel.setLocation(290,585);
        timeLabel.setVisible(true);
        GUI.add(timeLabel);

        anlamGosterLabel = new JLabel();
        font = new Font("Courier", Font.BOLD,30);
        anlamGosterLabel.setFont(font);
        anlamGosterLabel.setText("");
        anlamGosterLabel.setSize(380,30);
        anlamGosterLabel.setLocation(10,650);
        anlamGosterLabel.setVisible(false);
        GUI.add(anlamGosterLabel);


        //adding gui panel to frame
        //frame set visible true
        frame.add(GUI);
        frame.setContentPane(GUI);
        frame.setVisible(true);
    }

    /**
     * Oyunu tek modda baslatacak olan override edilmis metodumuzdur.
     */
    @Override
    public void oyunuBaslat(){
        System.out.println("Kelimator Tek kisilik : "+oyuncu.getIsim()+ "-alan : "+alan);

        // Ilgili alanin terimlerini dosyadan oku ve veri yapilarini olustur.
        kelimeleriEsitle(alan);

        oyunuOynat();
    }

    /**
     *  Oyuncu hamlelerinin kontrolünü sağlayan methoddur.
     *  Oyun sırasında gecerli olarak girilmis kelime aynı oyun icinde
     *  tekrardan girilmeye calisildiginda gecerli kabul edilmemelidir.
     */
    public void oyunuOynat() {

        kelimeGiris.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    hamleYap();
                }
            }
         });

        kelimeGirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hamleYap();
            }
        });

    }//oyunu oynat sonu

    private void hamleYap(){
        boolean hamle=false;
        String kelime = kelimeGiris.getText();
        //kelime = karakterCevirme(kelime.toLowerCase());
        System.out.println("Kelime : "+ kelime);
        if (!kelime.equals("") && !oyuncu.getHamleHaritasi().contains(kelime.toLowerCase()))
            hamle = kelimeDogrumu(kelime, ilkHarf.toLowerCase().charAt(0), alan);
        if(hamle) {
            // Gecerli hamleyi kaydet.
            oyuncu.kelimeKaydet(kelime.toLowerCase());

            // Puani guncelle.
            oyuncu.setPuan(oyuncu.getPuan() + puaniHesapla(jokerKullaniliyor));
            if(jokerKullaniliyor) {
                jokerKullaniliyor = false;
                animasyon(5);
            }else
                animasyon(10);

            listModel.addElement(kelime);
            jList.setModel(listModel);
            // Bir sonraki girilebilecek kelimenin ilk harfini belirler.
            ilkHarf =""+ kelime.charAt(kelime.length() - 1);
            basHarfLabel.setText(ilkHarf.toUpperCase());
            sure = 31;
            kelimeGiris.setText("");
            System.out.println(kelime+" : "+ sozluk.get(kelime.toLowerCase()));
        }else{
            Muzik.yanlisCevapSound();
        }
    }

    private void animasyon(int eklenenPuan){

        animasyonSure = 2;
        puanLabel.setText("+"+eklenenPuan);
        Muzik.dogruCevapSound();
        timer2.start();
    }

    public void save(){

        try {
            FileWriter writer =new FileWriter(new File("kaydedilenler\\"+oyuncu.getIsim()+"_"+alan+".csv"));
            writer.write(oyuncu.getIsim()+"\n");
            writer.write(oyuncu.getPuan().toString()+"\n");
            writer.write(sure+"\n");
            if(joker1)
                writer.write("1\n");
            else
                writer.write("0\n");
            if(joker2)
                writer.write("1\n");
            else
                writer.write("0\n");
            writer.write(sure+"\n");
            for(int i=0;i<oyuncu.getHamleHaritasi().size();++i){
                writer.write(oyuncu.getHamleHaritasi().get(i)+"\n");
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        try {
            timer.stop();
            if(clockCaliyorMu) {
                Muzik.clockDurdur();
                clockCaliyorMu=false;
            }
            if(muzikCaliyorMu)
                Muzik.durdur();
            listModel.clear();
            jList.setModel(listModel);

            Scanner scan =new Scanner(new File("kaydedilenler\\"+oyuncu.getIsim()+"_"+alan+".csv"));
            if(scan.hasNext()){
                oyuncu.setIsim(scan.nextLine());
            }
            if(scan.hasNext()){
                oyuncu.setPuan(Integer.parseInt(scan.nextLine()));
                puanLabel.setText(""+oyuncu.getPuan());
            }
            if(scan.hasNext()){
                sure =Integer.parseInt(scan.nextLine());
            }
            if(scan.hasNext()){
                int temp;
                temp =Integer.parseInt(scan.nextLine());
                if(temp==1)
                    joker1=true;
                else
                    joker1=false;
            }
            if(scan.hasNext()){
                int temp;
                temp =Integer.parseInt(scan.nextLine());
                if(temp==1)
                    joker2=true;
                else
                    joker2=false;
            }
            if(scan.hasNext()){
                sure =Integer.parseInt(scan.nextLine());
            }
            while(scan.hasNext()){
                String temp =scan.nextLine();
                oyuncu.getHamleHaritasi().add(temp);
                ilkHarf=temp.toUpperCase();
                listModel.addElement(temp);
                jList.setModel(listModel);
            }
            ilkHarf=""+ilkHarf.charAt(ilkHarf.length()-1);
            basHarfLabel.setText(ilkHarf);
            if(joker1)
                ekSureJokerButon.setEnabled(false);
            else
                ekSureJokerButon.setEnabled(true);
            if(joker2)
                yeniHarfJokerButon.setEnabled(false);
            else
                yeniHarfJokerButon.setEnabled(true);
            if(muzikCaliyorMu)
                Muzik.oynat("kelimatorMuzik.wav");
            timer.start();
        } catch (IOException e) {
            JFrame frame = new JFrame("HATA");
            JOptionPane.showMessageDialog(frame, "HATALI ŞİFRE");
        }

    }



    /**
     * Oyunun bitme durumudur Oyuncunun puanı ve oyundaki hamle haritası cağrılır.
     */
    public void oyunuBitir(){
        String dosyaAdi =alan+"_puanlari.csv";
        puanlariDosyadanAl(dosyaAdi,puanlar);
        puaniDegistir(oyuncu.getIsim(), oyuncu.getPuan(), puanlar);
        puaniKaydet(dosyaAdi,puanlar);
    }
}
