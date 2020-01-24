package Grup19Kelimator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/**
 *  Oyunun iki kisilik modda calısacak olan class yapısıdır.
 */
public class IkiKisilik extends Kelimator {

    protected Oyuncu oyuncu1;
    protected Oyuncu oyuncu2;
    protected String alan ;

    Timer timer; //p1 hamle suresi sayar
    Timer timer2;//p1 animasyon 2 saniye
    Timer timer3;//p2 hamle suresi sayar
    Timer timer4;//p2 animasyon 2 saniye
    Timer timer5;//kayan yazi 3 saniye tetikleyici
    Timer timer6;//kayan yazi surekli texti gunceller
    Timer timer7;//

    int p1sure = 99 ,p2sure=99, animasyonSure = 2 , kayanYaziTetiklemeSure = 3,kayanYaziSayac=0, kapatmaSuresi = 10;

    boolean muzikCaliyorMu = true,muzikIstek=true,clockCaliyorMu=false;
    boolean siraP1=true;
    boolean jokerKullaniliyor=false , kapat = false;
    boolean p1joker1=false , p1joker2=false;
    boolean p2joker1=false , p2joker2=false;

    String ilkHarf="A";
    String anlamiGosterilecekKelime;

    ArrayList<String> hamleler ;

    //player1 ve oyun motoru icin gerekli degiskenler
    JFrame frame;
    JPanel GUI;
    JLabel alanLabel;
    JLabel playerLabel;
    DefaultListModel<String> listModel;
    JList<String> jList;
    JButton ekSureJokerButon;
    JButton yeniHarfJokerButon;
    JButton muzikButton;
    JTextField kelimeGiris;
    JButton kelimeGirButton;
    JButton kapatButton;
    JLabel timeLabel;
    JLabel puanLabel;
    JLabel basHarfLabel;
    JLabel anlamGosterLabel;

    //player 2 gui degiskenleri
    JLabel playerLabel2;
    DefaultListModel<String> listModel2;
    JList<String> jList2;
    JButton ekSureJokerButon2;
    JButton yeniHarfJokerButon2;
    JTextField kelimeGiris2;
    JButton kelimeGirButton2;
    JLabel timeLabel2;
    JLabel puanLabel2;


    /**
     * No parameter constructor
     */
    IkiKisilik() {
        super();
        oyuncu1 = new Oyuncu();
        oyuncu2 = new Oyuncu();
    }

