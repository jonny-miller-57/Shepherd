import React, {Component} from 'react';

/**
 * A type called Edge that stores the coordinates
 * and the color associated with the edge
 */
type CampusBuilding ={
    x: number;
    y: number;
    shortName: string;
    longName: string;
}

export type {CampusBuilding};