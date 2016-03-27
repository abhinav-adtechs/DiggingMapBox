package co.swisapp.abhinav.diggingmapbox;


import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

public class MarkersList{

    public List<MarkerData> markers ;

    public List<MarkerData> getMarkers() {
        return markers;
    }

    public void setMarkers(List<MarkerData> markers) {
        this.markers = markers;
    }


    public class MarkerData {
        String title ;
        String snippet ;
        String imageUrl ;
        LatLng coordinates ;
        String videoUrl ;



        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSnippet() {
            return snippet;
        }

        public void setSnippet(String snippet) {
            this.snippet = snippet;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public LatLng getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(LatLng coordinates) {
            this.coordinates = coordinates;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }
}
