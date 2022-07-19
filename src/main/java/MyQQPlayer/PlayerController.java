package MyQQPlayer;


import com.leewyatt.rxcontrols.controls.*;
import com.leewyatt.rxcontrols.pojo.LrcDoc;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerController {


    public  static String unicodeEncode(String string) {
        char[] utfBytes = string.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    public static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }

    public String myAdd(String str){
            char[] c=str.toCharArray();
            String new_str="";
            for(int i=0;i<str.length();i++){
                if(c[i] != '\\' ){
                    new_str+=c[i];
                }else{
                    new_str=new_str+'\\'+c[i];
                }
            }
            return new_str;
    }



    //全局范围的音乐播放器对象
    private MediaPlayer player;  //避免音乐播放的时候被打断，这里将player设为全局变量是必要的

    private Slider soundSlider; //由于需要将音量滑块和音量大小进行绑定，这里就把音量滑块设置为全局变量

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane window;




    @FXML
    private RXAudioSpectrum audioSpectrum;

    @FXML
    private AnchorPane drawerPane;



    @FXML
    private StackPane loaderBtn; //登录按钮

    @FXML
    private RXTextField account; //账号文本

    @FXML
    private RXPasswordField password; //密码

    private String ac;  //账号
    private String pw;  //密码


    private ContextMenu winPopup;

    private String win_tishi;

    private Label winLabel;

    @FXML
    private Pane out;

    @FXML
    private AnchorPane log;
    @FXML
    private Button logIn;  //登录按钮

    @FXML
    private Button logUp;  //注册按钮


    @FXML
    private ContextMenu loaderPopup;
    @FXML
    private RXLrcView lrcView;

    @FXML
    private ToggleButton playBtn;

    @FXML
    private RXMediaProgressBar progressBar;



    @FXML
    private BorderPane sliderPane;



    @FXML
    private Label timeLabel;


    private Timeline showAnim;
    private Timeline hideAnim;

    private ContextMenu soundPopup;   //音量的弹窗

    private ContextMenu skinPopup;  //换肤按钮弹窗
    @FXML
    private StackPane skinBtn;   //换肤按钮


    @FXML
    private ListView<File> listView; //歌曲列表

    private SimpleDateFormat sdf=new SimpleDateFormat("mm:ss"); //播放时间的相关设置

    @FXML
    private StackPane soundBtn;

    @FXML
    void onHideSliderPaneAction(MouseEvent event) {
        showAnim.stop();
        hideAnim.play();
    }

    @FXML
    void onShowSliderPaneAction(MouseEvent event) {
        drawerPane.setVisible(true);
        hideAnim.stop();
        showAnim.play();
    }



    @FXML
    void onFullAction(MouseEvent event) {
        Stage stage = (Stage)drawerPane.getScene().getWindow();
        //全屏按钮只是两个状态来回变化，直接将当前全屏状态取反即可
        stage.setFullScreen(!stage.isFullScreen());
    }


    @FXML
    void onLoaderAction(MouseEvent event) {
        Stage stage=(Stage) lrcView.getScene().getWindow();
        double height=Screen.getPrimary().getBounds().getHeight();
        double width=Screen.getPrimary().getBounds().getWidth();
        //获取soundBtn面板在屏幕中的位置


        
        Bounds bounds=lrcView.localToScreen(lrcView.getBoundsInLocal());
        //System.out.print(bounds.getMaxY());
        if(null !=loaderPopup) {
            loaderPopup.show(stage,(bounds.getMinX()+bounds.getMaxX())/2-158, (bounds.getMinY()+bounds.getMaxY())/2-250);
        }else{
            System.out.println("loaderView is null");
        }
    }





    @FXML
    void onMiniAction(MouseEvent event) {
        //获取舞台
        Stage stage = (Stage)drawerPane.getScene().getWindow();
        //最小化
        stage.setIconified(true);

    }



    @FXML
    void oncloseAction(MouseEvent event) {
        Platform.exit();
        //System.exit(0);
    }

    @FXML
    void onSoundPopupAction(MouseEvent event) {
        Bounds bounds = soundBtn.localToScreen(soundBtn.getBoundsInLocal());
        soundPopup.show((Stage) drawerPane.getScene().getWindow(), bounds.getMinX() - 20, bounds.getMinY() - 165);
    }


    @FXML
    void onSkinPoppupAction(MouseEvent event) {
        Stage stage=(Stage) drawerPane.getScene().getWindow();
        //获取skinBtn面板在屏幕中的位置
        Bounds bounds = skinBtn.localToScreen(skinBtn.getBoundsInLocal());
        skinPopup.show(stage, bounds.getMaxX() - 170, bounds.getMaxY() + 10);
    }


//    @FXML
//    void onSoundPopupAction(MouseEvent event) {
//        Bounds bounds = soundBtn.localToScreen(soundBtn.getBoundsInLocal());
//        soundPopup.show(findStage(), bounds.getMinX() - 20, bounds.getMinY() - 165);
//    }
//
//    private Stage findStage() {
//        return (Stage) drawerPane.getScene().getWindow();
//    }

    //添加歌曲列表
    @FXML
    void onAddMusicAction(MouseEvent event) {
        Stage stage=(Stage) drawerPane.getScene().getWindow();
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("mp3","*.mp3"));
        List<File> fileList = fileChooser.showOpenMultipleDialog(stage);
        ObservableList<File> items= listView.getItems();
        if(ac!=null){
            Mysql mysql = new Mysql();
            try {
                mysql.pstmt = mysql.conn.prepareStatement("insert into music3(num,music) values(?,?)");
                if (fileList != null) {
                    fileList.forEach(file -> {

                                if (!items.contains(file)) {
                                    items.add(file);
                                }
                                try {
                                    mysql.pstmt.setString(1,ac);
                                   // System.out.println(unicodeEncode(file.toString()));
                                    mysql.pstmt.setString(2,unicodeEncode(file.toString()));

                                    mysql.pstmt.executeUpdate();


                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }


                            }

                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if (fileList != null) {

                fileList.forEach(file -> {
                            if (!items.contains(file)) {
                               // System.out.print(file.toString());
                                items.add(file);
                            }
                        }

                );
            }
        }



    }


    //播放按钮
    @FXML
    void onPlayAction(ActionEvent event) {
        if(player != null){
            if(playBtn.isSelected()){
                //初始状态播放键没有被选择，直接点击歌曲可以播放，因此正在播放时第一次点击播放不会打断
                player.play();
            }else{
                player.pause();
            }
        }else {
            if (listView.getItems().size() != 0) {
                listView.getSelectionModel().select(0);
            }
        }
    }
    //下一曲按钮
    @FXML
    void onPlayNextAction(MouseEvent event) {
        int size=listView.getItems().size();
        if(size<2){
            return;
        }
        int index=listView.getSelectionModel().getSelectedIndex();
        //当前歌曲是最后一首时，跳转到第一首
        index=(index == size-1)? 0:index+1;
        listView.getSelectionModel().select(index);
    }

    //上一曲按钮
    @FXML
    void onPlayPrevAction(MouseEvent event) {
        int size=listView.getItems().size();
        if(size<2){
            return;
        }
        int index=listView.getSelectionModel().getSelectedIndex();
        //当前歌曲是第一首时，跳转到最后一首
        index=(index == 0)? size-1:index-1;
        listView.getSelectionModel().select(index);
    }





    @FXML
    void initialize() {   //整个界面初始化
        initAnim();  //初始化场景
        initSoundPoppup(); //初始化音量条
        initSkinPoppup(); //初始化换肤弹窗
        initListView();  //歌曲列表初始化
        initProgressBar();//进度条初始化
        initLoaderView();
        initWin();
    }

    private void initLoaderView() {   //初始化登录界面
        loaderPopup=new ContextMenu(new SeparatorMenuItem());
        Parent loaderRoot=null;

        try {

//            winLabel.textProperty().addListener(e->{
//                winLabel.setText(win_tishi);
//            });
            FXMLLoader fxmlloader=new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/loader.fxml")));
//
            loaderRoot = fxmlloader.load();
//
            //使用命名空间来寻找音量滑块和下面的数字标签
            ObservableMap<String,Object> namespace= fxmlloader.getNamespace();
//
            account=(RXTextField) namespace.get("account");
            password=(RXPasswordField) namespace.get("password");
            logIn=(Button) namespace.get("logIn");
            logUp=(Button) namespace.get("signUp");

            logIn.setOnMouseClicked(e->{
                Bounds bounds1=logIn.localToScreen(logIn.getBoundsInLocal());
                double x,y;
                x=e.getScreenX();
                y=e.getScreenY();

                if(bounds1.contains(x,y)){
                    LogIn();
                }
            });
            logUp.setOnMouseClicked(e->{
                Bounds bounds2=logUp.localToScreen(logUp.getBoundsInLocal());
                double x,y;
                x=e.getScreenX();
                y=e.getScreenY();
                if (bounds2.contains(x,y)) {
                    LogUp();
                }
            });

        } catch (IOException e) {

            e.printStackTrace();
        }

        loaderPopup.getScene().setRoot(loaderRoot);
    }


    private void LogIn() {
//        System.out.println(account.getText());
//        System.out.println(password.getText());

        String ac=account.getText();
        String pw=password.getText();
        Mysql mysql=new Mysql();
        try {
            mysql.pstmt=mysql.conn.prepareStatement("select * from account where num='"+ac+"' ");
            mysql.rst=mysql.pstmt.executeQuery();

            while(mysql.rst.next()){
                if(pw.equals(mysql.rst.getString("password"))){
                    winLabel.setText("登陆成功！");
                    this.pw=pw;
                    this.ac=ac;


                }
                else{
                    winLabel.setText("登陆失败请重新登录！");
                }
                Stage stage=(Stage) drawerPane.getScene().getWindow();
                //获取soundBtn面板在屏幕中的位置
                Bounds bounds=lrcView.localToScreen(lrcView.getBoundsInLocal());
                loaderPopup.hide();


                winPopup.show(stage,(bounds.getMinX()+bounds.getMaxX())/2-300,(bounds.getMinY()+bounds.getMaxY())/2-200);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        //用于添加歌曲的测试
        try{
            mysql.pstmt = mysql.conn.prepareStatement("select * from music3 where num='" + ac + "' ");
            mysql.rst = mysql.pstmt.executeQuery();
            ObservableList<File> items= listView.getItems();
            while (mysql.rst.next()) {
                //System.out.println(myAdd(unicodeDecode(mysql.rst.getString("music"))));
                File file=new File(myAdd(unicodeDecode(mysql.rst.getString("music"))));
                if(!items.contains(file)){
                    items.add(file);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }


    private void LogUp() {
        String ac=account.getText();
        String pw=password.getText();
        Mysql mysql=new Mysql();
        boolean acc=false;
        boolean used=false;
        try {
            mysql.pstmt=mysql.conn.prepareStatement("select * from account where num=?");
            mysql.pstmt=mysql.conn.prepareStatement("select * from account where num='"+ac+"' ");
            mysql.rst=mysql.pstmt.executeQuery();
            while(mysql.rst.next()) {
                if (pw.equals(mysql.rst.getString("password"))) {
                    used = true;
                }
            }
            mysql.pstmt=mysql.conn.prepareStatement("insert into account(num,password) values(?,?)");
            mysql.pstmt.setString(1,ac);
            mysql.pstmt.setString(2,pw);
            mysql.pstmt.executeUpdate();
            mysql.pstmt=mysql.conn.prepareStatement("select * from account where num=?");
            mysql.pstmt=mysql.conn.prepareStatement("select * from account where num='"+ac+"' ");
            mysql.rst=mysql.pstmt.executeQuery();
            while(mysql.rst.next()) {
                if (pw.equals(mysql.rst.getString("password"))) {
                    acc = true;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        if(acc){
            winLabel.setText("注册成功，请返回登录！");
        }
        else{
            winLabel.setText("注册失败，请重新注册！");
        }
        if(used){
            winLabel.setText("该账号已被注册！");
        }
        Stage stage=(Stage) drawerPane.getScene().getWindow();
        //获取soundBtn面板在屏幕中的位置
        Bounds bounds=lrcView.localToScreen(lrcView.getBoundsInLocal());
        loaderPopup.hide();


        winPopup.show(stage,(bounds.getMinX()+bounds.getMaxX())/2-300,(bounds.getMinY()+bounds.getMaxY())/2-200);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }



    //进度条初始化
    private void initProgressBar() {
        //进度条的调整
//        progressBar.setOnMouseClicked(event -> {
//            if (player != null) {
//                player.seek(progressBar.getCurrentTime());
//                String currentTime = sdf.format(progressBar.getCurrentTime().toMillis());
//                String bufferedTimer = sdf.format(player.getBufferProgressTime().toMillis());
//                timeLabel.setText(currentTime+ " / "+bufferedTimer);
//            }
//        });

        EventHandler<MouseEvent> progressBarHandler = event -> {
            if (player != null) {
                player.seek(progressBar.getCurrentTime());
                String currentTime = sdf.format(progressBar.getCurrentTime().toMillis());
                String bufferedTimer = sdf.format(player.getBufferProgressTime().toMillis());
                timeLabel.setText(currentTime+ " / "+bufferedTimer);
            }
        };
        progressBar.setOnMouseClicked(progressBarHandler);
        progressBar.setOnMouseDragged(progressBarHandler);
    }



    //歌曲列表的初始化
    private void initListView() {      //添加歌曲触发事件

        listView.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
            @Override
            public ListCell<File> call(ListView<File> fileListView) {
                return new MusicListCell();
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener((object,oldFile,newFile) ->{
            //选择歌曲播放的时候如果有歌曲正在播放那就直接将之前的对象销毁重新创建！！！
            if (player != null) {
                player.stop();
                lrcView.setLrcDoc(null); //将歌词组件内容进行清空
                lrcView.currentTimeProperty().unbind();  //解除绑定
                lrcView.setCurrentTime(Duration.ZERO);
                player.setAudioSpectrumListener(null);
                progressBar.durationProperty().unbind();
                progressBar.setCurrentTime(Duration.ZERO);
                player.currentTimeProperty().removeListener((ob1, ov1, nv1) -> {
                    progressBar.setCurrentTime(nv1);
                });
                //关闭时直接给频谱组件传入一个数组即可（频谱的高度由数字决定）
                float[] empty=new float[128];
                Arrays.fill(empty,-60.0f);
                audioSpectrum.setMagnitudes(empty);
                timeLabel.setText("00:00 / 00:00");
                playBtn.setSelected(false);
                player.setOnEndOfMedia(null);
                player.dispose();
                player = null;
            }
            if(newFile !=null) {  //当选中的文件不为空时，创建MediaPlayer对象进行播放



                player = new MediaPlayer(new Media(newFile.toURI().toString()));
                player.setVolume(soundSlider.getValue() / 100); //通过音量滑块设置音量的大小

                //设置歌词
                //通过歌名提取出歌词文件的名称
                String lrcPath = newFile.getAbsolutePath().replaceAll("mp3$", "lrc");
                File lrcFile = new File(lrcPath);
                //歌词文件存在时才能进行操作
                if (lrcFile.exists()) {
                    byte[] bytes = new byte[0];
                    try {
                        bytes = Files.readAllBytes(lrcFile.toPath());
                        //使用网上的编码方式对歌词进行解析
                        lrcView.setLrcDoc(LrcDoc.parseLrcDoc(new String(bytes, EncodingDetect.detect(bytes))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //设置歌词进度
                lrcView.currentTimeProperty().bind(player.currentTimeProperty());

                //设置频谱可视化
                player.setAudioSpectrumListener(new AudioSpectrumListener() {
                    @Override
                    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                        audioSpectrum.setMagnitudes(magnitudes);  //频谱数据传递给频谱可视化组件
                    }
                });


                //设置进度条的总时长
                progressBar.durationProperty().bind(player.getMedia().durationProperty());
                //播放器的进度修改监听器
                player.currentTimeProperty().addListener((ob1, ov1, nv1) -> {
                    progressBar.setCurrentTime(nv1);
                    //随着播放时间的改变修改时间的显示
                    String currentTime = sdf.format(nv1.toMillis());
                    String bufferTime=null;
                    if(player.getBufferProgressTime() != null){
                        bufferTime = sdf.format(player.getBufferProgressTime().toMillis());
                    }

                    if(bufferTime==null){
                        bufferTime="00 : 00";
                    }
                    timeLabel.setText(currentTime+"/"+bufferTime);
                });

                playBtn.setSelected(true); //切换歌曲时该变播放键选中状态
                //如果播放完当前歌曲, 自动播放下一首
                player.setOnEndOfMedia(()->{
                    int size=listView.getItems().size();
                    if(size<2){
                        return;
                    }
                    int index=listView.getSelectionModel().getSelectedIndex();
                    //当前歌曲是最后一首时，跳转到第一首
                    index=(index == size-1)? 0:index+1;
                    listView.getSelectionModel().select(index);
                });
                player.play();

            }
            else{
                if (player != null) {
                    player.stop();
                    lrcView.setLrcDoc(null); //将歌词组件内容进行清空
                    lrcView.currentTimeProperty().unbind();  //解除绑定
                    lrcView.setCurrentTime(Duration.ZERO);
                    player.setAudioSpectrumListener(null);
                    progressBar.durationProperty().unbind();
                    progressBar.setCurrentTime(Duration.ZERO);
                    player.currentTimeProperty().removeListener((ob1, ov1, nv1) -> {
                        progressBar.setCurrentTime(nv1);
                    });
                    //关闭时直接给频谱组件传入一个数组即可（频谱的高度由数字决定）
                    float[] empty=new float[128];
                    Arrays.fill(empty,-60.0f);
                    audioSpectrum.setMagnitudes(empty);
                    timeLabel.setText("00:00 / 00:00");
                    playBtn.setSelected(false);    //还原播放键状态
                    player.setOnEndOfMedia(null);
                    player.dispose();
                    player = null;
                }
            }
        });
    }


    //换肤的初始化
    private void initSkinPoppup() {
        //使用上下文菜单，实现在点击别的区域自动消失
        //使用空白符，避免在传入的值为0的时候，直接不显示菜单
        skinPopup=new ContextMenu(new SeparatorMenuItem());
        Parent skinRoot= null;
        try {
            FXMLLoader fxmlloader=new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/skin2.fxml")));
            skinRoot=fxmlloader.load();
            ObservableMap<String,Object> namespace= fxmlloader.getNamespace();
            ToggleGroup skinGroup=(ToggleGroup) namespace.get("skinGroup");
            skinGroup.selectedToggleProperty().addListener((ob, ov, nv) ->{
                RXToggleButton btn = (RXToggleButton) nv;
                String skinName = btn.getText(); //获取按钮上的文本信息
                //将获取到的文本信息转化为 css 文件的名称字符串
                String skinUrl = Objects.requireNonNull(getClass().getResource("/css/" + skinName + ".css")).toExternalForm();
               // System.out.println(skinUrl);
                drawerPane.getScene().getRoot().getStylesheets().setAll(skinUrl);
                skinPopup.getScene().getRoot().getStylesheets().setAll(skinUrl);
                soundPopup.getScene().getRoot().getStylesheets().setAll(skinUrl);
            }) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
        skinPopup.getScene().setRoot(skinRoot);
    }

    //音量条的初始化
    private void initSoundPoppup(){
        //使用上下文菜单，实现在点击别的区域自动消失
        //使用空白符，避免在传入的值为0的时候，直接不显示菜单
        soundPopup=new ContextMenu(new SeparatorMenuItem());
        Parent soundRoot= null;
        try {
            FXMLLoader fxmlloader=new FXMLLoader(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/fxml/sound.fxml"))));
            soundRoot=fxmlloader.load();
            //使用命名空间来寻找音量滑块和下面的数字标签
            ObservableMap<String,Object> namespace= fxmlloader.getNamespace();
            soundSlider=(Slider) namespace.get("soundSlider");
            Label soundNumLabel=(Label) namespace.get("soundNum");

            //将滑块的百分比大小值绑定到数字标签上
            soundNumLabel.textProperty().bind(soundSlider.valueProperty().asString("%.0f%%"));
            //soundRoot = FXMLLoader.load(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/fxml/sound.fxml"))));

            //将音量滑块的数值和播放器音量进行绑定，同时改变
            soundSlider.valueProperty().addListener((object,oldValue,newValue)->{
                if(player != null){
                    player.setVolume(newValue.doubleValue()/100);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
        soundPopup.getScene().setRoot(soundRoot);
    }

    //

    private void initWin(){
        winPopup=new ContextMenu(new SeparatorMenuItem());
        Parent winRoot= null;
        try {
            FXMLLoader fxmlloader=new FXMLLoader(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/fxml/winlabel.fxml"))));
            winRoot=fxmlloader.load();
            //使用命名空间来寻找音量滑块和下面的数字标签
            ObservableMap<String,Object> namespace= fxmlloader.getNamespace();
            winLabel=(Label) namespace.get("winLabel");

        } catch (IOException e) {
            e.printStackTrace();
        }
        winPopup.getScene().setRoot(winRoot);
    }


    //场景的初始化
    private void initAnim(){
        showAnim=new Timeline(new KeyFrame(Duration.millis(300),new KeyValue(sliderPane.translateXProperty(), 0)));
        hideAnim=new Timeline(new KeyFrame(Duration.millis(300),new KeyValue(sliderPane.translateXProperty(), 300)));
        hideAnim.setOnFinished(event-> drawerPane.setVisible(false));
    }

}
