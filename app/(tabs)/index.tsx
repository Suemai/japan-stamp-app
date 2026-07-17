import React, {useRef, useState} from "react";
import {Camera, MapView, UserLocation} from "@maplibre/maplibre-react-native";
import {View, StyleSheet, Image, Pressable, Keyboard} from "react-native";
import SearchBar from "@/components/searchBar";
import {useRouter} from "expo-router";
import SearchArea from "@/components/searchArea";
import ReportStamp from "@/components/reportStamps";
import {colours} from "@/constants/colours";

/* Todo:
- button under search bar for search this area - DONE
 -> button to filter stamps by category - NOT DONE
*/

export default function Index() {

    const mapRef = useRef(null)
    const cameraRef = useRef<any>(null);
    const [userLocation, setUserLocation] = useState<any>(null);
    const [following] = useState(true);
    const [heading, setHeading] = useState(0);
    // const router = useRouter();

    const recenterIcon = require("../../assets/images/icons/location-target.png");

    const currentLocationHandler = () => {
        if (!userLocation) return;
        cameraRef.current?.setCamera({
            centerCoordinate: [userLocation.longitude, userLocation.latitude],
            zoomLevel: 20,
            heading: 0,
            animationMode: "flyTo",
            animationDuration: 600,
        });
        // setFollowing(true);
    }
    
    
  return (
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
              onPress={() => Keyboard.dismiss()}
              onRegionDidChange={(region) => {
                  //console.log("region changed, heading:", region.properties.heading);
                  setHeading(region.properties.heading);
              }}
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

          <View style={styles.reportButton}>
              <ReportStamp/>
          </View>

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
              <SearchArea/>
          </View>

          {heading !== 0 && (
              <Pressable
                  onPress={() => {
                      console.log("compass pressed")
                      cameraRef.current?.setCamera({
                          heading: 0,
                          animationDuration: 600
                      });
                  }}
                  style={styles.compass}
                  />
          )}

        </View>
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
        backgroundColor: colours.primary.background,
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
        tintColor: colours.primary.light,
    },
    reportButton: {
        position: "absolute",
        bottom: 155,
        right: 16,
        height: 48,
        width: 48
    },
    compass:{
        position: "absolute",
        top: 120,
        left: 16,
        width: 45,
        height: 45,
        zIndex: 10,
        // backgroundColor: 'rgba(255, 0, 0, 0.5)'  // To see it covers the compass comment it out later
    }
});