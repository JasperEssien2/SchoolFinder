package com.example.android.schoolfinder.normalUsers.WidgetsUtils;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.normalUsers.Interfaces.SearchSchoolOfflineDatabaseCallback;
import com.example.android.schoolfinder.normalUsers.SearchSchoolViewModels;
import com.example.android.schoolfinder.normalUsers.Utility.AppDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, SearchSchoolOfflineDatabaseCallback {

    private static final String TAG = WidgetRemoteViewsFactory.class.getSimpleName();
    private final Application application;
    private final Intent intent;
    private int appWidgetId;
    SearchSchoolViewModels viewModels;
    private List<School> schools;

    public WidgetRemoteViewsFactory(Application application, Intent intent) {
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        this.application = application;
        this.intent = intent;
        new SearchSchoolViewModels.GetAllSchoolAsyncTask(AppDatabase.getDatabase(application.getBaseContext()), this)
                .execute();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return schools.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews rv = new RemoteViews(application.getBaseContext().getPackageName(), R.layout.item_school_widget);
        if (schools != null) {
            String name = schools.get(i).getSchoolName();
            String address = schools.get(i).getSchoolLocation();
            String motto = schools.get(i).getSchoolMotto();

            if (schools.get(0).getSchoolLogoImageUrl() != null && !schools.get(0).getSchoolLogoImageUrl().isEmpty()) {
                Picasso
                        .get()
                        .load(schools.get(0).getSchoolLogoImageUrl())
                        .placeholder(R.color.colorLightGrey)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                rv.setImageViewBitmap(R.id.item_widget_image, bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }
            rv.setTextViewText(R.id.appwidget_list_item_school_name, name);
            rv.setTextViewText(R.id.appwidget_list_item_school_address, address);
            rv.setTextViewText(R.id.appwidget_list_item_school_motto, motto);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void schoolAdded() {

    }

    @Override
    public void schoolDeleted() {

    }

    @Override
    public void schoolGotten(School school) {

    }

    @Override
    public void schoolsGotten(LiveData<List<School>> listLiveData) {

    }

    @Override
    public void schoolsGotten(List<School> schools) {
        Log.e(TAG, "schoolsGotten() -- widget -- " + schools.toString());
        this.schools = schools;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(application.getBaseContext());
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(application.getBaseContext(), SavedSchoolWidget.class));
        if (schools.size() > 0) {

        } else {

        }
        //onDataSetChanged();
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
                R.id.appwidget_school_list);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_item_school_name);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_item_school_address);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_item_school_motto);
    }
}
