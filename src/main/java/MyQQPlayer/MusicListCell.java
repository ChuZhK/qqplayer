package MyQQPlayer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

import java.io.File;
import java.sql.SQLException;

public class MusicListCell extends ListCell<File> {

    private final Label label;
    private final BorderPane pane;

    private File item;
    public MusicListCell(){

        pane =new BorderPane();

        label =new Label();
        BorderPane.setAlignment(label, Pos.CENTER_LEFT); //左对齐
        Button button= new Button();   //删除按钮
        button.getStyleClass().add("remove-btn");
        button.setGraphic(new Region());
        button.setOnAction(event->{
            Mysql mysql=new Mysql();
            String name;

            if (getItem() == getListView().getSelectionModel().getSelectedItem()) {
                name=PlayerController.unicodeEncode(getListView().getSelectionModel().getSelectedItem().getAbsoluteFile().toString());

                getListView().getSelectionModel().clearSelection();
            }else{
                name=PlayerController.unicodeEncode(getItem().toString());
            }
            getListView().getItems().remove(getItem()); //清除选中的这一行

            try {
                //System.out.println(name);
                mysql.pstmt=mysql.conn.prepareStatement("delete from music3 where music=?");
                mysql.pstmt.setString(1,name);
                mysql.pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        });
        pane.setCenter(label);    //将表示音乐文件的字符串标签设置在中间
        pane.setRight(button);    //将按删除钮设置在右边

    }

    protected void updateItem(File item,boolean empty){  //对每一个子结点进行遍历
        super.updateItem(item,empty);

        //对音乐文件的名字进行字符串处理
        if (item == null || empty){  //检索不到文件的时候不加处理
            setGraphic(null);
            setText("");
        }else{
            String name=item.getName();
            label.setText(name.substring(0,name.length()-4));
            setGraphic(pane);
        }
    }


}