    IkiKisilik(String oyuncu1Isim , String oyuncu2Isim, String alan){
        super();
        oyuncu1 = new Oyuncu(oyuncu1Isim);
        oyuncu2 = new Oyuncu(oyuncu2Isim);
        this.alan = alan;
        hamleler = new ArrayList<>();
        guiOlustur();
        Muzik.oynat("KelimatorMuzik.wav");

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                --p1sure;
                timeLabel.setText(""+p1sure);
                if(p1sure<10 && !clockCaliyorMu){
                    if (muzikCaliyorMu){
                        Muzik.durdur();
                        muzikCaliyorMu=false;
                    }
                    Muzik.clockOynat();
                    clockCaliyorMu = true;
                }else if(p1sure>10 && clockCaliyorMu) {
                    if(clockCaliyorMu){
                        Muzik.clockDurdur();
                        clockCaliyorMu = false;
                    }
                    if(!muzikCaliyorMu && muzikIstek){
                        Muzik.oynat("KelimatorMuzik.wav");
                        muzikCaliyorMu=true;
                    }
                }
                if(p1sure==0){
                    if(clockCaliyorMu)
                        Muzik.clockDurdur();
                    timer.stop();
                    alanLabel.setText(oyuncu1.getIsim().toUpperCase()+" SURENIZ BITTI");
                    yeniHarfJokerButon.setEnabled(false);
                    ekSureJokerButon.setEnabled(false);
                    kelimeGiris.setEnabled(false);
                    kelimeGirButton.setEnabled(false);
                    if(p2sure>0)
                        siraDegis();
                    else{
                        alanLabel.setText("OYUN BITTI");
                        oyunuBitir();
                    }
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
                    puanLabel.setText(""+oyuncu1.getPuan());
                }
            }
        });


        timer3 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                --p2sure;
                timeLabel2.setText(""+p2sure);
                if(p2sure<10 && !clockCaliyorMu){
                    if (muzikCaliyorMu){
                        Muzik.durdur();
                        muzikCaliyorMu=false;
                    }
                    Muzik.clockOynat();
                    clockCaliyorMu = true;
                }else if(p2sure>10) {
                    if(clockCaliyorMu) {
                        Muzik.clockDurdur();
                        clockCaliyorMu = false;
                    }
                    if(!muzikCaliyorMu && muzikIstek){
                        Muzik.oynat("KelimatorMuzik.wav");
                        muzikCaliyorMu=true;
                    }
                }
                if(p2sure==0){
                    if(clockCaliyorMu)
                        Muzik.clockDurdur();
                    timer3.stop();
                    alanLabel.setText(oyuncu2.getIsim().toUpperCase()+" SURENIZ BITTI");
                    yeniHarfJokerButon2.setEnabled(false);
                    ekSureJokerButon2.setEnabled(false);
                    kelimeGiris2.setEnabled(false);
                    kelimeGirButton2.setEnabled(false);
                    if(p1sure>0)
                        siraDegis();
                    else{
                        alanLabel.setText("OYUN BITTI");
                        oyunuBitir();
                    }
                }
            }
        });

        timer4 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                --animasyonSure;
                if(animasyonSure ==0 ) {
                    timer4.stop();
                    puanLabel2.setText(""+oyuncu2.getPuan());
                }
            }
        });

        timer5 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                --kayanYaziTetiklemeSure;
                if(kayanYaziTetiklemeSure <=0 ){
                    timer5.stop();
                    kayanYaziSayac = 0;
                    timer6.start();
                }
            }
        });

        timer6 = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sub =anlamiGosterilecekKelime.substring(kayanYaziSayac,anlamiGosterilecekKelime.length()-1);
                if(sub.length()<1 || sub==null){
                    timer6.stop();
                    anlamGosterLabel.setVisible(false);
                    kayanYaziSayac = 0;
                }
                kayanYaziSayac++;
                anlamGosterLabel.setText(sub);
            }
        });

        timer7 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                --kapatmaSuresi;
                if(kapatmaSuresi==0){
                    timer7.stop();
                    Muzik.durdur();
                    if (clockCaliyorMu)
                        Muzik.clockDurdur();
                    frame.dispose();
                }
            }
        });

        kapatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oyunKurulu = false;
                if(kapat){
                    timer7.stop();
                    Muzik.durdur();
                    if (clockCaliyorMu)
                        Muzik.clockDurdur();
                    frame.dispose();
                }
                kapat=true;
                if(p1sure>0 || p2sure>0){
                    p1sure=0;
                    p2sure=0;
                    Muzik.durdur();
                    if (clockCaliyorMu)
                        Muzik.clockDurdur();
                    kelimeGirButton.setEnabled(false);
                    kelimeGiris.setEnabled(false);
                    ekSureJokerButon.setEnabled(false);
                    yeniHarfJokerButon.setEnabled(false);
                    kelimeGirButton2.setEnabled(false);
                    kelimeGiris2.setEnabled(false);
                    ekSureJokerButon2.setEnabled(false);
                    yeniHarfJokerButon2.setEnabled(false);
                    timer.stop();
                    timer3.stop();
                    String kaybeden ;
                    if (siraP1) {
                        oyuncu1.setPuan(-1);
                        puanLabel.setText("0");
                        kaybeden = oyuncu1.getIsim();
                    }
                    else {
                        oyuncu2.setPuan(-1);
                        puanLabel2.setText("0");
                        kaybeden = oyuncu2.getIsim();
                    }
                    oyunuBitir();
                    JFrame frame = new JFrame("YENİLGİ.");
                    JOptionPane.showMessageDialog(frame, kaybeden.toUpperCase()+" OYUNU TERKETTİNİZ. PUANINIZ: 0");
                    kapatmaSuresi=10;
                    timer7.start();
                }else {
                    Muzik.durdur();
                    if (clockCaliyorMu)
                        Muzik.clockDurdur();
                    frame.dispose();
                }
            }
        });

        ekSureJokerButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p1sure += 15;
                jokerKullaniliyor=true;
                p1joker1 =true;
                ekSureJokerButon.setEnabled(false);
            }
        });

        yeniHarfJokerButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                jokerKullaniliyor=true;
                p1joker2=true;
                yeniHarfJokerButon.setEnabled(false);
                char c = ilkHarf.charAt(0);
                while(c==ilkHarf.charAt(0) || c=='Q' || c=='X' || c=='W') {
                    Random rnd = new Random();
                    c = (char) (rnd.nextInt(26) + 'A');
                }
                ilkHarf = "" + c;
                basHarfLabel.setText("HARF : "+ilkHarf.toUpperCase());
            }
        });

        ekSureJokerButon2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p2sure += 15;
                jokerKullaniliyor=true;
                p2joker1 =true;
                ekSureJokerButon2.setEnabled(false);
            }
        });

        yeniHarfJokerButon2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                jokerKullaniliyor=true;
                p2joker2=true;
                yeniHarfJokerButon2.setEnabled(false);
                char c = ilkHarf.charAt(0);
                while(c==ilkHarf.charAt(0) || c=='Q' || c=='X' || c=='W') {
                    Random rnd = new Random();
                    c = (char) (rnd.nextInt(26) + 'A');
                }
                ilkHarf = "" + c;
                basHarfLabel.setText("HARF : "+ilkHarf.toUpperCase());
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

        jList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(jList.getSelectedIndex()!=-1) {
                    timer5.stop();
                    timer6.stop();
                    kayanYaziTetiklemeSure = 3;
                    String word = jList.getSelectedValue().toLowerCase();
                    anlamiGosterilecekKelime = sozluk.get(word).toUpperCase();
                    anlamGosterLabel.setText(word.toUpperCase() + " : " + anlamiGosterilecekKelime);
                    anlamGosterLabel.setVisible(true);
                    jList.clearSelection();
                    timer5.start();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        jList2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(jList2.getSelectedIndex()!=-1) {
                    timer5.stop();
                    timer6.stop();
                    kayanYaziTetiklemeSure = 3;
                    String word = jList2.getSelectedValue().toLowerCase();
                    anlamiGosterilecekKelime = sozluk.get(word).toUpperCase();
                    anlamGosterLabel.setText(word.toUpperCase() + " : " + anlamiGosterilecekKelime);
                    anlamGosterLabel.setVisible(true);
                    jList2.clearSelection();
                    timer5.start();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /**
     * Oyunu iki kisilik modda baslatacak olan override edilmis metodumuzdur.
     */
    public void oyunuBaslat(){
        System.out.println("Kelimator Iki kisilik : "+oyuncu1.getIsim()+" vs "+oyuncu2.getIsim()+ "-alan : "+alan);

        // Ilgili alanin terimlerini dosyadan oku ve veri yapilarini olustur.
        kelimeleriEsitle(alan);

        oyunuOynat();
    }

    private void siraDegis(){
        if(siraP1){
            if(p2sure>0) {
                //kendisine ait olanlari false, rakibininkileri true yapar
                kelimeGirButton.setEnabled(false);
                kelimeGiris.setEnabled(false);
                ekSureJokerButon.setEnabled(false);
                yeniHarfJokerButon.setEnabled(false);

                kelimeGirButton2.setEnabled(true);
                kelimeGiris2.setEnabled(true);
                if (!p2joker1)
                    ekSureJokerButon2.setEnabled(true);
                if (!p2joker2)
                    yeniHarfJokerButon2.setEnabled(true);
                timer3.start();
                siraP1 = !siraP1;
            }else if(p1sure>0)
                timer.start();
        }else {
            if(p1sure>0) {
                kelimeGirButton2.setEnabled(false);
                kelimeGiris2.setEnabled(false);
                ekSureJokerButon2.setEnabled(false);
                yeniHarfJokerButon2.setEnabled(false);

                kelimeGirButton.setEnabled(true);
                kelimeGiris.setEnabled(true);
                if (!p1joker1)
                    ekSureJokerButon.setEnabled(true);
                if (!p1joker2)
                    yeniHarfJokerButon.setEnabled(true);
                timer.start();
                siraP1 = !siraP1;
            }else if(p2sure>0)
                timer3.start();
        }
    }




    @Override
    public void oyunuOynat(){
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

        kelimeGiris2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    hamleYap();
                }
            }
        });

        kelimeGirButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hamleYap();
            }
        });

    }

    private void hamleYap(){
        boolean hamle=false;
        if(siraP1) {
            String kelime = kelimeGiris.getText();
            if (!kelime.equals("") && !hamleler.contains(kelime.toLowerCase()))
                hamle = kelimeDogrumu(kelime.toLowerCase(), ilkHarf.toLowerCase().charAt(0), alan);
            if (hamle) {
                if (clockCaliyorMu){
                    Muzik.clockDurdur();
                    clockCaliyorMu=false;
                }
                timer.stop();
                // Gecerli hamleyi kaydet.
                oyuncu1.kelimeKaydet(kelime);
                hamleler.add(kelime.toLowerCase());

                // Puani guncelle.
                oyuncu1.setPuan(oyuncu1.getPuan() + puaniHesapla(jokerKullaniliyor));
                if (jokerKullaniliyor) {
                    jokerKullaniliyor = false;
                    animasyon(5);
                } else
                    animasyon(10);

                listModel.addElement(kelime);
                jList.setModel(listModel);
                // Bir sonraki girilebilecek kelimenin ilk harfini belirler.
                ilkHarf = "" + kelime.charAt(kelime.length() - 1);
                basHarfLabel.setText("HARF : "+ilkHarf.toUpperCase());
                kelimeGiris.setText("");
                System.out.println(kelime + " : " + sozluk.get(kelime.toLowerCase()));
                siraDegis();
            }
            else{
                Muzik.yanlisCevapSound();
            }
        }else {
            String kelime = kelimeGiris2.getText();
            if (!kelime.equals("") && !hamleler.contains(kelime.toLowerCase()))
                hamle = kelimeDogrumu(kelime.toLowerCase(), ilkHarf.toLowerCase().charAt(0), alan);
            if (hamle) {
                if (clockCaliyorMu){
                    Muzik.clockDurdur();
                    clockCaliyorMu=false;
                }
                timer3.stop();
                // Gecerli hamleyi kaydet.
                oyuncu2.kelimeKaydet(kelime);
                hamleler.add(kelime.toLowerCase());

                // Puani guncelle.
                oyuncu2.setPuan(oyuncu2.getPuan() + puaniHesapla(jokerKullaniliyor));
                if (jokerKullaniliyor) {
                    jokerKullaniliyor = false;
                    animasyon(5);
                } else
                    animasyon(10);

                listModel2.addElement(kelime);
                jList2.setModel(listModel2);
                // Bir sonraki girilebilecek kelimenin ilk harfini belirler.
                ilkHarf = "" + kelime.charAt(kelime.length() - 1);
                basHarfLabel.setText("HARF : "+ilkHarf.toUpperCase());
                kelimeGiris2.setText("");
                System.out.println(kelime + " : " + sozluk.get(kelime.toLowerCase()));
                siraDegis();
            }else{
                Muzik.yanlisCevapSound();
            }
        }
    }

    private void animasyon(int eklenenPuan){

        animasyonSure = 2;
        if(siraP1) {
            puanLabel.setText("+" + eklenenPuan);
            Muzik.dogruCevapSound();
            timer2.start();
        }else{
            puanLabel2.setText("+" + eklenenPuan);
            Muzik.dogruCevapSound();
            timer4.start();
        }
    }


    /**
     * Oyunun bitme durumudur Bir oyuncu üzerinden oyuncuların puanı ve oyundaki hamle haritası bilgisi elde edilir.
     */
    public void oyunuBitir(){

        Font font = new Font("Courier", Font.BOLD,20);
        basHarfLabel.setFont(font);
        timeLabel.setFont(font);
        timeLabel.setLocation(450,250);

        basHarfLabel.setText("KAZANAN");
        if(oyuncu1.getPuan() > oyuncu2.getPuan())
            timeLabel.setText(oyuncu1.getIsim().toUpperCase());
        else if(oyuncu1.getPuan() < oyuncu2.getPuan())
            timeLabel.setText(oyuncu2.getIsim().toUpperCase());
        else {
            basHarfLabel.setText("OYUN SONUCU");
            timeLabel.setText("BERABERE");
            timeLabel.setSize(300,65);
        }
        timeLabel2.setVisible(false);


        String dosyaAdi =alan+"_puanlari.csv";
        puanlariDosyadanAl(dosyaAdi,puanlar);
        puaniDegistir(oyuncu1.getIsim(), oyuncu1.getPuan(), puanlar);
        puaniDegistir(oyuncu2.getIsim(), oyuncu2.getPuan(), puanlar);
        puaniKaydet(dosyaAdi,puanlar);
    }

    private void guiOlustur(){
        //textler icin font
        Font font = new Font("Courier", Font.BOLD,18);

        //frame
        frame = new JFrame("Iki Kisilik Kelimator");
        frame.setSize(1100,680);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //panel
        GUI = new JPanel();
        GUI.setSize(1250,680);
        GUI.setBackground(Color.pink);
        GUI.setLayout(null);

        //Player 1 oyun alani , ekranin sol yarisinin tasarimi
        //alan label
        alanLabel = new JLabel();
        alanLabel.setFont(font);
        alanLabel.setText("ALAN: "+alan.toUpperCase());
        alanLabel.setSize(330,18);
        alanLabel.setLocation(10,620);
        alanLabel.setVisible(true);
        GUI.add(alanLabel);

        //alan label
        anlamGosterLabel = new JLabel();
        anlamGosterLabel.setFont(font);
        anlamGosterLabel.setText("");
        anlamGosterLabel.setSize(300,18);
        anlamGosterLabel.setLocation(390,620);
        anlamGosterLabel.setVisible(false);
        GUI.add(anlamGosterLabel);

        //oyuncu adi label
        playerLabel = new JLabel();
        font = new Font("Courier", Font.BOLD,40);
        playerLabel.setFont(font);
        playerLabel.setText(oyuncu1.getIsim().toUpperCase());
        playerLabel.setSize(300,40);
        playerLabel.setLocation(10,10);
        playerLabel.setVisible(true);
        GUI.add(playerLabel);

        //Ilk Harf label
        basHarfLabel = new JLabel();
        font = new Font("Courier", Font.BOLD,36);
        basHarfLabel.setFont(font);
        basHarfLabel.setText("HARF : "+ilkHarf.toUpperCase());
        basHarfLabel.setSize(250,36);
        basHarfLabel.setLocation(450,150);
        basHarfLabel.setVisible(true);
        GUI.add(basHarfLabel);

        //puan label
        puanLabel = new JLabel();
        font = new Font("Courier", Font.BOLD,40);
        puanLabel.setFont(font);
        puanLabel.setText("0"/*""+oyuncu1.getPuan()+"-"*/);
        puanLabel.setSize(120,40);
        puanLabel.setLocation(330,10);
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
        ekSureJokerButon.setSize(120,30);
        ekSureJokerButon.setLocation(10,585);
        ekSureJokerButon.setVisible(true);
        GUI.add(ekSureJokerButon);

        //yeni Harf joker
        yeniHarfJokerButon = new JButton();
        yeniHarfJokerButon.setText("YENİ HARF");
        yeniHarfJokerButon.setSize(120,30);
        yeniHarfJokerButon.setLocation(140,585);
        yeniHarfJokerButon.setVisible(true);
        GUI.add(yeniHarfJokerButon);

        //muzik ac kapa butonu
        muzikButton = new JButton();
        muzikButton.setText("Müziği Kapat");
        muzikButton.setSize(120,30);
        muzikButton.setLocation(475,475);
        muzikButton.setVisible(true);
        GUI.add(muzikButton);

        //kelime input giris
        kelimeGiris = new JTextField();
        kelimeGiris.setVisible(true);
        kelimeGiris.setSize(250,30);
        kelimeGiris.setLocation(10,550);
        GUI.add(kelimeGiris);

        //kelime gir buton
        kelimeGirButton = new JButton();
        kelimeGirButton.setText("Kelime Gir");
        kelimeGirButton.setSize(120,30);
        kelimeGirButton.setLocation(270,550);
        kelimeGirButton.setVisible(true);
        GUI.add(kelimeGirButton);

        //kapat butonu
        kapatButton = new JButton();
        kapatButton.setText("Kapat");
        kapatButton.setSize(120,30);
        kapatButton.setLocation(475,515);
        kapatButton.setVisible(true);
        GUI.add(kapatButton);

        //sure label
        timeLabel = new JLabel();
        font = new Font("Courier", Font.BOLD,65);
        timeLabel.setFont(font);
        timeLabel.setText(""+p1sure);
        timeLabel.setSize(120,65);
        timeLabel.setLocation(400,250);
        timeLabel.setVisible(true);
        GUI.add(timeLabel);

        //--------------

        //Player 2 oyun alani , ekranin sag yarisinin tasarimi
        //oyuncu adi label
        playerLabel2 = new JLabel();
        font = new Font("Courier", Font.BOLD,40);
        playerLabel2.setFont(font);
        playerLabel2.setText(oyuncu2.getIsim().toUpperCase());
        playerLabel2.setSize(300,40);
        playerLabel2.setLocation(690,10);
        playerLabel2.setVisible(true);
        GUI.add(playerLabel2);


        //puan label
        puanLabel2 = new JLabel();
        font = new Font("Courier", Font.BOLD,40);
        puanLabel2.setFont(font);
        puanLabel2.setText("0"/*""+oyuncu1.getPuan()*/);
        puanLabel2.setSize(120,40);
        puanLabel2.setLocation(1020,10);
        puanLabel2.setVisible(true);
        GUI.add(puanLabel2);


        //hamle haritasi liste
        listModel2 = new DefaultListModel<>();
        jList2 = new JList<String>();
        title = BorderFactory.createTitledBorder("Hamle Haritası");
        title.setTitleJustification(TitledBorder.CENTER);
        jList2.setBorder(title);
        jList2.setSize(380,495);
        jList2.setLocation(690,50);
        jList2.setVisible(true);
        GUI.add(jList2);

        //ek sure joker
        ekSureJokerButon2 = new JButton();
        ekSureJokerButon2.setText("+15 SANİYE");
        ekSureJokerButon2.setSize(120,30);
        ekSureJokerButon2.setLocation(690,585);
        ekSureJokerButon2.setVisible(true);
        ekSureJokerButon2.setEnabled(false);
        GUI.add(ekSureJokerButon2);

        //yeni Harf joker
        yeniHarfJokerButon2 = new JButton();
        yeniHarfJokerButon2.setText("YENİ HARF");
        yeniHarfJokerButon2.setSize(120,30);
        yeniHarfJokerButon2.setLocation(820,585);
        yeniHarfJokerButon2.setVisible(true);
        yeniHarfJokerButon2.setEnabled(false);
        GUI.add(yeniHarfJokerButon2);

        //kelime input giris
        kelimeGiris2 = new JTextField();
        kelimeGiris2.setVisible(true);
        kelimeGiris2.setSize(250,30);
        kelimeGiris2.setLocation(690,550);
        kelimeGiris2.setEnabled(false);
        GUI.add(kelimeGiris2);

        //kelime gir buton
        kelimeGirButton2 = new JButton();
        kelimeGirButton2.setText("Kelime Gir");
        kelimeGirButton2.setSize(120,30);
        kelimeGirButton2.setLocation(950,550);
        kelimeGirButton2.setVisible(true);
        kelimeGirButton2.setEnabled(false);
        GUI.add(kelimeGirButton2);

        //sure label
        timeLabel2 = new JLabel();
        font = new Font("Courier", Font.BOLD,65);
        timeLabel2.setFont(font);
        timeLabel2.setText(""+p2sure);
        timeLabel2.setSize(120,65);
        timeLabel2.setLocation(610,250);
        timeLabel2.setVisible(true);
        GUI.add(timeLabel2);

        //--------------

        //adding gui panel to frame
        //frame set visible true
        frame.add(GUI);
        frame.setContentPane(GUI);
        frame.setVisible(true);
    }
}
