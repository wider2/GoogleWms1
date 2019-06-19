package wms.googlewms;

import android.util.Log;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.io.Proj4FileReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class TileProviderFactory {

    private String WMS_URL;

    public void setUrl(String WMS_URL) {
        this.WMS_URL = WMS_URL;
    }

    public WMSTileProvider getOsgeoWmsTileProvider() {

        WMSTileProvider tileProvider = new WMSTileProvider(256, 256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                String s = "";
                try {
                    double[] bbox = getBoundingBox(x, y, zoom);
                    if (WMS_URL.contains("%f1")) {
                        s = WMS_URL.replace("%f1", String.valueOf(bbox[MINX]));
                        s = s.replace("%f2", String.valueOf(bbox[MINY]));
                        s = s.replace("%f3", String.valueOf(bbox[MAXX]));
                        s = s.replace("%f4", String.valueOf(bbox[MAXY]));
                    } else {
                        if (WMS_URL.contains("EPSG:4326")) {
                            LePoint lePoint = meters2degress(bbox[MINX], bbox[MINY]);
                            LePoint lePoint2 = meters2degress(bbox[MAXX], bbox[MAXY]);
                            s = String.format(Locale.US, WMS_URL, lePoint.getX(), lePoint.getY(), lePoint2.getX(), lePoint2.getY());
                        } else if (WMS_URL.contains("EPSG:3301")) {
                            //LePoint lePoint = toWGS84("3301", bbox[MINX], bbox[MINY]);
                            //LePoint lePoint2 = toWGS84("3301", bbox[MAXX], bbox[MAXY]);

                            LePoint lePoint = fromEpsg3857("3301", bbox[MINX], bbox[MINY]);
                            LePoint lePoint2 = fromEpsg3857("3301", bbox[MAXX], bbox[MAXY]);
                            s = String.format(Locale.US, WMS_URL, lePoint.getX(), lePoint.getY(), lePoint2.getX(), lePoint2.getY());
                        } else {
                            s = String.format(Locale.US, WMS_URL, bbox[MINX], bbox[MINY], bbox[MAXX], bbox[MAXY]);
                        }
                    }
                } catch (Exception ex) {
                    Log.d("TileProviderFactory", ex.getMessage());
                }
                Log.d("WMSDEMO", s);
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };
        return tileProvider;
    }

    private LePoint meters2degress(Double x, Double y) {

        Double lon, lat;
        lon = x * 180 / 20037508.34;
        lat = (Math.atan(Math.exp(y * Math.PI / 20037508.34)) * 360 / Math.PI) - 90;

        LePoint lePoint = new LePoint();
        lePoint.setX(lon);
        lePoint.setY(lat);
        return lePoint;
    }

    // test function just to check data
    private LePoint PointToCoordinate(Double x, Double y) {
        Double lon, lat;
        Double bound = 20037508.34;

        lon = (x * 180) / bound;
        lat = Math.atan(Math.exp(y * Math.PI / bound)) * 360 / Math.PI - 90;

        LePoint lePoint = new LePoint();
        lePoint.setX(lon);
        lePoint.setY(lat);
        return lePoint;
    }

    //https://mvnrepository.com/artifact/io.jeo/proj4j/0.1.1
    private LePoint toWGS84(String originalEpsg, Double x, Double y) {
        Double dNorth, dEast;
        Proj4FileReader proj4FileReader = new Proj4FileReader();
        String[] paramStr = new String[0];
        try {
            paramStr = proj4FileReader.readParametersFromFile("epsg", originalEpsg); //2345
        } catch (IOException e) {
            e.printStackTrace();
        }

        CRSFactory targetFactory = new CRSFactory();
        CoordinateReferenceSystem target = targetFactory.createFromParameters(originalEpsg, paramStr);

        //epsg:4326
        CRSFactory crsFactory = new CRSFactory();
        String wgs84_param = "+title=long/lat:WGS84 +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degress";
        CoordinateReferenceSystem wgs84 = crsFactory.createFromParameters("WGS84", wgs84_param);

        CoordinateTransformFactory ctf = new CoordinateTransformFactory();
        CoordinateTransform transform = ctf.createTransform(target, wgs84);

        ProjCoordinate projCoordinate = new ProjCoordinate(x, y);
        transform.transform(projCoordinate, projCoordinate);
        dNorth = projCoordinate.y;
        dEast = projCoordinate.x;

        LePoint lePoint = new LePoint();
        lePoint.setX(dNorth);
        lePoint.setY(dEast);
        return lePoint;
    }

    //3301
    //https://www.perfectline.co/blog/2011/02/proj4js-l-est-and-geopoint/
    private LePoint fromEpsg3857(String originalEpsg, Double x, Double y) {
        Double dNorth, dEast;
        Proj4FileReader proj4FileReader = new Proj4FileReader();
        String[] paramStr = new String[0];
        try {
            paramStr = proj4FileReader.readParametersFromFile("epsg", originalEpsg); //3301
        } catch (IOException e) {
            e.printStackTrace();
        }

        CRSFactory targetFactory = new CRSFactory();
        CoordinateReferenceSystem target = targetFactory.createFromParameters(originalEpsg, paramStr);

        CRSFactory crsFactory = new CRSFactory();
        String epsg3857_param = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m";
        CoordinateReferenceSystem epsg3857 = crsFactory.createFromParameters("EPSG:3857", epsg3857_param);

        CoordinateTransformFactory ctf = new CoordinateTransformFactory();
        CoordinateTransform transform = ctf.createTransform(epsg3857, target);

        ProjCoordinate projCoordinate = new ProjCoordinate(x, y);
        transform.transform(projCoordinate, projCoordinate);
        dNorth = projCoordinate.y;
        dEast = projCoordinate.x;

        //dNorth = roundCoordinate(dNorth);
        //dEast = roundCoordinate(dEast);

        LePoint lePoint = new LePoint();
        lePoint.setY(dNorth);
        lePoint.setX(dEast);
        return lePoint;
    }

    private Double roundCoordinate(Double src) {
        Double result = 0.0;
        String lpart, rpart;
        long leftPart, rightPart;

        long source = Math.round(src);
        String str = String.valueOf(source);
        lpart = str.substring(0, str.length() - 3);
        rpart = str.substring(str.length() - 3);
        leftPart = Long.parseLong(lpart);
        rightPart = Long.parseLong(rpart);

        if (rightPart < 300) {
            lpart += "000";
        } else if (rightPart >= 300) {
            lpart += "500";
        } else {
            leftPart += 1;
            lpart = leftPart + "000";
        }
        result = Double.parseDouble(lpart);
        return result;
    }
}
