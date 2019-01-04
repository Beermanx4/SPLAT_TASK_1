package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;


public class TabController {
    @FXML
    private ListView<TextFlow> text_data = new ListView<>();
    private int focus = 0;
    private int count = 0;
    private int index_count = 0;
    private String text_to_color;
    private ArrayList<Integer> indexes = new ArrayList<>();

    void addStringToTab(String line, Boolean text_to_color_presents) {

        if (text_to_color_presents) {
            indexes.add(count);
            int i = line.indexOf(text_to_color);
            TextFlow text_flow = new TextFlow();
            int j = 0;
            int len = text_to_color.length();
            while (i != -1) {
                Text before = new Text(line.substring(j, i));
                Text text = new Text(text_to_color);
                text.setFill(Color.RED);
                text_flow.getChildren().addAll(before, text);
                j = i + len;
                i = line.indexOf(text_to_color, j);
            }
            Text after = new Text(line.substring(j));
            text_flow.getChildren().add(after);
            text_data.getItems().add(text_flow);
        } else {
            Text text = new Text(line);
            TextFlow textFlow = new TextFlow(text);
            text_data.getItems().add(textFlow);
        }

        count++;
    }

    void setTextToColor(String text) {
        text_to_color = text;
    }

    @FXML
    private void goToNextPosition() {
        if (index_count < indexes.size() - 1)
            index_count++;
        text_data.getSelectionModel().select(indexes.get(index_count));
        focus = text_data.getSelectionModel().getSelectedIndex() - 5;
        text_data.scrollTo(focus);
    }

    @FXML
    void setStartPosition() {
        text_data.getSelectionModel().select(indexes.get(index_count));
        focus = text_data.getSelectionModel().getSelectedIndex() + 5;
        text_data.scrollTo(focus);
    }

    @FXML
    private void goToPrevPosition() {
        if (index_count > 0)
            index_count--;
        text_data.getSelectionModel().select(indexes.get(index_count));
        focus = text_data.getSelectionModel().getSelectedIndex() - 5;
        text_data.scrollTo(focus);
    }
}
