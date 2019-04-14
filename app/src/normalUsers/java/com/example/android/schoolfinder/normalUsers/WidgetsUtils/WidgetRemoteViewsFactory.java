package com.example.android.schoolfinder.normalUsers.WidgetsUtils;

import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.normalUsers.Activities.SchoolDetailActivity;
import com.example.android.schoolfinder.normalUsers.Interfaces.SearchSchoolOfflineDatabaseCallback;
import com.example.android.schoolfinder.normalUsers.SearchSchoolViewModels;
import com.example.android.schoolfinder.normalUsers.Utility.AppDatabase;

import java.util.List;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, SearchSchoolOfflineDatabaseCallback {

    private static final String TAG = WidgetRemoteViewsFactory.class.getSimpleName();
    private Application application;
    private final Intent intent;
    private Context applicationContext;
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

    public WidgetRemoteViewsFactory(Context applicationContext, Intent intent) {

        this.applicationContext = applicationContext;
        this.intent = intent;
        new SearchSchoolViewModels.GetAllSchoolAsyncTask(AppDatabase.getDatabase(applicationContext), this)
                .execute();
    }

    @Override
    public void onCreate() {
    }


    @Override
    public void onDataSetChanged() {


//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_item_school_name);
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_item_school_address);
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_item_school_motto);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return schools == null ? 0 : schools.size();
    }


    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews rv = new RemoteViews(applicationContext.getPackageName(), R.layout.item_school_widget);
        if (schools != null) {
            String name = schools.get(i).getSchoolName();
            String address = schools.get(i).getSchoolLocation();
            String motto = schools.get(i).getSchoolMotto();

            if (schools.get(0).getSchoolLogoImageUrl() != null && !schools.get(0).getSchoolLogoImageUrl().isEmpty()) {
                Bitmap image1 = BitmapFactory.decodeResource(applicationContext.getResources(), R.drawable.logo_place_holder);
                rv.setImageViewBitmap(R.id.item_widget_image, image1);
//                Picasso
//                        .get()
//                        .load(schools.get(0).getSchoolLogoImageUrl())
//                        .placeholder(R.color.colorLightGrey)
//                        .into(new Target() {
//                            @Override
//                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                rv.setImageViewBitmap(R.id.item_widget_image, bitmap);
//                            }
//
//                            @Override
//                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//                            }
//
//                            @Override
//                            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                            }
//                        });
            }
            rv.setTextViewText(R.id.appwidget_list_item_school_name, name);
            rv.setTextViewText(R.id.appwidget_list_item_school_address, address);
            rv.setTextViewText(R.id.appwidget_list_item_school_motto, motto);
            Intent clickIntentTemplate = new Intent(applicationContext, SchoolDetailActivity.class);
            clickIntentTemplate.putExtra(BundleConstants.SCHOOL_ID_BUNDLE, schools.get(i).getId());
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(applicationContext)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        clickPendingIntentTemplate
            rv.setPendingIntentTemplate(R.id.appwidget_school_list, clickPendingIntentTemplate);
//            rv.setOnClickPendingIntent();
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
        final RemoteViews views = new RemoteViews(applicationContext.getPackageName(), R.layout.item_school_widget);
        if (schools.size() > 0) {
//            onDataSetChanged();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(applicationContext);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                    new ComponentName(applicationContext, SavedSchoolWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
                    R.id.appwidget_school_list);
            Intent intent = new Intent(applicationContext, ListViewWidgetService.class);
            views.setRemoteAdapter(R.id.appwidget_school_list, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } else {

        }

    }
}
