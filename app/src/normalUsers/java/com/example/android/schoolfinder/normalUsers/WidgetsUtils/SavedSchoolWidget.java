package com.example.android.schoolfinder.normalUsers.WidgetsUtils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.normalUsers.Activities.SchoolDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class SavedSchoolWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.school_widget_provider);
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);

        Intent intent = new Intent(context, ListViewWidgetService.class);
        views.setRemoteAdapter(R.id.appwidget_school_list, intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void launchDetailActivity(Context context, RemoteViews views) {
        // template to handle the click listener for each item
        Intent clickIntentTemplate = new Intent(context, SchoolDetailActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        clickPendingIntentTemplate
        views.setPendingIntentTemplate(R.id.appwidget_school_list, clickPendingIntentTemplate);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action == null) return;
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, SavedSchoolWidget.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.appwidget_school_list);
        }
        super.onReceive(context, intent);
    }

}

