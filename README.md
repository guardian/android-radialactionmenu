RadialActionMenu
=======

![](website/static/anim.gif)

# What is this?
An implementation of a radial action menu. The implementation is designed specific for our use-case but should be fairly simple to customise.

# How to use this?
Define the layout in the root view of your Fragment/Activity and provide some attributes.
```
<com.theguardian.ui.RadialActionMenuView
    android:id="@+id/long_press_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:positioningRadius="100dp"
    app:actionButtonSelectedRadius="45dp"
    app:circleRadius="35dp"
    app:unselectedStrokeWidth="1dp"
    app:selectedActionButtonRadius="30dp"
    app:actionButtonRadius="25dp"
    app:iconTextSize="22dp"
    app:primaryColor="@color/action_bar_home_icon_default"
    app:secondaryColor="@color/white"
    app:circleBackgroundInactive="@color/card_fade"
    app:descriptionTextSize="@dimen/card_meta_text_size"
    app:circleColor="@color/long_press_circle_color"
    app:circleStrokeWidth="6dp"
    />
```

Then in your Fragment/Activity set a typeface you'd like to use for the description text.

```
radialActionMenu = (RadialActionMenuView) findViewById(R.id.radial_action_menu);
radialActionMenu.setDescriptionTypeface(TypefaceHelper.getAgateRegular());
```

And when a user long-presses on a view, activate the long press radial action menu, passing your long press action with some description text, an ID, and a callback.

```
List<RadialActionMenuAction> actions = new ArrayList<>();
actions.add(new IconFontRadialActionMenuAction(new RadialActionMenuIcon(R.integer.save_page_icon, tf), getString(R.string.long_press_save), 0));
actions.add(new IconFontRadialActionMenuAction(new RadialActionMenuIcon(R.integer.share_icon, tf), getString(R.string.long_press_share), 1));
radialActionMenu.longClicked(selected -> {
    switch (selected.id) {
        case 0:
            handleSave();
            break;
        case 1:
            handleShare();
            break;
    }
}, actions);
```

# License
RadialActionMenu is released under the Apache 2.0 license.

This implementation was partially inspired by [CircularFloatingActionMenu](https://github.com/oguzbilgener/CircularFloatingActionMenu)