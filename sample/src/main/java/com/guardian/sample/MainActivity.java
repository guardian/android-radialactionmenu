package com.guardian.sample;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.theguardian.ui.IconFontRadialActionMenuAction;
import com.theguardian.ui.RadialActionMenuAction;
import com.theguardian.ui.RadialActionMenuIcon;
import com.theguardian.ui.RadialActionMenuView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    RadialActionMenuView radialActionMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radialActionMenuView = (RadialActionMenuView) findViewById(R.id.radial_action_menu_view);

        findViewById(R.id.button).setOnLongClickListener(listener);
        findViewById(R.id.button1).setOnLongClickListener(listener);
        findViewById(R.id.button2).setOnLongClickListener(listener);
        findViewById(R.id.button3).setOnLongClickListener(listener);
        findViewById(R.id.button4).setOnLongClickListener(listener);
    }

    private View.OnLongClickListener listener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            List<RadialActionMenuAction> actions = new ArrayList<>();
            actions.add(new IconFontRadialActionMenuAction(new RadialActionMenuIcon(R.integer.icon_thumbs_up, Typeface.create("sans-serif", Typeface.NORMAL)), "Yay", 0));
            actions.add(new IconFontRadialActionMenuAction(new RadialActionMenuIcon(R.integer.icon_thumbs_down, Typeface.create("sans-serif", Typeface.NORMAL)), "Boo", 1));
            actions.add(new IconFontRadialActionMenuAction(new RadialActionMenuIcon(R.integer.icon_cat, Typeface.create("sans-serif", Typeface.NORMAL)), "Meow", 2));
            actions.add(new IconFontRadialActionMenuAction(new RadialActionMenuIcon(R.integer.icon_dog, Typeface.create("sans-serif", Typeface.NORMAL)), "Woof", 3));

            radialActionMenuView.showMenu(new RadialActionMenuView.OnActionSelectedListener() {
                @Override
                public void onActionSelected(RadialActionMenuAction actionSelected) {
                    if(actionSelected != null) {
                        Toast.makeText(MainActivity.this, "You selected item '" + actionSelected.description + "'", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }, actions);
            return true;
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        radialActionMenuView.snoopOnTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}
