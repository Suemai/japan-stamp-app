import React from "react";
import {Camera, MapView, UserLocation} from "@maplibre/maplibre-react-native";

/* Todo:
- search bar on top
- visible compass on top right
- compass onPress to reset north
- button for current location on bottom right
- button for reporting a stamp on bottom left
- button under search bar for search this area
 -> button to filter stamps by category
*/

export default function Index() {
  return (
    <MapView
        style={{flex: 1}}
        mapStyle = "https://tiles.openfreemap.org/styles/liberty"
        // logoEnabled = {true}
        compassEnabled = {true}
        // compassViewPosition = {1}
        localizeLabels = {true}
        attributionEnabled = {true}
        attributionPosition = {{bottom: 10, left: 15}}>

        <Camera
        zoomLevel = {20}
        animationMode = "flyTo"
        followUserLocation = {true}>

        </Camera>

        <UserLocation
        visible = {true}
        showsUserHeadingIndicator = {true}
        onUpdate = {(location) => {
            console.log("UserLocation:", location.coords );
        }}
        >

        </UserLocation>

    </MapView>
  );
}
