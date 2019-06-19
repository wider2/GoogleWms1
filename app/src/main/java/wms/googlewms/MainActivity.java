package wms.googlewms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TileOverlay tileOverlay;
    private String WMS_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    private void initilizeMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //mMap.setMyLocationEnabled(true);
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE).compassEnabled(true).rotateGesturesEnabled(true).tiltGesturesEnabled(true);

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (mMap != null) {
            setUpMap();
        }

        LatLng sydney = new LatLng(59.43, 24.75);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
    }

    private void setUpMap() {
        if (tileOverlay != null) {
            tileOverlay.remove();
        }
        if (WMS_URL.contains("{zoom}")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            CustomUrlTileProvider mTileProvider = new CustomUrlTileProvider(256, 256, WMS_URL);
            tileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mTileProvider).zIndex(-1));
        } else if (WMS_URL.contains("http")) {

            TileProviderFactory wmsTileProvider = new TileProviderFactory();
            wmsTileProvider.setUrl(WMS_URL);
            TileProvider wmsResult = wmsTileProvider.getOsgeoWmsTileProvider();
            tileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(wmsResult));

            if (WMS_URL.contains(".ee")) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                LatLng tallinn = new LatLng(59.43, 24.75);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tallinn, 14)); //15
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (tileOverlay != null) tileOverlay.remove();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_map1:
                WMS_URL = "http://a.tile.openstreetmap.org/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;
            case R.id.action_map2:
                WMS_URL = "http://a.tile.opencyclemap.org/cycle/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;
            case R.id.action_map3:
                WMS_URL = "http://a.tiles.wmflabs.org/hikebike/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;
            case R.id.action_map4:
                WMS_URL = "https://a.tile.opentopomap.org/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;

            case R.id.action_map5:
                WMS_URL = "http://tile.mtbmap.cz/mtbmap_tiles/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;
            case R.id.action_map6:
                WMS_URL = "http://a.tile.stamen.com/terrain/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;
            case R.id.action_map7:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                WMS_URL = "https://ahocevar.com/geoserver/wms?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image/png&TRANSPARENT=true&LAYERS=topp:states&TILED=true&WIDTH=256&HEIGHT=256&CRS=EPSG:3857&STYLES=&BBOX=%f,%f,%f,%f";
                setUpMap();
                return true;
            case R.id.action_map8:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                WMS_URL =
                        "http://sedac.ciesin.columbia.edu/geoserver/wms" +
                                "?service=WMS" +
                                "&version=1.1.1" +
                                "&request=GetMap" +
                                "&layers=gpw-v3-population-density_2000" +
                                "&bbox=%f,%f,%f,%f" +
                                "&width=256" +
                                "&height=256" +
                                "&srs=EPSG:900913" +
                                "&format=image/png" +
                                "&transparent=true";
                setUpMap();
                return true;

            case R.id.action_map9:
                WMS_URL = "https://a.tile.openstreetmap.se/hydda/full/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;
            case R.id.action_map10:
                WMS_URL = "https://a.tile.openstreetmap.se/hydda/base/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;

            case R.id.action_map11:
                WMS_URL = "https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{zoom}/{y}/{x}";
                //WMS_URL="https://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Specialty/ESRI_StateCityHighway_USA/MapServer";
                setUpMap();
                return true;

            case R.id.action_map12:
                WMS_URL = "http://shangetu1.map.bdimg.com/it/u=x={x};y={y}8;z={zoom};v=009;type=sate&fm=46&udt=20150601";
                setUpMap();
                return true;

            case R.id.action_map13:
                WMS_URL = "https://a.tile.hosted.thunderforest.com/komoot-2/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;

            case R.id.action_map14:
                WMS_URL = "https://a.tiles.mapbox.com/v4/mapquest.streets-mb/{zoom}/{x}/{y}@2x.png?access_token=pk.eyJ1IjoibWFwcXVlc3QiLCJhIjoiY2Q2N2RlMmNhY2NiZTRkMzlmZjJmZDk0NWU0ZGJlNTMifQ.mPRiEubbajc6a5y9ISgydg";
                setUpMap();
                return true;
            case R.id.action_map15:
                WMS_URL = "https://a.tiles.mapbox.com/v4/mapquest.satellite-mb/{zoom}/{x}/{y}@2x.png?access_token=pk.eyJ1IjoibWFwcXVlc3QiLCJhIjoiY2Q2N2RlMmNhY2NiZTRkMzlmZjJmZDk0NWU0ZGJlNTMifQ.mPRiEubbajc6a5y9ISgydg";
                setUpMap();
                return true;

            case R.id.action_map16:
                WMS_URL = "https://tile1.maptoolkit.net/terrain/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;

            case R.id.action_map17:
                WMS_URL = "https://map1.viamichelin.com/map/mapdirect?map=viamichelin&z={zoom}&x={x}&y={y}&format=png&version=201503191157&layer=background";
                setUpMap();
                return true;

            case R.id.action_map18:
                WMS_URL = "https://tiles1-bc7b4da77e971c12cb0e069bffcf2771.skobblermaps.com/TileService/tiles/2.0/01021113210/5/{zoom}/{x}/{y}.png@2x?traffic=false";
                setUpMap();
                return true;

            case R.id.action_map19:
                WMS_URL = "https://a-mydrive.api-system.tomtom.com/map/1/tile/basic/main/{zoom}/{x}/{y}.png?key=sATA9OwG11zrMKQcCxR3eSEjj2n8Jsrg";
                setUpMap();
                return true;

            case R.id.action_map20:
                WMS_URL = "http://alpha.map1.eu/tiles/{zoom}/{x}/{y}.jpg";
                setUpMap();
                return true;

            case R.id.action_map21:
                WMS_URL = "https://services.geodataonline.no/arcgis/rest/services/Geocache_WMAS_WGS84/GeocacheBasis/MapServer/tile/{zoom}/{y}/{x}";
                setUpMap();
                return true;

            case R.id.action_map22:
                WMS_URL = "https://tile1.f4map.com/tiles/f4_2d/{zoom}/{x}/{y}.png";
                setUpMap();
                return true;

            //https://lists.osgeo.org/pipermail/openlayers-users/2010-June.txt
            //https://geoportaal.maaamet.ee/eng/Services/Public-WMS-service-p346.html
            case R.id.action_map23:
                WMS_URL = "http://kaart.maaamet.ee/wms/alus-geo?STYLES=&SRS=EPSG:4326&FORMAT=image/png&VERSION=1.1.1&REQUEST=GetMap&LAYERS=of10000,TOPOYKSUS_6569,TOPOYKSUS_7793&bbox=%f,%f,%f,%f&width=256&height=256";
                setUpMap();
                return true;

            case R.id.action_map24:
                //very strict tail
                //WMS_URL = "https://kls.pria.ee/kaart/proxy_tms.php?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image/png&TRANSPARENT=true&LAYERS=kaart&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG:3301&STYLES=&BBOX=%f,%f,%f,%f";
                WMS_URL = "http://kaart.tallinn.ee/tar/?VERSION=1.3.0&LAYERS=aluskaart_yldine&STYLES=&CRS=EPSG:3301&REQUEST=GetMap&FORMAT=image/png&BBOX=%f,%f,%f,%f&WIDTH=256&HEIGHT=256";
                setUpMap();
                return true;

            case R.id.action_map25:
                WMS_URL = "http://kaart.maaamet.ee/wms/fotokaart?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetMap&FORMAT=image/jpeg&SRS=EPSG:3301&LAYERS=EESTIFOTO,reljeef&WIDTH=256&HEIGHT=256&bbox=%f,%f,%f,%f";
                setUpMap();
                return true;

            case R.id.action_map26:
                WMS_URL = "https://pump.reach-u.com/delfi-eesti/{zoom}/{x}/{y}";
                setUpMap();
                return true;

            case R.id.action_map27:
                WMS_URL = "http://kaart.maaamet.ee/wms/alus?STYLES=&SRS=EPSG:3301&FORMAT=image/png&VERSION=1.1.1&REQUEST=GetMap&LAYERS=MA-ALUS&bbox=%f,%f,%f,%f&width=256&height=256";
                setUpMap();
                return true;

            //layers - kihi nimikirjad
            //https://geoportaal.maaamet.ee/docs/WMS/MapCache_teenused_juhend_v2.pdf
            case R.id.action_map28:
                //strict tail: 500, 1000
                //WMS_URL = "http://kaart.maaamet.ee/wms/kaart?STYLES=&SRS=EPSG:3301&FORMAT=image/png&VERSION=1.1.1&REQUEST=GetMap&LAYERS=&bbox=%f,%f,%f,%f&width=256&height=256";
                //WMS_URL = "http://tiles.maaamet.ee/tm/?LAYERS=kaart&FORMAT=image/png&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&EXCEPTIONS=application/vnd.ogc.se_inimage&SRS=EPSG:3301&BBOX=%f,%f,%f,%f&WIDTH=256&HEIGHT=256";

                //they use little numbers, it is not a coordinates x,y
                //http://tiles.maaamet.ee/tm/tms/1.0.0/foto/6/28/36.jpeg
                //WMS_URL = "http://tiles.maaamet.ee/tm/tms/1.0.0/hybriid/{zoom}/{x}/{y}.png";
                //WMS_URL = "http://tiles.maaamet.ee/tm/tms/1.0.0/hybriid/{zoom}/{x}/{y}.jpeg&ASUTUS=MAAAMET&KESKKOND=LIVE&IS=XGIS";

                WMS_URL = "http://kaart.maaamet.ee/wms/alus?STYLES=&SRS=EPSG:3301&FORMAT=image/png&VERSION=1.1.1&REQUEST=GetMap&LAYERS=epk_v&bbox=%f,%f,%f,%f&width=256&height=256";
                setUpMap();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
