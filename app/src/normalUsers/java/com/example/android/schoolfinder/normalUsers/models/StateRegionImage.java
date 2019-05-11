package com.example.android.schoolfinder.normalUsers.models;

import com.example.android.countryregioncitypicker.Models.StateRegion;

public class StateRegionImage extends StateRegion {

    private String stateImageUrl;
    private boolean isSelected;

    public StateRegionImage(String toponymName, String stateImageUrl) {
        super();
        super.setToponymName(toponymName);
        this.stateImageUrl = stateImageUrl;
    }

    public String getStateImageUrl() {
        return stateImageUrl;
    }

    public void setStateImageUrl(String stateImageUrl) {
//        Document doc = Jsoup.parse("test.html");
//        Elements metaTags = doc.getElementsByTag("meta");
//
////        Example ex = new Example();
//
//        for (Element metaTag : metaTags) {
//            String content = metaTag.attr("content");
//            String name = metaTag.attr("name");
//
//            if("d.title".equals(name)) {
//                ex.setTitle(content);
//            }
//            if("d.description".equals(name)) {
//                ex.setDescription(content);
//            }
//            if("d.language".equals(name)) {
//                ex.setLanguage(content);
//            }
//        }
        this.stateImageUrl = stateImageUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
