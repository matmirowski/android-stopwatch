package pl.mateusz.stopwatch;

import android.content.Context;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

public class LapTextView extends androidx.appcompat.widget.AppCompatTextView {
    public LapTextView(Context context, String htmlText) {
        super(context);
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(10,10,10,10);
        this.setLayoutParams(params);
        this.setText(Html.fromHtml(htmlText));
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
    }
}
