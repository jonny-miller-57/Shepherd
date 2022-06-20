/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import L, { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import type {Edge} from "./Edge";
import {MapContainer, TileLayer} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";
import {CampusBuilding} from "./CampusBuilding";
import MapPoint from "./MapPoint";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [47.656208969681408, -122.308629711767204];

interface MapProps {
    edges: Edge[],
    buildings: CampusBuilding[],
    onClick(shortName: string): void;
}

interface MapState {}

class Map extends Component<MapProps, MapState> {
  render() {
    let key = 1;

    // builds array of MapLines
    let lines: JSX.Element[] = [];
    for (const edge of this.props.edges) {
        lines.push(<MapLine color={edge.color} x1={edge.x1} y1={edge.y1} x2={edge.x2} y2={edge.y2} key={key.toString()}/>);
        key++;
    }

    key = 1;

    // builds array of MapPoints
    let points: JSX.Element[] = [];
    for (let building of this.props.buildings) {
        points.push(<MapPoint onClick={this.props.onClick} shortName={building.shortName} building={building} key={key.toString()}/>)
        key++;
    }

    return (
      <div id="map">
        <MapContainer
          center={position}
          zoom={16}
          scrollWheelZoom={false}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {lines}
          {points}
        </MapContainer>
      </div>
    );
  }
}

export default Map;