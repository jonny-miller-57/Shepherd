import React, {Component} from 'react';

/**
 * A type called BoulderProblem that stores information
 * of a particular boulder problem
 */
type BoulderProblem ={
    destination: string;
    area: string;
    subarea: string;
    boulder: string;
    name: string;
    grade: string;
    stars: number;
    description: string;
    key: string;
}

export type {BoulderProblem};