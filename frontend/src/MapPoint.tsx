import React, {Component} from "react";
import {Marker, Tooltip} from "react-leaflet";
import {
    UW_LATITUDE,
    UW_LATITUDE_OFFSET,
    UW_LATITUDE_SCALE,
    UW_LONGITUDE,
    UW_LONGITUDE_OFFSET,
    UW_LONGITUDE_SCALE
} from "./Constants";
import L, {LatLngExpression} from "leaflet";
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';
import {CampusBuilding} from "./CampusBuilding";

const greenIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-green.png',
    shadowUrl: iconShadow,
});

const blueIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-blue.png',
    shadowUrl: iconShadow,
});

const redIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png',
    shadowUrl: iconShadow,
});

const yellowIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-gold.png',
    shadowUrl: iconShadow,
});

const DefaultIcon = L.icon({
    iconUrl: icon,
    shadowUrl: iconShadow
});

L.Marker.prototype.options.icon = yellowIcon;

interface MapPointProps {
    shortName: string;
    building: CampusBuilding;
    key: string;
    onClick(shortName: string): void;
}

interface MapPointState {

}

/**
 * Converts x coordinate to longitude
 */
function xToLon(x: number): number {
    return UW_LONGITUDE + (x - UW_LONGITUDE_OFFSET) * UW_LONGITUDE_SCALE;
}

/**
 * Converts y coordinate to latitude
 */
function yToLat(y: number): number {
    return UW_LATITUDE + (y - UW_LATITUDE_OFFSET) * UW_LATITUDE_SCALE;
}

/**
 * A component that will render a line on the React Leaflet map of color from
 * point x1,y1 to x2,y2. This line will convert from the assignment's coordinate
 * system (where 0,0 is the top-left of the UW campus) to latitude and
 * longitude, which the React Leaflet map uses
 */
class MapPoint extends Component<MapPointProps, MapPointState> {
    constructor(props: any) {
        super(props);
        this.state = {

        };
    }

    render() {
        const position: LatLngExpression = [yToLat(this.props.building.y), xToLon(this.props.building.x)];
        return (
            <Marker
                position={position}
                eventHandlers={{
                    click: () => {
                        this.props.onClick(this.props.shortName);
                    },
                }}
            >
                <Tooltip> {this.props.building.shortName} : {this.props.building.longName} </Tooltip>
            </Marker>
        );
    }
}

export default MapPoint;