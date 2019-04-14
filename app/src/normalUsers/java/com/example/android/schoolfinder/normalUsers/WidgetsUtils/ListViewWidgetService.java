package com.example.android.schoolfinder.normalUsers.WidgetsUtils;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        return new WidgetRemoteViewsFactory(this.getApplication(), intent);
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }


}
