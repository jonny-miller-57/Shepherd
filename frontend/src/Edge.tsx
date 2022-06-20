import React, {Component} from 'react';

/**
 * A type called Edge that stores the coordinates
 * and the color associated with the edge
 */
type Edge ={
    x1: number;
    y1: number;
    x2: number;
    y2: number;
    color: string;
    key: string;
}

export type {Edge};