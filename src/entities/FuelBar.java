package entities;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class FuelBar extends StackPane {
    private ReadOnlyDoubleProperty workDone;
    private double totalWork;

    private ProgressBar bar;
    private Text text;
    private String labelFormatSpecifier;

    private static int DEFAULT_LABEL_PADDING = 5;

    public FuelBar(ReadOnlyDoubleProperty workDone, final double totalWork, final String labelFormatSpecifier) {
        this.setWorkDone(workDone);
        this.setTotalWork(totalWork);
        this.setLabelFormatSpecifier(labelFormatSpecifier);
        this.setText(new Text());
        this.setBar(new ProgressBar());

        syncProgress();
        workDone.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                syncProgress();
            }
        });

        //Allows the progress bar to expand to fill available horizontal space
        bar.setMaxWidth(Double.MAX_VALUE);
        getChildren().setAll(bar, text);
    }

    public ReadOnlyDoubleProperty getWorkDone() {
        return this.workDone;
    }

    private void setWorkDone(ReadOnlyDoubleProperty workDone) {
        this.workDone = workDone;
    }

    private double getTotalWork() {
        return this.totalWork;
    }

    private void setTotalWork(double totalWork) {
        this.totalWork = totalWork;
    }

    private Text getText() {
        return this.text;
    }

    private void setText(Text text) {
        this.text = text;
    }

    private ProgressBar getBar() {
        return this.bar;
    }

    private void setBar(ProgressBar bar) {
        this.bar = bar;
    }

    private String getLabelFormatSpecifier() {
        return this.labelFormatSpecifier;
    }

    private void setLabelFormatSpecifier(String labelFormatSpecifier) {
        this.labelFormatSpecifier = labelFormatSpecifier;
    }

    private void setProgress(double progress) {
        this.bar.setProgress(progress);
    }

    //Synchronizes the progress indicated with the work done
    private void syncProgress() {
        if (this.getWorkDone() == null || this.getTotalWork() == 0) {
            this.getText().setText("");
            this.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        } else {
            this.getText().setText(String.format(this.getLabelFormatSpecifier(), Math.ceil(this.getWorkDone().get())));
            this.setProgress(this.getWorkDone().get() / this.getTotalWork());
        }

        bar.setMinHeight(text.getBoundsInLocal().getHeight() + DEFAULT_LABEL_PADDING * 2);
        bar.setMinWidth(text.getBoundsInLocal().getWidth() + DEFAULT_LABEL_PADDING * 2);
    }
}