import React, {useRef} from 'react';
import { ShapeSource, SymbolLayer, Images } from '@maplibre/maplibre-react-native';
import {StampLocation} from "@/data/tempData";

const PIN_COLOR = {
    stampColour: '#eb7364',
    cluster: '#cf56df',
};

interface Props {
    locations: StampLocation[];
    onSelectLocation: (LocationId: number) => void;
    cameraRef: React.RefObject<any>;
}

export function StampMarkers({ locations, onSelectLocation, cameraRef }: Props) {
    const shapeSourceRef = useRef<any>(null);

    const shape = {
        type: 'FeatureCollection' as const,
        features: locations.map((loc) => ({
            type: 'Feature' as const,
            properties: {
                id: loc.id
            },
            geometry: {
                type: 'Point' as const,
                coordinates: [loc.longitude, loc.latitude]
            },
        })),
    };

    async function handlePress(e: any) {
        const feature = e.features?.[0];
        const locationId = Number(feature.properties?.id);

        if (!feature) {
            return;
        }
        // cluster
        if (feature.properties.point_count) {
            const zoom = await shapeSourceRef.current?.getClusterExpansionZoom(feature);
            cameraRef.current?.setCamera({
                centerCoordinate: feature.geometry.coordinates,
                zoomLevel: zoom,
                animationDuration: 300
            });
        } else {
            onSelectLocation(locationId);
        }
    }

    return (
        <>
            <Images
                images={{
                    pin: {
                        source: require('@/assets/images/icons/pin.png'),
                        sdf: true,
                    },
                }}
            />
            <ShapeSource
                ref={shapeSourceRef}
                id="stampLocations"
                shape={shape}
                cluster
                clusterRadius={45}
                clusterMaxZoomLevel={14}
                onPress={handlePress}>

                <SymbolLayer
                    id="clusters"
                    filter={['has', 'point_count']}
                    style={{
                        iconImage: 'pin',
                        iconColor: PIN_COLOR.cluster,
                        iconAllowOverlap: true
                }}
                />
                <SymbolLayer
                    id="clusterCount"
                    filter={['has', 'point_count']}
                    style={{
                        textField: ['case', ['>', ['get', 'point_count'], 99],
                            '99+', ['to-string', ['get', 'point_count']]],
                        textSize: 12,
                        textColor: '#EBE3D0',
                        textAllowOverlap: true,
                    }}
                />
                <SymbolLayer
                    id="pins"
                    filter={['!', ['has', 'point_count']]}
                    style={{
                        iconSize: 0.3,
                        iconImage: 'pin',
                        iconColor: PIN_COLOR.stampColour,
                        iconAllowOverlap: true,

                }}
                />
            </ShapeSource>
        </>
    );
}
