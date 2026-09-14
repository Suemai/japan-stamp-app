import { FilterBar } from "@/components/filters/filterBar";
import { LocationSheet } from "@/components/homeDetails/locationSheet";
import { StampSheet } from "@/components/homeDetails/stampSheet";
import ReportStamp from "@/components/reportStamps";
import { StampMarkers } from "@/components/stampMarkers";
import { colours } from "@/constants/colours";
import { fetchLocationById, fetchLocationMarkers, fetchStampsAroundLocation, MapCoordinate, StampRow, StampSetRow } from '@/lib/stampApi';
import { supabase } from '@/lib/supabase';
import { TrueSheet } from '@lodev09/react-native-true-sheet';
import { Camera, MapView, UserLocation } from "@maplibre/maplibre-react-native";
import React, { useEffect, useRef, useState } from "react";
import { Image, Keyboard, Pressable, StyleSheet, View } from "react-native";
import Toast from 'react-native-toast-message';

function distanceBetweenCoordinates(first: MapCoordinate, second: MapCoordinate): number {
    const earthRadiusKm = 6371;
    const firstLatitude = first.latitude * Math.PI / 180;
    const secondLatitude = second.latitude * Math.PI / 180;
    const latitudeDifference = secondLatitude - firstLatitude;
    const longitudeDifference = (second.longitude - first.longitude) * Math.PI / 180;
    const haversine = Math.sin(latitudeDifference / 2) ** 2
        + Math.cos(firstLatitude)
        * Math.cos(secondLatitude)
        * Math.sin(longitudeDifference / 2) ** 2;

    return 2 * earthRadiusKm * Math.asin(Math.sqrt(haversine));
}

/* Todo:
- button under search bar for search this area - DONE
 -> button to filter stamps by category - NOT DONE
*/

