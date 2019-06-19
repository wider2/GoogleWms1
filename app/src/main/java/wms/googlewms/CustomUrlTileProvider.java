package wms.googlewms;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

class CustomUrlTileProvider extends UrlTileProvider {

    private String baseUrl;

    CustomUrlTileProvider(int width, int height, String url) {
        super(width, height);
        this.baseUrl = url;
    }

    @Override
    public URL getTileUrl(int x, int y, int zoom) {
        try {
            return new URL(baseUrl.replace("{zoom}", "" + zoom).replace("{x}", "" + x).replace("{y}", "" + y));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //throw new AssertionError(e);
        }
        return null;
    }

    //just for test
    private boolean checkTileExists(int x, int y, int zoom) {
        int minZoom = 0;
        int maxZoom = 0;

        return !(zoom < minZoom || zoom > maxZoom);
    }
}