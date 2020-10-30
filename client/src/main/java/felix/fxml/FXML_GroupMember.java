package felix.fxml;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class FXML_GroupMember extends Pane
{
    private String groupParent;
    private String memberName;
    private Pane onlinePane;

    public FXML_GroupMember(boolean isOwner, boolean visible, String groupParent, String memberName, boolean online)
    {
        super.setVisible(visible);
        super.setManaged(visible);
        this.groupParent = groupParent;
        this.memberName = memberName;
        final int WIDTH = 170;
        super.setPrefWidth(WIDTH);
        super.setStyle("-fx-background-color: #909090; -fx-background-radius: 0 5 5 0; -fx-border-radius: 0 5 5 0;");
        GridPane gridPane = new GridPane();
        Label name = new Label(this.memberName);
        name.setFont(new Font(20));
        name.setPadding(new Insets(0, 0, 0, 10));
        onlinePane = new Pane();
        onlinePane.setStyle((online ? "-fx-background-color:green;" : "-fx-background-color:red;") + "-fx-background-radius: 2 0 0 2; -fx-border-radius: 2 0 0 2;");
        onlinePane.setPrefWidth(5);
        Pane nothing = new Pane();
        nothing.setPrefWidth(15);
        nothing.setStyle("-fx-background-color: #606060;");
        if (isOwner)
        {
            ImageView imageView = new ImageView(new Image(this.getClass().getResourceAsStream("/owner-crown.png")));
            imageView.setFitHeight(13);
            imageView.setFitWidth(13);
            nothing.getChildren().add(imageView);
        }
        ColumnConstraints marginColumn = new ColumnConstraints();
        marginColumn.setPrefWidth(15);
        ColumnConstraints onlineColumn = new ColumnConstraints();
        onlineColumn.setPrefWidth(5);
        ColumnConstraints nameColumn = new ColumnConstraints();
        nameColumn.setPrefWidth(150);
        gridPane.getColumnConstraints().addAll(marginColumn, onlineColumn, nameColumn);
        gridPane.addColumn(0, nothing);
        gridPane.addColumn(1, onlinePane);
        gridPane.addColumn(2, name);
        super.getChildren().addAll(gridPane);
    }

    public void setVisible()
    {
        super.setVisible(true);
        super.setManaged(true);
    }

    public String getMemberName()
    {
        return this.memberName;
    }

    public void setOnline(boolean online)
    {
        this.onlinePane.setStyle(online ? "-fx-background-color:green;" : "-fx-background-color:red;");
    }

    public String getGroupParent()
    {
        return groupParent;
    }
}