export default function Index() {

    const mapRef = useRef(null)
    const cameraRef = useRef<any>(null);
    const [userLocation, setUserLocation] = useState<any>(null);
    const [following, setFollowing] = useState(true);
    const [heading, setHeading] = useState(0);
    const [locations, setLocations] = useState<Array<StampSetRow & { stamps: StampRow[] }>>([]);
    const [mapView, setMapView] = useState<{ centre: MapCoordinate; zoom: number; searchRadiusKm: number } | null>(null);

    useEffect(() => {
        fetchLocationMarkers()
            .then(rows => {
                const lightweightRows = rows.map(row => ({
                    ...row,
                    created_by: null,
                    name: '',
                    address: '',
                    location: '',
                    hours: null,
                    holiday_mode: 'open' as const,
                    holiday_details: '',
                    has_fee: false,
                    fee_amount: 0,
                    fee_currency: '',
                    publicly_viewable: true,
                    created_at: '',
                    updated_at: '',
                    stamps: [],
                })) as Array<StampSetRow & { stamps: StampRow[] }>;
                setLocations(lightweightRows);
            })
            .catch(error => console.error('fetchLocationMarkers error:', error));
    }, []);

    const [selectedLocationId, setSelectedLocationId] = useState<string | null>(null);
    const selectedLocation = locations.find(
        (location) => location.id === selectedLocationId
    );
    const [selectedStampId, setSelectedStampId] = useState<string | null>(null);
    const selectedStamp = selectedLocation?.stamps.find(
        (stamp) => stamp.id === selectedStampId
    );
    const sheetRef = useRef<TrueSheet>(null);

    // const router = useRouter();

    useEffect(() => {
        const initializeAuth = async () => {
            // 1. Fetch the existing session from secure storage
            const { data: { session }, error } = await supabase.auth.getSession();

            if (error) {
                console.error('Error fetching session:', error.message);
                return;
            }

            // 2. Only sign in anonymously if absolutely no session exists
            // 12f4d554-f9a3-4b67-a214-333562966b6e
            if (!session) {
                console.log("attempting to sign in...");
                const { data, error: signInError } = await supabase.auth.signInAnonymously();
                
                if (signInError) {
                    console.error('Anonymous sign-in failed:', signInError.message);
                } else {
                    console.log('Signed in anonymously as:', data.user?.id);
                }
            } else {
                // 3. An account is already active (could be permanent or previous anonymous)
                const isAnonymous = session.user?.is_anonymous;
                console.log(`User already active. Type: ${isAnonymous ? 'Anonymous' : 'Permanent'}, id: ${session.user?.id}`);
            }
        };

        initializeAuth();
    }, []);

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
        // setFollowing(true);
    }

    async function handleSelectLocation(id: string) {
        const detail = await fetchLocationById(id);
        if (detail?.[0]) {
            const hydrated = detail[0];
            setLocations(current => {
                const existing = current.find(row => row.id === id);
                if (existing) {
                    return current.map(row => row.id === id ? hydrated : row);
                }
                return [...current, hydrated];
            });
        }
        setSelectedLocationId(id);
        await sheetRef.current?.present();
    }
    async function handleCloseSheet() {
        await sheetRef.current?.dismiss();
        setSelectedLocationId(null);
        setSelectedStampId(null);
    }

    async function handleSearchArea() {
        console.log("Searching around map area...");
        if (!mapView) return;

        try {
            const stamps = await fetchStampsAroundLocation(mapView.centre, mapView.searchRadiusKm);
            const locationMap = new Map<string, StampSetRow & { stamps: StampRow[] }>();

            for (const stamp of stamps) {
                const location = stamp.stamp_locations;
                if (!location) continue;

                const existing = locationMap.get(location.id);
                if (existing) {
                    existing.stamps.push(stamp);
                } else {
                    locationMap.set(location.id, { ...location, stamps: [stamp] });
                }
            }

            setLocations(Array.from(locationMap.values()));
            setSelectedLocationId(null);
            setSelectedStampId(null);
        } catch (error) {
            console.error('Search around map location failed:', error);
        }
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
                  const [longitude, latitude] = region.geometry.coordinates;
                  const centre = { longitude, latitude };
                  const [northEast, southWest] = region.properties.visibleBounds;
                  const searchRadiusKm = Math.max(
                      distanceBetweenCoordinates(centre, {
                          longitude: northEast[0],
                          latitude: northEast[1],
                      }),
                      distanceBetweenCoordinates(centre, {
                          longitude: southWest[0],
                          latitude: southWest[1],
                      }),
                  );
                  setMapView({
                      centre,
                      zoom: region.properties.zoomLevel,
                      searchRadiusKm,
                  });
                  if (region.properties.isUserInteraction) {
                      setFollowing(false);
                  }
                  setHeading(region.properties.heading);
              }}
              >

              <Camera
                  ref = {cameraRef}
                  defaultSettings={{ zoomLevel: 20 }}
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

              <StampMarkers
                  locations={locations}
                  onSelectLocation={handleSelectLocation}
                  cameraRef={cameraRef}
              />

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

          {/*Search bar, Search buttons, filter pins*/}
          <View
              className="absolute top-16 left-4 right-4 z-10">
              {/*<SearchBar*/}
              {/*placeholder={"Search for a stamp"}/>*/}
              <FilterBar/>
              {/* <SearchArea/> */}
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

          <TrueSheet ref={sheetRef}
                     detents={['auto', 0.55, 0.9]}
                     onDidDismiss={handleCloseSheet}>

              {selectedStamp ? (
                  <StampSheet
                      stamp={selectedStamp}
                      locationName={selectedLocation?.name ?? ""}
                      onBack={() => setSelectedStampId(null)}
                      onClose={handleCloseSheet}
                      // TODO: Implement these functions to handle user actions
                      onToggleWishlist={() => {
                          Toast.show({
                              type: 'success',
                              text1: 'Added to wishlist',
                          });
                          console.log("Added to wishlist");
                      }}
                      onToggleObtained={() => {
                            Toast.show({
                                type: 'success',
                                text1: 'Marked as obtained',
                            });
                            console.log("Marked as obtained");
                      }}
                        onVote={() => {
                            Toast.show({
                                type: 'success',
                                text1: 'Voted for stamp',
                            });
                            console.log("Voted for stamp");
                        }}
                  />
              ) :
                  <LocationSheet
                    location={selectedLocation}
                    onSelectStamp={(stampId) => {
                        setSelectedStampId(stampId);
                        console.log('Location sheet -> Selected stamp:', stampId);
                    }}
                />
              }
          </TrueSheet>

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