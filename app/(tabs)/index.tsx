import React, {useRef, useState} from "react";
import {Camera, MapView, UserLocation} from "@maplibre/maplibre-react-native";
import {View, StyleSheet, Image, Pressable, Keyboard} from "react-native";
import SearchBar from "@/components/searchBar";
import {useRouter} from "expo-router";

/* Todo:
- button for reporting a stamp on bottom left
- button under search bar for search this area
 -> button to filter stamps by category
*/

export default function Index() {

    const mapRef = useRef(null)
    const cameraRef = useRef<any>(null);
    const [userLocation, setUserLocation] = useState<any>(null);
    const [following, setFollowing] = useState(true);
    // const router = useRouter();

    const recenterIcon = require("../../assets/images/icons/location-target.png");

    const currentLocationHandler = () => {
        if (!userLocation) return;
        setFollowing(true);
        cameraRef.current?.setCamera({
            centerCoordinate: [userLocation.longitude, userLocation.latitude],
            zoomLevel: 20,
            heading: 0,
            animationMode: "flyTo",
            animationDuration: 600,
        });
    }
    
    
  return (
      <Pressable style={{flex: 1}} onPress={Keyboard.dismiss}>
      <View style={{flex: 1}}>
          <MapView
              ref = {mapRef}
              style={{flex: 1}}
              mapStyle = "https://tiles.openfreemap.org/styles/liberty"
              // logoEnabled = {true}
              compassEnabled = {true}
              compassViewPosition = {0}
              compassViewMargins = {{x: 15, y: 120}}
              localizeLabels = {true}
              attributionEnabled = {true}
              attributionPosition = {{bottom: 10, left: 15}}
              rotateEnabled = {true}
              >

              <Camera
                  ref = {cameraRef}
                  zoomLevel = {20}
                  animationMode = "flyTo"
                  followUserLocation = {following}>
              </Camera>

              <UserLocation
                  visible = {true}
                  showsUserHeadingIndicator = {true}
                  onUpdate = {(location) => {
                      //console.log("UserLocation:", location.coords);
                      setUserLocation(location.coords);
                  }}
              >
              </UserLocation>
          </MapView>

          <Pressable
          style={styles.recenter}
          onPress={currentLocationHandler}>
              <Image
                  source={recenterIcon}
                  style={styles.icon}/>
          </Pressable>

          <View
              className="absolute top-16 left-4 right-4 z-10">
              <SearchBar
              placeholder={"Search for a stamp"}/>
          </View>
        </View>
      </Pressable>
  );
}

const styles = StyleSheet.create({
    recenter: {
        position: "absolute",
        bottom: 100,
        right: 16,
        width: 48,
        height: 48,
        borderRadius: 12,
        backgroundColor: "#fff",
        alignItems: "center",
        justifyContent: "center",
        shadowColor: "#000",
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.18,
        shadowRadius: 6,
        elevation: 5,
    },
    icon: {
        width: 25,
        height: 25,
        tintColor: "#000",
    }
});